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
package fr.paris.lutece.plugins.blog.service;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

public class BlogIdService extends ResourceIdService
{

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "blog.rbac.blog.resourceType";
    private static final String PROPERTY_LABEL_VIEW = "blog.rbac.blog.permission.view";
    private static final String PROPERTY_LABEL_CREATE = "blog.rbac.blog.permission.create";
    private static final String PROPERTY_LABEL_MODIFY = "blog.rbac.blog.permission.modify";
    private static final String PROPERTY_LABEL_DELETE = "blog.rbac.blog.permission.delete";
    private static final String PROPERTY_LABEL_PUBLISH = "blog.rbac.blog.permission.publish";
    private static final String PROPERTY_LABEL_ARCHIVE = "blog.rbac.blog.permission.archive";

    @Override
    public void register( )
    {

        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( BlogIdService.class.getName( ) );
        rt.setPluginName( BlogPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( Blog.PROPERTY_RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_PUBLISH );
        p.setPermissionTitleKey( PROPERTY_LABEL_PUBLISH );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_ARCHIVE);
        p.setPermissionTitleKey( PROPERTY_LABEL_ARCHIVE );
        rt.registerPermission( p );

        // for all permissions

        ResourceTypeManager.registerResourceType( rt );

    }

    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {

        List<Blog> listBlogs = BlogHome.getBlogsList( );

        return ReferenceList.convert( listBlogs, "id", "name", true );
    }

    @Override
    public String getTitle( String strId, Locale locale )
    {

        Blog blog = BlogHome.findByPrimaryKey( Integer.parseInt( strId ) );

        return blog.getName( );
    }

}
