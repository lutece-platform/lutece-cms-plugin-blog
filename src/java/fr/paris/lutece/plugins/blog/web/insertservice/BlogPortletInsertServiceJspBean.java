/*
 * Copyright (c) 2002-2024, City of Paris
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
package fr.paris.lutece.plugins.blog.web.insertservice;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.utils.BlogUtils;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.url.UrlItem;

@SessionScoped
@Named
public class BlogPortletInsertServiceJspBean extends AbstractBlogInsertServiceJspBean
{
    private static final long serialVersionUID = -8820693221945285537L;

    private static final String BLOG_PORTLET_INSERT_SERVICE = "blog_portlet_insert_service";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInsertServiceSelectorUI( HttpServletRequest request )
    {
        setInsertServiceType( BLOG_PORTLET_INSERT_SERVICE );
        return super.getInsertServiceSelectorUI( request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSearchBlogLink( HttpServletRequest request )
    {
        List<Integer> blogIdsList = getBlogIdsWithFilter( request );
        // List of Blog object returned to the user
        List<Blog> blogResultList = new ArrayList<>( );

        for ( Integer blogId : blogIdsList )
        {
            // Only get the content of the blog without any of their attached document
            Blog blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( blogId );
            if ( blog != null )
            {
                blogResultList.add( blog );
            }
        }
        setBlogList( blogResultList );

        // Reload the main view to display the list of blogs matching the user's search
        return getInsertServiceSelectorUI( request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String buildBlogLink( Blog blog, String customlinkText, String customLinkTitle, String targetedWindow )
    {
        // Build the URL for the blog's Portlet
        UrlItem url = new UrlItem( AppPathService.getPortalUrl( ) );
        url.addParameter( MVCUtils.PARAMETER_PAGE, BlogUtils.CONSTANT_PARAMETER_BLOG );
        url.addParameter( BlogUtils.CONSTANT_PARAMETER_ID, blog.getId( ) );

        return BlogUtils.buildCustomBlogLink( url.getUrl( ), StringUtils.isEmpty( customlinkText ) ? blog.getName( ) : customlinkText, customLinkTitle,
                targetedWindow );
    }
}
