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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.insertservice.BlogLinkPOJO;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.utils.BlogUtils;
import fr.paris.lutece.util.url.UrlItem;

public class BlogResourceInsertServiceJspBean extends AbstractBlogInsertServiceJspBean
{
    private static final long serialVersionUID = 6107987688167050292L;

    // Parameters
    private static final String PARAMETER_SELECTED_DOCUMENT_ID = "selected_document_id";

    // Blog Servlet URL
    private static final String DOCUMENT_RESOURCE_SERVLET_URL = "servlet/plugins/blogs/file";

    // Constants
    private static final String CONSTANT_BLOG_RESOURCE_INSERT_SERVICE = "blog_resource_insert_service";
    private static final String CONSTANT_ID_FILE = "id_file";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInsertServiceSelectorUI( HttpServletRequest request )
    {
        setInsertServiceType( CONSTANT_BLOG_RESOURCE_INSERT_SERVICE );
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
            Blog blog = BlogService.getInstance( ).loadBlog( blogId );
            // Check if the specified blog exists and contains any document
            if ( blog != null && CollectionUtils.isNotEmpty( blog.getDocContent( ) ) )
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
    public String doInsertBlogLink( HttpServletRequest request )
    {
        String strInput = request.getParameter( PARAMETER_INPUT );

        // Retrieve the specified values (url, text...) for the link being created
        BlogLinkPOJO blogLinkContent = getSelectedBlogData( request );

        // Retrieve the ID of the document Resource which has been selected
        String strSelectedDocumentId = request.getParameter( PARAMETER_SELECTED_DOCUMENT_ID );
        int documentId = Integer.parseInt( strSelectedDocumentId );

        // Build the complete link
        String strBlogLink = buildBlogResourceLink( documentId, blogLinkContent.getBlogLinkText( ), blogLinkContent.getBlogLinkTitle( ),
                blogLinkContent.getBlogLinkTargetedWindow( ) );

        // Insert the link in the editor
        return insertUrlWithoutEscape( request, strInput, strBlogLink );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BlogLinkPOJO getSelectedBlogData( HttpServletRequest request )
    {
        // Get the text to display on the blog link
        String blogLinkText = request.getParameter( PARAMETER_LINK_TEXT );

        // Get the title of the link
        String blogLinkTitle = request.getParameter( PARAMETER_LINK_TITLE );

        // Get the type of target window to use for the links
        String targetedWindow = BlogUtils.CONSTANT_LINK_TARGET_NEW_WINDOW;

        return new BlogLinkPOJO( blogLinkText, blogLinkTitle, targetedWindow, null );
    }

    /**
     * Build a URL link to reach a blog's Resource (file, image...)
     * 
     * @param blogResourceId
     *            The ID of the resource
     * @param customlinkText
     *            The text shown in the link
     * @param customLinkTitle
     *            The title of the link
     * @param targetedWindow
     *            The window targeted when the link is clicked
     * @return an HTML link for the Resource's URL
     */
    private String buildBlogResourceLink( int blogResourceId, String customlinkText, String customLinkTitle, String targetedWindow )
    {
        // Build the URL of the blog's Resource document
        UrlItem url = new UrlItem( DOCUMENT_RESOURCE_SERVLET_URL );
        url.addParameter( CONSTANT_ID_FILE, blogResourceId );

        return BlogUtils.buildCustomBlogLink( url.getUrl( ), customlinkText, customLinkTitle, targetedWindow );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String buildBlogLink( Blog blog, String customlinkText, String customLinkTitle, String targetedWindow )
    {
        return null;
    }
}
