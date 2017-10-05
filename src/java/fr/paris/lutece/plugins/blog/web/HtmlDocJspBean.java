/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import fr.paris.lutece.plugins.blog.business.DocContent;
import fr.paris.lutece.plugins.blog.business.DocContentHome;
import fr.paris.lutece.plugins.blog.business.HtmlDoc;
import fr.paris.lutece.plugins.blog.business.HtmlDocHome;
import fr.paris.lutece.plugins.blog.business.HtmldocSearchFilter;
import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.plugins.blog.business.portlet.HtmlDocPublication;
import fr.paris.lutece.plugins.blog.business.portlet.HtmlDocPublicationHome;
import fr.paris.lutece.plugins.blog.service.HtmlDocService;
import fr.paris.lutece.plugins.blog.service.docsearch.HtmlDocSearchService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.resource.ExtendableResourcePluginActionManager;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.portal.service.resource.ExtendableResourceRemovalListenerService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.xml.sax.InputSource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * This class provides the user interface to manage HtmlDoc features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageHtmlDocs.jsp", controllerPath = "jsp/admin/plugins/blog/", right = "HTMLDOCS_MANAGEMENT" )
public class HtmlDocJspBean extends ManageHtmldocsJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_HTMLDOCS = "/admin/plugins/blog/manage_htmldocs.html";
    private static final String TEMPLATE_CREATE_HTMLDOC = "/admin/plugins/blog/create_htmldoc.html";
    private static final String TEMPLATE_MODIFY_HTMLDOC = "/admin/plugins/blog/modify_htmldoc.html";
    private static final String TEMPLATE_HISTORY_HTMLDOC = "admin/plugins/blog/history_htmldoc.html";
    private static final String TEMPLATE_PREVIEW_HTMLDOC = "admin/plugins/blog/preview_htmldoc.html";
    private static final String TEMPLATE_DIFF_HTMLDOC = "admin/plugins/blog/diff_htmldoc.html";

    // Parameters
    protected static final String PARAMETER_ID_HTMLDOC = "id";
    protected static final String PARAMETER_VERSION_HTMLDOC = "htmldoc_version";
    protected static final String PARAMETER_VERSION_HTMLDOC2 = "htmldoc_version2";
    protected static final String PARAMETER_CONTENT_LABEL = "content_label";
    protected static final String PARAMETER_HTML_CONTENT = "html_content";
    protected static final String PARAMETER_EDIT_COMMENT = "edit_comment";
    protected static final String PARAMETER_DESCRIPTION = "description";
    protected static final String PARAMETER_VIEW = "view";
    protected static final String PARAMETER_BUTTON_SEARCH = "button_search";
    protected static final String PARAMETER_SEARCH_TEXT = "search_text";
    protected static final String PARAMETER_UPDATE_ATTACHMENT = "update_attachment";
    protected static final String PARAMETER_TAG = "tag_doc";
    protected static final String PARAMETER_TAG_NAME = "tag_name";

    protected static final String PARAMETER_TAG_TO_REMOVE = "tag_remove";
    protected static final String PARAMETER_SHAREABLE = "shareable";
    protected static final String PARAMETER_PRIORITY = "tag_priority";
    protected static final String PARAMETER_TAG_ACTION = "tagAction";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_HTMLDOCS = "blog.manage_htmldocs.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_HTMLDOC = "blog.modify_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_HTMLDOC = "blog.create_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_HISTORY_HTMLDOC = "blog.history_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_PREVIEW_HTMLDOC = "blog.preview_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_DIFF_HTMLDOC = "blog.diff_htmldoc.pageTitle";
    // Properties
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "blog.listItems.itemsPerPage";

    // Markers
    protected static final String MARK_HTMLDOC_LIST = "htmldoc_list";
    protected static final String MARK_HTMLDOC_VERSION_LIST = "htmldoc_version_list";
    protected static final String MARK_HTMLDOC = "htmldoc";
    protected static final String MARK_WEBAPP_URL = "webapp_url";
    protected static final String MARK_IS_CHECKED = "is_checked";
    protected static final String MARK_CURRENT_USER = "current_user";
    protected static final String MARK_ID_HTMLDOC = "id";
    protected static final String MARK_SEARCH_TEXT = "search_text";
    protected static final String MARK_DIFF = "htmldoc_diff";
    protected static final String MARK_HTMLDOC2 = "htmldoc2";
    protected static final String MARK_LIST_TAG = "list_tag";
    protected static final String MARK_SORTED_ATTRIBUTE = "sorted_attribute_name";
    protected static final String MARK_TAG = "tags";

    private static final String JSP_MANAGE_HTMLDOCS = "jsp/admin/plugins/blog/ManageHtmlDocs.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_HTMLDOC = "blog.message.confirmRemoveHtmlDoc";
    private static final String MESSAGE_ERROR_DOCUMENT_IS_PUBLISHED = "blog.message.errorDocumentIsPublished";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "blog.model.entity.htmldoc.attribute.";

    // Views
    protected static final String VIEW_MANAGE_HTMLDOCS = "manageHtmlDocs";
    private static final String VIEW_CREATE_HTMLDOC = "createHtmlDoc";
    private static final String VIEW_MODIFY_HTMLDOC = "modifyHtmlDoc";
    private static final String VIEW_HISTORY_HTMLDOC = "historyHtmlDoc";
    private static final String VIEW_PREVIEW_HTMLDOC = "previewHtmlDoc";
    private static final String VIEW_DIFF_HTMLDOC = "diffHtmlDoc";

    // Actions
    private static final String ACTION_CREATE_HTMLDOC = "createHtmlDoc";
    private static final String ACTION_MODIFY_HTMLDOC = "modifyHtmlDoc";
    private static final String ACTION_REMOVE_HTMLDOC = "removeHtmlDoc";
    private static final String ACTION_CONFIRM_REMOVE_HTMLDOC = "confirmRemoveHtmlDoc";
    private static final String ACTION_ADD_TAG = "addTag";
    private static final String ACTION_REMOVE_TAG = "removeTag";
    private static final String ACTION_UPDATE_PRIORITY_TAG = "updatePriorityTag";
    // Infos
    private static final String INFO_HTMLDOC_CREATED = "blog.info.htmldoc.created";
    private static final String INFO_HTMLDOC_UPDATED = "blog.info.htmldoc.updated";
    private static final String INFO_HTMLDOC_REMOVED = "blog.info.htmldoc.removed";

    // Filter Marks
    protected static final String MARK_HTMLDOC_FILTER_LIST = "htmldoc_filter_list";
    protected static final String MARK_HTMLDOC_FILTER_NAME = "Nom";
    protected static final String MARK_HTMLDOC_FILTER_DATE = "Date";
    protected static final String MARK_HTMLDOC_FILTER_USER = "Utilisateur";
    protected static final String MARK_PAGINATOR = "paginator";
    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    protected static final String MARK_ASC_SORT = "asc_sort";

    // Session variable to store working values
    protected HtmlDoc _htmldoc;
    protected boolean _bIsChecked = false;
    protected String _strSearchText;
    protected String _strCurrentPageIndex;
    protected int _nItemsPerPage;
    protected int _nDefaultItemsPerPage;
    protected boolean _bIsSorted = false;
    protected String _strSortedAttributeName;
    protected Boolean _bIsAscSort;
    protected String [ ] _strTag;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_HTMLDOCS, defaultView = true )
    public String getManageHtmlDocs( HttpServletRequest request )
    {
        _htmldoc = null;
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        // SORT
        String strSortedAttributeName = request.getParameter( MARK_SORTED_ATTRIBUTE );
        String strAscSort = null;

        AdminUser user = AdminUserService.getAdminUser( request );
        List<Integer> listHtmlDocsId = new ArrayList<Integer>( );
        String strButtonSearch = request.getParameter( PARAMETER_BUTTON_SEARCH );
        if ( strButtonSearch != null )
        {
            // CURRENT USER
            _bIsChecked = request.getParameter( MARK_CURRENT_USER ) != null;
            _strSearchText = request.getParameter( PARAMETER_SEARCH_TEXT );
            _strTag = request.getParameterValues( PARAMETER_TAG );
        }

        if ( StringUtils.isNotBlank( _strSearchText ) || ( _strTag != null && _strTag.length > 0 ) || _bIsChecked )
        {
            HtmldocSearchFilter filter = new HtmldocSearchFilter( );
            if ( StringUtils.isNotBlank( _strSearchText ) )
                filter.setKeywords( _strSearchText );
            if ( _strTag != null && ( _strTag.length > 0 ) )
                filter.setTag( _strTag );
            if ( _bIsChecked )
                filter.setUser( user.getFirstName( ) );
            HtmlDocSearchService.getInstance( ).getSearchResults( filter, listHtmlDocsId );

        }

        else
        {

            listHtmlDocsId = HtmlDocHome.getIdHtmlDocsList( );
        }

        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<Integer>( (List<Integer>) listHtmlDocsId, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        List<HtmlDoc> listDocuments = new ArrayList<HtmlDoc>( );

        for ( Integer documentId : paginator.getPageItems( ) )
        {
            HtmlDoc document = HtmlDocService.getInstance( ).findByPrimaryKeyWithoutBinaries( documentId );

            if ( document != null )
            {
                listDocuments.add( document );
            }
        }

        if ( strSortedAttributeName != null || _bIsSorted == true )
        {
            if ( strSortedAttributeName == null )
            {
                strSortedAttributeName = _strSortedAttributeName;
            }
            strAscSort = request.getParameter( MARK_ASC_SORT );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );
            if ( strAscSort == null )
            {
                bIsAscSort = _bIsAscSort;
            }

            Collections.sort( listDocuments, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );

            _bIsSorted = true;

            _strSortedAttributeName = strSortedAttributeName;
            _bIsAscSort = bIsAscSort;
        }

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_HTMLDOC_LIST, listDocuments );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_HTMLDOC_FILTER_LIST, getHtmldocFilterList( ) );
        model.put( MARK_LIST_TAG, TagHome.getTagsReferenceList( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_IS_CHECKED, _bIsChecked );
        model.put( MARK_SEARCH_TEXT, _strSearchText );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_TAG, _strTag );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_HTMLDOCS, TEMPLATE_MANAGE_HTMLDOCS, model );
    }

    @View( value = VIEW_HISTORY_HTMLDOC )
    public String getHistoryHtmlDoc( HttpServletRequest request )
    {
        _htmldoc = null;
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        List<HtmlDoc> listHtmlDocsVersions = HtmlDocHome.getHtmlDocsVersionsList( nId );

        UrlItem urlHistory = new UrlItem( JSP_MANAGE_HTMLDOCS );
        urlHistory.addParameter( PARAMETER_VIEW, VIEW_HISTORY_HTMLDOC );
        urlHistory.addParameter( PARAMETER_ID_HTMLDOC, nId );

        Map<String, Object> model = getPaginatedListModel( request, MARK_HTMLDOC_LIST, listHtmlDocsVersions, urlHistory.getUrl( ) );

        model.put( MARK_ID_HTMLDOC, nId );

        return getPage( PROPERTY_PAGE_TITLE_HISTORY_HTMLDOC, TEMPLATE_HISTORY_HTMLDOC, model );
    }

    /**
     * Returns the form to create a htmldoc
     *
     * @param request
     *            The Http request
     * @return the html code of the htmldoc form
     */
    @View( VIEW_CREATE_HTMLDOC )
    public String getCreateHtmlDoc( HttpServletRequest request )
    {
        _htmldoc = ( _htmldoc != null ) ? _htmldoc : new HtmlDoc( );

        Map<String, Object> model = getModel( );
        model.put( MARK_HTMLDOC, _htmldoc );
        model.put( MARK_LIST_TAG, getTageList( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_HTMLDOC, TEMPLATE_CREATE_HTMLDOC, model );
    }

    /**
     * Gets the current
     *
     * @return the current date in sql format
     */
    public java.sql.Timestamp getSqlDate( )
    {
        java.util.Date utilDate = new java.util.Date( );
        java.sql.Timestamp sqlDate = new java.sql.Timestamp( utilDate.getTime( ) );

        return ( sqlDate );
    }

    /**
     * Process the data capture form of a new htmldoc
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_HTMLDOC )
    public String doCreateHtmlDoc( HttpServletRequest request )
    {

        _htmldoc.setCreationDate( getSqlDate( ) );
        _htmldoc.setUpdateDate( getSqlDate( ) );
        _htmldoc.setUser( AdminUserService.getAdminUser( request ).getFirstName( ) );
        _htmldoc.setUserCreator( AdminUserService.getAdminUser( request ).getFirstName( ) );
        _htmldoc.setVersion( 1 );
        _htmldoc.setAttachedPortletId( 0 );
        populate( _htmldoc, request );

        // Check constraints
        if ( !validateBean( _htmldoc, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_HTMLDOC );
        }

        // HtmlDocHome.addInitialVersion( _htmldoc );
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        DocContent docContent = setContent( multipartRequest, request.getLocale( ) );
        HtmlDocService.getInstance( ).createDocument( _htmldoc, docContent );

        addInfo( INFO_HTMLDOC_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_HTMLDOCS );
    }

    @Action( ACTION_ADD_TAG )
    public String doAddTag( HttpServletRequest request )
    {
        String strIdTag = request.getParameter( PARAMETER_TAG );
        String strTagName = request.getParameter( PARAMETER_TAG_NAME );

        Tag tag = new Tag( Integer.parseInt( strIdTag ), _htmldoc.getTag( ).size( ) + 1 );
        tag.setName( strTagName );

        _htmldoc.addTag( tag );

        return JsonUtil.buildJsonResponse( new JsonResponse( "SUCESS" ) );

    }

    @Action( ACTION_REMOVE_TAG )
    public String doRemoveTag( HttpServletRequest request )
    {
        String strIdTag = request.getParameter( PARAMETER_TAG );
        Tag tag = new Tag( );
        tag.setIdTag( Integer.parseInt( strIdTag ) );
        _htmldoc.deleteTag( tag );

        return JsonUtil.buildJsonResponse( new JsonResponse( "SUCESS" ) );

    }

    @Action( ACTION_UPDATE_PRIORITY_TAG )
    public String doUpdatePriorityTag( HttpServletRequest request )
    {
        Tag tg = null;
        Tag tagMove = null;
        int nPriorityToSet = 0;
        int nPriority = 0;

        String strIdTag = request.getParameter( PARAMETER_TAG );
        String strAction = request.getParameter( PARAMETER_TAG_ACTION );

        for ( Tag tag : _htmldoc.getTag( ) )
        {
            if ( tag.getIdTag( ) == Integer.parseInt( strIdTag ) )
            {
                tg = tag;
                nPriorityToSet = tag.getPriority( );
                nPriority = tag.getPriority( );
            }
        }
        for ( Tag tag : _htmldoc.getTag( ) )
        {
            if ( strAction.equals( "moveUp" ) && tag.getPriority( ) == nPriority - 1 )
            {
                tagMove = tag;
                tagMove.setPriority( tagMove.getPriority( ) + 1 );
                nPriorityToSet = nPriority - 1;

            }
            else
                if ( strAction.equals( "moveDown" ) && tag.getPriority( ) == nPriority + 1 )
                {
                    tagMove = tag;
                    tagMove.setPriority( tagMove.getPriority( ) - 1 );
                    nPriorityToSet = nPriority + 1;

                }
        }
        tg.setPriority( nPriorityToSet );

        if ( tagMove != null )
        {

            return JsonUtil.buildJsonResponse( new JsonResponse( String.valueOf( tagMove.getIdTag( ) ) ) );

        }
        return JsonUtil.buildJsonResponse( new JsonResponse( String.valueOf( tg.getIdTag( ) ) ) );

    }

    /**
     * Manages the removal form of a htmldoc whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_HTMLDOC )
    public String getConfirmRemoveHtmlDoc( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_HTMLDOC ) );
        url.addParameter( PARAMETER_ID_HTMLDOC, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_HTMLDOC, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a htmldoc
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage htmldocs
     */
    @Action( ACTION_REMOVE_HTMLDOC )
    public String doRemoveHtmlDoc( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        List<HtmlDocPublication> docPublication = HtmlDocPublicationHome.getDocPublicationByIdDoc( nId );

        if ( docPublication.size( ) > 0 )
        {
            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DOCUMENT_IS_PUBLISHED, AdminMessage.TYPE_STOP );

            return redirect( request, strMessageUrl );
        }
        HtmlDocService.getInstance( ).deleteDocument( nId );
        ExtendableResourceRemovalListenerService.doRemoveResourceExtentions( HtmlDoc.PROPERTY_RESOURCE_TYPE, String.valueOf( nId ) );

        addInfo( INFO_HTMLDOC_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_HTMLDOCS );
    }

    /**
     * Returns the form to update info about a htmldoc
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_HTMLDOC )
    public String getModifyHtmlDoc( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        String strResetVersion = request.getParameter( PARAMETER_VERSION_HTMLDOC );
        int nVersion = -1;
        if ( strResetVersion != null )
        {
            nVersion = Integer.parseInt( strResetVersion );
        }

        if ( _htmldoc == null || ( _htmldoc.getId( ) != nId ) || ( strResetVersion != null && _htmldoc.getVersion( ) != nVersion ) )
        {
            if ( strResetVersion != null )
            {
                _htmldoc = HtmlDocHome.findVersion( nId, nVersion );
            }
            else
            {
                _htmldoc = HtmlDocService.getInstance( ).loadDocument( nId );
            }
            // _htmldoc.setEditComment("");
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_HTMLDOC, _htmldoc );
        model.put( MARK_LIST_TAG, getTageList( ) );

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        ExtendableResourcePluginActionManager.fillModel( request, getUser( ), model, String.valueOf( nId ), HtmlDoc.PROPERTY_RESOURCE_TYPE );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_HTMLDOC, TEMPLATE_MODIFY_HTMLDOC, model );
    }

    /**
     * Process the change form of a htmldoc
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_HTMLDOC )
    public String doModifyHtmlDoc( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        String strHtmlContent = request.getParameter( PARAMETER_HTML_CONTENT );
        String strEditComment = request.getParameter( PARAMETER_EDIT_COMMENT );
        String strContentLabel = request.getParameter( PARAMETER_CONTENT_LABEL );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strShareable = request.getParameter( PARAMETER_SHAREABLE );

        String strUpdate_attachment = request.getParameter( PARAMETER_UPDATE_ATTACHMENT );
        boolean bIsUpdatable = ( ( strUpdate_attachment == null ) || strUpdate_attachment.equals( "" ) ) ? false : true;

        HtmlDoc latestVersionHtmlDoc = HtmlDocHome.findByPrimaryKey( nId );
        if ( _htmldoc == null || ( _htmldoc.getId( ) != nId ) )
        {
            _htmldoc = latestVersionHtmlDoc;
        }

        _htmldoc.setContentLabel( strContentLabel );
        _htmldoc.setDescription( strDescription );
        _htmldoc.setShareable( Boolean.parseBoolean( strShareable ) );
        _htmldoc.setHtmlContent( strHtmlContent );
        _htmldoc.setEditComment( strEditComment );
        _htmldoc.setUpdateDate( getSqlDate( ) );
        _htmldoc.setUser( AdminUserService.getAdminUser( request ).getFirstName( ) );

        // Check constraints
        if ( !validateBean( _htmldoc, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_HTMLDOC, PARAMETER_ID_HTMLDOC, _htmldoc.getId( ) );
        }

        _htmldoc.setVersion( latestVersionHtmlDoc.getVersion( ) + 1 );

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        DocContent docContent = setContent( multipartRequest, request.getLocale( ) );
        if ( bIsUpdatable && docContent == null )
        {

            DocContentHome.remove( nId );
            docContent = null;
        }
        else
            if ( docContent == null )
            {

                docContent = _htmldoc.getDocContent( );

            }

        HtmlDocService.getInstance( ).updateDocument( _htmldoc, docContent );

        addInfo( INFO_HTMLDOC_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_HTMLDOCS );
    }

    /**
     * Returns the preview of an htmldoc
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_PREVIEW_HTMLDOC )
    public String getPreviewHtmlDoc( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        String strVersion = request.getParameter( PARAMETER_VERSION_HTMLDOC );
        int nVersion = -1;
        if ( strVersion != null )
        {
            nVersion = Integer.parseInt( strVersion );
        }

        HtmlDoc htmldoc;
        if ( strVersion != null )
        {
            htmldoc = HtmlDocHome.findVersion( nId, nVersion );
        }
        else
        {
            htmldoc = HtmlDocService.getInstance( ).loadDocument( nId );
        }
        htmldoc.setHtmldocPubilcation( HtmlDocPublicationHome.getDocPublicationByIdDoc( nId ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_LIST_TAG, TagHome.getTagsReferenceList( ) );

        model.put( MARK_HTMLDOC, htmldoc );

        ExtendableResourcePluginActionManager.fillModel( request, getUser( ), model, String.valueOf( nId ), HtmlDoc.PROPERTY_RESOURCE_TYPE );

        return getPage( PROPERTY_PAGE_TITLE_PREVIEW_HTMLDOC, TEMPLATE_PREVIEW_HTMLDOC, model );
    }

    /**
     * Returns the diff of two htmldocs versions
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_DIFF_HTMLDOC )
    public String getDiffHtmlDoc( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        String strVersion = request.getParameter( PARAMETER_VERSION_HTMLDOC );
        int nVersion = -1;
        if ( strVersion != null )
        {
            nVersion = Integer.parseInt( strVersion );
        }
        String strVersion2 = request.getParameter( PARAMETER_VERSION_HTMLDOC2 );

        HtmlDoc htmldoc;
        if ( strVersion != null )
        {
            htmldoc = HtmlDocHome.findVersion( nId, nVersion );
        }
        else
        {
            htmldoc = HtmlDocHome.findByPrimaryKey( nId );
        }

        int nVersion2 = htmldoc.getVersion( ) - 1;
        if ( strVersion2 != null )
        {
            nVersion2 = Integer.parseInt( strVersion2 );
        }

        HtmlDoc htmldoc2 = HtmlDocHome.findVersion( nId, nVersion2 );
        if ( htmldoc2 == null )
        {
            htmldoc2 = HtmlDocHome.findByPrimaryKey( nId );
        }

        if ( htmldoc2.getVersion( ) > htmldoc.getVersion( ) )
        {
            HtmlDoc tmp = htmldoc2;
            htmldoc2 = htmldoc;
            htmldoc = tmp;
        }

        String strDiff = null;
        HtmlCleaner cleaner = new HtmlCleaner( );
        try
        {
            SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance( );
            TransformerHandler result = tf.newTransformerHandler( );
            result.getTransformer( ).setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
            StringWriter resultWriter = new StringWriter( );
            result.setResult( new StreamResult( resultWriter ) );
            Locale locale = getLocale( );

            DomTreeBuilder oldHandler = new DomTreeBuilder( );
            cleaner.cleanAndParse( new InputSource( new ByteArrayInputStream( htmldoc2.getHtmlContent( ).getBytes( "UTF-8" ) ) ), oldHandler );
            TextNodeComparator leftComparator = new TextNodeComparator( oldHandler, locale );

            DomTreeBuilder newHandler = new DomTreeBuilder( );
            cleaner.cleanAndParse( new InputSource( new ByteArrayInputStream( htmldoc.getHtmlContent( ).getBytes( "UTF-8" ) ) ), newHandler );
            TextNodeComparator rightComparator = new TextNodeComparator( newHandler, locale );

            HtmlSaxDiffOutput output = new HtmlSaxDiffOutput( result, "" );
            HTMLDiffer differ = new HTMLDiffer( output );
            differ.diff( leftComparator, rightComparator );

            strDiff = resultWriter.toString( );
        }
        catch( Exception e )
        {
            AppLogService.error( "Error generating daisy diff for htmldoc " + nId + ":" + htmldoc.getContentLabel( ) + "; versions (" + htmldoc.getVersion( )
                    + "," + htmldoc2.getVersion( ) + ")", e );
        }

        List<HtmlDoc> listHtmlDocsVersions = HtmlDocHome.getHtmlDocsVersionsList( nId );

        Map<String, Object> model = getModel( );
        model.put( MARK_HTMLDOC, htmldoc );
        model.put( MARK_HTMLDOC2, htmldoc2 );
        model.put( MARK_DIFF, strDiff );
        model.put( MARK_HTMLDOC_VERSION_LIST, listHtmlDocsVersions );

        return getPage( PROPERTY_PAGE_TITLE_DIFF_HTMLDOC, TEMPLATE_DIFF_HTMLDOC, model );
    }

    private ReferenceList getHtmldocFilterList( )
    {
        ReferenceList list = new ReferenceList( );
        list.addItem( MARK_HTMLDOC_FILTER_NAME, "Nom" );
        list.addItem( MARK_HTMLDOC_FILTER_DATE, "Date" );
        list.addItem( MARK_HTMLDOC_FILTER_USER, "Utilisateur" );

        return list;
    }

    public DocContent setContent( MultipartHttpServletRequest mRequest, Locale locale )
    {

        FileItem fileParameterBinaryValue = mRequest.getFile( "attachment" );
        // boolean bToResize = ( ( strToResize == null ) || strToResize.equals( "" ) ) ? false : true;

        if ( fileParameterBinaryValue != null ) // If the field is a file
        {

            String strContentType = fileParameterBinaryValue.getContentType( );
            byte [ ] bytes = fileParameterBinaryValue.get( );
            String strFileName = fileParameterBinaryValue.getName( );

            if ( !ArrayUtils.isEmpty( bytes ) )
            {

                DocContent docContent = new DocContent( );
                docContent.setBinaryValue( bytes );
                docContent.setValueContentType( strContentType );
                docContent.setTextValue( strFileName );

                return docContent;
            }

        }

        return null;
    }

    /**
     * 
     * @return htmlDocList
     */
    private ReferenceList getTageList( )
    {

        ReferenceList htmlDocList = TagHome.getTagsReferenceList( );
        int index = 0;

        for ( ReferenceItem item : TagHome.getTagsReferenceList( ) )
        {
            for ( Tag tg : _htmldoc.getTag( ) )
            {
                if ( item.getCode( ).equals( String.valueOf( tg.getIdTag( ) ) ) )
                {

                    htmlDocList.remove( index );
                    index--;
                }
            }
            index++;
        }

        return htmlDocList;
    }
}
