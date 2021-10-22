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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.plugins.blog.business.BlogPageTemplate;
import fr.paris.lutece.plugins.blog.business.BlogPageTemplateHome;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents business objects BlogsPortlet
 */
public class BlogPortlet extends PortletHtmlContent
{
    public static final String RESOURCE_ID = "BLOG_PORTLET";
    public static final String MARK_BLOG = "blog";
    public static final String MARK_PORTLET_NAME = "portlet_name";
    public static final String MARK_PORTLET_ID = "portlet_id";

    private int _nPageTemplateDocument;

    /**
     * Sets the identifier of the portlet type to value specified
     */
    public BlogPortlet( )
    {
        setPortletTypeId( BlogPortletHome.getInstance( ).getPortletTypeId( ) );
    }

    private int _nContentId;

    private String _strName;

    private BlogPublication _blogPublication;

    /**
     * Returns the HTML code of the blogsPortlet portlet with XML heading
     *
     * @param request
     *            The HTTP servlet request
     * @return the HTML code of the blogsPortlet portlet
     */
    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
        GregorianCalendar calendar = new java.util.GregorianCalendar( );
        Blog blog = BlogHome.findByPrimaryKey( this.getContentId( ) );
        BlogPublication docPub = BlogPublicationHome.findDocPublicationByPimaryKey( this.getId( ), this.getContentId( ) );
        HashMap<String, Object> model = new HashMap<>( );
        BlogPageTemplate pageTemplate = BlogPageTemplateHome.findByPrimaryKey( this.getPageTemplateDocument( ) );

        if ( docPub != null && docPub.getIdBlog( ) != 0 && docPub.getDateBeginPublishing( ).before( new Date( calendar.getTimeInMillis( ) ) )
                && docPub.getDateEndPublishing( ).after( new Date( calendar.getTimeInMillis( ) ) ) )
        {
            if ( this.getDisplayPortletTitle( ) == 0 )
            {

                model.put( MARK_PORTLET_NAME, this.getName( ) );

            }
            model.put( MARK_BLOG, blog );
        }
        model.put( MARK_PORTLET_ID, this.getId( ) );
        Locale locale = null;
        if ( request != null )
        {
            locale = request.getLocale( );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( pageTemplate.getFile( ), locale, model );

        return template.getHtml( );

    }

    /**
     * Updates the current instance of the blogsPortlet object
     */
    public void update( )
    {
        BlogPortletHome.getInstance( ).update( this );
    }

    /**
     * Removes the current instance of the blogsPortlet object
     */
    @Override
    public void remove( )
    {
        BlogPublicationHome.removeByIdPortlet( this.getId( ) );
        BlogPortletHome.getInstance( ).remove( this );
    }

    /**
     * Sets the id of the content
     *
     * @param nContentId
     *            id of the content
     */
    public void setContentId( int nContentId )
    {
        _nContentId = nContentId;
    }

    /**
     * Get the id of the html document
     *
     * @return the id of the document
     */
    public int getContentId( )
    {
        return _nContentId;
    }

    /**
     * Sets the BlogPublication of the html document
     *
     * @param blogPublication
     *            the publication of the document
     */
    public void setBlogPublication( BlogPublication blogPublication )
    {
        _blogPublication = blogPublication;
    }

    /**
     * Get the BlogPublication of the html document
     *
     * @return the BlogPublication of the document
     */
    public BlogPublication getBlogPublication( )
    {
        return _blogPublication;
    }

    /**
     * Sets the name of the html document
     *
     * @param strName
     *            the name of the document
     */
    public void setPortletName( String strName )
    {
        _strName = strName;
    }

    /**
     * Get the name of the html document
     *
     * @return the name of the document
     */
    public String getPortletName( )
    {
        return _strName;
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
}
