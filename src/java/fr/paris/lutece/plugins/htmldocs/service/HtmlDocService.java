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
package fr.paris.lutece.plugins.htmldocs.service;


import fr.paris.lutece.plugins.htmldocs.business.DocContent;
import fr.paris.lutece.plugins.htmldocs.business.DocContentHome;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDocHome;



/**
 * This Service manages document actions (create, move, delete, validate ...)
 * and notify listeners.
 */
public class HtmlDocService
{
 
    private static HtmlDocService _singleton = new HtmlDocService(  );
    

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static HtmlDocService getInstance(  )
    {
        return _singleton;
    }

   
    public void createDocument( HtmlDoc htmlDoc, DocContent docContent)
        
    {
    	HtmlDocHome.addInitialVersion(htmlDoc);
    	if(docContent != null ){
    	
    		docContent.setIdHtmlDocument(htmlDoc.getId( ));
    		DocContentHome.create(docContent);        
    	 
    	}
    }
    
    public void deleteDocument( int nId)
    
    {
    	DocContentHome.remove(nId);
    	HtmlDocHome.remove( nId );
        HtmlDocHome.removeVersions( nId );
        
    }
    
 
    public void updateDocument(  HtmlDoc htmlDoc, DocContent docContent)
    
    {
    	HtmlDocHome.addNewVersion( htmlDoc );
    	if(docContent != null && DocContentHome.getDocsContent(htmlDoc.getId( ))!=null){
        	
    		docContent.setIdHtmlDocument(htmlDoc.getId( ));
        	DocContentHome.update(docContent);
    	 
    	}else if(docContent != null ){
    		
    		docContent.setIdHtmlDocument(htmlDoc.getId( ));
        	DocContentHome.create(docContent);
    	}
        
    }
    
   public HtmlDoc loadDocument( int nIdDocument)
    
    {
	   HtmlDoc htmlDoc=HtmlDocHome.findByPrimaryKey( nIdDocument );
       DocContent docContent= DocContentHome.getDocsContent(nIdDocument);
       htmlDoc.setDocContent(docContent);
       
       return htmlDoc;
	   
    }


   
   
}
