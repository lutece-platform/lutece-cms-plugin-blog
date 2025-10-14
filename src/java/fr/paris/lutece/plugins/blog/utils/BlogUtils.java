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
/*
 * AnnounceUtils.java
 *
 * Created on 23 octobre 2009, 11:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.blog.utils;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import fr.paris.lutece.plugins.blog.business.BlogSearchFilter;
import fr.paris.lutece.plugins.blog.service.BlogPlugin;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Utility class for announce plugin
 */
public final class BlogUtils
{
    // Constants
    public static final String CONSTANT_WHERE = " WHERE ";
    public static final String CONSTANT_AND = " AND ";
    public static final int CONSTANT_ID_NULL = -1;
    public static final String CONSTANT_TYPE_RESOURCE = "BLOG_DOCUMENT";
    public static final String CONSTANT_PARAMETER_BLOG = "blog";
    public static final String CONSTANT_PARAMETER_ID = "id";
    public static final String CONSTANT_LINK_TARGET_NEW_WINDOW = "_blank";

    // Templates
    public static final String TEMPLATE_INSERT_BLOG_LINK = "admin/plugins/blog/insertservice/insert_blog_link.html";

    /**
     * Private default constructor
     */
    private BlogUtils( )
    {
        // Do nothing
    }

    /**
     * Builds a query with filters placed in parameters
     *
     * @param strSelect
     *            the select of the query
     * @param listStrFilter
     *            the list of filter to add in the query
     * @param strOrder
     *            the order by of the query
     * @return a query
     */
    public static String buildRequetteWithFilter( String strSelect, List<String> listStrFilter, String strOrder )
    {
        StringBuilder strBuffer = new StringBuilder( );
        strBuffer.append( strSelect );

        int nCount = 0;

        for ( String strFilter : listStrFilter )
        {
            if ( ++nCount == 1 )
            {
                strBuffer.append( CONSTANT_WHERE );
            }

            strBuffer.append( strFilter );

            if ( nCount != listStrFilter.size( ) )
            {
                strBuffer.append( CONSTANT_AND );
            }
        }

        if ( strOrder != null )
        {
            strBuffer.append( strOrder );
        }

        return strBuffer.toString( );
    }

    /**
     * write the http header in the response
     *
     * @param request
     *            the httpServletRequest
     * @param response
     *            the http response
     * @param strFileName
     *            the name of the file who must insert in the response
     *
     */
    public static void addHeaderResponse( HttpServletRequest request, HttpServletResponse response, String strFileName )
    {
        response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
        response.setHeader( "Pragma", "public" );
        response.setHeader( "Expires", "0" );
        response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );
    }

    /**
     * Create a Blog search filter with specific values. The unnecessary parameters can be replaced by null values
     *
     * @param searchKeyword
     *            Keywords to search for in the blog posts
     * @param searchTagsArray
     *            Specific tags to search for
     * @param user
     *            Filter by the user who created the blog
     * @param isUnpublished
     *            Search for blogs currently unpublished. Set to '0' to retrieve all blogs, '1' published, '2' unpublished
     * @param strDateUpdateBlogAfter
     *            Filter by blogs updated after a specific date
     * @param strDateUpdateBlogBefore
     *            Filter by blogs updated before a specific date
     * @param userEditor
     *            Filter by the last user who edited the blog
     * @param locale
     *            The Locale to use for the dates' format
     * @return the BlogSearchFilter objects created with the given parameters
     */
    public static BlogSearchFilter buildBlogSearchFilter( String searchKeyword, String [ ] searchTagsArray, AdminUser user, int isUnpublished,
            String strDateUpdateBlogAfter, String strDateUpdateBlogBefore, String userEditor, Locale locale )
    {
        BlogSearchFilter filter = new BlogSearchFilter( );

        if ( StringUtils.isNotBlank( searchKeyword ) )
        {
            filter.setKeywords( searchKeyword );
        }
        if ( !ArrayUtils.isEmpty( searchTagsArray ) )
        {
            filter.setTag( searchTagsArray );
        }
        if ( user != null )
        {
            filter.setUser( user.getAccessCode( ) );
        }
        if ( isUnpublished > 0 )
        {
            filter.setIsUnpulished( isUnpublished );
        }
        if ( StringUtils.isNotBlank( strDateUpdateBlogAfter ) )
        {
            filter.setUpdateDateAfter( DateUtil.formatDate( strDateUpdateBlogAfter, locale ) );
        }
        if ( StringUtils.isNotBlank( strDateUpdateBlogBefore ) )
        {
            filter.setUpdateDateBefor( DateUtil.formatDate( strDateUpdateBlogBefore, locale ) );
        }
        if ( StringUtils.isNotBlank( userEditor ) )
        {
            filter.setUserEditedBlogVersion( userEditor );
        }
        return filter;
    }

    /**
     * Create an HTML link to reach the specified URL
     *
     * @param customLinkUrl
     *            URL of the link
     * @param customLinkText
     *            Text to display in the link
     * @param customLinkTitle
     *            Title of the link
     * @param targetedWindow
     *            Window where the link will be opened ("_self", "_blank", etc.). Opens in new window by default ("_blank")
     * @return the HTML link to the specified url
     */
    public static String buildCustomBlogLink( String customLinkUrl, String customLinkText, String customLinkTitle, String targetedWindow )
    {
        HashMap<String, Object> model = new HashMap<>( );

        model.put( "url", customLinkUrl );
        model.put( "target", targetedWindow );
        model.put( "title", StringEscapeUtils.escapeHtml4( customLinkTitle ) );
        model.put( "text", StringEscapeUtils.escapeHtml4( customLinkText ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_INSERT_BLOG_LINK, null, model );

        return template.getHtml( );
    }

    /**
     * Gets the plugin
     *
     * @return the plugin
     */
    public static Plugin getPlugin( )
    {
        return PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
    }

    public static String removeAccents(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]+", "");
    }
}
