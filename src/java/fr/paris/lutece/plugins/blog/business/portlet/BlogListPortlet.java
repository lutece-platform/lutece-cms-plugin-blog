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
package fr.paris.lutece.plugins.blog.business.portlet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import fr.paris.lutece.plugins.blog.business.BlogPageTemplate;
import fr.paris.lutece.plugins.blog.business.BlogPageTemplateHome;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogFilter;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.service.PublishingService;
import fr.paris.lutece.plugins.blog.utils.BlogUtils;
import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents business objects BlogsList Portlet
 */
public class BlogListPortlet extends PortletHtmlContent
{
    public static final String RESOURCE_ID = "BLOG_LIST_PORTLET";

    // ///////////////////////////////////////////////////////////////////////////////
    public static final String MARK_LIST_BLOG_PUBLISHED = "blog_list_published";
    public static final String MARK_PAGE_TEMPLATE = "page_template";
    public static final String MARK_PORTLET_ID = "portlet_id";
    public static final String MARK_PORTLET_NAME = "portlet_name";

    // ///////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nPageTemplateDocument;
    private int _nPortletId;
    private List<BlogPublication> _arrayBlogs = new ArrayList<>( );
    private Set<Integer> _removedBlogsId = new HashSet<>( );

    /**
     * Sets the identifier of the portlet type to the value specified in the BlogsListPortletHome class
     */
    public BlogListPortlet( )
    {
        setPortletTypeId( BlogListPortletHome.getInstance( ).getPortletTypeId( ) );
    }

    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
        GregorianCalendar calendar = new java.util.GregorianCalendar( );
        Date date = new Date( calendar.getTimeInMillis( ) );
        BlogFilter documentFilter = new BlogFilter( );
        List<Integer> listIdDoc = PublishingService.getPublishedBlogsIdsListByPortletIds( new int [ ] {
                this.getId( )
        }, date, date, BlogUtils.getPlugin( ) );
        Integer [ ] docId = listIdDoc.toArray( new Integer [ listIdDoc.size( )] );
        // Default we published a blog that as id=0
        if ( docId == null || docId.length == 0 )
        {

            docId = new Integer [ 1];
            docId [0] = 0;
        }
        documentFilter.setIds( docId );
        documentFilter.setLoadBinaries( true );
        documentFilter.setOrderInPortlet( true );
        documentFilter.setPortletId( this.getId( ) );

        List<Blog> listBlogsPublished = BlogHome.findByFilter( documentFilter );
        BlogPageTemplate pageTemplate = BlogPageTemplateHome.findByPrimaryKey( this.getPageTemplateDocument( ) );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_BLOG_PUBLISHED, listBlogsPublished );
        model.put( MARK_PAGE_TEMPLATE, pageTemplate );
        model.put( MARK_PORTLET_ID, this.getId( ) );
        if ( this.getDisplayPortletTitle( ) == 0 )
        {

            model.put( MARK_PORTLET_NAME, this.getName( ) );

        }
        Locale locale = null;
        if ( request != null )
        {
            locale = request.getLocale( );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( pageTemplate.getFile( ), locale, model );

        return template.getHtml( );
    }

    /**
     * Updates the current instance of the Blogs List Portlet object
     */
    public void update( )
    {
        BlogListPortletHome.getInstance( ).update( this );
    }

    /**
     * Removes the current instance of the Blogs List Portlet object
     */
    @Override
    public void remove( )
    {
        BlogListPortletHome.getInstance( ).remove( this );
    }

    /**
     * Returns the nPortletId
     *
     * @return The nPortletId
     */
    public int getPortletId( )
    {
        return _nPortletId;
    }

    /**
     * Sets the IdPortlet
     *
     * @param nPortletId
     *            The nPortletId
     */
    public void setPortletId( int nPortletId )
    {
        _nPortletId = nPortletId;
    }

    /**
     * Sets the parent page identifier of the portlet to the value specified in parameter
     *
     * @param nPageTemplateDocument
     *            the code
     */
    public void setPageTemplateDocument( int nPageTemplateDocument )
    {
        _nPageTemplateDocument = nPageTemplateDocument;
    }

    /**
     * Returns the identifier of the parent page of the portlet
     *
     * @return the parent page identifier
     */
    public int getPageTemplateDocument( )
    {
        return _nPageTemplateDocument;
    }

    /**
     * @return the _arrayBlogs
     */
    public List<BlogPublication> getArrayBlogs( )
    {
        return _arrayBlogs;
    }

    /**
     * @param arrayBlogs
     *            the _arrayBlogs to set
     */
    public void setArrayBlogs( List<BlogPublication> arrayBlogs )
    {
        _arrayBlogs = arrayBlogs;
    }

    /**
     * BlogDoPublication in the list _arrayBlogs
     * 
     * @param doc
     *            The blog publication
     */
    public void addIdBlogs( BlogPublication doc )
    {

        boolean bool = false;
        for ( BlogPublication dc : _arrayBlogs )
        {

            if ( doc.getIdBlog( ) == dc.getIdBlog( ) )
            {
                bool = true;
                break;
            }

        }
        if ( !bool )
        {
            _arrayBlogs.add( doc );
        }
        _removedBlogsId.remove( doc.getIdBlog( ) );
    }

    /**
     * set BlogPublication in the list by order
     * 
     * @param order
     *            The order in portlet publication
     * @param doc
     *            The blog
     */
    public void addIdBlogs( int order, BlogPublication doc )
    {

        boolean bool = false;
        BlogPublication blog = null;
        for ( BlogPublication dc : _arrayBlogs )
        {

            if ( doc.getIdBlog( ) == dc.getIdBlog( ) )
            {
                bool = true;
                blog = dc;
                break;
            }

        }
        if ( bool )
        {

            _arrayBlogs.remove( blog );

        }
        _arrayBlogs.add( order, doc );
        _removedBlogsId.remove( doc.getIdBlog( ) );
    }

    /**
     * Remove blog publication
     * 
     * @param doc
     *            The blog Publication
     */
    public void removeBlogs( BlogPublication doc )
    {

        for ( BlogPublication dc : _arrayBlogs )
        {

            if ( doc.getIdBlog( ) == dc.getIdBlog( ) )
            {
                _arrayBlogs.remove( dc );
                break;
            }

        }
        _removedBlogsId.add( doc.getIdBlog( ) );
    }

    public Set<Integer> getRemovedBlogsId( )
    {
        return new HashSet<>( _removedBlogsId );
    }
}
