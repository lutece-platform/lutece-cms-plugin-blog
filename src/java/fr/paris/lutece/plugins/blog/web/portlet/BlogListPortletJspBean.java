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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.BlogSearchFilter;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortlet;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.service.PublishingService;
import fr.paris.lutece.plugins.blog.service.docsearch.BlogSearchService;
import fr.paris.lutece.plugins.blog.web.BlogPublicationJspBean;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import java.util.Map;

/**
 * This class provides the user interface to manage BlogList Portlet
 */
public class BlogListPortletJspBean extends PortletJspBean
{
    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LIST_HTMLDOC = "blog_list";
    public static final String MARK_LIST_PAGES = "pages_list";
    public static final String MARK_LIST_HTMLDOC_PUBLISHED = "blog_list_published";

    public static final String PARAMETER_ACTION_PORTLET_ADD = "add";
    public static final String PARAMETER_ACTION_PORTLET_REMOVE = "remove";
    public static final String PARAMETER_ACTION_PORTLET = "action";
    private static final String PARAMETER_PAGE_TEMPLATE_CODE = "page_template_code";

    private static final String PARAMETER_DOCUMENT_ID = "idDocument";
    private static final String PARAMETER_DOCUMENT_ORDER = "orderDocument";
    private static final String PARAMETER_REFRESH_BUTTON = "refresh";
    protected static final String PARAMETER_TAG = "tag_doc";

    protected static final String PARAMETER_SEARCH_TEXT = "search_text";
    protected static final String PARAMETER_UNPUBLISHED = "unpublished";
    protected static final String PARAMETER_DATE_UPDATE_BLOG_AFTER = "dateUpdateBlogAfter";
    protected static final String PARAMETER_DATE_UPDATE_BLOG_BEFOR = "dateUpdateBlogBefor";

    protected static final String PARAMETER_BUTTON_SEARCH = "button_search";
    protected static final String PARAMETER_BUTTON_RESET = "button_reset";

    protected static final String MARK_IS_CHECKED = "is_checked";
    protected static final String MARK_CURRENT_USER = "current_user";
    protected static final String MARK_ID_BLOG = "id";
    protected static final String MARK_SEARCH_TEXT = "search_text";

    protected static final String MARK_PAGINATOR = "paginator";
    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    protected static final String MARK_DATE_UPDATE_BLOG_AFTER = "dateUpdateBlogAfter";
    protected static final String MARK_DATE_UPDATE_BLOG_BEFOR = "dateUpdateBlogBefor";
    protected static final String MARK_UNPUBLISHED = "unpublished";
    protected static final String MARK_LIST_TAG = "list_tag";

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "blog.listItems.itemsPerPage";

    private static final String VIEW_MODIFY_PORTLET = "getModify";

    private static final String RESPONSE_SUCCESS = "SUCCESS";

    // //////////////////////////////////////////////////////////////////////////
    // Constants
    private static final long serialVersionUID = 1L;

    // //////////////////////////////////////////////////////////////////////////
    // Class attributes

    private BlogListPortlet _portlet;

    protected String _strCurrentPageIndex;
    protected int _nItemsPerPage;

