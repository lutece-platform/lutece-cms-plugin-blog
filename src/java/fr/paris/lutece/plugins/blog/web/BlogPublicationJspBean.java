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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortlet;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPortlet;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPortletHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.business.portlet.PortletFilter;
import fr.paris.lutece.plugins.blog.business.portlet.PortletOrder;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.service.PublishingService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.portlet.PortletService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.api.user.User;

/**
 * This class provides the user interface to manage HtmlDoc features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManagePublicationBlogs.jsp", controllerPath = "jsp/admin/plugins/blog/", right = "BLOG_MANAGEMENT" )
public class BlogPublicationJspBean extends BlogJspBean
{

    private static final long serialVersionUID = -2768032240064881165L;
    private static final String TEMPLATE_PUBLICATION_HTMLDOC = "/admin/plugins/blog/publication_blog.html";
    private static final String TEMPLATE_PORTLET_PAGE_PATH = "/admin/plugins/blog/portlet_page_path.html";

    // Properties for page titles

    // Properties
    private static final String PROPERTY_PAGE_TITLE_PUBLICATION_HTMLDOC = "blog.publication_blogpageTitle";
    private static final String PROPERTY_DISPLAY_LATEST_PORTLETS = "blog.manage_document_publishing.labelDisplayLatestPortlets";

    // Markers
    public static final String MARK_PORTLET_LIST = "portlet_list";
    private static final String MARK_PORTLET_FILTER_ERROR = "portlet_filter_error";
    private static final String MARK_ORDER_PORTLET = "order_portlet";
    private static final String MARK_ORDER_PORTLET_ASC = "order_portlet_asc";
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_LIST_PAGE = "page_list";

    private static final String MARK_DOCUMENT_PORTLET_LIST = "document_portlet_list";
    private static final String MARK_DOCUMENT_LIST_PORTLET_LIST = "document_list_portlet_list";
    private static final String MARK_PORTLET_FILTER = "portlet_filter";
    private static final String MARK_LABEL_DISPLAY_LATEST_PORTLETS = "label_display_latest_portlets";
    public static final String DATE_END_PUBLICATION = AppPropertiesService.getProperty( "blog.date.end.publication", "2030-01-01 11:59:59" );

    // Properties

    // Validations

    // Views
    private static final String VIEW_MANAGE_PUBLICATION = "manageBlogsPublication";

    // Actions
    private static final String ACTION_PUBLISHE_DOCUMENT = "publishDocument";
    private static final String ACTION_UNPUBLISHE_DOCUMENT = "unPublishDocument";

    // Infos

    // Filter Marks
    // Parma
    private static final String PARAMETER_PUBLISHED_DATE = "dateBeginPublishing";
    private static final String PARAMETER_UNPUBLISHED_DATE = "dateEndPublishing";
    private static final String PARAMETER_PORTLET_ID = "idPortlet";

    private static final String PARAMETER_ORDER_PORTLET = "order_portlet";
    private static final String PARAMETER_ORDER_PORTLET_ASC = "order_portlet_asc";
    private static final String PARAMETER_PORTLET_FILTER_TYPE = "portlet_filter_type";
    private static final String PARAMETER_PORTLET_FILTER_VALUE = "portlet_filter_value";

    private static final String PARAMETER_IS_DISPLAY_LATEST_PORTLETS = "is_display_latest_portlets";

    // Message
    private static final String ERROR_MESSAGE_NOT_NUMERIC_FIELD = "blog.message.notNumericField";
    private static final String ERROR_MESSAGE_MESSAGE_MANDATORY_SEARCH_FIELD = "blog.message.mandatory.searchField";

    // Session variable to store working values
    protected BlogPublication _blogPublication;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_PUBLICATION )
    public String getManageBlogPublication( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        Object [ ] messageNumberOfMaxLatestPortletsDisplay = {
                String.valueOf( PortletFilter.PROPERTY_NUMBER_OF_MAX_LATEST_PORTLETS_DISPLAY )
        };
        String strErrorFilter = null;
        _blogPublication = ( _blogPublication != null ) ? _blogPublication : new BlogPublication( );

        if ( _blog == null || ( _blog.getId( ) != nId ) )
        {

            _blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( nId );

        }
        PortletOrder pOrder = new PortletOrder( );
        String strOrderPortlet = request.getParameter( PARAMETER_ORDER_PORTLET );
        boolean bIsDisplayPortlets = ( ( request.getParameter( PARAMETER_IS_DISPLAY_LATEST_PORTLETS ) == null )
                || Boolean.valueOf( request.getParameter( PARAMETER_IS_DISPLAY_LATEST_PORTLETS ) ) );

        String strOrderPortletAsc = request.getParameter( PARAMETER_ORDER_PORTLET_ASC );
        int nOrderPortlet = -1;
        int nOrderPortletAsc = -1;

        Map<String, Object> model = getModel( );
        PortletFilter portletFilter = null;
        if ( !bIsDisplayPortlets )
        {

            portletFilter = new PortletFilter( );
            portletFilter.setPortletFilterType( request.getParameter( PARAMETER_PORTLET_FILTER_TYPE ) );
            portletFilter.setDisplayLatestPortlets( bIsDisplayPortlets );
            portletFilter.setSearchValue( request.getParameter( PARAMETER_PORTLET_FILTER_VALUE ) );
            strErrorFilter = setFillFilter( portletFilter );

            if ( strErrorFilter != null )
            {
                model.put( MARK_PORTLET_FILTER_ERROR, strErrorFilter );
            }

        }

        if ( StringUtils.isNotBlank( strOrderPortlet ) && StringUtils.isNumeric( strOrderPortlet ) && StringUtils.isNotBlank( strOrderPortletAsc )
                && StringUtils.isNumeric( strOrderPortletAsc ) )
        {
            nOrderPortlet = Integer.parseInt( strOrderPortlet );
            nOrderPortletAsc = Integer.parseInt( strOrderPortletAsc );
            pOrder.setTypeOrder( nOrderPortlet );
            pOrder.setSortAsc( nOrderPortletAsc == PortletOrder.SORT_ASC );
        }

        Collection<ReferenceItem> listDocumentListPortlets = getListAuthorizedDocumentListPortlets( _blog.getId( ), pOrder,
                ( strErrorFilter == null ) ? portletFilter : null );
        Collection<ReferenceItem> listDocumentPortlets = getListAuthorizedDocumentPortlets( _blog.getId( ), pOrder,
                ( strErrorFilter == null ) ? portletFilter : null );

        model.put( MARK_DOCUMENT_PORTLET_LIST, listDocumentPortlets );
        model.put( MARK_DOCUMENT_LIST_PORTLET_LIST, listDocumentListPortlets );
        model.put( MARK_BLOG, _blog );
        model.put( MARK_PORTLET_LIST, PublishingService.getInstance( ).getBlogsPortletstoPublish( ) );

        model.put( MARK_ORDER_PORTLET, nOrderPortlet );
        model.put( MARK_ORDER_PORTLET_ASC, nOrderPortletAsc );
        model.put( MARK_PORTLET_FILTER, portletFilter );
        model.put( MARK_LABEL_DISPLAY_LATEST_PORTLETS,
                I18nService.getLocalizedString( PROPERTY_DISPLAY_LATEST_PORTLETS, messageNumberOfMaxLatestPortletsDisplay, getLocale( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_PUBLICATION_HTMLDOC, TEMPLATE_PUBLICATION_HTMLDOC, model );
    }

    /**
     * Publish a document in the portlet
     * 
     * @param request
     * @return View mange publication
     * @throws ParseException
     */
    @Action( ACTION_PUBLISHE_DOCUMENT )
    public String doPublishDocument( HttpServletRequest request ) throws ParseException
    {
        populateBlogPublication( _blogPublication, request );

        if ( _blogPublication.getDateBeginPublishing( ) == null )
        {

            _blogPublication.setDateBeginPublishing( new Date( System.currentTimeMillis( ) ) );
        }
        if ( _blogPublication.getDateEndPublishing( ) == null )
        {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            _blogPublication.setDateEndPublishing( new Date( sdf.parse( DATE_END_PUBLICATION ).getTime( ) ) );
        }
        if ( _blogPublication.getIdPortlet( ) != 0 )
        {

            BlogPublicationHome.create( _blogPublication );

            _blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( _blogPublication.getIdBlog( ) );
        }

        return getManageBlogPublication( request );

    }

    /**
     * Unpublish a document in the portlet
     * 
     * @param request
     *            The request
     * @return view manage blog publication
     */
    @Action( ACTION_UNPUBLISHE_DOCUMENT )
    public String doUnPublishDocument( HttpServletRequest request )
    {

        populate( _blogPublication, request );

        BlogPublicationHome.remove( _blogPublication.getIdBlog( ), _blogPublication.getIdPortlet( ) );
        _blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( _blogPublication.getIdBlog( ) );

        return getManageBlogPublication( request );

    }

    /**
     * Populate BlogPublication object
     * 
     * @param blogPublication
     *            The BlogPublication
     * @param request
     *            The request
     * @throws ParseException
     */
    private void populateBlogPublication( BlogPublication htmldocPublication, HttpServletRequest request ) throws ParseException
    {

        int nIdDoc = Integer.parseInt( request.getParameter( PARAMETER_ID_BLOG ) );
        String strIdPortlet = request.getParameter( PARAMETER_PORTLET_ID );
        int nIdPortlet = Integer.parseInt( strIdPortlet != null ? strIdPortlet : "0" );

        String dateBeginPublishingStr = request.getParameter( PARAMETER_PUBLISHED_DATE );
        String dateEndPublishingStr = request.getParameter( PARAMETER_UNPUBLISHED_DATE );
        java.sql.Date dateBeginPublishing = null;
        java.sql.Date dateEndPublishing = null;

        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
        java.util.Date parsed;

        if ( dateBeginPublishingStr != null && !dateBeginPublishingStr.isEmpty( ) )
        {
            parsed = sdf.parse( dateBeginPublishingStr );
            dateBeginPublishing = new java.sql.Date( parsed.getTime( ) );
        }
        if ( dateEndPublishingStr != null && !dateEndPublishingStr.isEmpty( ) )
        {
            parsed = sdf.parse( dateEndPublishingStr );
            dateEndPublishing = new java.sql.Date( parsed.getTime( ) );
        }
        int nBlogOrder = BlogListPortletHome.getMinDocBlogOrder( );
        nBlogOrder = nBlogOrder - 1;

        htmldocPublication.setIdBlog( nIdDoc );
        htmldocPublication.setIdPortlet( nIdPortlet );
        htmldocPublication.setDateBeginPublishing( dateBeginPublishing );
        htmldocPublication.setDateEndPublishing( dateEndPublishing );
        htmldocPublication.setBlogOrder( nBlogOrder );

    }

    /**
     * Fill the searchFilter
     * 
     * @param portletFilter
     *            {@link PortletFilter}
     * @return return error message if an error appear
     */
    public String setFillFilter( PortletFilter portletFilter )
    {
        String strErrorMessage = null;
        String strValue;

        if ( ( portletFilter.getSearchValue( ) != null ) && !portletFilter.getSearchValue( ).trim( ).equals( "" )
                && ( portletFilter.getPortletFilterType( ) != null ) )
        {
            strValue = portletFilter.getSearchValue( ).trim( );

            if ( portletFilter.getPortletFilterType( ).equals( PortletFilter.PAGE_NAME ) )
            {
                portletFilter.setPageName( strValue.split( PortletFilter.CONSTANTE_SPACE_STRING ) );
            }
            else
                if ( portletFilter.getPortletFilterType( ).equals( PortletFilter.PORTLET_NAME ) )
                {
                    portletFilter.setPortletName( strValue.split( PortletFilter.CONSTANTE_SPACE_STRING ) );
                }
                else
                    if ( portletFilter.getPortletFilterType( ).equals( PortletFilter.PAGE_ID ) )
                    {
                        if ( StringUtils.isNumeric( strValue ) )
                        {
                            portletFilter.setIdPage( Integer.parseInt( strValue ) );
                        }
                        else
                        {
                            strErrorMessage = I18nService.getLocalizedString( ERROR_MESSAGE_NOT_NUMERIC_FIELD, getLocale( ) );
                        }
                    }
        }

        else
        {
            strErrorMessage = I18nService.getLocalizedString( ERROR_MESSAGE_MESSAGE_MANDATORY_SEARCH_FIELD, getLocale( ) );
        }

        return strErrorMessage;
    }

    /**
     * Get the list of authorized portlets.
     *
     * Check :
     * <ul>
     * <li>if user is authorized to manage DocumentListPortlet</li>
     * <li>if user is authorized to manage DocumentPortlet</li>
     * <li>For each portlet :
     * <ul>
     * <li>if user is authorized to manage the linked page</li>
     * <li>if portlet isn't in autopublication mode</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param nDocumentId
     *            The document id
     * @param pOrder
     *            The portlet order
     * @param pFilter
     *            The portlet filter
     * @return A collection of {@link ReferenceItem}
     */
    private Collection<ReferenceItem> getListAuthorizedDocumentListPortlets( int nDocumentId, PortletOrder pOrder, PortletFilter pFilter )
    {
        Collection<ReferenceItem> listPortlets = new ArrayList<>( );

        // Check role PERMISSION_MANAGE for DocumentListPortlet
        if ( RBACService.isAuthorized( PortletType.RESOURCE_TYPE, BlogListPortlet.RESOURCE_ID, PortletResourceIdService.PERMISSION_MANAGE, (User) getUser( ) ) )
        {
            listPortlets.addAll( BlogListPortletHome.findByFilter( nDocumentId, pOrder, pFilter ) );
        }

        // check ROLE PERMISSION_MANAGE for PAGE and WORKGROUP
        return filterByWorkgroup( listPortlets, pFilter );
    }

    /**
     * Filter the given portlets list by its workgroup
     *
     * @param listPortlets
     *            a collection of {@link ReferenceItem}
     * @param pFilter
     *            The portlet filter
     * @return a collection of {@link ReferenceItem}
     */
    private Collection<ReferenceItem> filterByWorkgroup( Collection<ReferenceItem> listPortlets, PortletFilter pFilter )
    {
        // check ROLE PERMISSION_MANAGE for PAGE and WORKGROUP
        Collection<ReferenceItem> listFilteredPortlets = new ArrayList<>( );

        // Check role PERMISSION_MANAGE for workgroup and page and check if
        // portlet isn't autopublished
        for ( ReferenceItem item : listPortlets )
        {
            Portlet portlet = PortletHome.findByPrimaryKey( Integer.parseInt( item.getCode( ) ) );

            if ( ( portlet != null ) && PortletService.getInstance( ).isAuthorized( portlet, getUser( ) ) )
            {
                Map<String, Object> subModel = new HashMap<>( );
                subModel.put( MARK_LIST_PAGE, PortalService.getPagePath( PortletHome.findByPrimaryKey( Integer.parseInt( item.getCode( ) ) ).getPageId( ) ) );
                subModel.put( MARK_PORTLET, item );

                HtmlTemplate subTemplate = AppTemplateService.getTemplate( TEMPLATE_PORTLET_PAGE_PATH, getLocale( ), subModel );
                item.setName( subTemplate.getHtml( ) );

                listFilteredPortlets.add( item );

                if ( ( ( pFilter == null ) || pFilter.isDisplayLatestPortlets( ) )
                        && ( listFilteredPortlets.size( ) >= PortletFilter.PROPERTY_NUMBER_OF_MAX_LATEST_PORTLETS_DISPLAY ) )
                {
                    break;
                }
            }
        }

        return listFilteredPortlets;
    }

    /**
     * Get the list of authorized portlets.
     *
     * Check :
     * <ul>
     * <li>if user is authorized to manage DocumentListPortlet</li>
     * <li>if user is authorized to manage DocumentPortlet</li>
     * <li>For each portlet :
     * <ul>
     * <li>if user is authorized to manage the linked page</li>
     * <li>if portlet isn't in autopublication mode</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param nDocumentId
     *            The document id
     * 
     * @param pOrder
     *            The portlet order
     * @param pFilter
     *            The portlet filter
     * @return A collection of {@link ReferenceItem}
     */
    private Collection<ReferenceItem> getListAuthorizedDocumentPortlets( int nDocumentId, PortletOrder pOrder, PortletFilter pFilter )
    {
        Collection<ReferenceItem> listPortlets = new ArrayList<>( );

        // Check role PERMISSION_MANAGE for DocumentPortlet
        if ( RBACService.isAuthorized( PortletType.RESOURCE_TYPE, BlogPortlet.RESOURCE_ID, PortletResourceIdService.PERMISSION_MANAGE, (User) getUser( ) ) )
        {
            listPortlets.addAll( BlogPortletHome.findByFilter( nDocumentId, pOrder, pFilter ) );
        }

        return filterByWorkgroup( listPortlets, pFilter );
    }

}
