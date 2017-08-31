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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmlDocPublication;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmlDocPublicationHome;
import fr.paris.lutece.plugins.htmldocs.service.HtmlDocService;
import fr.paris.lutece.plugins.htmldocs.service.PublishingService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage HtmlDoc features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManagePublicationHtmlDocs.jsp", controllerPath = "jsp/admin/plugins/htmldocs/", right = "HTMLDOCS_MANAGEMENT" )
public class HtmlDocPublicationJspBean extends HtmlDocJspBean
{
   


    private static final String TEMPLATE_PUBLICATION_HTMLDOC = "/admin/plugins/htmldocs/publication_htmldoc.html";

    // Properties for page titles
  
    // Properties
    private static final String PROPERTY_PAGE_TITLE_PUBLICATION_HTMLDOC = "htmldocs.publication_htmldoc.pageTitle";

   
    // Markers
    public static final String MARK_PORTLET_LIST = "portlet_list";

 

    // Properties


    // Validations

    // Views
    private static final String VIEW_MANAGE_PUBLICATION = "manageHtmlDocsPublication";

    // Actions
    private static final String ACTION_PUBLISHE_DOCUMENT = "publishDocument";
    private static final String ACTION_UNPUBLISHE_DOCUMENT = "unPublishDocument";

    // Infos
   
    // Filter Marks
    //Parma
    private static final String PARAMETER_PUBLISHED_DATE = "dateBeginPublishing";
    private static final String PARAMETER_UNPUBLISHED_DATE = "dateEndPublishing";
    private static final String PARAMETER_PORTLET_ID = "idPortlet";



    // Session variable to store working values
    protected HtmlDocPublication _htmldocPublication;


    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_PUBLICATION )
    public String getManageHtmlDocsPublication( HttpServletRequest request )
	{ 
    	int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
	    
    	_htmldocPublication = ( _htmldocPublication != null ) ? _htmldocPublication : new HtmlDocPublication( );

	    if ( _htmldoc == null || ( _htmldoc.getId( ) != nId )  )
	    {
	       
	            _htmldoc = HtmlDocService.getInstance().findByPrimaryKeyWithoutBinaries( nId );
	           
	      
	    }
	
	    Map<String, Object> model = getModel( );
	    model.put( MARK_HTMLDOC, _htmldoc );
	    model.put( MARK_PORTLET_LIST, PublishingService.getInstance().getHtmlDocsPortletstoPublish( ) );

	
	    return getPage( PROPERTY_PAGE_TITLE_PUBLICATION_HTMLDOC, TEMPLATE_PUBLICATION_HTMLDOC, model );
    }
    
    @Action( ACTION_PUBLISHE_DOCUMENT )
    public String doPublishDocument( HttpServletRequest request ) throws ParseException
    {
    	populateHtmlDocPublication(_htmldocPublication, request);
    	HtmlDocPublicationHome.create(_htmldocPublication);
        _htmldoc = HtmlDocService.getInstance().findByPrimaryKeyWithoutBinaries( _htmldocPublication.getIdDocument( ) );

    	
    	return getManageHtmlDocsPublication( request );
    	
    }
    
    @Action( ACTION_UNPUBLISHE_DOCUMENT )
    public String doUnPublishDocument( HttpServletRequest request )
    {
    	
    	populate(_htmldocPublication, request);
    	
    	HtmlDocPublicationHome.remove(_htmldocPublication.getIdDocument(), _htmldocPublication.getIdPortlet());
        _htmldoc = HtmlDocService.getInstance().findByPrimaryKeyWithoutBinaries( _htmldocPublication.getIdDocument( ) );
    	
    	return getManageHtmlDocsPublication( request );
    	
    }


    private void populateHtmlDocPublication(HtmlDocPublication htmldocPublication,HttpServletRequest request) throws ParseException{
    	
    	int nIdDoc = Integer.parseInt( request.getParameter( PARAMETER_ID_HTMLDOC ) );
    	int nIdPortlet = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );

    	String dateBeginPublishing = request.getParameter( PARAMETER_PUBLISHED_DATE ) ;
    	String dateEndPublishing = request.getParameter( PARAMETER_UNPUBLISHED_DATE ) ;
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date parsed = null;
        
        parsed = sdf.parse(dateBeginPublishing);
        java.sql.Date _dateBeginPublishing = new java.sql.Date(parsed.getTime());
        
        parsed = sdf.parse(dateEndPublishing);
        java.sql.Date _dateEndPublishing = new java.sql.Date(parsed.getTime());
       	
    	htmldocPublication.setIdDocument(nIdDoc);
    	htmldocPublication.setIdPortlet(nIdPortlet);
    	htmldocPublication.setDateBeginPublishing(_dateBeginPublishing);
    	htmldocPublication.setDateEndPublishing(_dateEndPublishing);

    }
  
}
