/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.blog.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * Calendar Dashboard Component This component displays directories
 */
public class BlogDashboardComponent extends DashboardComponent
{
    // PARAMETERS
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";

    // MARKS
    private static final String MARK_URL = "url";
    private static final String MARK_ICON = "icon";
    private static final String MARK_LAST_MODIFIED_DOCUMENT = "last_modified_document";
    protected static final String MARK_PERMISSION_CREATE_BLOG = "permission_manage_create_blog";
    protected static final String MARK_PERMISSION_MODIFY_BLOG = "permission_manage_modify_blog";
    protected static final String MARK_PERMISSION_PUBLISH_BLOG = "permission_manage_publish_blog";
    protected static final String MARK_PERMISSION_DELETE_BLOG = "permission_manage_delete_blog";

    private static final String PROPERTY_NIMBER_DOCUMENT_LOADED = "number.documents.to.be.loaded";

    // TEMPALTES
    private static final String TEMPLATE_DASHBOARD = "/admin/plugins/blog/dashboard/blogs_dashboard.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Right right = RightHome.findByPrimaryKey( getRight( ) );
        Plugin plugin = PluginService.getPlugin( right.getPluginName( ) );

        if ( !( ( plugin.getDbPoolName( ) != null ) && !AppConnectionService.NO_POOL_DEFINED.equals( plugin.getDbPoolName( ) ) ) )
        {
            return StringUtils.EMPTY;
        }

        String strValue = DatastoreService.getDataValue( PROPERTY_NIMBER_DOCUMENT_LOADED, "5" );
        List<Blog> lastModifiedDocument = BlogService.getInstance( ).getLastModifiedBlogsList( Integer.parseInt( strValue ) );

        UrlItem url = new UrlItem( right.getUrl( ) );
        url.addParameter( PARAMETER_PLUGIN_NAME, right.getPluginName( ) );

        boolean bPermissionCreate = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_CREATE, (User) user );
        boolean bPermissionModify = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_MODIFY, (User) user );
        boolean bPermissionDelete = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_DELETE, (User) user );
        boolean bPermissionPublish = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_PUBLISH, (User) user );

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_PERMISSION_CREATE_BLOG, bPermissionCreate );
        model.put( MARK_PERMISSION_MODIFY_BLOG, bPermissionModify );
        model.put( MARK_PERMISSION_DELETE_BLOG, bPermissionDelete );
        model.put( MARK_PERMISSION_PUBLISH_BLOG, bPermissionPublish );

        model.put( MARK_LAST_MODIFIED_DOCUMENT, lastModifiedDocument );
        model.put( MARK_URL, url.getUrl( ) );
        model.put( MARK_ICON, plugin.getIconUrl( ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD, user.getLocale( ), model );

        return t.getHtml( );
    }
}
