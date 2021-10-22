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
package fr.paris.lutece.plugins.blog.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Blog objects
 */
public final class DocContentDAO implements IDocContentDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_document ) FROM blog_content";
    private static final String SQL_QUERY_INSERT_CONTENT = "INSERT INTO blog_content ( id_document, id_type, text_value , binary_value, mime_type ) VALUES ( ?, ? , ? , ?, ? )";
    private static final String SQL_QUERY_INSERT_CONTENT_IN_BLOG = "INSERT INTO blog_blog_content ( id_blog, id_document, priority ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_SELECT_CONTENT = "SELECT a.id_document, id_type, text_value , binary_value, mime_type, b.priority FROM blog_content a , blog_blog_content b WHERE b.id_blog = ?  AND a.id_document = b.id_document";
    private static final String SQL_QUERY_DELETE = "DELETE FROM blog_blog_content WHERE id_blog = ?  ;";
    private static final String SQL_QUERY_DELETE_BY_ID_IN_BLOG = "DELETE FROM blog_blog_content WHERE id_document = ?  ;";
    private static final String SQL_QUERY_UPDATE = "UPDATE blog_content SET id_type= ?, text_value = ?, binary_value = ?, mime_type = ? WHERE id_document = ?";
    private static final String SQL_QUERY_SELECT_CONTENT_BY_PRIMARY_KEY = "SELECT  id_document, id_type , text_value , binary_value, mime_type FROM blog_content WHERE id_document = ? ";
    private static final String SQL_QUERY_DELETE_BY_ID = "DELETE FROM blog_content WHERE id_document = ?  ;";

    private static final String SQL_QUERY_SELECT_CONTENT_TYPE_BY_PRIMARY_KEY = "SELECT id_type, type_label FROM blog_content_type WHERE id_type = ? ";
    private static final String SQL_QUERY_SELECT_CONTENT_TYPE = "SELECT id_type, type_label FROM blog_content_type ";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin ) )
        {
            daoUtil.executeQuery( );
            int nKey = 1;

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }

            daoUtil.free( );
            return nKey;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insertDocContent( DocContent docContent, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONTENT, plugin ) )
        {
            docContent.setId( newPrimaryKey( plugin ) );

            daoUtil.setInt( 1, docContent.getId( ) );
            daoUtil.setInt( 2, docContent.getContentType( ).getIdContentType( ) );
            daoUtil.setString( 3, docContent.getTextValue( ) );
            daoUtil.setBytes( 4, docContent.getBinaryValue( ) );
            daoUtil.setString( 5, docContent.getValueContentType( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    @Override
    public void insertDocContentInBlog( int nIdBlog, int nIdDocument, int nPriority, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONTENT_IN_BLOG, plugin ) )
        {

            daoUtil.setInt( 1, nIdBlog );
            daoUtil.setInt( 2, nIdDocument );
            daoUtil.setInt( 3, nPriority );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DocContent> loadDocContentByIdHtemldoc( int idBlog, Plugin plugin )
    {
        List<DocContent> listDoc = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTENT, plugin ) )
        {
            daoUtil.setInt( 1, idBlog );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                DocContent docContent = new DocContent( );

                docContent.setId( daoUtil.getInt( 1 ) );
                docContent.setContentType( loadContentType( daoUtil.getInt( 2 ), plugin ) );
                docContent.setTextValue( daoUtil.getString( 3 ) );
                docContent.setBinaryValue( daoUtil.getBytes( 4 ) );
                docContent.setValueContentType( daoUtil.getString( 5 ) );
                docContent.setPriority( daoUtil.getInt( 6 ) );

                listDoc.add( docContent );
            }
            daoUtil.free( );
        }
        return listDoc;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DocContent loadDocContent( int idDocument, Plugin plugin )
    {
        DocContent docContent = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTENT_BY_PRIMARY_KEY, plugin ) )
        {
            daoUtil.setInt( 1, idDocument );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                docContent = new DocContent( );

                docContent.setId( daoUtil.getInt( 1 ) );
                docContent.setContentType( loadContentType( daoUtil.getInt( 2 ), plugin ) );
                docContent.setTextValue( daoUtil.getString( 3 ) );
                docContent.setBinaryValue( daoUtil.getBytes( 4 ) );
                docContent.setValueContentType( daoUtil.getString( 5 ) );

            }
            daoUtil.free( );
        }
        return docContent;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nBlogId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nBlogId );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteById( int nDocumentId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID, plugin ) )
        {
            daoUtil.setInt( 1, nDocumentId );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    @Override
    public void deleteInBlogById( int nDocumentId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID_IN_BLOG, plugin ) )
        {
            daoUtil.setInt( 1, nDocumentId );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DocContent docContent, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            daoUtil.setInt( 1, docContent.getContentType( ).getIdContentType( ) );

            daoUtil.setString( 2, docContent.getTextValue( ) );
            daoUtil.setBytes( 3, docContent.getBinaryValue( ) );
            daoUtil.setString( 4, docContent.getValueContentType( ) );
            daoUtil.setInt( 5, docContent.getId( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ContentType loadContentType( int idType, Plugin plugin )
    {
        ContentType contentType = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTENT_TYPE_BY_PRIMARY_KEY, plugin ) )
        {
            daoUtil.setInt( 1, idType );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                contentType = new ContentType( );

                contentType.setIdContentType( daoUtil.getInt( 1 ) );
                contentType.setLabel( daoUtil.getString( 2 ) );

            }
            daoUtil.free( );

            return contentType;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ContentType> loadListContentType( Plugin plugin )
    {
        List<ContentType> listcontentType = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTENT_TYPE, plugin ) )
        {

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                ContentType contentType = new ContentType( );

                contentType.setIdContentType( daoUtil.getInt( 1 ) );
                contentType.setLabel( daoUtil.getString( 2 ) );
                listcontentType.add( contentType );

            }
            daoUtil.free( );
        }
        return listcontentType;
    }
}
