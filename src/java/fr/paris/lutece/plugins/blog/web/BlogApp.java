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

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class manages Blogs page.
 *
 */
@Controller( xpageName = BlogApp.XPAGE_NAME, pageTitleI18nKey = BlogApp.MESSAGE_PAGE_TITLE, pagePathI18nKey = BlogApp.MESSAGE_PATH )
public class BlogApp extends MVCApplication
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected static final String XPAGE_NAME = "blog";

    // Messages
    protected static final String MESSAGE_PAGE_TITLE = "blog.xpage.blog.view.pageTitle";
    protected static final String MESSAGE_PATH = "blog.xpage.blog.view.pagePathLabel";
    // Templates

    private static final String TEMPLATE_VIEW_BLOG = "skin/plugins/blog/view_blog.html";

    // Parameters
    protected static final String PARAMETER_ID_BLOG = "id";
    protected static final String PARAMETER_ID_PORTLET = "portlet_id";

    protected static final String PARAMETER_VIEW = "view";

    // Properties for page titles

    // Filter Marks
    protected static final String MARK_BLOG = "blog";
    protected static final String MARK_LIST_DOC = "blog_list";

    // Views
    private static final String VIEW_DETAILS = "documentDetails";
    protected static final String MARK_LIST_TAG = "list_tag";

    /**
     * Gets the BLOG details view
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_DETAILS, defaultView = true )
    public XPage getBlogDetails( HttpServletRequest request )
    {

        List<BlogPublication> listBlogPub = new ArrayList<>( );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String idPortlet = request.getParameter( PARAMETER_ID_PORTLET );
        if ( idPortlet != null && !idPortlet.isEmpty( ) )
        {

            listBlogPub = BlogPublicationHome.getDocPublicationByPortlet( Integer.parseInt( idPortlet ) );
        }
        List<Blog> listBlogs = new ArrayList<>( );

        for ( Blog doc : BlogService.getInstance( ).getListBlogWithoutBinaries( ) )
        {
            for ( BlogPublication pub : listBlogPub )
            {
                if ( doc.getId( ) == pub.getIdBlog( ) )
                {
                    doc.setAttachedPortletId( Integer.parseInt( idPortlet ) );
                    listBlogs.add( doc );

                }
            }

        }

        Blog blog = BlogService.getInstance( ).loadBlog( nId );
        Map<String, Object> model = getModel( );
        model.put( MARK_BLOG, blog );
        model.put( MARK_LIST_DOC, listBlogs );
        model.put( MARK_LIST_TAG, TagHome.getTagsReferenceList( ) );

        return getXPage( TEMPLATE_VIEW_BLOG, request.getLocale( ), model );
    }

}
