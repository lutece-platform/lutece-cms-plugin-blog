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
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.outerj.daisy.diff.DaisyDiff;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.commons.lang.StringUtils;

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
    private static final String TEMPLATE_HISTORY_HTMLDOC = "admin/plugins/htmldocs/history_htmldoc.html";
    private static final String TEMPLATE_PREVIEW_HTMLDOC = "admin/plugins/htmldocs/preview_htmldoc.html";
    private static final String TEMPLATE_DIFF_HTMLDOC = "admin/plugins/htmldocs/diff_htmldoc.html";

    // Parameters
    private static final String PARAMETER_ID_HTMLDOC = "id";
    private static final String PARAMETER_VERSION_HTMLDOC = "htmldoc_version";
    private static final String PARAMETER_VERSION_HTMLDOC2 = "htmldoc_version2";
    private static final String PARAMETER_HTML_CONTENT = "html_content";
    private static final String PARAMETER_EDIT_COMMENT = "edit_comment";
    private static final String PARAMETER_VIEW = "view";
    private static final String PARAMETER_BUTTON_SEARCH = "button_search";
    private static final String PARAMETER_SEARCH_TEXT = "search_text";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_HTMLDOCS = "htmldocs.manage_htmldocs.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_HTMLDOC = "htmldocs.modify_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_HTMLDOC = "htmldocs.create_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_HISTORY_HTMLDOC = "htmldocs.history_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_PREVIEW_HTMLDOC = "htmldocs.preview_htmldoc.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_DIFF_HTMLDOC = "htmldocs.diff_htmldoc.pageTitle";

    // Markers
    private static final String MARK_HTMLDOC_LIST = "htmldoc_list";
    private static final String MARK_HTMLDOC_VERSION_LIST = "htmldoc_version_list";
    private static final String MARK_HTMLDOC = "htmldoc";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_IS_CHECKED = "is_checked";
    private static final String MARK_CURRENT_USER = "current_user";
    private static final String MARK_ID_HTMLDOC = "id";
    private static final String MARK_SEARCH_TEXT = "search_text";
    private static final String MARK_DIFF = "htmldoc_diff";
    private static final String MARK_HTMLDOC2 = "htmldoc2";

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
    private static final String VIEW_HISTORY_HTMLDOC = "historyHtmlDoc";
    private static final String VIEW_PREVIEW_HTMLDOC = "previewHtmlDoc";
    private static final String VIEW_DIFF_HTMLDOC = "diffHtmlDoc";

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
    private boolean _bIsChecked = false;
    private String _strSearchText;

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

        AdminUser user = AdminUserService.getAdminUser( request );

        String strButtonSearch = request.getParameter ( PARAMETER_BUTTON_SEARCH );
        if ( strButtonSearch != null ) {
            // CURRENT USER
            _bIsChecked = request.getParameter( MARK_CURRENT_USER ) != null;
            _strSearchText = request.getParameter( PARAMETER_SEARCH_TEXT );
        }

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

        if ( StringUtils.isNotBlank( _strSearchText ) )
        {
            Iterator<HtmlDoc> iterator = listHtmlDocs.iterator( );
            while ( iterator.hasNext( ) )
            {
                HtmlDoc doc = iterator.next( );
                BodyContentHandler handler = new BodyContentHandler( );
                HtmlParser htmlparser = new HtmlParser( );
                Metadata metadata = new Metadata( );
                ParseContext parseContext = new ParseContext( );
                String strContent;
                try {
                    htmlparser.parse(new ByteArrayInputStream(doc.getHtmlContent().getBytes("UTF-8")), handler, metadata, parseContext);
                    strContent = handler.toString();
                } catch (IOException | SAXException | TikaException e) {
                    AppLogService.error( "Error parsing htmldoc content, not fatal but defaulting to raw html for search (docId: "
                            + doc.getId() + " , title: " + doc.getContentLabel() + ": exception: "+e , e);
                    strContent = doc.getHtmlContent();
                }

                if ( !(
                     strContent.contains( _strSearchText )
                  || doc.getContentLabel( ).contains( _strSearchText )
                  || doc.getEditComment( ).contains( _strSearchText )
                  || doc.getUser( ).contains( _strSearchText )
                  || doc.getUserCreator( ).contains( _strSearchText )
                ) )
                {
                    iterator.remove( );
                }
            }
        }

        Map<String, Object> model = getPaginatedListModel( request, MARK_HTMLDOC_LIST, listHtmlDocs, JSP_MANAGE_HTMLDOCS );
        model.put( MARK_HTMLDOC_FILTER_LIST, getHtmldocFilterList( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_IS_CHECKED, _bIsChecked );
        model.put( MARK_SEARCH_TEXT, _strSearchText );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_HTMLDOCS, TEMPLATE_MANAGE_HTMLDOCS, model );
    }

    @View( value = VIEW_HISTORY_HTMLDOC )
    public String getHistoryHtmlDoc( HttpServletRequest request )
    {
        _htmldoc = null;
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
        List<HtmlDoc> listHtmlDocsVersions = HtmlDocHome.getHtmlDocsVersionsList( nId );

        UrlItem urlHistory = new UrlItem( JSP_MANAGE_HTMLDOCS );
        urlHistory.addParameter( PARAMETER_VIEW , VIEW_HISTORY_HTMLDOC );
        urlHistory.addParameter( PARAMETER_ID_HTMLDOC , nId );

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

        HtmlDocHome.addInitialVersion( _htmldoc );
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
        String strResetVersion = request.getParameter( PARAMETER_VERSION_HTMLDOC );
        int nVersion = -1;
        if (strResetVersion != null )
        {
            nVersion = Integer.parseInt( strResetVersion );
        }

        if ( _htmldoc == null || ( _htmldoc.getId( ) != nId ) || ( strResetVersion != null && _htmldoc.getVersion( ) != nVersion ) )
        {
            if ( strResetVersion != null )
            {
                _htmldoc = HtmlDocHome.findVersion( nId, nVersion );
            } else
            {
                _htmldoc = HtmlDocHome.findByPrimaryKey( nId );
            }
            _htmldoc.setEditComment("");
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_HTMLDOC, _htmldoc );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

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

        HtmlDoc latestVersionHtmlDoc = HtmlDocHome.findByPrimaryKey( nId );
        if ( _htmldoc == null || ( _htmldoc.getId( ) != nId ) )
        {
            _htmldoc = latestVersionHtmlDoc;
        }

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
        HtmlDocHome.addNewVersion( _htmldoc );
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
        if (strVersion != null )
        {
            nVersion = Integer.parseInt( strVersion );
        }

        HtmlDoc htmldoc;
        if ( strVersion != null )
        {
            htmldoc = HtmlDocHome.findVersion( nId, nVersion );
        } else
        {
            htmldoc = HtmlDocHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_HTMLDOC, htmldoc );

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
        if (strVersion != null )
        {
            nVersion = Integer.parseInt( strVersion );
        }
        String strVersion2 = request.getParameter( PARAMETER_VERSION_HTMLDOC2 );

        HtmlDoc htmldoc;
        if ( strVersion != null )
        {
            htmldoc = HtmlDocHome.findVersion( nId, nVersion );
        } else
        {
            htmldoc = HtmlDocHome.findByPrimaryKey( nId );
        }

        int nVersion2 = htmldoc.getVersion( ) - 1;
        if ( strVersion2 != null )
        {
            nVersion2 = Integer.parseInt( strVersion2 );
        }

        HtmlDoc htmldoc2 = HtmlDocHome.findVersion( nId, nVersion2 );
        if ( htmldoc2 == null ) {
            htmldoc2 = HtmlDocHome.findByPrimaryKey( nId );
        }

        if ( htmldoc2.getVersion() > htmldoc.getVersion() ) {
            HtmlDoc tmp = htmldoc2;
            htmldoc2 = htmldoc;
            htmldoc = tmp;
        }

        String strDiff = null;
        HtmlCleaner cleaner = new HtmlCleaner();
        try {
          SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
          TransformerHandler result = tf.newTransformerHandler();
          result.getTransformer().setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
          StringWriter resultWriter = new StringWriter();
          result.setResult(new StreamResult(resultWriter));
          Locale locale = getLocale();

          DomTreeBuilder oldHandler = new DomTreeBuilder();
          cleaner.cleanAndParse(new InputSource(new ByteArrayInputStream(htmldoc2.getHtmlContent().getBytes("UTF-8"))), oldHandler);
          TextNodeComparator leftComparator = new TextNodeComparator( oldHandler, locale );

          DomTreeBuilder newHandler = new DomTreeBuilder();
          cleaner.cleanAndParse(new InputSource(new ByteArrayInputStream(htmldoc.getHtmlContent().getBytes("UTF-8"))), newHandler);
          TextNodeComparator rightComparator = new TextNodeComparator( newHandler, locale );

          HtmlSaxDiffOutput output = new HtmlSaxDiffOutput(result, "");
          HTMLDiffer differ = new HTMLDiffer(output);
          differ.diff(leftComparator, rightComparator);

          strDiff = resultWriter.toString();
        } catch (Exception e) {
          AppLogService.error( "Error generating daisy diff for htmldoc " + nId + ":" + htmldoc.getContentLabel() + "; versions ("+ htmldoc.getVersion() + "," + htmldoc2.getVersion() + ")", e);
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
}
