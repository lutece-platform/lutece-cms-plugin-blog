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
package fr.paris.lutece.plugins.htmldocs.web;

import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDocHome;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmldocsPortlet;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmldocsPortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage HtmlDoc features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageHtmlDocs.jsp", controllerPath = "jsp/admin/plugins/htmldocs/", right = "HTMLDOCS_MANAGEMENT" )
public class HtmlDocJspBean extends ManageHtmldocsJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_HTMLDOCS = "/admin/plugins/htmldocs/manage_htmldocs.html";
    private static final String TEMPLATE_CREATE_HTMLDOC = "/admin/plugins/htmldocs/create_htmldoc.html";
    private static final String TEMPLATE_MODIFY_HTMLDOC = "/admin/plugins/htmldocs/modify_htmldoc.html";

    // Parameters
    private static final String PARAMETER_ID_HTMLDOC = "id";
    private static final String PARAMETER_VERSION_HTMLDOC = "htmldoc_version";
    private static final String PARAMETER_HTML_CONTENT = "html_content";
    private static final String PARAMETER_ACTUAL_VERSION = "actual_version";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_HTMLDOCS = "htmldocs.manage_htmldocs.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_HTMLDOC = "htmldocs.modify_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_HTMLDOC = "htmldocs.create_htmldoc.pageTitle";

    // Markers
    private static final String MARK_HTMLDOC_LIST = "htmldoc_list";
    private static final String MARK_HTMLDOC_VERSION_LIST = "htmldoc_version_list";
    private static final String MARK_HTMLDOC = "htmldoc";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_SORTED_ATTRIBUTE = "sorted_attribute_name";
    private static final String MARK_ASC_SORT = "asc_sort";
    private static final String MARK_IS_CHECKED = "is_checked";
    private static final String MARK_CHECKED = "checked";
    private static final String MARK_UNCHECKED = "unchecked";
    private static final String MARK_CURRENT_USER = "current_user";
    private static final String MARK_ACTUAL_VERSION = "actual_version";

    private static final String JSP_MANAGE_HTMLDOCS = "jsp/admin/plugins/htmldocs/ManageHtmlDocs.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_HTMLDOC = "htmldocs.message.confirmRemoveHtmlDoc";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "htmldocs.model.entity.htmldoc.attribute.";

    // Views
    private static final String VIEW_MANAGE_HTMLDOCS = "manageHtmlDocs";
    private static final String VIEW_CREATE_HTMLDOC = "createHtmlDoc";
    private static final String VIEW_MODIFY_HTMLDOC = "modifyHtmlDoc";
    private static final String VIEW_PREVIOUS_VERSION_HTMLDOC = "previousVersionHtmlDoc";

    // Actions
    private static final String ACTION_CREATE_HTMLDOC = "createHtmlDoc";
    private static final String ACTION_MODIFY_HTMLDOC = "modifyHtmlDoc";
    private static final String ACTION_REMOVE_HTMLDOC = "removeHtmlDoc";
    private static final String ACTION_CONFIRM_REMOVE_HTMLDOC = "confirmRemoveHtmlDoc";

    // Infos
    private static final String INFO_HTMLDOC_CREATED = "htmldocs.info.htmldoc.created";
    private static final String INFO_HTMLDOC_UPDATED = "htmldocs.info.htmldoc.updated";
    private static final String INFO_HTMLDOC_REMOVED = "htmldocs.info.htmldoc.removed";

    // Filter Marks
    private static final String MARK_HTMLDOC_FILTER_LIST = "htmldoc_filter_list";
    private static final String MARK_HTMLDOC_FILTER_NAME = "Nom";
    private static final String MARK_HTMLDOC_FILTER_DATE = "Date";
    private static final String MARK_HTMLDOC_FILTER_USER = "Utilisateur";

    // Session variable to store working values
    private HtmlDoc _htmldoc;
    private boolean _bIsSorted = false;
    private String _strSortedAttributeName;
    private boolean _bIsChecked = false;

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
        List<HtmlDoc> listHtmlDocs = HtmlDocHome.getHtmlDocsList( );
        List<HtmlDoc> listHtmlDocsVersions = HtmlDocHome.getHtmlDocsVersionsList( );

        // SORT
        String strSortedAttributeName = request.getParameter( MARK_SORTED_ATTRIBUTE );
        String strAscSort = null;

        if ( strSortedAttributeName != null || _bIsSorted == true )
        {
            if ( strSortedAttributeName == null )
            {
                strSortedAttributeName = _strSortedAttributeName;
            }
            strAscSort = request.getParameter( MARK_ASC_SORT );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listHtmlDocs, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );

            _bIsSorted = true;

            _strSortedAttributeName = strSortedAttributeName;
        }

        // CURRENT USER
        String strCurrentUser = request.getParameter( MARK_CURRENT_USER );
        String strIsChecked = null;
        AdminUser user = AdminUserService.getAdminUser( request );

        if ( strCurrentUser != null )
        {
            if ( _bIsChecked == true )
                _bIsChecked = false;
            else
                if ( _bIsChecked == false )
                {
                    _bIsChecked = true;
                    Iterator<HtmlDoc> iterator = listHtmlDocs.iterator( );
                    while ( iterator.hasNext( ) )
                    {
                        HtmlDoc doc = iterator.next( );
                        if ( doc.getUser( ).compareTo( user.getFirstName( ) ) != 0 )
                        {
                            iterator.remove( );
                        }
                    }
                }
        }
        else
            if ( _bIsChecked == true )
            {
                Iterator<HtmlDoc> iterator = listHtmlDocs.iterator( );
                while ( iterator.hasNext( ) )
                {
                    HtmlDoc doc = iterator.next( );
                    if ( doc.getUser( ).compareTo( user.getFirstName( ) ) != 0 )
                    {
                        iterator.remove( );
                    }
                }
            }
        if ( _bIsChecked == false )
            strIsChecked = MARK_UNCHECKED;
        else
            strIsChecked = MARK_CHECKED;

        Map<String, Object> model = getPaginatedListModel( request, MARK_HTMLDOC_LIST, listHtmlDocs, JSP_MANAGE_HTMLDOCS );
        model.put( MARK_HTMLDOC_FILTER_LIST, getHtmldocFilterList( ) );
        model.put( MARK_HTMLDOC_VERSION_LIST, listHtmlDocsVersions );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_IS_CHECKED, strIsChecked );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_HTMLDOCS, TEMPLATE_MANAGE_HTMLDOCS, model );
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
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_HTMLDOC, TEMPLATE_CREATE_HTMLDOC, model );
    }

    /**
     * Gets the current
     *
     * @return the current date in sql format
     */
    public java.sql.Date getSqlDate( )
    {
        java.util.Date utilDate = new java.util.Date( );
        java.sql.Date sqlDate = new java.sql.Date( utilDate.getTime( ) );

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

        HtmlDocHome.create( _htmldoc );
        HtmlDocHome.createVersion( _htmldoc );
        addInfo( INFO_HTMLDOC_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_HTMLDOCS );
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
        HtmlDoc htmldoc = HtmlDocHome.findByPrimaryKey( nId );
        int nAttachedPortletId = htmldoc.getAttachedPortletId( );
        if ( nAttachedPortletId != 0 )
        {
            HtmldocsPortletHome.getInstance( ).remove( HtmldocsPortletHome.findByPrimaryKey( nAttachedPortletId ) );
        }
        HtmlDocHome.remove( nId );
        HtmlDocHome.removeVersions( nId );
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

        if ( _htmldoc == null || ( _htmldoc.getId( ) != nId ) )
        {
            _htmldoc = HtmlDocHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_HTMLDOC, _htmldoc );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_HTMLDOC, TEMPLATE_MODIFY_HTMLDOC, model );
    }

    /**
     * Returns the form to update info about a htmldoc
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_PREVIOUS_VERSION_HTMLDOC )
    public String getPreviousVersionHtmlDoc( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        int nVersion = Integer.parseInt( request.getParameter( PARAMETER_VERSION_HTMLDOC ) );
        int nActualVersion = Integer.parseInt( request.getParameter( PARAMETER_ACTUAL_VERSION ) );

        _htmldoc = HtmlDocHome.findVersion( nId, nVersion );
        _htmldoc.setVersion( nVersion++ );

        Map<String, Object> model = getModel( );
        model.put( MARK_HTMLDOC, _htmldoc );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_ACTUAL_VERSION, nActualVersion );

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
        String strVersion = request.getParameter( PARAMETER_ACTUAL_VERSION );
        String strHtmlContent = request.getParameter( PARAMETER_HTML_CONTENT );

        if ( _htmldoc == null || ( _htmldoc.getId( ) != nId ) )
        {
            _htmldoc = HtmlDocHome.findByPrimaryKey( nId );
        }

        if ( strVersion != null )
        {
            int nVersion = Integer.parseInt( strVersion );
            _htmldoc.setVersion( nVersion );
        }
        _htmldoc.setHtmlContent( strHtmlContent );
        _htmldoc.setUser( AdminUserService.getAdminUser( request ).getFirstName( ) );

        // Check constraints
        if ( !validateBean( _htmldoc, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_HTMLDOC, PARAMETER_ID_HTMLDOC, _htmldoc.getId( ) );
        }

        _htmldoc.setVersion( _htmldoc.getVersion( ) + 1 );
        HtmlDocHome.createVersion( _htmldoc );
        HtmlDocHome.update( _htmldoc );
        addInfo( INFO_HTMLDOC_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_HTMLDOCS );
    }

    private ReferenceList getHtmldocFilterList( )
    {
        ReferenceList list = new ReferenceList( );
        list.addItem( MARK_HTMLDOC_FILTER_NAME, "Nom" );
        list.addItem( MARK_HTMLDOC_FILTER_DATE, "Date" );
        list.addItem( MARK_HTMLDOC_FILTER_USER, "Utilisateur" );

        return list;
    }
}
