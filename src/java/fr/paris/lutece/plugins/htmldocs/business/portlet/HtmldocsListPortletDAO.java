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

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class provides Data Access methods for ArticlesListPortlet objects
 */
public final class HtmldocsListPortletDAO implements IHtmlDocsListPortletDAO
{
	
    private static final String SQL_QUERY_SELECTALL = "SELECT id_portlet , id_page_template_document FROM htmldocs_list_portlet ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO htmldocs_list_portlet ( id_portlet , id_page_template_document ) VALUES ( ? , ? )";
    private static final String SQL_QUERY_SELECT = "SELECT id_portlet , id_page_template_document FROM htmldocs_list_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE htmldocs_list_portlet SET id_portlet = ?, id_page_template_document = ? WHERE id_portlet = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM htmldocs_list_portlet WHERE id_portlet= ? ";
   /* private static final String SQL_QUERY_DELETE_PUBLISHED_DOCUMENT_PORTLET = " DELETE FROM document_published WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_DOCUMENTS_BY_TYPE_AND_CATEGORY = "SELECT DISTINCT b.id_portlet , a.name, a.date_update " +
        "FROM document_list_portlet b " +
        "LEFT JOIN document_published c ON b.id_portlet = c.id_portlet AND c.id_document= ? " +
        "INNER JOIN core_portlet a ON b.id_portlet = a.id_portlet " +
        "LEFT OUTER JOIN document_category_list_portlet d ON b.id_portlet = d.id_portlet " +
        "INNER JOIN core_page f ON a.id_page = f.id_page " +
        "WHERE c.id_portlet IS NULL AND b.code_document_type = ? AND (d.id_category IN (SELECT e.id_category " +
        "FROM document_category_link e WHERE e.id_document = ?) OR d.id_category IS NULL) ";*/
    private static final String SQL_QUERY_CHECK_IS_ALIAS = "SELECT id_alias FROM core_portlet_alias WHERE id_alias = ?";

    //Category
    private static final String SQL_QUERY_INSERT_HTMLDOCS_PORTLET = "INSERT INTO htmldocs_list_portlet_htmldocs ( id_portlet , id_html_doc, status ) VALUES ( ? , ?, ? )";
    private static final String SQL_QUERY_DELETE_HTMLDOCS_PORTLET = " DELETE FROM htmldocs_list_portlet_htmldocs WHERE id_portlet = ? ";
    //private static final String SQL_QUERY_DELETE_AUTO_PUBLICATION_PORTLET = " DELETE FROM document_auto_publication WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_CATEGORY_PORTLET = "SELECT id_html_doc FROM htmldocs_list_portlet_htmldocs WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_PAGE_PORTLET="SELECT id_page_template_document,description from  htmldocs_page_template";
   

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table htmldocs_list_portlet
     *
     * @param portlet the instance of the Portlet object to insert
     */
    public void insert( Portlet portlet )
    {
        HtmlDocsListPortlet p = (HtmlDocsListPortlet) portlet;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, p.getId(  ) );
        daoUtil.setInt( 2, p.getPageTemplateDocument( ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        insertHtmlsDocsId( portlet );
    }

    /**
     * Insert a list of doc for a specified portlet
     * @param portlet the DocumentListPortlet to insert
     */
    private void insertHtmlsDocsId( Portlet portlet )
    {
    	HtmlDocsListPortlet p = (HtmlDocsListPortlet) portlet;

        if ( !p.getArrayIdHtmlDOcs().isEmpty() )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_HTMLDOCS_PORTLET );

            for ( int nIdDoc : p.getArrayIdHtmlDOcs() )
            {
                daoUtil.setInt( 1, p.getId(  ) );
                daoUtil.setInt( 2, nIdDoc );
                daoUtil.setInt( 3, 1 );

                daoUtil.executeUpdate(  );
            }

            daoUtil.free(  );
        }
    }

    /**
    * Deletes records for a portlet identifier in the tables htmldocs_list_portlet
    * htmldocs_list_portlet_htmldocs
    *
    * @param nPortletId the portlet identifier
    */
    public void delete( int nPortletId )
    {
        deleteHtmlsDocsId( nPortletId );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

    }

    /**
     * Delete docs for the specified portlet
     * @param nPortletId The portlet identifier
     */
    private void deleteHtmlsDocsId( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_HTMLDOCS_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }


    /**
     * Loads the data of Document List Portlet whose identifier is specified in parameter
     *
     * @param nPortletId The Portlet identifier
     * @return theDocumentListPortlet object
     */
    public Portlet load( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        HtmlDocsListPortlet portlet = new HtmlDocsListPortlet(  );

        if ( daoUtil.next(  ) )
        {
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setPageTemplateDocument( daoUtil.getInt( 2 ) );
        }

        daoUtil.free(  );

        portlet.setArrayIdHtmlDOcs( loadHtmlsDocsId( nPortletId ) );

        return portlet;
    }
    
    public Map<Integer,String> loadPages( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PAGE_PORTLET );
        daoUtil.executeQuery(  );

        Map<Integer, String> page = new HashMap<Integer,String>();

        while ( daoUtil.next(  ) )
        {
        	page.put(daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );


        return page;
    }

    /**
     * Load a list of Id HtmlDocs
     * @param nPortletId
     * @return List of IdDoc
     */
    private List<Integer> loadHtmlsDocsId( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CATEGORY_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        List<Integer> nListIdCategory = new ArrayList<Integer>(  );

        while ( daoUtil.next(  ) )
        {
            nListIdCategory.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return nListIdCategory;
    }

    /**
     * Update the record in the table
     *
     * @param portlet A portlet
     */
    public void store( Portlet portlet )
    {
    	HtmlDocsListPortlet p = (HtmlDocsListPortlet) portlet;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, p.getId(  ) );
        daoUtil.setInt( 2, p.getPageTemplateDocument( ) );
        daoUtil.setInt( 3, p.getId(  ) );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );

        deleteHtmlsDocsId( p.getId(  ) );
        insertHtmlsDocsId( p );
    }

   
    /**
     * Tests if is a portlet is portlet type alias
     *
     * @param nPortletId The identifier of the document
     * @return true if the portlet is alias, false otherwise
     */
    public boolean checkIsAliasPortlet( int nPortletId )
    {
        boolean bIsAlias = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_IS_ALIAS );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bIsAlias = true;
        }

        daoUtil.free(  );

        return bIsAlias;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectHtmlDocListPortletReferenceList( Plugin plugin )
    {
        ReferenceList htmlDocPortletList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
        	htmlDocPortletList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return htmlDocPortletList;
    }
}
