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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogSearchFilter;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.plugins.blog.business.insertservice.BlogLinkPOJO;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.service.docsearch.BlogSearchService;
import fr.paris.lutece.plugins.blog.utils.BlogUtils;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.insert.InsertServiceJspBean;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectionBean;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to insert a link toward a blog post or a blog's Resource
 *
 */
public abstract class AbstractBlogInsertServiceJspBean extends InsertServiceJspBean implements InsertServiceSelectionBean
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 5418289952051358700L;

    // Templates
    private static final String TEMPLATE_SELECT_BLOG_LINK = "admin/plugins/blog/insertservice/select_blog_link.html";

    // Parameters
    protected static final String PARAMETER_INPUT = "input";
    protected static final String PARAMETER_SELECTED_BLOG_ID = "selected_blog_id";
    protected static final String PARAMETER_LINK_TEXT = "custom_link_text";
    protected static final String PARAMETER_LINK_TITLE = "custom_link_title";
    private static final String PARAMETER_BLOG_SEARCH_TEXT = "search_text";
    private static final String PARAMETER_SELECTED_TAGS = "selected_tags";
    private static final String PARAMETER_DATE_BLOG_PUBLISHED_AFTER = "date_blog_published_after";
    private static final String PARAMETER_DATE_BLOG_PUBLISHED_BEFORE = "date_blog_published_before";

    // Marks
    private static final String MARK_INPUT = "input";
    private static final String MARK_LANGUAGE = "language";
    private static final String MARK_BLOG_SEARCH_TEXT = "search_text";
    private static final String MARK_TAGS = "tags";
    private static final String MARK_TAGS_REFERENCE_LIST = "tags_reference_list";
    private static final String MARK_BLOG_LIST_FILTERED = "blog_list_filtered";
    private static final String MARK_BLOG_INSERT_SERVICE_TYPE = "blog_insert_service_type";

    // List of the blogs found after the search
    private Collection<Blog> _blogsList;

    // List of selected tags for the filtered search
    private String [ ] _tagsArray;

    // Stores the type of Insert Service to be used in the main view's template
    private String _insertServiceType = "blog_insert_service";

    /**
     * Get the HTML form where the blog to insert is selected
     *
     * @param request
     *            The http request
     * @return the HTML code to display
     */
    @Override
    public String getInsertServiceSelectorUI( HttpServletRequest request )
    {
        Locale locale = AdminUserService.getLocale( request );

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_INPUT, request.getParameter( PARAMETER_INPUT ) );
        model.put( MARK_LANGUAGE, locale );
        // Set the type of Blog Insert Service being used
        model.put( MARK_BLOG_INSERT_SERVICE_TYPE, getInsertServiceType( ) );

        // Set the value of the keywords being searched for
        String blogSearchTextString = request.getParameter( PARAMETER_BLOG_SEARCH_TEXT );
        model.put( MARK_BLOG_SEARCH_TEXT, blogSearchTextString );

        // Display the available Tags in the selection field
        model.put( MARK_TAGS_REFERENCE_LIST, TagHome.getTagsReferenceList( ) );
        // Set the Tags selected by the user
        model.put( MARK_TAGS, _tagsArray );

        // Set the list of blog posts matching the user's search
        model.put( MARK_BLOG_LIST_FILTERED, _blogsList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECT_BLOG_LINK, locale, model );

        return template.getHtml( );
    }

    /**
     * Insert the link toward the selected blog post element into the html editor
     *
     * @param request
     *            The http request
     * @return the link to display in the html code
     */
    public String doInsertBlogLink( HttpServletRequest request )
    {
        String strInput = request.getParameter( PARAMETER_INPUT );

        BlogLinkPOJO blogLinkContent = getSelectedBlogData( request );

        String strBlogLink = buildBlogLink( blogLinkContent.getBlog( ), blogLinkContent.getBlogLinkText( ), blogLinkContent.getBlogLinkTitle( ),
                blogLinkContent.getBlogLinkTargetedWindow( ) );

        return insertUrl( request, strInput, strBlogLink );
    }

    /**
     * Get the values used to buil the blog link (text, title...), from the submitted form
     *
     * @param request
     *            The http request containing the submitted parameters
     * @return a BlogLinkPOJO containing the data necessary to build a blog link
     */
    protected BlogLinkPOJO getSelectedBlogData( HttpServletRequest request )
    {
        // Get the ID of the chosen blog
        String strSelectedBlogId = request.getParameter( PARAMETER_SELECTED_BLOG_ID );
        int selectedBlogId = 0;

        if ( StringUtils.isNumeric( strSelectedBlogId ) )
        {
            selectedBlogId = Integer.parseInt( strSelectedBlogId );
        }

        // Get the text to display on the blog link
        String blogLinkText = request.getParameter( PARAMETER_LINK_TEXT );

        // Get the title of the link
        String blogLinkTitle = request.getParameter( PARAMETER_LINK_TITLE );

        // Get the type of target window to use for the links
        String targetedWindow = BlogUtils.CONSTANT_LINK_TARGET_NEW_WINDOW;

        Blog blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( selectedBlogId );

        return new BlogLinkPOJO( blogLinkText, blogLinkTitle, targetedWindow, blog );
    }

    /**
     * Get the values specified in the search filter and look for the blogs matching them
     *
     * @param request
     *            The http request containing the submitted filter values
     * @return a List of Integer corresponding to the blogs matching the search filter
     */
    protected List<Integer> getBlogIdsWithFilter( HttpServletRequest request )
    {
        // Get the potential filters from the form
        String blogSearchText = request.getParameter( PARAMETER_BLOG_SEARCH_TEXT );
        _tagsArray = request.getParameterValues( PARAMETER_SELECTED_TAGS );
        String strPublishedDateAfter = request.getParameter( PARAMETER_DATE_BLOG_PUBLISHED_AFTER );
        String strPublishedDateBefore = request.getParameter( PARAMETER_DATE_BLOG_PUBLISHED_BEFORE );

        // List of the Blog IDs matching the search
        List<Integer> blogIdsList = new ArrayList<>( );

        // Create a search filter to find specific blogs
        BlogSearchFilter filter = BlogUtils.buildBlogSearchFilter( blogSearchText, _tagsArray, null, 0, strPublishedDateAfter, strPublishedDateBefore, null,
                AdminUserService.getLocale( request ) );

        // Get a List of the IDs of indexed blogs that match the filters
        BlogSearchService.getInstance( ).getSearchResults( filter, blogIdsList );

        return blogIdsList;
    }

    /**
     * Search for the blogs matching the user's search and reload the HTML form where the blog link to insert is selected
     *
     * @param request
     *            The http request
     * @return the HTML code to display the blogs that can be selected to create a link from
     */
    public abstract String doSearchBlogLink( HttpServletRequest request );

    /**
     * Create an HTML link to reach the URL of the specified blog post
     *
     * @param blog
     *            Blog object for which the link is created
     * @param customlinkText
     *            Text to display in the link
     * @param customLinkTitle
     *            Title of the link
     * @param targetedWindow
     *            Window where the link will be opened ("_self", "_blank", etc.). Opens in new window by default ("_blank")
     * @return the HTML link to the specified Blog
     */
    protected abstract String buildBlogLink( Blog blog, String customlinkText, String customLinkTitle, String targetedWindow );

    /**
     * Get the type of the Blog Insert Service being used
     *
     * @return the name of the Blog Insert Service type
     */
    protected String getInsertServiceType( )
    {
        return _insertServiceType;
    }

    /**
     * Set the type of the Blog Insert Service being used
     *
     * @param strInsertServiceType
     *            The type of Insert Service used
     */
    protected void setInsertServiceType( String strInsertServiceType )
    {
        _insertServiceType = strInsertServiceType;
    }

    /**
     * Set the List of Blogs found by the search
     *
     * @param blogsList
     *            The List of Blog objects found
     */
    protected void setBlogList( Collection<Blog> blogsList )
    {
        _blogsList = blogsList;
    }
}
