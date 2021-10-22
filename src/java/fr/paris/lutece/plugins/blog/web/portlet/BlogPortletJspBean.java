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
package fr.paris.lutece.plugins.blog.web.portlet;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPortlet;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPortletHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.business.user.AdminUser;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides the user interface to manage BlogsPortlet features
 */
public class BlogPortletJspBean extends PortletJspBean
{

    private static final long serialVersionUID = 5744334133144418317L;
    public static final String MARK_HTML_CONTENT = "htmlcontent";
    public static final String MARK_EDIT_COMMENT = "editcomment";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LIST_HTMLDOC = "blog_list";
    public static final String MARK_LIST_PAGES = "pages_list";
    public static final String MARK_BLOG_ID = "blog_id";

    public static final String PARAMETER_CONTENT_ID = "content_id";
    public static final String PARAMETER_HTML_CONTENT = "html_content";
    public static final String PARAMETER_EDIT_COMMENT = "edit_comment";
    public static final String PARAMETER_PORTLET_NAME = "portlet_name";
    public static final String PARAMETER_HTMLDOC_SELECTED = "blog_selected";
    private static final String PARAMETER_PAGE_TEMPLATE_CODE = "page_template_code";

    public static final String TEMPLATE_MODIFY_PORTLET = "admin/portlet/modify_portlet.html";

    /**
     * Returns the BlogPortlet form of creation
     *
     * @param request
     *            The Http rquest
     * @return the html code of the Blog portlet form
     */
    @Override
    public String getCreate( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        List<Blog> listBlog = BlogHome.getBlogsList( );
        HashMap<String, Object> model = new HashMap<>( );

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LIST_HTMLDOC, listBlog );
        model.put( MARK_LIST_PAGES, BlogListPortletHome.loadPages( BlogPortlet.RESOURCE_ID ) );

        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId, model );

        return template.getHtml( );
    }

    /**
     * Returns the BlogPortlet form for update
     * 
     * @param request
     *            The Http request
     * @return the html code of the BlogPortlet form
     */
    @Override
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        BlogPortlet portlet = (BlogPortlet) PortletHome.findByPrimaryKey( nPortletId );
        Blog blog = BlogHome.findByPrimaryKey( portlet.getContentId( ) );
        HashMap<String, Object> model = new HashMap<>( );

        model.put( MARK_HTML_CONTENT, blog.getHtmlContent( ) );
        model.put( MARK_EDIT_COMMENT, blog.getEditComment( ) );
        model.put( MARK_LIST_PAGES, BlogListPortletHome.loadPages( BlogPortlet.RESOURCE_ID ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_BLOG_ID, blog.getId( ) );

        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml( );
    }

    /**
     * Treats the creation form of a new Blog portlet
     *
     * @param request
     *            The Http request
     * @return The jsp URL which displays the view of the created BlogPortlet portlet
     */
    @Override
    public String doCreate( HttpServletRequest request )
    {
        BlogPortlet portlet = new BlogPortlet( );
        AdminUser user = AdminUserService.getAdminUser( request );
        String strSelectedBlog = request.getParameter( PARAMETER_HTMLDOC_SELECTED );
        String strTemplateCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );

        // recovers portlet specific attributes
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        Blog blog = new Blog( );
        if ( strSelectedBlog == null || StringUtils.isEmpty( strSelectedBlog ) || !StringUtils.isNumeric( strSelectedBlog ) )
        {
            blog.setContentLabel( request.getParameter( PARAMETER_PORTLET_NAME ) );
            blog.setVersion( 1 );
            blog.setCreationDate( getSqlDate( ) );
            blog.setUpdateDate( getSqlDate( ) );
            blog.setHtmlContent( request.getParameter( PARAMETER_HTML_CONTENT ) );
            // TODO error validation on edit comment length
            blog.setEditComment( request.getParameter( PARAMETER_EDIT_COMMENT ) );
            blog.setUser( user.getAccessCode( ) );
            blog.setUserCreator( user.getAccessCode( ) );
            BlogHome.addInitialVersion( blog );
        }
        else
        {
            blog = BlogHome.findByPrimaryKey( Integer.parseInt( strSelectedBlog ) );
        }
        int nContentId = blog.getId( );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageTemplateDocument( Integer.parseInt( strTemplateCode ) );
        portlet.setPageId( nPageId );
        portlet.setContentId( nContentId );
        portlet.setPortletName( request.getParameter( PARAMETER_PORTLET_NAME ) );

        // Creates the portlet
        BlogPortletHome.getInstance( ).create( portlet );
        blog.setAttachedPortletId( portlet.getId( ) );
        BlogHome.update( blog );
        int nbPublication = BlogPublicationHome.countPublicationByIdBlogAndDate( blog.getId( ), new Date( ) );
        // First publication of this blog -> indexing needed
        if ( nbPublication == 1 )
        {
            BlogService.getInstance( ).fireCreateBlogEvent( blog.getId( ) );
        }

        // Displays the page with the new Portlet
        return getPageUrl( nPageId );
    }

    /**
     * Treats the update form of the BlogPortlet portlet whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return The jsp URL which displays the view of the updated portlet
     */
    @Override
    public String doModify( HttpServletRequest request )
    {
        // fetches portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        // recovers portlet attributes
        String strDocumentTypeCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );
        int nPortletId = Integer.parseInt( strPortletId );
        BlogPortlet portlet = (BlogPortlet) PortletHome.findByPrimaryKey( nPortletId );
        Blog blog = BlogHome.findByPrimaryKey( portlet.getContentId( ) );
        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }
        portlet.setPageTemplateDocument( Integer.parseInt( strDocumentTypeCode ) );
        // updates the blog
        blog.setHtmlContent( request.getParameter( PARAMETER_HTML_CONTENT ) );
        blog.setEditComment( request.getParameter( PARAMETER_EDIT_COMMENT ) );
        blog.setUpdateDate( getSqlDate( ) );
        blog.setVersion( blog.getVersion( ) + 1 );
        BlogHome.addNewVersion( blog );

        portlet.setBlogPublication( BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, portlet.getContentId( ) ) );
        // updates the portlet
        portlet.update( );

        // update of this blog -> re-indexing needed
        BlogService.getInstance( ).fireUpdateBlogEvent( blog.getId( ) );
        // displays the page with the updated portlet
        return getPageUrl( portlet.getPageId( ) );
    }

    /**
     * Gets the current date in sql format
     *
     * @return the current date in sql format
     */
    protected java.sql.Timestamp getSqlDate( )
    {
        java.util.Date utilDate = new java.util.Date( );
        java.sql.Timestamp sqlDate = new java.sql.Timestamp( utilDate.getTime( ) );

        return ( sqlDate );
    }
}
