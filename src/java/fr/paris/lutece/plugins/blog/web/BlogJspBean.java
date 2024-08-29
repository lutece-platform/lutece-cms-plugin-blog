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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import fr.paris.lutece.plugins.blog.utils.BlogUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.xml.sax.InputSource;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.BlogSearchFilter;
import fr.paris.lutece.plugins.blog.business.ContentType;
import fr.paris.lutece.plugins.blog.business.DocContent;
import fr.paris.lutece.plugins.blog.business.DocContentHome;
import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.service.BlogServiceSession;
import fr.paris.lutece.plugins.blog.service.BlogSessionListner;
import fr.paris.lutece.plugins.blog.service.docsearch.BlogSearchService;
import fr.paris.lutece.plugins.blog.utils.BlogLock;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.resource.ExtendableResourceRemovalListenerService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.resource.ExtendableResourcePluginActionManager;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.plugins.blog.business.BlogAdminDashboardHome;
import fr.paris.lutece.plugins.blog.web.utils.BlogConstant;
import static fr.paris.lutece.plugins.blog.web.adminDashboard.BlogAdminDashboardJspBean.RIGHT_AVANCED_CONFIGURATION;

/**
 * This class provides the user interface to manage Blog features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageBlogs.jsp", controllerPath = "jsp/admin/plugins/blog/", right = "BLOG_MANAGEMENT" )
public class BlogJspBean extends ManageBlogJspBean
{
    private static final long serialVersionUID = 9149742923528515045L;

    // Templates
    private static final String TEMPLATE_MANAGE_BLOGS = "/admin/plugins/blog/manage_blogs.html";
    private static final String TEMPLATE_CREATE_BLOG = "/admin/plugins/blog/create_blog.html";
    private static final String TEMPLATE_MODIFY_BLOG = "/admin/plugins/blog/modify_blog.html";
    private static final String TEMPLATE_HISTORY_BLOG = "admin/plugins/blog/history_blog.html";
    private static final String TEMPLATE_PREVIEW_BLOG = "admin/plugins/blog/preview_blog.html";
    private static final String TEMPLATE_DIFF_BLOG = "admin/plugins/blog/diff_blog.html";

    // Parameters
    protected static final String PARAMETER_ID_BLOG = "id";
    protected static final String PARAMETER_VERSION_BLOG = "blog_version";
    protected static final String PARAMETER_VERSION_BLOG2 = "blog_version2";
    protected static final String PARAMETER_CONTENT_LABEL = "content_label";
    protected static final String PARAMETER_HTML_CONTENT = "html_content";
    protected static final String PARAMETER_EDIT_COMMENT = "edit_comment";
    protected static final String PARAMETER_DESCRIPTION = "description";
    protected static final String PARAMETER_FILE_NAME = "fileName";
    protected static final String PARAMETER_VIEW = "view";
    protected static final String PARAMETER_BUTTON_SEARCH = "button_search";
    protected static final String PARAMETER_BUTTON_RESET = "button_reset";

    protected static final String PARAMETER_SEARCH_TEXT = "search_text";
    protected static final String PARAMETER_UPDATE_ATTACHMENT = "update_attachment";
    protected static final String PARAMETER_TAG = "tag_doc";
    protected static final String PARAMETER_TAG_NAME = "tag_name";
    protected static final String PARAMETER_URL = "url";
    protected static final String PARAMETER_UNPUBLISHED = "unpublished";
    protected static final String PARAMETER_DATE_UPDATE_BLOG_AFTER = "dateUpdateBlogAfter";
    protected static final String PARAMETER_DATE_UPDATE_BLOG_BEFOR = "dateUpdateBlogBefor";

    protected static final String PARAMETER_TAG_TO_REMOVE = "tag_remove";
    protected static final String PARAMETER_SHAREABLE = "shareable";
    protected static final String PARAMETER_PRIORITY = "tag_priority";
    protected static final String PARAMETER_TAG_ACTION = "tagAction";
    protected static final String PARAMETER_ACTION_BUTTON = "button";
    protected static final String PARAMETER_APPLY = "apply";
    protected static final String PARAMETER_TYPE_ID = "idType";
    protected static final String PARAMETER_CONTENT_ID = "idContent";
    protected static final String PARAMETER_CONTENT_ACTION = "contentAction";
    protected static final String PARAMETER_INFO_MESSAGE = "info_message";
    protected static final String PARAMETER_TO_ARCHIVE = "to_archive";
    protected static final String PARAMETER_SELECTED_BLOGS = "select_blog_id";
    protected static final String PARAMETER_SELECTED_BLOG_ACTION = "select_blog_action";
    protected static final String PARAMETER_SELECTED_BLOG_IDS_LIST = "selected_blogs_list";


    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_BLOG = "blog.manage_blog.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_BLOG = "blog.modify_blog.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_BLOG = "blog.create_blog.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_HISTORY_BLOG = "blog.history_blog.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_PREVIEW_BLOG = "blog.preview_blog.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_DIFF_BLOG = "blog.diff_blog.pageTitle";
    protected static final String PROPERTY_USE_UPLOAD_IMAGE_PLUGIN = "use_upload_image_plugin";

    // Markers
    protected static final String MARK_BLOG_LIST = "blog_list";
    protected static final String MARK_BLOG_VERSION_LIST = "blog_version_list";
    protected static final String MARK_BLOG = "blog";
    protected static final String MARK_WEBAPP_URL = "webapp_url";
    protected static final String MARK_IS_CHECKED = "is_checked";
    protected static final String MARK_CURRENT_USER = "current_user";
    protected static final String MARK_ID_BLOG = "id";
    protected static final String MARK_SEARCH_TEXT = "search_text";
    protected static final String MARK_DIFF = "blog_diff";
    protected static final String MARK_BLOG2 = "blog2";
    protected static final String MARK_LIST_TAG = "list_tag";
    protected static final String MARK_LIST_IMAGE_TYPE = "image_type";
    protected static final String MARK_SORTED_ATTRIBUTE = "sorted_attribute_name";
    protected static final String MARK_TAG = "tags";
    protected static final String MARK_USE_UPLOAD_IMAGE_PLUGIN = "is_crop_image";
    protected static final String MARK_PERMISSION_CREATE_BLOG = "permission_manage_create_blog";
    protected static final String MARK_PERMISSION_MODIFY_BLOG = "permission_manage_modify_blog";
    protected static final String MARK_PERMISSION_PUBLISH_BLOG = "permission_manage_publish_blog";
    protected static final String MARK_PERMISSION_DELETE_BLOG = "permission_manage_delete_blog";
    protected static final String MARK_PERMISSION_ARCHIVE_BLOG = "permission_manage_archive_blog";
    private static final String MARK_NUMBER_MANDATORY_TAGS = "number_mandatory_tags";
    private static final String MARK_STATUS_FILTER = "status_filter";

    private static final String JSP_MANAGE_BLOGS = "jsp/admin/plugins/blog/ManageBlogs.jsp";

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "blog.listItems.itemsPerPage";
    private static final String MESSAGE_CONFIRM_REMOVE_BLOG = "blog.message.confirmRemoveBlog";
    private static final String MESSAGE_ERROR_DOCUMENT_IS_ACTIVE = "blog.message.errorDocumentIsActive";
    private static final String MESSAGE_CONFIRM_REMOVE_HISTORY_BLOG = "blog.message.confirmRemoveHistoryBlog";
    private static final String ACCESS_DENIED_MESSAGE = "portal.message.user.accessDenied";
    private static final String MESSAGE_CONFIRM_ARCHIVE_BLOG = "blog.message.confirmArchiveBlog";
    private static final String MESSAGE_CONFIRM_ARCHIVE_BLOG_PUBLISHED = "blog.message.confirmArchiveBlogPublished";
    private static final String MESSAGE_CONFIRM_ARCHIVE_MULTIPLE_BLOGS = "blog.message.confirmArchiveMultipleBlogs";
    private static final String MESSAGE_CONFIRM_ARCHIVE_MULTIPLE_PUBLISHED_BLOGS ="blog.message.confirmArchiveMultipleBlogsPublished";
    private static final String MESSAGE_ERROR_DOCUMENT_IS_PUBLISHED = "blog.message.errorDocumentIsPublished";
    private static final String MESSAGE_CONFIRM_UNARCHIVE_MULTIPLE_BLOGS = "blog.message.confirmUnarchiveMultipleBlogs";
    private static final String MESSAGE_CONFIRM_UNARCHIVE_BLOG= "blog.message.confirmUnarchiveBlog";
    private static final String MESSAGE_CONFIRM_REMOVE_MULTIPE_BLOGS = "blog.message.confirmRemoveMultipleBlogs";

    private static final String INFO_BLOG_UNARCHIVED = "blog.info.blog.blogUnarchived";
    private static final String INFO_MULTIPLE_BLOGS_UNARCHIVED = "blog.info.blog.multipleBlogsUnarchived";
    private static final String INFO_BLOG_ARCHIVED = "blog.info.blog.blogArchived";
    private static final String INFO_MULTIPLE_BLOGS_ARCHIVED = "blog.info.blog.multipleBlogsArchived";
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "blog.model.entity.blog.attribute.";

    // Views
    protected static final String VIEW_MANAGE_BLOGS = "manageBlogs";
    private static final String VIEW_CREATE_BLOG = "createBlog";
    private static final String VIEW_MODIFY_BLOG = "modifyBlog";
    private static final String VIEW_HISTORY_BLOG = "historyBlog";
    private static final String VIEW_PREVIEW_BLOG = "previewBlog";
    private static final String VIEW_DIFF_BLOG = "diffBlog";

    // Actions
    private static final String ACTION_CREATE_BLOG = "createBlog";
    private static final String ACTION_MODIFY_BLOG = "modifyBlog";
    private static final String ACTION_REMOVE_BLOG = "removeBlog";
    private static final String ACTION_CONFIRM_REMOVE_BLOG = "confirmRemoveBlog";
    private static final String ACTION_ADD_TAG = "addTag";
    private static final String ACTION_REMOVE_TAG = "removeTag";
    private static final String ACTION_UPDATE_PRIORITY_TAG = "updatePriorityTag";
    private static final String ACTION_ADD_FILE_CONTENT = "addContent";
    private static final String ACTION_REMOVE_FILE_CONTENT = "deleteContent";
    private static final String ACTION_UPDATE_PRIORITY_FILE_CONTENT = "updatePriorityContent";
    private static final String ACTION_UPDATE_CONTENT_TYPE = "updateContentType";
    private static final String ACTION_DUPLICATE_BLOG = "duplicateBlog";
    private static final String ACTION_REMOVE_HISTORY_BLOG = "removeHistoryBlog";
    private static final String ACTION_CONFIRM_REMOVE_HISTORY_BLOG = "confirmRemoveHistoryBlog";
    private static final String ACTION_UPDATE_ARCHIVE_MULTIPLE_BLOGS = "updateArchiveMultipleBlogs";
    private static final String ACTION_REMOVE_MULTIPLE_BLOGS = "removeMultipleBlogs";
    private static final String ACTION_EXECUTE_SELECTED_ACTION = "form_checkbox_action";
    private static final String ACTION_CONFIRM_REMOVE_MULTIPLE_BLOGS = "confirmRemoveMultipleBlogs";
    private static final String ACTION_CONFIRM_ARCHIVE_BLOGS = "confirmArchiveBlogs";
    private static final String ACTION_CONFIRM_UNARCHIVE_BLOGS = "confirmUnarchiveBlogs";

    // Infos
    private static final String INFO_BLOG_CREATED = "blog.info.blog.created";
    private static final String INFO_BLOG_UPDATED = "blog.info.blog.updated";
    private static final String INFO_BLOG_REMOVED = "blog.info.blog.removed";
    private static final String BLOG_LOCKED = "blog.message.blogLocked";
    private static final String INFO_HISTORY_BLOG_REMOVED = "blog.info.history.blog.removed";
    private static final String INFO_MULTIPLE_BLOGS_REMOVED = "blog.info.blog.multipleBlogsRemoved";

    // Errors
    private static final String ERROR_HISTORY_BLOG_CANT_REMOVE_ORIGINAL = "blog.error.history.blog.cantRemoveOriginal";
    private static final String ERROR_HISTORY_BLOG_NOT_REMOVED = "blog.error.history.blog.notRemoved";
    private static final String MESSAGE_ERROR_MANDATORY_TAGS = "blog.message.errorMandatoryTags";
    private static final String ERROR_ACTION_EXECUTION_FAILED = "blog.message.error.action.executionFailed";
    private static final String ERROR_ACTION_NOT_FOUND = "blog.message.error.action.notFound";
    // Filter Marks
    protected static final String MARK_BLOG_FILTER_LIST = "blog_filter_list";
    protected static final String MARK_BLOG_FILTER_NAME = "Nom";
    protected static final String MARK_BLOG_FILTER_DATE = "Date";
    protected static final String MARK_BLOG_FILTER_USER = "Utilisateur";
    protected static final String MARK_PAGINATOR = "paginator";
    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    protected static final String MARK_ASC_SORT = "asc_sort";
    protected static final String MARK_DATE_UPDATE_BLOG_AFTER = "dateUpdateBlogAfter";
    protected static final String MARK_DATE_UPDATE_BLOG_BEFOR = "dateUpdateBlogBefor";
    protected static final String MARK_UNPUBLISHED = "unpublished";
    protected static final String MARK_LIST_BLOG_CONTRIBUTORS = "list_blog_contributors";

    public static final String CONSTANT_DUPLICATE_BLOG_NAME = "Copie de ";

    private static final String RESPONSE_BLOG_LOCKED = "BLOG_LOCKED";
    private static final String RESPONSE_SUCCESS = "SUCCESS";
    private static final String RESPONSE_FAILURE = "FAILURE";

    // Session variable to store working values
    private static Map<Integer, BlogLock> _mapLockBlog = new HashMap<>( );
    protected Blog _blog;
    protected boolean _bIsChecked = false;
    protected String _strSearchText;
    protected int _nIsUnpublished;
    protected String _dateUpdateBlogAfter;
    protected String _dateUpdateBlogBefor;
    protected String _strCurrentPageIndex;
    protected int _nItemsPerPage;
    protected int _nDefaultItemsPerPage;
    protected boolean _bIsSorted = false;
    protected String _strSortedAttributeName;
    protected Boolean _bIsAscSort;
    protected String [ ] _strTag;
    private List<Integer> _listSelectedBlogIds = new ArrayList<>( );


    // Session variable to store working values
    private final BlogServiceSession _blogServiceSession = BlogServiceSession.getInstance( );

    /**
     * Build the Manage View
     *
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_BLOGS, defaultView = true )
    public String getManageBlogs( HttpServletRequest request )
    {
        _blog = null;
        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );
        // SORT
        String strSortedAttributeName = request.getParameter( MARK_SORTED_ATTRIBUTE );
        String strAscSort;
        SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        AdminUser user = AdminUserService.getAdminUser( request );
        List<Integer> listBlogsId = new ArrayList<>( );
        String strButtonSearch = request.getParameter( PARAMETER_BUTTON_SEARCH );
        String strButtonReset = request.getParameter( PARAMETER_BUTTON_RESET );
        String strUnpublished = request.getParameter(PARAMETER_UNPUBLISHED);



        if ( strButtonSearch != null )
        {
            // CURRENT USER
            _bIsChecked = request.getParameter( MARK_CURRENT_USER ) != null;
            _strSearchText = request.getParameter( PARAMETER_SEARCH_TEXT );
            _strTag = request.getParameterValues( PARAMETER_TAG );
            _dateUpdateBlogAfter = request.getParameter( PARAMETER_DATE_UPDATE_BLOG_AFTER );
            _dateUpdateBlogBefor = request.getParameter( PARAMETER_DATE_UPDATE_BLOG_BEFOR );
            if (StringUtils.isNotBlank(strUnpublished))
            {
                _nIsUnpublished = Integer.parseInt( strUnpublished );
            }
            else
            {
                _nIsUnpublished = 0;
            }
        }
        else
        {
            if ( strButtonReset != null )
            {
                _bIsChecked = false;
                _strSearchText = null;
                _strTag = null;
                _dateUpdateBlogAfter = null;
                _dateUpdateBlogBefor = null;
                _nIsUnpublished = 0;
            }
        }
        if ( StringUtils.isNotBlank( _strSearchText ) || ( _strTag != null && _strTag.length > 0 ) || _bIsChecked || _nIsUnpublished > 0
                || _dateUpdateBlogAfter != null || _dateUpdateBlogBefor != null )
        {
            BlogSearchFilter filter = new BlogSearchFilter( );
            if ( StringUtils.isNotBlank( _strSearchText ) )
            {
                filter.setKeywords( BlogUtils.removeAccents( _strSearchText ) );
            }
            if ( !ArrayUtils.isEmpty( _strTag ) )
            {
                filter.setTag( _strTag );
            }
            if ( _bIsChecked )
            {
                filter.setUser( user.getAccessCode( ) );
            }
            if(_nIsUnpublished == 3)
            {
                filter.setIsArchived(true);
            }
            else
            {
                filter.setIsArchived( false );
            }
            filter.setIsUnpulished(_nIsUnpublished);

            if ( _dateUpdateBlogAfter != null )
            {
                try {
                    filter.setUpdateDateAfter( isoDateFormat.parse(_dateUpdateBlogAfter)) ;
                } catch (ParseException e) {
                    AppLogService.error( "Bad Date Format for indexed item: " + e.getMessage( ) );
                }
            }
            if ( _dateUpdateBlogBefor != null )
            {
                try {
                    filter.setUpdateDateBefor( isoDateFormat.parse( _dateUpdateBlogBefor ) ) ;
                } catch ( ParseException e ) {
                    AppLogService.error( "Bad Date Format for indexed item: " + e.getMessage( ) );
                }
            }

            BlogSearchService.getInstance( ).getSearchResults( filter, listBlogsId );

        }
        else
        {
            listBlogsId = BlogHome.getIdBlogsList( );
        }
        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<>( (List<Integer>) listBlogsId, _nItemsPerPage, getHomeUrl( request ),
                AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        List<Blog> listDocuments = new ArrayList<>( );
        HashMap<Integer, List<String>> mapContributors = new HashMap<>( );
        for ( Integer documentId : paginator.getPageItems( ) )
        {
            Blog document = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( documentId );

            if ( document != null )
            {
                if ( _mapLockBlog.containsKey( document.getId( ) ) && !_mapLockBlog.get( document.getId( ) ).getSessionId( )
                        .equals( request.getSession( ).getId( ) ) )
                {

                    document.setLocked( true );
                }
                listDocuments.add( document );
                List<String> listContributors = BlogHome.getUsersEditedBlogVersions( document.getId( ) );
                mapContributors.put( document.getId( ), listContributors );
            }
        }

        if ( strSortedAttributeName != null || _bIsSorted )
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
        boolean bPermissionCreate = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_CREATE,
                (User) getUser( ) );
        boolean bPermissionModify = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_MODIFY,
                (User) getUser( ) );
        boolean bPermissionDelete = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_DELETE,
                (User) getUser( ) );
        boolean bPermissionPublish = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_PUBLISH,
                (User) getUser( ) );
        boolean bPermissionArchive = RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_ARCHIVE,
                (User) getUser( ) );

        HttpSession session = request.getSession();

        Map<String, Object> model = new HashMap<>( );
        if( session.getAttribute(PARAMETER_INFO_MESSAGE  ) != null )
        {
            Locale locale = getLocale( );
            String messageKey = request.getSession().getAttribute(PARAMETER_INFO_MESSAGE).toString();
            model.put( PARAMETER_INFO_MESSAGE, I18nService.getLocalizedString( messageKey, locale ) );
            session.removeAttribute(PARAMETER_INFO_MESSAGE);
        } else {
            model.put( PARAMETER_INFO_MESSAGE, null );
        }

        model.put( MARK_STATUS_FILTER, _nIsUnpublished );
        model.put( MARK_BLOG_LIST, listDocuments );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_BLOG_FILTER_LIST, getBlogFilterList( ) );
        model.put( MARK_LIST_TAG, TagHome.getTagsReferenceList( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_IS_CHECKED, _bIsChecked );
        model.put( MARK_SEARCH_TEXT, _strSearchText );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_TAG, _strTag );
        model.put( MARK_DATE_UPDATE_BLOG_AFTER, _dateUpdateBlogAfter );
        model.put( MARK_DATE_UPDATE_BLOG_BEFOR, _dateUpdateBlogBefor );
        model.put( MARK_UNPUBLISHED, _nIsUnpublished );
        model.put( MARK_LIST_BLOG_CONTRIBUTORS, mapContributors );
        model.put( MARK_PERMISSION_CREATE_BLOG, bPermissionCreate );
        model.put( MARK_PERMISSION_MODIFY_BLOG, bPermissionModify );
        model.put( MARK_PERMISSION_DELETE_BLOG, bPermissionDelete );
        model.put( MARK_PERMISSION_PUBLISH_BLOG, bPermissionPublish );
        model.put( MARK_PERMISSION_ARCHIVE_BLOG, bPermissionArchive );
        String idDashboardStr = AppPropertiesService.getProperty(BlogConstant.PROPERTY_ADVANCED_MAIN_DASHBOARD_ID);
        int idDashboard = (idDashboardStr != null) ? Integer.parseInt(idDashboardStr) : 1;
        model.put( MARK_NUMBER_MANDATORY_TAGS, BlogAdminDashboardHome.selectNumberMandatoryTags(idDashboard) );


        return getPage( PROPERTY_PAGE_TITLE_MANAGE_BLOG, TEMPLATE_MANAGE_BLOGS, model );
    }

    /**
     * Return View history page
     *
     * @param request
     *            The request
     * @return the hostory page
     */
    @View( value = VIEW_HISTORY_BLOG )
    public String getHistoryBlog( HttpServletRequest request )
    {
        _blog = null;
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        List<Blog> listBlogsVersions = BlogHome.getBlogsVersionsList( nId );

        UrlItem urlHistory = new UrlItem( JSP_MANAGE_BLOGS );
        urlHistory.addParameter( PARAMETER_VIEW, VIEW_HISTORY_BLOG );
        urlHistory.addParameter( PARAMETER_ID_BLOG, nId );

        Map<String, Object> model = getPaginatedListModel( request, MARK_BLOG_LIST, listBlogsVersions, urlHistory.getUrl( ) );

        model.put( MARK_ID_BLOG, nId );

        return getPage( PROPERTY_PAGE_TITLE_HISTORY_BLOG, TEMPLATE_HISTORY_BLOG, model );
    }

    /**
     * Manages the removal of a blog's version from its history
     *
     * @param request
     *            The Http request
     * @return the html code for the removal's confirmation page
     * @throws AccessDeniedException
     */
    @Action( ACTION_CONFIRM_REMOVE_HISTORY_BLOG )
    public String getconfirmRemoveHistoryBlog( HttpServletRequest request ) throws AccessDeniedException
    {
        String strId = request.getParameter( PARAMETER_ID_BLOG );
        int nId = Integer.parseInt( strId );
        String strVersion = request.getParameter( PARAMETER_VERSION_BLOG );

        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, strId, Blog.PERMISSION_DELETE, (User) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_HISTORY_BLOG ) );
        url.addParameter( PARAMETER_ID_BLOG, nId );
        url.addParameter( PARAMETER_VERSION_BLOG, strVersion );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_HISTORY_BLOG, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal of a blog's version
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the page to manage the blog's versions
     */
    @Action( ACTION_REMOVE_HISTORY_BLOG )
    public String doRemoveHistoryBlog( HttpServletRequest request )
    {
        String strBlogId = request.getParameter( PARAMETER_ID_BLOG );
        int nBlogId = Integer.parseInt( strBlogId );
        String strVersion = request.getParameter( PARAMETER_VERSION_BLOG );
        int nVersion = -1;

        // Make sure the version number is valid, and that the original version ( 1 ) is not being removed somehow
        if ( StringUtils.isNumeric( strVersion ) )
        {
            nVersion = Integer.parseInt( strVersion );
            if ( nVersion == 1 )
            {
                // If a request to delete the original version was made, redirect the user and display an error message
                addError( ERROR_HISTORY_BLOG_CANT_REMOVE_ORIGINAL, getLocale( ) );
                return redirect( request, VIEW_HISTORY_BLOG, PARAMETER_ID_BLOG, nBlogId );
            }
        }
        else
        {
            // In case the version number is not valid
            addError( ERROR_HISTORY_BLOG_NOT_REMOVED, getLocale( ) );
            AppLogService.error( "Plugin Blog: Can't delete invalid version {} for blog ID {}", strVersion, strBlogId );
            return redirect( request, VIEW_HISTORY_BLOG, PARAMETER_ID_BLOG, nBlogId );
        }

        if ( RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, strBlogId, Blog.PERMISSION_DELETE, (User) getUser( ) ) )
        {
            Blog blog = BlogService.getInstance( ).loadBlog( nBlogId );

            // Check if the blog's current version is the one being removed
            if ( blog.getVersion( ) == nVersion )
            {
                // Check if this blog is being modified by another user
                if ( checkLockBlog( nBlogId, request.getSession( ).getId( ) ) )
                {
                    // Inform the user that the blog is locked by another user and redirect to the blog's history view
                    UrlItem url = new UrlItem( getViewFullUrl( VIEW_HISTORY_BLOG ) );
                    url.addParameter( PARAMETER_ID_BLOG, nBlogId );
                    String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
                    return redirect( request, strMessageUrl );
                }
                // Check if this blog is currently published
                List<BlogPublication> docPublication = BlogPublicationHome.getDocPublicationByIdDoc( nBlogId );
                if ( CollectionUtils.isNotEmpty( docPublication ) || BlogHome.findByPrimaryKey( nBlogId ).isArchived() )
                {
                    // Inform the user that the blog is currently published and redirect to the blog's history view
                    UrlItem url = new UrlItem( getViewFullUrl( VIEW_HISTORY_BLOG ) );
                    url.addParameter( PARAMETER_ID_BLOG, nBlogId );
                    String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DOCUMENT_IS_PUBLISHED, url.getUrl( ),
                            AdminMessage.TYPE_STOP );
                    return redirect( request, strMessageUrl );
                }
                // Delete the current version of the blog and revert to its previous version
                BlogService.getInstance( ).revertBlogToPreviousVersion( blog );
            }
            else
            {
                // Delete an older version of the blog
                BlogService.getInstance( ).deleteBlogVersion( nBlogId, nVersion );
            }
            addInfo( INFO_HISTORY_BLOG_REMOVED, getLocale( ) );
        }
        return redirect( request, VIEW_HISTORY_BLOG, PARAMETER_ID_BLOG, nBlogId );
    }

    /**
     * Returns the form to create a blog
     *
     * @param request
     *            The Http request
     * @return the html code of the blog form
     * @throws AccessDeniedException
     */
    @View( VIEW_CREATE_BLOG )
    public String getCreateBlog( HttpServletRequest request ) throws AccessDeniedException
    {

        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_CREATE, (User) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }

        _blog = ( _blog != null && _blog.getId( ) == 0 ) ? _blog : new Blog( );
        _blogServiceSession.saveBlogInSession( request.getSession( ), _blog );
        _blog.getTag( ).sort( ( tg1, tg2 ) -> tg1.getPriority( ) - tg2.getPriority( ) );

        String useCropImage = DatastoreService.getDataValue( PROPERTY_USE_UPLOAD_IMAGE_PLUGIN, "false" );
        Map<String, Object> model = getModel( );
        boolean bPermissionCreate = RBACService.isAuthorized( Tag.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Tag.PERMISSION_CREATE,
                (User) getUser( ) );
 String idDashboardStr = AppPropertiesService.getProperty(BlogConstant.PROPERTY_ADVANCED_MAIN_DASHBOARD_ID);
        int idDashboard = (idDashboardStr != null) ? Integer.parseInt(idDashboardStr) : 1;        model.put( MARK_NUMBER_MANDATORY_TAGS, BlogAdminDashboardHome.selectNumberMandatoryTags(idDashboard ) );
        model.put( MARK_LIST_IMAGE_TYPE, DocContentHome.getListContentType( ) );
        model.put( MARK_PERMISSION_CREATE_TAG, bPermissionCreate );
        model.put( MARK_BLOG, _blog );
        model.put( MARK_LIST_TAG, getTageList( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_USE_UPLOAD_IMAGE_PLUGIN, Boolean.parseBoolean( useCropImage ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_BLOG, TEMPLATE_CREATE_BLOG, model );
    }

    /**
     * Process the data capture form of a new blog
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_BLOG )
    public String doCreateBlog( HttpServletRequest request )
    {

        if ( RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_CREATE, (User) getUser( ) ) )
        {
            String strAction = request.getParameter( PARAMETER_ACTION_BUTTON );
            _blog.setCreationDate( getSqlDate( ) );
            _blog.setUpdateDate( getSqlDate( ) );
            _blog.setUser( AdminUserService.getAdminUser( request ).getAccessCode( ) );
            _blog.setUserCreator( AdminUserService.getAdminUser( request ).getAccessCode( ) );
            _blog.setVersion( 1 );
            _blog.setAttachedPortletId( 0 );
            _blog.setArchived( false );
            populate( _blog, request );

            // Check constraints
            if ( !validateBean( _blog, VALIDATION_ATTRIBUTES_PREFIX ) )
            {
                return redirectView( request, VIEW_CREATE_BLOG );
            }
            // Check if the number of mandatory tags is respected
     String idDashboardStr = AppPropertiesService.getProperty(BlogConstant.PROPERTY_ADVANCED_MAIN_DASHBOARD_ID);
        int idDashboard = (idDashboardStr != null) ? Integer.parseInt(idDashboardStr) : 1;
        int nNumberMandatoryTags = BlogAdminDashboardHome.selectNumberMandatoryTags(idDashboard);
            if (nNumberMandatoryTags  > _blog.getTag( ).size( ) )
            {
                String strMessage = I18nService.getLocalizedString(MESSAGE_ERROR_MANDATORY_TAGS, getLocale( ));
                addError( strMessage, getLocale( ) );
                return redirectView( request, VIEW_CREATE_BLOG );
            }

            BlogService.getInstance( ).createBlog( _blog, _blog.getDocContent( ) );
            _blogServiceSession.removeBlogFromSession( request.getSession( ), _blog.getId( ) );

            if ( strAction != null && strAction.equals( PARAMETER_APPLY ) )
            {

                return redirect( request, VIEW_MODIFY_BLOG, PARAMETER_ID_BLOG, _blog.getId( ) );
            }

            addInfo( INFO_BLOG_CREATED, getLocale( ) );
        }
        return redirectView( request, VIEW_MANAGE_BLOGS );
    }

    /**
     * Return Json if the tag is added
     *
     * @param request
     *            The request
     * @return Json The Json success or echec
     */
    @Action( ACTION_ADD_TAG )
    public String doAddTag( HttpServletRequest request )
    {
        String strIdTag = request.getParameter( PARAMETER_TAG );
        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String nIdSession = request.getSession( ).getId( );
        _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nIdBlog );
        if ( _mapLockBlog.get( nIdBlog ) != null && _mapLockBlog.get( nIdBlog ).getSessionId( ).equals( nIdSession ) )
        {

            lockBlog( nIdBlog, request.getSession( ).getId( ) );
        }
        else
            if ( _blog.getId( ) != 0 )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_BLOG_LOCKED ) );
            }

        if ( RBACService.isAuthorized( Tag.PROPERTY_RESOURCE_TYPE, strIdTag, Tag.PERMISSION_CREATE, (User) getUser( ) ) )
        {

            String strTagName = request.getParameter( PARAMETER_TAG_NAME );

            Tag tag = new Tag( Integer.parseInt( strIdTag ), _blog.getTag( ).size( ) + 1 );
            tag.setName( strTagName );

            _blog.addTag( tag );

            return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_SUCCESS ) );
        }
        return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_FAILURE ) );

    }

    /**
     * Return Json if the tag is removed
     *
     * @param request
     * @return Json The Json success or echec
     */
    @Action( ACTION_REMOVE_TAG )
    public String doRemoveTag( HttpServletRequest request )
    {
        String strIdTag = request.getParameter( PARAMETER_TAG );
        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String nIdSession = request.getSession( ).getId( );
        _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nIdBlog );
        if ( _mapLockBlog.get( nIdBlog ) != null && _mapLockBlog.get( nIdBlog ).getSessionId( ).equals( nIdSession ) )
        {

            lockBlog( nIdBlog, request.getSession( ).getId( ) );
        }
        else
            if ( _blog.getId( ) != 0 )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_BLOG_LOCKED ) );
            }

        if ( RBACService.isAuthorized( Tag.PROPERTY_RESOURCE_TYPE, strIdTag, Tag.PERMISSION_DELETE, (User) getUser( ) ) )
        {
            Tag tag = _blog.getTag( ).stream( ).filter( tg -> tg.getIdTag( ) == Integer.parseInt( strIdTag ) ).collect( Collectors.toList( ) ).get( 0 );
            _blog.deleteTag( tag );

            List<Tag> listTag = _blog.getTag( ).stream( ).map( ( Tag tg ) -> {
                if ( ( tg.getPriority( ) > tag.getPriority( ) ) )
                {
                    tg.setPriority( tg.getPriority( ) - 1 );
                }
                return tg;
            } ).collect( Collectors.toList( ) );

            _blog.setTag( listTag );
            return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_SUCCESS ) );
        }
        return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_FAILURE ) );

    }

    /**
     * Return Json if the tag is updated
     *
     * @param request
     * @return Json The Json success or echec
     */
    @Action( ACTION_UPDATE_PRIORITY_TAG )
    public String doUpdatePriorityTag( HttpServletRequest request )
    {
        Tag tg = null;
        Tag tagMove = null;
        int nPriorityToSet = 0;
        int nPriority = 0;

        String strIdTag = request.getParameter( PARAMETER_TAG );
        String strAction = request.getParameter( PARAMETER_TAG_ACTION );
        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String nIdSession = request.getSession( ).getId( );
        _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nIdBlog );
        if ( _mapLockBlog.get( nIdBlog ) != null && _mapLockBlog.get( nIdBlog ).getSessionId( ).equals( nIdSession ) )
        {

            lockBlog( nIdBlog, request.getSession( ).getId( ) );
        }
        else
            if ( _blog.getId( ) != 0 )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_BLOG_LOCKED ) );
            }

        for ( Tag tag : _blog.getTag( ) )
        {
            if ( tag.getIdTag( ) == Integer.parseInt( strIdTag ) )
            {
                tg = tag;
                nPriorityToSet = tag.getPriority( );
                nPriority = tag.getPriority( );
            }
        }
        for ( Tag tag : _blog.getTag( ) )
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
     * Manages the removal form of a blog whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     * @throws AccessDeniedException
     */
    @Action( ACTION_CONFIRM_REMOVE_BLOG )
    public String getConfirmRemoveBlog( HttpServletRequest request ) throws AccessDeniedException
    {

        String strId = request.getParameter( PARAMETER_ID_BLOG );
        int nId = Integer.parseInt( strId );
        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, strId, Blog.PERMISSION_DELETE, (User) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }
        if ( checkLockBlog( nId, request.getSession( ).getId( ) ) )
        {

            UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
            String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );

        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_BLOG ) );
        url.addParameter( PARAMETER_ID_BLOG, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_BLOG, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a blog
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage blog
     */
    @Action( ACTION_REMOVE_BLOG )
    public String doRemoveBlog( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_ID_BLOG );

        int nId = Integer.parseInt( strId );

        if ( RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, strId, Blog.PERMISSION_DELETE, (User) getUser( ) ) )
        {

            if ( checkLockBlog( nId, request.getSession( ).getId( ) ) )
            {

                UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
                String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
                return redirect( request, strMessageUrl );

            }

            List<BlogPublication> docPublication = BlogPublicationHome.getDocPublicationByIdDoc( nId );

            if ( CollectionUtils.isNotEmpty( docPublication ) || !BlogHome.findByPrimaryKey( nId ).isArchived() )
            {
                String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DOCUMENT_IS_ACTIVE, AdminMessage.TYPE_STOP );

                return redirect( request, strMessageUrl );
            }
            BlogService.getInstance( ).deleteBlog( nId );
            _blogServiceSession.removeBlogFromSession( request.getSession( ), nId );
            unLockBlog( nId );

            ExtendableResourceRemovalListenerService.doRemoveResourceExtentions( Blog.PROPERTY_RESOURCE_TYPE, String.valueOf( nId ) );

            HttpSession session = request.getSession();
            session.setAttribute( PARAMETER_INFO_MESSAGE,  INFO_BLOG_REMOVED);
        }
        return redirectView( request, VIEW_MANAGE_BLOGS );
    }

    @Action( ACTION_DUPLICATE_BLOG )
    public String doDuplicateBlog( HttpServletRequest request ) throws AccessDeniedException
    {

        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_CREATE, (User) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }

        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        _blog = BlogService.getInstance( ).loadBlog( nIdBlog );

        Timestamp sqlDate = getSqlDate( );

        _blog.setContentLabel( CONSTANT_DUPLICATE_BLOG_NAME + _blog.getContentLabel( ) );
        _blog.setCreationDate( sqlDate );
        _blog.setUpdateDate( sqlDate );
        _blog.setUser( AdminUserService.getAdminUser( request ).getAccessCode( ) );
        _blog.setUserCreator( AdminUserService.getAdminUser( request ).getAccessCode( ) );
        _blog.setVersion( 1 );
        _blog.setAttachedPortletId( 0 );

        BlogService.getInstance( ).createBlog( _blog, _blog.getDocContent( ) );

        return redirectView( request, VIEW_MANAGE_BLOGS );
    }

    /**
     * Returns the form to update info about a blog
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     * @throws AccessDeniedException
     */
    @View( VIEW_MODIFY_BLOG )
    public String getModifyBlog( HttpServletRequest request ) throws AccessDeniedException
    {
        String strId = request.getParameter( PARAMETER_ID_BLOG );

        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, strId, Blog.PERMISSION_MODIFY, (User) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }
        int nId = Integer.parseInt( strId );
        String strResetVersion = request.getParameter( PARAMETER_VERSION_BLOG );
        String useCropImage = DatastoreService.getDataValue( PROPERTY_USE_UPLOAD_IMAGE_PLUGIN, "false" );
        String sessionId = request.getSession( ).getId( );

        int nVersion = -1;
        if ( strResetVersion != null )
        {
            nVersion = Integer.parseInt( strResetVersion );
        }

        if ( strResetVersion != null )
        {

            _blog = BlogHome.findVersion( nId, nVersion );
            _blogServiceSession.saveBlogInSession( request.getSession( ), _blog );
        }
        else
        {
            _blog = BlogService.getInstance( ).loadBlog( nId );
            _blogServiceSession.saveBlogInSession( request.getSession( ), _blog );

        }

        if ( checkLockBlog( nId, sessionId ) )
        {

            UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
            String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }
        for ( HttpSession httpSession : BlogSessionListner.getMapSession( ).values( ) )
        {

            if ( !httpSession.getId( ).equals( sessionId ) )
            {

                _blogServiceSession.removeBlogFromSession( httpSession, nId );
            }
        }

        lockBlog( nId, sessionId );

        _blog.getTag( ).sort( ( tg1, tg2 ) -> tg1.getPriority( ) - tg2.getPriority( ) );
        _blog.getDocContent( ).sort( ( dc1, dc2 ) -> dc1.getPriority( ) - dc2.getPriority( ) );
        Map<String, Object> model = getModel( );

        boolean bPermissionCreate = RBACService.isAuthorized( Tag.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Tag.PERMISSION_CREATE,
                (User) getUser( ) );
 String idDashboardStr = AppPropertiesService.getProperty(BlogConstant.PROPERTY_ADVANCED_MAIN_DASHBOARD_ID);
        int idDashboard = (idDashboardStr != null) ? Integer.parseInt(idDashboardStr) : 1;
        model.put( MARK_LIST_IMAGE_TYPE, DocContentHome.getListContentType( ) );
        model.put( MARK_PERMISSION_CREATE_TAG, bPermissionCreate );
        model.put( MARK_BLOG, _blog );
        model.put( MARK_LIST_TAG, getTageList( ) );
        model.put( MARK_USE_UPLOAD_IMAGE_PLUGIN, Boolean.parseBoolean( useCropImage ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_NUMBER_MANDATORY_TAGS, BlogAdminDashboardHome.selectNumberMandatoryTags(idDashboard) );

        ExtendableResourcePluginActionManager.fillModel( request, getUser( ), model, String.valueOf( nId ), Blog.PROPERTY_RESOURCE_TYPE );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_BLOG, TEMPLATE_MODIFY_BLOG, model );
    }

    /**
     * Process the change form of a blog
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_BLOG )
    public String doModifyBlog( HttpServletRequest request )
    {

        String strId = request.getParameter( PARAMETER_ID_BLOG );
        String strAction = request.getParameter( PARAMETER_ACTION_BUTTON );
        int nId = Integer.parseInt( strId );
        String strHtmlContent = request.getParameter( PARAMETER_HTML_CONTENT );
        String strEditComment = request.getParameter( PARAMETER_EDIT_COMMENT );
        String strContentLabel = request.getParameter( PARAMETER_CONTENT_LABEL );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strShareable = request.getParameter( PARAMETER_SHAREABLE );
        String strUrl = request.getParameter( PARAMETER_URL );

        if ( RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, strId, Blog.PERMISSION_MODIFY, (User) getUser( ) ) )
        {
            _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nId );
            if ( checkLockBlog( Integer.parseInt( strId ), request.getSession( ).getId( ) ) || _blog == null )
            {

                UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
                String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
                return redirect( request, strMessageUrl );
            }
            Blog latestVersionBlog = BlogService.getInstance( ).loadBlog( nId );

            _blog.setContentLabel( strContentLabel );
            _blog.setDescription( strDescription );
            _blog.setShareable( Boolean.parseBoolean( strShareable ) );
            _blog.setHtmlContent( strHtmlContent );
            _blog.setEditComment( strEditComment );
            _blog.setUpdateDate( getSqlDate( ) );
            _blog.setUser( AdminUserService.getAdminUser( request ).getAccessCode( ) );
            _blog.setUrl( strUrl );

            // Check constraints
            if ( !validateBean( _blog, VALIDATION_ATTRIBUTES_PREFIX ) )
            {
                return redirect( request, VIEW_MODIFY_BLOG, PARAMETER_ID_BLOG, _blog.getId( ) );
            }
            // Check if the number of mandatory tags is respected
     String idDashboardStr = AppPropertiesService.getProperty(BlogConstant.PROPERTY_ADVANCED_MAIN_DASHBOARD_ID);
        int idDashboard = (idDashboardStr != null) ? Integer.parseInt(idDashboardStr) : 1;
        int nNumberMandatoryTags = BlogAdminDashboardHome.selectNumberMandatoryTags(idDashboard);
            if (nNumberMandatoryTags > _blog.getTag( ).size( ) )
            {
                String strMessage = I18nService.getLocalizedString(MESSAGE_ERROR_MANDATORY_TAGS, getLocale( ));
                addError( strMessage, getLocale( ) );
                return redirectView( request, VIEW_CREATE_BLOG );
            }

            if ( strAction != null && strAction.equals( PARAMETER_APPLY ) )
            {
                BlogService.getInstance( ).updateBlogWithoutVersion( _blog, _blog.getDocContent( ) );
                _blogServiceSession.removeBlogFromSession( request.getSession( ), nId );
                unLockBlog( nId );

                return redirect( request, VIEW_MODIFY_BLOG, PARAMETER_ID_BLOG, _blog.getId( ) );

            }
            else
            {
                _blog.setVersion( latestVersionBlog.getVersion( ) + 1 );
                BlogService.getInstance( ).updateBlog( _blog, _blog.getDocContent( ) );
                _blogServiceSession.removeBlogFromSession( request.getSession( ), nId );
                unLockBlog( nId );
                addInfo( INFO_BLOG_UPDATED, getLocale( ) );
            }
        }
        return redirectView( request, VIEW_MANAGE_BLOGS );
    }

    /**
     * Returns the preview of an blog
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_PREVIEW_BLOG )
    public String getPreviewBlog( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String strVersion = request.getParameter( PARAMETER_VERSION_BLOG );
        int nVersion = -1;
        if ( strVersion != null )
        {
            nVersion = Integer.parseInt( strVersion );
        }

        Blog blog;
        if ( strVersion != null )
        {
            blog = BlogHome.findVersion( nId, nVersion );
        }
        else
        {
            blog = BlogService.getInstance( ).loadBlog( nId );
        }
        blog.setBlogPublication( BlogPublicationHome.getDocPublicationByIdDoc( nId ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_LIST_TAG, TagHome.getTagsReferenceList( ) );

        model.put( MARK_BLOG, blog );

        ExtendableResourcePluginActionManager.fillModel( request, getUser( ), model, String.valueOf( nId ), Blog.PROPERTY_RESOURCE_TYPE );

        return getPage( PROPERTY_PAGE_TITLE_PREVIEW_BLOG, TEMPLATE_PREVIEW_BLOG, model );
    }

    /**
     * Returns the diff of two blog versions
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_DIFF_BLOG )
    public String getDiffBlog( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String strVersion = request.getParameter( PARAMETER_VERSION_BLOG );
        int nVersion = -1;
        if ( strVersion != null )
        {
            nVersion = Integer.parseInt( strVersion );
        }
        String strVersion2 = request.getParameter( PARAMETER_VERSION_BLOG2 );

        Blog blog;
        if ( strVersion != null )
        {
            blog = BlogHome.findVersion( nId, nVersion );
        }
        else
        {
            blog = BlogHome.findByPrimaryKey( nId );
        }

        int nVersion2 = blog.getVersion( ) - 1;
        if ( strVersion2 != null )
        {
            nVersion2 = Integer.parseInt( strVersion2 );
        }

        Blog blog2 = BlogHome.findVersion( nId, nVersion2 );
        if ( blog2 == null )
        {
            blog2 = BlogHome.findByPrimaryKey( nId );
        }

        if ( blog2.getVersion( ) > blog.getVersion( ) )
        {
            Blog tmp = blog2;
            blog2 = blog;
            blog = tmp;
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
            cleaner.cleanAndParse( new InputSource( new ByteArrayInputStream( blog2.getHtmlContent( ).getBytes( StandardCharsets.UTF_8 ) ) ), oldHandler );
            TextNodeComparator leftComparator = new TextNodeComparator( oldHandler, locale );

            DomTreeBuilder newHandler = new DomTreeBuilder( );
            cleaner.cleanAndParse( new InputSource( new ByteArrayInputStream( blog.getHtmlContent( ).getBytes( StandardCharsets.UTF_8 ) ) ), newHandler );
            TextNodeComparator rightComparator = new TextNodeComparator( newHandler, locale );

            HtmlSaxDiffOutput output = new HtmlSaxDiffOutput( result, "" );
            HTMLDiffer differ = new HTMLDiffer( output );
            differ.diff( leftComparator, rightComparator );

            strDiff = resultWriter.toString( );
        }
        catch( Exception e )
        {
            AppLogService.error( "Error generating daisy diff for blog " + nId + ":" + blog.getContentLabel( ) + "; versions (" + blog.getVersion( ) + ","
                    + blog2.getVersion( ) + ")", e );
        }

        List<Blog> listBlogsVersions = BlogHome.getBlogsVersionsList( nId );

        Map<String, Object> model = getModel( );
        model.put( MARK_BLOG, blog );
        model.put( MARK_BLOG2, blog2 );
        model.put( MARK_DIFF, strDiff );
        model.put( MARK_BLOG_VERSION_LIST, listBlogsVersions );

        return getPage( PROPERTY_PAGE_TITLE_DIFF_BLOG, TEMPLATE_DIFF_BLOG, model );
    }

    /**
     *
     * Added docContent to the htmlDoc content list
     *
     * @param request
     *            The Http request
     * @return
     */
    @Action( ACTION_ADD_FILE_CONTENT )
    public String addContent( HttpServletRequest request )
    {

        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String nIdSession = request.getSession( ).getId( );
        _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nIdBlog );

        if ( _mapLockBlog.get( nIdBlog ) != null && _mapLockBlog.get( nIdBlog ).getSessionId( ).equals( nIdSession ) )
        {

            lockBlog( nIdBlog, request.getSession( ).getId( ) );
        }
        else
            if ( _blog.getId( ) != 0 )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_BLOG_LOCKED ) );
            }

        /* Gestion du mimeType */
        String result = request.getParameter( "fileContent" );
        String firstDelimiter = "[;]";
        String secondDelimiter = "[:]";
        String thirdDelimiter = "[,]";
        String [ ] firstParts = result.split( firstDelimiter );
        String partAfterFirstDelimiter = firstParts [0];
        String [ ] secondParts = partAfterFirstDelimiter.split( secondDelimiter );
        // Le mimeType
        String mimeType = secondParts[1];
        // Le fichier en base64
        String base64FileString = StringUtils.EMPTY;
        // Gestion des fichiers vides
        if ( !result.endsWith( "," ) )
        {
            String [ ] thirdParts = result.split( thirdDelimiter );
            base64FileString = thirdParts [1];
        }

        byte [ ] fileByteArray = Base64.getDecoder( ).decode( base64FileString );

        String strFileName = request.getParameter( PARAMETER_FILE_NAME );
        String strFileType = request.getParameter( "fileType" );

        if ( StringUtils.isEmpty( mimeType ) )
        {

            InputStream is = new ByteArrayInputStream( fileByteArray );

            // Trouver le type du fichier
            try
            {
                mimeType = URLConnection.guessContentTypeFromStream( is );
            }
            catch( IOException ioException )
            {
                AppLogService.error( ioException.getStackTrace( ), ioException );
            }

        }

        DocContent docContent = new DocContent( );
        docContent.setBinaryValue( fileByteArray );
        docContent.setValueContentType( mimeType );
        docContent.setTextValue( strFileName );

        if ( strFileType != null )
        {

            ContentType contType = new ContentType( );
            contType.setIdContentType( Integer.parseInt( strFileType ) );
            docContent.setContentType( contType );
        }
        docContent.setPriority( _blog.getDocContent( ).size( ) + 1 );
        _blog.addContent( docContent );
        DocContentHome.create( docContent );
        String [ ] results = {
                strFileName, String.valueOf( docContent.getId( ) )
        };

        return JsonUtil.buildJsonResponse( new JsonResponse( results ) );

    }

    /**
     * delete docContent in the htmlDoc content list
     *
     * @param request
     *            The Http request
     * @return
     */
    @Action( ACTION_REMOVE_FILE_CONTENT )
    public String removeContent( HttpServletRequest request )
    {

        int nIdDoc = Integer.parseInt( request.getParameter( PARAMETER_CONTENT_ID ) );
        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String nIdSession = request.getSession( ).getId( );
        _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nIdBlog );

        if ( _mapLockBlog.get( nIdBlog ) != null && _mapLockBlog.get( nIdBlog ).getSessionId( ).equals( nIdSession ) )
        {

            lockBlog( nIdBlog, request.getSession( ).getId( ) );
        }
        else
            if ( _blog.getId( ) != 0 )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_BLOG_LOCKED ) );
            }

        DocContent docCont = _blog.getDocContent( ).stream( ).filter( dc -> dc.getId( ) == nIdDoc ).collect( Collectors.toList( ) ).get( 0 );
        List<DocContent> listDocs = _blog.getDocContent( ).stream( ).map( ( DocContent dc ) -> {
            if ( ( dc.getPriority( ) > docCont.getPriority( ) ) && ( docCont.getId( ) != dc.getId( ) ) )
            {

                dc.setPriority( dc.getPriority( ) - 1 );
            }
            return dc;
        } ).collect( Collectors.toList( ) );

        _blog.setDocContent( listDocs );
        _blog.deleteDocContent( nIdDoc );
        DocContentHome.removeInBlogById( nIdDoc );

        return JsonUtil.buildJsonResponse( new JsonResponse( nIdDoc ) );

    }

    /**
     * Return Json if the the content is updated
     *
     * @param request
     * @return Json The Json success or echec
     */
    @Action( ACTION_UPDATE_PRIORITY_FILE_CONTENT )
    public String doUpdatePriorityContent( HttpServletRequest request )
    {
        DocContent docCont = null;
        DocContent docContMove = null;
        int nPriorityToSet = 0;
        int nPriority = 0;

        String strIdDocContent = request.getParameter( PARAMETER_CONTENT_ID );
        String strAction = request.getParameter( PARAMETER_CONTENT_ACTION );
        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String nIdSession = request.getSession( ).getId( );
        _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nIdBlog );
        if ( _mapLockBlog.get( nIdBlog ) != null && _mapLockBlog.get( nIdBlog ).getSessionId( ).equals( nIdSession ) )
        {

            lockBlog( nIdBlog, request.getSession( ).getId( ) );
        }
        else
            if ( _blog.getId( ) != 0 )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_BLOG_LOCKED ) );
            }

        for ( DocContent dc : _blog.getDocContent( ) )
        {
            if ( dc.getId( ) == Integer.parseInt( strIdDocContent ) )
            {
                docCont = dc;
                nPriorityToSet = dc.getPriority( );
                nPriority = dc.getPriority( );
            }
        }
        for ( DocContent dc : _blog.getDocContent( ) )
        {
            if ( strAction.equals( "moveUp" ) && dc.getPriority( ) == nPriority - 1 )
            {
                docContMove = dc;
                docContMove.setPriority( dc.getPriority( ) + 1 );
                nPriorityToSet = nPriority - 1;

            }
            else
                if ( strAction.equals( "moveDown" ) && dc.getPriority( ) == nPriority + 1 )
                {
                    docContMove = dc;
                    docContMove.setPriority( docContMove.getPriority( ) - 1 );
                    nPriorityToSet = nPriority + 1;

                }
        }
        docCont.setPriority( nPriorityToSet );

        if ( docContMove != null )
        {

            return JsonUtil.buildJsonResponse( new JsonResponse( String.valueOf( docContMove.getId( ) ) ) );

        }
        return JsonUtil.buildJsonResponse( new JsonResponse( String.valueOf( docCont.getId( ) ) ) );

    }

    /**
     *
     * @param request
     * @return
     */
    @Action( ACTION_UPDATE_CONTENT_TYPE )
    public String updateContentType( HttpServletRequest request )
    {

        String strContentTypeId = request.getParameter( PARAMETER_TYPE_ID );
        String strContentId = request.getParameter( PARAMETER_CONTENT_ID );
        int nIdBlog = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String nIdSession = request.getSession( ).getId( );
        _blog = _blogServiceSession.getBlogFromSession( request.getSession( ), nIdBlog );

        if ( _mapLockBlog.get( nIdBlog ) != null && _mapLockBlog.get( nIdBlog ).getSessionId( ).equals( nIdSession ) )
        {

            lockBlog( nIdBlog, request.getSession( ).getId( ) );
        }
        else
            if ( _blog.getId( ) != 0 )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_BLOG_LOCKED ) );
            }

        for ( DocContent content : _blog.getDocContent( ) )
        {

            if ( strContentId != null && content.getId( ) == Integer.parseInt( strContentId ) )
            {

                ContentType contType = new ContentType( );
                contType.setIdContentType( Integer.parseInt( strContentTypeId ) );

                content.setContentType( contType );
                break;
            }
        }

        return JsonUtil.buildJsonResponse( new JsonResponse( RESPONSE_SUCCESS ) );
    }

    /**
     * Set content of the blog
     *
     * @param mRequest
     * @param locale
     * @return the content of the blog
     */

    public DocContent setContent( MultipartHttpServletRequest mRequest, Locale locale )
    {

        FileItem fileParameterBinaryValue = mRequest.getFile( "attachment" );

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
     * @return
     */
    private ReferenceList getBlogFilterList( )
    {
        ReferenceList list = new ReferenceList( );
        list.addItem( MARK_BLOG_FILTER_NAME, "Nom" );
        list.addItem( MARK_BLOG_FILTER_DATE, "Date" );
        list.addItem( MARK_BLOG_FILTER_USER, "Utilisateur" );

        return list;
    }

    /**
     *
     * @return BlogList
     */
    private ReferenceList getTageList( )
    {

        ReferenceList blogList = TagHome.getTagsReferenceList( );

        for ( Tag tg : _blog.getTag( ) )
        {
            blogList.removeIf( item -> item.getCode( ).equals( String.valueOf( tg.getIdTag( ) ) ) );

        }
        return blogList;
    }

    /**
     * Check if the blog is locked
     *
     * @param nIdBlog
     *            The Id blog
     * @param strIdSession
     *            The Id session
     * @return return true if the blog is locked else false
     */
    private static boolean checkLockBlog( int nIdBlog, String strIdSession )
    {
        return _mapLockBlog.get( nIdBlog ) != null && !_mapLockBlog.get( nIdBlog ).getSessionId( ).equals( strIdSession );
    }


    /**
     * Display the confirmation message before one or multiple selected blog posts are deleted
     *
     * @param request
     *            The Http request
     * @return the html code to confirm the action
     * @throws AccessDeniedException
     */
    @Action( ACTION_CONFIRM_REMOVE_MULTIPLE_BLOGS )
    public String getConfirmRemoveMultipleBlogs( HttpServletRequest request ) throws AccessDeniedException
    {
        // Check if the user has the permission to archive a blog
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        User user = AdminUserService.getAdminUser( request );
        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_DELETE, user )
                &&  !adminUser.checkRight( RIGHT_AVANCED_CONFIGURATION ) )
        {
            String strMessage = I18nService.getLocalizedString( ACCESS_DENIED_MESSAGE, getLocale( ) );
            throw new AccessDeniedException( strMessage );
        }

        // Check if one of the blog selected is currently locked. Display a message and redirect the user if it's the case
        if ( checkLockMultipleBlogs( request.getSession( ).getId( ) ) )
        {
            UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
            String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_MULTIPLE_BLOGS ) );
        url.addParameter( PARAMETER_SELECTED_BLOGS, _listSelectedBlogIds.stream( ).map( String::valueOf ).collect( Collectors.joining( "," ) ) );
        // Check if there's 1 or multiple posts to be removed, to adapt the content of the displayed message
        String confirmationMessage = _listSelectedBlogIds.size( ) > 1 ? MESSAGE_CONFIRM_REMOVE_MULTIPE_BLOGS : MESSAGE_CONFIRM_REMOVE_BLOG;
        if( _listSelectedBlogIds.size( ) > 1 )
        {
            Object [ ] messageArgs = {
                    _listSelectedBlogIds.size( )
            };
            return redirect( request, AdminMessageService.getMessageUrl( request, confirmationMessage, messageArgs, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ));
        }
        else
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, confirmationMessage, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ));
        }
    }

    /**
     * Handles the manual delete of multiple blog posts
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the main blog posts' management view
     * @throws AccessDeniedException
     */
    @Action( ACTION_REMOVE_MULTIPLE_BLOGS )
    public String doRemoveMultipleBlog( HttpServletRequest request ) throws AccessDeniedException
    {
        User user = AdminUserService.getAdminUser( request );
        AdminUser adminUser = AdminUserService.getAdminUser( request );

        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_DELETE, user )
                &&  !adminUser.checkRight( RIGHT_AVANCED_CONFIGURATION ) )
        {
            String strMessage = I18nService.getLocalizedString( ACCESS_DENIED_MESSAGE, getLocale( ) );
            throw new AccessDeniedException( strMessage );
        }
        // Get a List of the selected posts' IDs, from the current session
        _listSelectedBlogIds = (List<Integer>) request.getSession( ).getAttribute( PARAMETER_SELECTED_BLOG_IDS_LIST );

        // Check if any of the selected post is being modified by another user
        if ( checkLockMultipleBlogs( request.getSession( ).getId( ) ) )
        {
            UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
            String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }
        for ( int docPublicationId : _listSelectedBlogIds )
        {
            if ( CollectionUtils.isNotEmpty( BlogPublicationHome.getDocPublicationByIdDoc( docPublicationId ) ) || !BlogHome.findByPrimaryKey( docPublicationId ).isArchived() )
            {
                String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DOCUMENT_IS_ACTIVE, AdminMessage.TYPE_STOP );

                return redirect( request, strMessageUrl );
            }
        }
        removeMultipleBlogs( _listSelectedBlogIds );
        HttpSession session = request.getSession();
        session.setAttribute( PARAMETER_INFO_MESSAGE,  getRemovedResultMessageKey( _listSelectedBlogIds ));
        return redirectView( request, VIEW_MANAGE_BLOGS );
    }
    private void removeMultipleBlogs( List<Integer> listBlogIds )
    {
        if( listBlogIds == null )
        {
            return;
        }
        for ( int blogId : listBlogIds )
        {
            BlogPublicationHome.getDocPublicationByIdDoc( blogId );
            if ( BlogPublicationHome.getDocPublicationByIdDoc( blogId ) != null && BlogPublicationHome.getDocPublicationByIdDoc( blogId ).size( ) > 0 )
            {
                BlogPublicationHome.removeByBlogId( blogId );
            }
            BlogHome.removeVersions( blogId );
            BlogHome.remove( blogId );
        }
    }
    /**
     * Display the confirmation message before one or multiple selected blog posts are removed
     *
     * @param request
     *            The Http request
     * @return the html code to confirm the action
     * @throws AccessDeniedException
     */
    @Action( ACTION_CONFIRM_ARCHIVE_BLOGS )
    public String getconfirmArchiveBlogs( HttpServletRequest request ) throws AccessDeniedException
    {
        // Check if the user has the permission to archive a blog
        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_ARCHIVE,
                (User) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }

        // Check if one of the blog selected is currently locked. Display a message and redirect the user if it's the case
        if ( checkLockMultipleBlogs( request.getSession( ).getId( ) ) )
        {
            UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
            String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_UPDATE_ARCHIVE_MULTIPLE_BLOGS ) );
        url.addParameter( PARAMETER_SELECTED_BLOGS, _listSelectedBlogIds.stream( ).map( String::valueOf ).collect( Collectors.joining( "," ) ) );
        url.addParameter( PARAMETER_TO_ARCHIVE, String.valueOf( true ));
        // Check if there's 1 or multiple posts being archived, to adapt the content of the displayed message
        if( _listSelectedBlogIds.size( ) > 1 )
        {
            int numberPublishedBlogs = 0;
            for ( int i = 0; i < _listSelectedBlogIds.size(); i++ )
            {
                // check if published
                if ( CollectionUtils.isNotEmpty( BlogPublicationHome.getDocPublicationByIdDoc( _listSelectedBlogIds.get( i ) ) ) )
                {
                    numberPublishedBlogs++;
                }
            }
            if( numberPublishedBlogs > 0 )
            {
                Object [ ] messageArgs = {
                        _listSelectedBlogIds.size( ),
                        numberPublishedBlogs
                };
                return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ARCHIVE_MULTIPLE_PUBLISHED_BLOGS, messageArgs, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ));
            }
            else
            {
                Object [ ] messageArgs = {
                        _listSelectedBlogIds.size( )
                };
                return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ARCHIVE_MULTIPLE_BLOGS, messageArgs, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ));
            }
        }
        else
        {
            if( CollectionUtils.isNotEmpty( BlogPublicationHome.getDocPublicationByIdDoc( _listSelectedBlogIds.get( 0 ) ) ) )
            {
                return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ARCHIVE_BLOG_PUBLISHED, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ));
            }
            else
            {
                return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ARCHIVE_BLOG, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ));
            }
        }
    }
    /**
     * Display the confirmation message before one or multiple selected blog posts are archived
     *
     * @param request
     *            The Http request
     * @return the html code to confirm the action
     * @throws AccessDeniedException
     */
    @Action( ACTION_CONFIRM_UNARCHIVE_BLOGS )
    public String getconfirmUnarchiveBlogs( HttpServletRequest request ) throws AccessDeniedException
    {
        // Check if the user has the permission to archive a blog
        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_ARCHIVE,
                (User) getUser( ) ) )
        {
            String strMessage = I18nService.getLocalizedString( ACCESS_DENIED_MESSAGE, getLocale( ) );
            throw new AccessDeniedException( strMessage );
        }

        // Check if one of the blog selected is currently locked. Display a message and redirect the user if it's the case
        if ( checkLockMultipleBlogs( request.getSession( ).getId( ) ) )
        {
            UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
            String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_UPDATE_ARCHIVE_MULTIPLE_BLOGS ) );
        url.addParameter( PARAMETER_SELECTED_BLOGS, _listSelectedBlogIds.stream( ).map( String::valueOf ).collect( Collectors.joining( "," ) ) );
        url.addParameter( PARAMETER_TO_ARCHIVE, String.valueOf( false ));
        // Check if there's 1 or multiple posts being archived, to adapt the content of the displayed message
        String confirmationMessage = _listSelectedBlogIds.size( ) > 1 ? MESSAGE_CONFIRM_UNARCHIVE_MULTIPLE_BLOGS : MESSAGE_CONFIRM_UNARCHIVE_BLOG;
        if( _listSelectedBlogIds.size( ) > 1 )
        {
            Object [ ] messageArgs = {
                    _listSelectedBlogIds.size( )
            };
            return redirect( request, AdminMessageService.getMessageUrl( request, confirmationMessage, messageArgs, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ));
        }
        else
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, confirmationMessage, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ) );
        }
    }

    /**
     * Handles the manual archiving of multiple blog posts
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the main blog posts' management view
     * @throws AccessDeniedException
     */
    @Action( ACTION_UPDATE_ARCHIVE_MULTIPLE_BLOGS )
    public String doArchiveMultipleBlog( HttpServletRequest request ) throws AccessDeniedException
    {
        User user = AdminUserService.getAdminUser( request );
        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Blog.PERMISSION_ARCHIVE, user ) )
        {
            String strMessage = I18nService.getLocalizedString( ACCESS_DENIED_MESSAGE, getLocale( ) );
            throw new AccessDeniedException( strMessage );
        }
        // Get a List of the selected posts' IDs, from the current session
        _listSelectedBlogIds = (List<Integer>) request.getSession( ).getAttribute( PARAMETER_SELECTED_BLOG_IDS_LIST );

        // Check if any of the selected post is being modified by another user
        if ( checkLockMultipleBlogs( request.getSession( ).getId( ) ) )
        {
            UrlItem url = new UrlItem( getActionUrl( VIEW_MANAGE_BLOGS ) );
            String strMessageUrl = AdminMessageService.getMessageUrl( request, BLOG_LOCKED, url.getUrl( ), AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }
        // Archive the selected blog posts
        Boolean bArchive = Boolean.parseBoolean( request.getParameter( PARAMETER_TO_ARCHIVE ) );
        updateArchiveMultipleBlogs( _listSelectedBlogIds, bArchive );
        HttpSession session = request.getSession();
        session.setAttribute( PARAMETER_INFO_MESSAGE, getArchivedResultMessageKey( bArchive, _listSelectedBlogIds ) );
        return redirectView( request, VIEW_MANAGE_BLOGS );
    }

    /**
     * Get the key of the message to display after the archiving of multiple blog posts
     * @param toArchived
     * @param listBlogIds
     * @return the key of the message to display
     */
    private String getArchivedResultMessageKey(Boolean toArchived, List<Integer> listBlogIds)
    {
        if(toArchived)
        {
            if(listBlogIds.size() == 1)
            {
                return INFO_BLOG_ARCHIVED;
            }
            else
            {
                return INFO_MULTIPLE_BLOGS_ARCHIVED;
            }
        }
        else
        {
            if(listBlogIds.size() == 1)
            {
                return INFO_BLOG_UNARCHIVED;
            }
            else
            {
                return INFO_MULTIPLE_BLOGS_UNARCHIVED;
            }
        }
    }
    /**
     * Get the key of the message to display after the removing of multiple blog posts
     * @param listBlogIds
     * @return the key of the message to display
     */
    private String getRemovedResultMessageKey(List<Integer> listBlogIds)
    {
        if(listBlogIds.size() == 1)
        {
            return INFO_BLOG_REMOVED;
        }
        else
        {
            return INFO_MULTIPLE_BLOGS_REMOVED;
        }
    };

    /**
     * Process a specific action on a selection of multiple blog post elements
     *
     * @param request
     *            The Http request
     * @return the URL to redirect to once the action is executed
     * @throws AccessDeniedException
     */
    @Action( ACTION_EXECUTE_SELECTED_ACTION )
    public String doExecuteSelectedAction( HttpServletRequest request ) throws AccessDeniedException
    {
        Locale locale = getLocale( );

        // Get the selected action
        int selectedActionId = getSelectedAction( request );
        // Get the IDs of the blog posts selected for the action
        _listSelectedBlogIds = getSelectedBlogPostsIds( request );

        // Check if the content retrieved is null. Redirect and display an error if it is the case
        if ( selectedActionId == -1 || CollectionUtils.isEmpty( _listSelectedBlogIds ) )
        {
            addError( ERROR_ACTION_EXECUTION_FAILED, locale );
            return redirectView( request, VIEW_MANAGE_BLOGS );
        }

        // Save the list of selected blogs in the current session's attributes
        request.getSession( ).setAttribute( PARAMETER_SELECTED_BLOG_IDS_LIST, _listSelectedBlogIds );

        // Execute the action selected by the user
        if ( selectedActionId == 0 )
        {
            return getconfirmArchiveBlogs( request );
        }
        else if ( selectedActionId == 1 )
        {
            return getconfirmUnarchiveBlogs( request );
        }
        else if ( selectedActionId == 2 )
            {
                return getConfirmRemoveMultipleBlogs( request );
            }
            else
            {
                addError( ERROR_ACTION_NOT_FOUND, locale );
                return redirectView( request, VIEW_MANAGE_BLOGS );
            }
    }

    /**
     * Get the value of the action selected by the user
     *
     * @param request
     *            The Http request
     * @return the ID of the selected action, or -1 if the value couldn't be parsed properly
     *
     */
    private int getSelectedAction( HttpServletRequest request )
    {
        // Retrieve the value of the selected action from the request
        String strSelectedActionId = request.getParameter( PARAMETER_SELECTED_BLOG_ACTION );
        if ( StringUtils.isNumeric( strSelectedActionId ) )
        {
            return Integer.parseInt( strSelectedActionId );
        }
        return -1;
    }


    /**
     * Get the IDs of the blog posts selected by the user
     *
     * @param request
     *            The Http request
     * @return a List of the selected IDs, or an empty List if no element was selected
     */
    private List<Integer> getSelectedBlogPostsIds( HttpServletRequest request )
    {
        // Retrieve an array containing the selected blog post IDs from the request
        String [ ] listSelectedBlogPosts = request.getParameterValues( PARAMETER_SELECTED_BLOGS );
        if ( ArrayUtils.isNotEmpty( listSelectedBlogPosts ) )
        {
            // Convert the value of the IDs from strings to integers and put them in a List
            return Arrays.stream( listSelectedBlogPosts ).map( Integer::parseInt ).collect( Collectors.toList( ) );
        }
        return Collections.emptyList( );
    }


    /**
     * Check if the given blogs are locked
     *            The IDs of the blogs to check
     * @param strIdSession
     *            The ID of the session
     * @return true if one of the blogs is locked
     */
    private synchronized boolean checkLockMultipleBlogs( String strIdSession )
    {
        if( CollectionUtils.isEmpty( _listSelectedBlogIds ))
        {
            return false;
        }
        for ( int blogId : _listSelectedBlogIds )
        {
            // If one of the blogs is locked, return true
            if ( checkLockBlog( blogId, strIdSession ) )
            {
                return true;
            }
        }
        // None of the blogs is locked
        return false;
    }

    private void updateArchiveMultipleBlogs( List<Integer> listBlogIds, boolean bArchive )
    {
        if( listBlogIds == null )
        {
            return;
        }
        for ( int i = 0; i < listBlogIds.size( ); i++ )
        {
            Integer blogId = listBlogIds.get( i );
            BlogHome.updateBlogArchiveId(bArchive, blogId);
            Blog blog = BlogHome.findByPrimaryKey( blogId );
            BlogSearchService.getInstance( ).updateDocument( blog );
            BlogSearchService.getInstance( ).updateDocument( blog );
            if( bArchive )
            {
                BlogPublicationHome.getDocPublicationByIdDoc( blogId );

                BlogPublicationHome.getDocPublicationByIdDoc( blogId );
                if ( BlogPublicationHome.getDocPublicationByIdDoc( blogId ) != null && BlogPublicationHome.getDocPublicationByIdDoc( blogId ).size( ) > 0 )
                {
                    BlogPublicationHome.removeByBlogId( blogId );
                }
            }
        }
    }

    /**
     * Lock blog
     *
     * @param nIdBlog
     *            The Id blog
     * @param strIdSession
     *            The Id session
     */
    private void lockBlog( int nIdBlog, String strIdSession )
    {

        _mapLockBlog.remove( nIdBlog );
        _mapLockBlog.put( nIdBlog, new BlogLock( strIdSession, System.currentTimeMillis( ) ) );

    }

    /**
     * Unlock Blog
     *
     * @param nIdBlog
     *            The id Blog
     */
    private void unLockBlog( int nIdBlog )
    {

        _mapLockBlog.remove( nIdBlog );

    }

    /**
     * Unlock Blogs By Session Id
     *
     * @param strIdSession
     *            The Id session
     */
    public static void unLockedBlogByIdSession( String strIdSession )
    {

        _mapLockBlog.values( ).removeIf( id -> id.getSessionId( ).equals( strIdSession ) );
    }

    /**
     * Unlock the blog if the lock clearance time has passed
     *
     * @param nTime
     *            the clearance time
     */
    public static void unLockedBlogByTime( long nTime )
    {

        long currentTime = System.currentTimeMillis( );
        _mapLockBlog.values( ).removeIf( id -> id.getTime( ) + nTime < currentTime );
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

}
