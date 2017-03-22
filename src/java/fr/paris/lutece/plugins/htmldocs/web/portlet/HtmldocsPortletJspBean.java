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
package fr.paris.lutece.plugins.htmldocs.web.portlet;

import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmldocsPortlet;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmldocsPortletHome;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDocHome;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.business.user.AdminUser;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides the user interface to manage HtmldocsPortlet features
 */
public class HtmldocsPortletJspBean extends PortletJspBean
{

    public static final String MARK_HTML_CONTENT = "htmlcontent";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LIST_HTMLDOC = "htmldoc_list";
    public static final String TEMPLATE_MODIFY_PORTLET = "admin/portlet/modify_portlet.html";
    public static final String PARAMETER_CONTENT_ID = "content_id";
    public static final String PARAMETER_HTML_CONTENT = "html_content";
    public static final String PARAMETER_PORTLET_NAME = "portlet_name";
    public static final String PARAMETER_HTMLDOC_SELECTED = "htmldoc_selected";

    private int nVersion = 1;

    /**
     * Returns the HtmldocsPortlet form of creation
     *
     * @param request
     *            The Http rquest
     * @return the html code of the HtmldocsPortlet portlet form
     */
    @Override
    public String getCreate( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        List<HtmlDoc> listHtmlDocs = HtmlDocHome.getHtmlDocsList( );
        HashMap<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LIST_HTMLDOC, listHtmlDocs );
        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId, model );

        return template.getHtml( );
    }

    /**
     * Returns the HtmldocsPortlet form for update
     * 
     * @param request
     *            The Http request
     * @return the html code of the HtmldocsPortlet form
     */
    @Override
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        HtmldocsPortlet portlet = (HtmldocsPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HtmlDoc htmlDoc = HtmlDocHome.findByPrimaryKey( portlet.getContentId( ) );
        HashMap<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_HTML_CONTENT, htmlDoc.getHtmlContent( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml( );
    }

    /**
     * Gets the current date in sql format
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
     * Treats the creation form of a new HtmldocsPortlet portlet
     *
     * @param request
     *            The Http request
     * @return The jsp URL which displays the view of the created HtmldocsPortlet portlet
     */
    @Override
    public String doCreate( HttpServletRequest request )
    {
        HtmldocsPortlet portlet = new HtmldocsPortlet( );
        AdminUser user = AdminUserService.getAdminUser( request );
        String strSelectedHtmldoc = request.getParameter( PARAMETER_HTMLDOC_SELECTED );
        nVersion = 1;
        // recovers portlet specific attributes
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        HtmlDoc htmldoc = new HtmlDoc( );
        if ( strSelectedHtmldoc.compareTo( "Htmldocs" ) == 0 )
        {
            htmldoc.setContentLabel( request.getParameter( PARAMETER_PORTLET_NAME ) );
            htmldoc.setVersion( nVersion );
            htmldoc.setCreationDate( getSqlDate( ) );
            htmldoc.setUpdateDate( getSqlDate( ) );
            htmldoc.setHtmlContent( request.getParameter( PARAMETER_HTML_CONTENT ) );
            htmldoc.setUser( user.getFirstName( ) );
            htmldoc.setUserCreator( user.getFirstName( ) );
            HtmlDocHome.create( htmldoc );
        }
        else
        {
            htmldoc = HtmlDocHome.findByName( strSelectedHtmldoc );
        }
        int nContentId = htmldoc.getId( );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nPageId );
        portlet.setContentId( nContentId );
        portlet.setPortletName( request.getParameter( PARAMETER_PORTLET_NAME ) );

        // Creates the portlet
        HtmldocsPortletHome.getInstance( ).create( portlet );
        htmldoc.setAttachedPortletId( portlet.getId( ) );
        HtmlDocHome.update( htmldoc );

        // Displays the page with the new Portlet
        return getPageUrl( nPageId );
    }

    /**
     * Treats the update form of the HtmldocsPortlet portlet whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return The jsp URL which displays the view of the updated portlet
     */
    @Override
    public String doModify( HttpServletRequest request )
    {
        // fetches portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        HtmldocsPortlet portlet = (HtmldocsPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HtmlDoc htmlDoc = HtmlDocHome.findByPrimaryKey( portlet.getContentId( ) );

        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // updates the HtmlDoc
        htmlDoc.setHtmlContent( request.getParameter( PARAMETER_HTML_CONTENT ) );
        htmlDoc.setUpdateDate( getSqlDate( ) );
        htmlDoc.setVersion( nVersion++ );
        HtmlDocHome.update( htmlDoc );

        // updates the portlet
        portlet.update( );

        // displays the page with the updated portlet
        return getPageUrl( portlet.getPageId( ) );
    }
}
