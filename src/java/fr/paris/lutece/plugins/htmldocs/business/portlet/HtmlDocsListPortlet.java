/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.htmldocs.business.portlet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import fr.paris.lutece.plugins.htmldocs.business.DocumentPageTemplate;
import fr.paris.lutece.plugins.htmldocs.business.DocumentPageTemplateHome;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.service.HtmlDocService;
import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects ArticlesList Portlet
 */
public class HtmlDocsListPortlet extends PortletHtmlContent
{
    public static final String RESOURCE_ID = "HTMLDOCS_LIST_PORTLET";

    /////////////////////////////////////////////////////////////////////////////////
    public static final String MARK_LIST_HTMLDOC_PUBLISHED = "htmldoc_list_published";
    public static final String MARK_PAGE_TEMPLATE = "page_template";
   

    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nPageTemplateDocument;
    private int _nPortletId;
    private List<HtmlDocPublication> _arrayHtmlDOcs= new ArrayList<HtmlDocPublication>( );


    /**
     * Sets the identifier of the portlet type to the value specified in the
     * ArticlesListPortletHome class
     */
    public HtmlDocsListPortlet(  )
    {
        setPortletTypeId( HtmlDocsListPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

   
    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
    	  List<HtmlDoc> listHtmlDocs = HtmlDocService.getInstance().getListDocWhithContent( );
          List<HtmlDoc> listHtmlDocsPublished= new ArrayList<HtmlDoc>();
          GregorianCalendar calendar =new java.util.GregorianCalendar();
          
          for(HtmlDocPublication docPub:this.getArrayHtmlDOcs()){
          for(HtmlDoc doc:listHtmlDocs){	 
        	  	
         		 if(docPub.getIdDocument( ) == doc.getId() && docPub.getDateBeginPublishing().before(new Date(calendar.getTimeInMillis( ))) && docPub.getDateEndPublishing().after(new Date(calendar.getTimeInMillis( )))){
         			 listHtmlDocsPublished.add(doc);
         		 }
         	 }
         	
         	 
          }
    		
    	  DocumentPageTemplate pageTemplate= DocumentPageTemplateHome.findByPrimaryKey(this.getPageTemplateDocument( ));

       	  HashMap<String, Object> model = new HashMap<String, Object>( );
          model.put( MARK_LIST_HTMLDOC_PUBLISHED, listHtmlDocsPublished );
          model.put( MARK_PAGE_TEMPLATE, pageTemplate );
          HtmlTemplate template = AppTemplateService.getTemplate( pageTemplate.getFile( ), request.getLocale( ), model );
       	 
    	

        return template.getHtml();
    }

    
    /**
     * Updates the current instance of the Articles List Portlet object
     */
    public void update(  )
    {
        HtmlDocsListPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the Articles List Portlet object
     */
    public void remove(  )
    {
        HtmlDocsListPortletHome.getInstance(  ).remove( this );
    }

    /**
     * Returns the nPortletId
     *
     * @return The nPortletId
     */
    public int getPortletId(  )
    {
        return _nPortletId;
    }

    /**
     * Sets the IdPortlet
     *
     * @param nPortletId The nPortletId
     */
    public void setPortletId( int nPortletId )
    {
        _nPortletId = nPortletId;
    }

    /**
     * Sets the parent page identifier of the portlet to the value specified in
     * parameter
     *
     * @param nPageTemplateDocument the code
     */
    public void setPageTemplateDocument( int nPageTemplateDocument )
    {
    	_nPageTemplateDocument = nPageTemplateDocument;
    }

    /**
     * Returns the identifier of the parent page of the portlet
     *
     * @return the parent page identifier
     */
    public int getPageTemplateDocument(  )
    {
        return _nPageTemplateDocument;
    }
    
    /**
     * @return the _arrayHtmlDOcs
     */
    public List<HtmlDocPublication> getArrayHtmlDOcs(  )
    {
        return _arrayHtmlDOcs;
    }

    /**
     * @param arrayHtmlDOcs the _arrayHtmlDOcs to set
     */
    public void setArrayHtmlDOcs( List<HtmlDocPublication> arrayHtmlDOcs )
    {
    	_arrayHtmlDOcs = arrayHtmlDOcs;
    }
    /**
     * 
     * @param doc
     */
    public void addIdHtmlDocs(HtmlDocPublication doc){
    	
    	boolean bool= false;
    	for(HtmlDocPublication dc:_arrayHtmlDOcs){
    		
    		if(doc.getIdDocument() == dc.getIdDocument( )){
    			bool=true;
    			break;
    		}
    	
    	}
    	if(!bool) _arrayHtmlDOcs.add( doc );
    }
    /**
     * 
     * @param doc
     */
    public void removeHtmlDocs(HtmlDocPublication doc){
    	
		for(HtmlDocPublication dc:_arrayHtmlDOcs){
		   
		   	if(doc.getIdDocument() == dc.getIdDocument( )){
		   		_arrayHtmlDOcs.remove( dc );
		   		break;
		   	}
		   	
		}
    	
    }

   
}
