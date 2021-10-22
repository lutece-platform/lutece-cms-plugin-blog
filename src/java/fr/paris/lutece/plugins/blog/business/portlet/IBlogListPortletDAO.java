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
/*
 * IDocumentListPortletDAO.java
 *
 * Created on 10 octobre 2006, 15:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.blog.business.portlet;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public interface IBlogListPortletDAO extends IPortletInterfaceDAO
{
    /**
     * Deletes records for a portlet identifier in the tables Blogs_list_portlet
     *
     *
     * @param nPortletId
     *            the portlet identifier
     */
    @Override
    void delete( int nPortletId );

    /**
     * Insert a new record in the table Blogs_list_portlet
     *
     *
     * @param portlet
     *            the instance of the Portlet object to insert
     */
    @Override
    void insert( Portlet portlet );

    /**
     * Loads the data of Document List Portlet whose identifier is specified in parameter
     *
     *
     * @param nPortletId
     *            The Portlet identifier
     * @return theDocumentListPortlet object
     */
    @Override
    Portlet load( int nPortletId );

    /**
     * Load the portlet template whose type is specified in parameter
     * 
     * @param strPortletType
     * @return Map template
     */
    Map<Integer, String> loadPages( String strPortletType );

    /**
     * Update the record in the table
     *
     *
     * @param portlet
     *            A portlet
     */
    @Override
    void store( Portlet portlet );

    /**
     * Tests if is a portlet is portlet type alias
     *
     * @param nPortletId
     *            The identifier of the document
     * @return true if the portlet is alias, false otherwise
     */
    boolean checkIsAliasPortlet( int nPortletId );

    /**
     * 
     * @param plugin
     * @return
     */
    ReferenceList selectBlogListPortletReferenceList( Plugin plugin );

    /**
     * Load the list of portlet
     * 
     * @param nDocumentId
     *            the document ID
     * @param pOrder
     *            order of the portlets
     * @param pFilter
     * @return The Collection of the ReferenceItem
     */

    Collection<ReferenceItem> selectPortletByType( int nDocumentId, PortletOrder pOrder, PortletFilter pFilter );

    /**
     * Get the min of document blog order
     *
     * @return The minimum order from blog list portlet
     */
    int selectMinDocumentBlogOrder( );
}
