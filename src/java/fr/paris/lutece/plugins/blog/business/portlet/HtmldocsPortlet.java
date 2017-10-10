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
package fr.paris.lutece.plugins.blog.business.portlet;

import java.sql.Date;
import java.util.GregorianCalendar;

import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.plugins.blog.business.HtmlDoc;
import fr.paris.lutece.plugins.blog.business.HtmlDocHome;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents business objects HtmldocsPortlet
 */
public class HtmldocsPortlet extends PortletHtmlContent
{
    public static final String RESOURCE_ID = "HTMLDOCS_PORTLET";

    /**
     * Sets the identifier of the portlet type to value specified
     */
    public HtmldocsPortlet( )
    {
        setPortletTypeId( HtmldocsPortletHome.getInstance( ).getPortletTypeId( ) );
    }

    private int _nContentId;

    private String _strName;

    private HtmlDocPublication _htmlDocPublication;

    /**
     * Returns the HTML code of the HtmldocsPortlet portlet with XML heading
     *
     * @param request
     *            The HTTP servlet request
     * @return the HTML code of the HtmldocsPortlet portlet
     */
    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
        GregorianCalendar calendar = new java.util.GregorianCalendar( );
        StringBuilder builder= new StringBuilder();
        HtmlDoc htmldoc = HtmlDocHome.findByPrimaryKey( this.getContentId( ) );
        HtmlDocPublication docPub = HtmlDocPublicationHome.findDocPublicationByPimaryKey( this.getContentId( ), this.getId( ) );
        if ( docPub != null && docPub.getIdDocument( ) != 0 && docPub.getDateBeginPublishing( ).before( new Date( calendar.getTimeInMillis( ) ) )
                && docPub.getDateEndPublishing( ).after( new Date( calendar.getTimeInMillis( ) ) ) )
        {
        	if(this.getDisplayPortletTitle() == 0){
        		
        		builder.append(this.getName());
        		builder.append( "<br />");
            
            }
             builder.append( htmldoc.getHtmlContent( ) );
             
             return builder.toString();
        }

        return "";
    }

    /**
     * Updates the current instance of the HtmldocsPortlet object
     */
    public void update( )
    {
        HtmldocsPortletHome.getInstance( ).update( this );
    }

    /**
     * Removes the current instance of the HtmldocsPortlet object
     */
    @Override
    public void remove( )
    {
        HtmlDocPublicationHome.removeByIdPortlet( this.getId( ) );
        // HtmlDocHome.remove( this.getContentId( ) );
        HtmldocsPortletHome.getInstance( ).remove( this );
    }

    /**
     * Sets the id of the html document
     *
     * @param the
     *            id of the document
     */
    public void setContentId( int nContentId )
    {
        _nContentId = nContentId;
    }

    /**
     * Get the id of the html document
     *
     * @return the id of the document
     */
    public int getContentId( )
    {
        return _nContentId;
    }

    /**
     * Sets the HtmlDocPublication of the html document
     *
     * @param the
     *            HtmlDocPublication of the document
     */
    public void setHtmlDocPublication( HtmlDocPublication htmlDocPublication )
    {
        _htmlDocPublication = htmlDocPublication;
    }

    /**
     * Get the HtmlDocPublication of the html document
     *
     * @return the HtmlDocPublication of the document
     */
    public HtmlDocPublication getHtmlDocPublication( )
    {
        return _htmlDocPublication;
    }

    /**
     * Sets the name of the html document
     *
     * @param the
     *            name of the document
     */
    public void setPortletName( String strName )
    {
        _strName = strName;
    }

    /**
     * Get the name of the html document
     *
     * @return the name of the document
     */
    public String getPortletName( )
    {
        return _strName;
    }
}