    protected int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 20 );

    // Session variable to store working values
    protected boolean _bIsChecked = false;
    protected String _strSearchText;
    protected boolean _bIsUnpulished = false;
    protected String _dateUpdateBlogAfter;
    protected String _dateUpdateBlogBefor;
    protected boolean _bIsSorted = false;
    protected String _strSortedAttributeName;
    protected Boolean _bIsAscSort;
    protected String [ ] _strTag;

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix( )
    {
        return "portlet.blogdocument";
    }

    /**
     * Return a model that contains the list and paginator infos
     * 
     * @param request
     *            The HTTP request
     * @return The map
     */
    protected Map<String, Object> getPaginatedListModel( HttpServletRequest request )
    {

        List<Integer> listBlogsId = new ArrayList<>( );
        AdminUser user = AdminUserService.getAdminUser( request );

        if ( StringUtils.isNotBlank( _strSearchText ) || ArrayUtils.isNotEmpty( _strTag ) || _bIsChecked || _bIsUnpulished || _dateUpdateBlogAfter != null
                || _dateUpdateBlogBefor != null )
        {
            BlogSearchFilter filter = new BlogSearchFilter( );
            if ( StringUtils.isNotBlank( _strSearchText ) )
            {
                filter.setKeywords( _strSearchText );
            }
            if ( ArrayUtils.isNotEmpty( _strTag ) )
            {
                filter.setTag( _strTag );
            }
            if ( _bIsChecked )
            {
                filter.setUser( user.getAccessCode( ) );
            }
            if ( _bIsUnpulished )
            {
                filter.setIsUnpulished( _bIsUnpulished );
            }
            if ( _dateUpdateBlogAfter != null )
            {
                filter.setUpdateDateAfter( DateUtil.formatDate( _dateUpdateBlogAfter, request.getLocale( ) ) );
            }
            if ( _dateUpdateBlogBefor != null )
            {
                filter.setUpdateDateBefor( DateUtil.formatDate( _dateUpdateBlogBefor, request.getLocale( ) ) );
            }

            BlogSearchService.getInstance( ).getSearchResults( filter, listBlogsId );

        }
        else
        {

            listBlogsId = BlogHome.getIdBlogsList( );
        }

        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nDefaultItemsPerPage, _nItemsPerPage );
        List<Blog> listBlogPublished = new ArrayList<>( );
        List<Blog> listBlogNotPublished = new ArrayList<>( );

        for ( BlogPublication i : _portlet.getArrayBlogs( ) )
        {
            Blog blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( i.getIdBlog( ) );
            listBlogPublished.add( blog );
            listBlogsId.removeIf( blg -> blg.equals( blog.getId( ) ) );
        }

        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<>( listBlogsId, _nItemsPerPage, getCurrentUrlFromRequest( request ),
                AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );
        List<Blog> listBlog = new ArrayList<>( );

        for ( Integer documentId : paginator.getPageItems( ) )
        {
            Blog blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( documentId );

            if ( blog != null )
            {
                listBlog.add( blog );
            }
        }

        listBlogNotPublished.addAll( listBlog );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LIST_HTMLDOC, listBlogNotPublished );
        model.put( MARK_LIST_HTMLDOC_PUBLISHED, listBlogPublished );

        model.put( MARK_LIST_TAG, TagHome.getTagsReferenceList( ) );
        model.put( MARK_IS_CHECKED, _bIsChecked );
        model.put( MARK_SEARCH_TEXT, _strSearchText );
        model.put( MARK_DATE_UPDATE_BLOG_AFTER, _dateUpdateBlogAfter );
        model.put( MARK_DATE_UPDATE_BLOG_BEFOR, _dateUpdateBlogBefor );
        model.put( MARK_UNPUBLISHED, _bIsUnpulished );

        model.put( MARK_LIST_PAGES, BlogListPortletHome.loadPages( BlogListPortlet.RESOURCE_ID ) );

        return model;
    }

    /**
     * 
     * @param request
     */
    private void setSearchBlog( HttpServletRequest request, String strButtonSearch, String strButtonReset )
    {

        if ( strButtonSearch != null )
        {
            // CURRENT USER
            _bIsChecked = request.getParameter( MARK_CURRENT_USER ) != null;
            _strSearchText = request.getParameter( PARAMETER_SEARCH_TEXT );
            _strTag = request.getParameterValues( PARAMETER_TAG );
            _bIsUnpulished = request.getParameter( PARAMETER_UNPUBLISHED ) != null;
            _dateUpdateBlogAfter = request.getParameter( PARAMETER_DATE_UPDATE_BLOG_AFTER );
            _dateUpdateBlogBefor = request.getParameter( PARAMETER_DATE_UPDATE_BLOG_BEFOR );

        }
        else
            if ( strButtonReset != null )
            {

                _bIsChecked = false;
                _strSearchText = null;
                _strTag = null;
                _bIsUnpulished = false;
                _dateUpdateBlogAfter = null;
                _dateUpdateBlogBefor = null;

            }
    }

    /**
     * Returns the Download portlet creation form
     *
     * @param request
     *            The http request
     * @return The HTML form
     */
    @Override
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        _portlet = ( _portlet != null && request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) != null ) ? _portlet : new BlogListPortlet( );
        Map<String, Object> model = getPaginatedListModel( request );
        HtmlTemplate template = getCreateTemplate( strIdPage, strIdPortletType, model );

        return template.getHtml( );
    }

    /**
     * Returns the Download portlet modification form
     *
     * @param request
     *            The Http request
     * @return The HTML form
     */
    @View( VIEW_MODIFY_PORTLET )
    @Override
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        _portlet = ( _portlet != null && request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) != null ) ? _portlet
                : (BlogListPortlet) PortletHome.findByPrimaryKey( nPortletId );
        Map<String, Object> model = getPaginatedListModel( request );

        HtmlTemplate template = getModifyTemplate( _portlet, model );

        return template.getHtml( );
    }

    /**
     * Process portlet's creation
     *
     * @param request
     *            The Http request
     * @return The Jsp management URL of the process result
     */
    @Override
    public String doCreate( HttpServletRequest request )
    {

        String button = request.getParameter( PARAMETER_REFRESH_BUTTON );
        _nDefaultItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strButtonSearch = request.getParameter( PARAMETER_BUTTON_SEARCH );
        String strButtonReset = request.getParameter( PARAMETER_BUTTON_RESET );
        int nIdPage = Integer.parseInt( strIdPage );

        if ( ( button != null && button.equals( PARAMETER_REFRESH_BUTTON ) ) || strButtonSearch != null || strButtonReset != null )
        {

            setSearchBlog( request, strButtonSearch, strButtonReset );
            return "../../DoCreatePortlet.jsp?portlet_type_id=" + BlogListPortlet.RESOURCE_ID + "&page_id=" + strIdPage + "&"
                    + AbstractPaginator.PARAMETER_PAGE_INDEX + "=1";
        }

        int order = 1;

        // gets the identifier of the parent page
        String strTemplateCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, _portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        _portlet.setPageId( nIdPage );

        // gets the specific parameters
        _portlet.setPageTemplateDocument( Integer.parseInt( strTemplateCode ) );

        for ( BlogPublication doc : _portlet.getArrayBlogs( ) )
        {

            doc.setBlogOrder( order );
            order++;

        }
        // Portlet creation
        BlogListPortletHome.getInstance( ).create( _portlet );

        for ( BlogPublication doc : _portlet.getArrayBlogs( ) )
        {
            int nbPublication = BlogPublicationHome.countPublicationByIdBlogAndDate( doc.getIdBlog( ), new java.util.Date( ) );
            // First publication of this blog -> indexing needed
            if ( nbPublication == 1 )
            {
                BlogService.getInstance( ).fireCreateBlogEvent( doc.getIdBlog( ) );
            }
        }

        // Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request
     *            The http request
     * @return Management's Url
     */
    @Override
    public String doModify( HttpServletRequest request )
    {

        _nDefaultItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );
        String button = request.getParameter( PARAMETER_REFRESH_BUTTON );
        String strButtonSearch = request.getParameter( PARAMETER_BUTTON_SEARCH );
        String strButtonReset = request.getParameter( PARAMETER_BUTTON_RESET );

        if ( ( button != null && button.equals( PARAMETER_REFRESH_BUTTON ) ) || strButtonSearch != null || strButtonReset != null )
        {

            setSearchBlog( request, strButtonSearch, strButtonReset );
            return "../../DoModifyPortlet.jsp?portlet_id=" + _portlet.getId( ) + "&" + AbstractPaginator.PARAMETER_PAGE_INDEX + "=1";
        }

        int order = 1;

        // recovers portlet attributes
        String strDocumentTypeCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );
        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, _portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        _portlet.setPageTemplateDocument( Integer.parseInt( strDocumentTypeCode ) );

        for ( BlogPublication doc : _portlet.getArrayBlogs( ) )
        {

            doc.setBlogOrder( order );
            order++;

        }
        // updates the portlet
        _portlet.update( );

        for ( Integer removedBlog : _portlet.getRemovedBlogsId( ) )
        {
            int nbPublication = BlogPublicationHome.countPublicationByIdBlogAndDate( removedBlog, new java.util.Date( ) );
            // Last publication of this blog removed -> removing from index
            if ( nbPublication == 0 )
            {
                BlogService.getInstance( ).fireDeleteBlogEvent( removedBlog );
            }
        }

        for ( BlogPublication doc : _portlet.getArrayBlogs( ) )
        {
            int nbPublication = BlogPublicationHome.countPublicationByIdBlogAndDate( doc.getIdBlog( ), new java.util.Date( ) );
            // First publication of this blog -> indexing needed
            if ( nbPublication == 1 )
            {
                BlogService.getInstance( ).fireCreateBlogEvent( doc.getIdBlog( ) );
            }
        }

        // displays the page withe the potlet updated
        return getPageUrl( _portlet.getPageId( ) );
    }

    /**
     * Update blog portlet
     * 
     * @param request
     * @return Json The Json succes or echec
     * @throws ParseException
     */
    public String updatePortletDocument( HttpServletRequest request ) throws ParseException
    {
        // recovers portlet attributes

        String strAction = request.getParameter( PARAMETER_ACTION_PORTLET );
        String strIdDocument = request.getParameter( PARAMETER_DOCUMENT_ID );
        String strOrderDocument = request.getParameter( PARAMETER_DOCUMENT_ORDER );

        int nIdDocument = Integer.parseInt( strIdDocument );
        BlogPublication doc = PublishingService.getInstance( ).getBlogPublication( _portlet.getId( ), nIdDocument );
        if ( doc == null )
        {
            doc = new BlogPublication( );
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            doc.setDateEndPublishing( new Date( sdf.parse( BlogPublicationJspBean.DATE_END_PUBLICATION ).getTime( ) ) );
            doc.setIdBlog( nIdDocument );
        }
        if ( strAction != null && !strAction.isEmpty( ) && strAction.equals( PARAMETER_ACTION_PORTLET_ADD ) )
        {

            int nDocumentOrder = Integer.parseInt( strOrderDocument );
            _portlet.addIdBlogs( nDocumentOrder, doc );

        }
        else
            if ( strAction != null && !strAction.isEmpty( ) && strAction.equals( PARAMETER_ACTION_PORTLET_REMOVE ) )
            {

                _portlet.removeBlogs( doc );

            }

        return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_SUCCESS ) );
    }

    /**
     * 
     * @param request
     * @return
     */
    public static String getCurrentUrlFromRequest( HttpServletRequest request )
    {
        StringBuffer sbRequestURL = request.getRequestURL( );
        String queryString = request.getQueryString( );

        if ( queryString == null )
        {

            return sbRequestURL.toString( );

        }
        else
            if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) != null )
            {

                String [ ] query = queryString.split( "&" + AbstractPaginator.PARAMETER_PAGE_INDEX );
                queryString = query [0];
            }

        return sbRequestURL.append( '?' ).append( queryString ).toString( );
    }

}
