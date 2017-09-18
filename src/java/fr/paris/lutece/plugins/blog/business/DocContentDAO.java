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

package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for HtmlDoc objects
 */
public final class DocContentDAO implements IDocContentDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_document ) FROM htmldocs_content";
    private static final String SQL_QUERY_INSERT_CONTENT = "INSERT INTO htmldocs_content ( id_document, id_html_doc, text_value , binary_value, mime_type ) VALUES ( ? , ? , ? , ? , ? )";
    private static final String SQL_QUERY_SELECT_CONTENT = "SELECT  id_document, id_html_doc , text_value , binary_value, mime_type FROM htmldocs_content WHERE id_html_doc = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM htmldocs_content WHERE id_html_doc = ?  ;";
    private static final String SQL_QUERY_UPDATE = "UPDATE htmldocs_content SET  id_html_doc = ?, text_value = ?, binary_value = ?, mime_type = ? WHERE id_html_doc = ?";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );
        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insertDocContent( DocContent docContent, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONTENT, plugin );
        docContent.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, docContent.getId( ) );
        daoUtil.setInt( 2, docContent.getIdHtmlDocument( ) );
        daoUtil.setString( 3, docContent.getTextValue( ) );
        daoUtil.setBytes( 4, docContent.getBinaryValue( ) );
        daoUtil.setString( 5, docContent.getValueContentType( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DocContent loadDocContent( int idDocument, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTENT, plugin );
        daoUtil.setInt( 1, idDocument );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            DocContent docContent = new DocContent( );

            docContent.setId( daoUtil.getInt( 1 ) );
            docContent.setIdHtmlDocument( daoUtil.getInt( 2 ) );
            docContent.setBinaryValue( daoUtil.getBytes( 4 ) );
            docContent.setTextValue( daoUtil.getString( 3 ) );
            docContent.setValueContentType( daoUtil.getString( 5 ) );
            daoUtil.free( );

            return docContent;
        }
        daoUtil.free( );

        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nDocumentId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nDocumentId );

        daoUtil.executeUpdate( );
        daoUtil.free( );

    }

    @Override
    public void store( DocContent docContent, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        // daoUtil.setInt( 1, docContent.getId( ));
        daoUtil.setInt( 1, docContent.getIdHtmlDocument( ) );
        daoUtil.setString( 2, docContent.getTextValue( ) );
        daoUtil.setBytes( 3, docContent.getBinaryValue( ) );
        daoUtil.setString( 4, docContent.getValueContentType( ) );
        daoUtil.setInt( 5, docContent.getIdHtmlDocument( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
