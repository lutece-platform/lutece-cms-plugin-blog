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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for TagDAO objects
 */
public final class TagDAO implements ITagDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_tag ) FROM blog_tag";
    private static final String SQL_QUERY_INSERT_TAG = "INSERT INTO blog_tag ( id_tag, name ) VALUES ( ? , ? )";
    private static final String SQL_QUERY_SELECT_TAG = "SELECT  id_tag, name FROM blog_tag WHERE id_tag = ? ";
    private static final String SQL_QUERY_SELECT_ALL_TAG = "SELECT  id_tag, name FROM blog_tag ";
    private static final String SQL_QUERY_SELECT_TAG_BY_NAME = "SELECT  id_tag, name FROM blog_tag WHERE name = ? ";

    private static final String SQL_QUERY_DELETE = "DELETE FROM blog_tag WHERE id_tag = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE blog_tag SET  id_tag = ?, name = ? WHERE id_tag = ?";

    private static final String SQL_QUERY_INSERT_TAG_DOC = "INSERT INTO blog_tag_document ( id_tag, id_blog, priority ) VALUES ( ? , ?, ? )";
    private static final String SQL_QUERY_SELECT_TAG_DOC = " SELECT  a.id_tag, t.name, a.priority FROM blog_tag_document a Inner join blog_tag t on (t.id_tag= a.id_tag) WHERE a.id_blog = ? ORDER BY priority";
    private static final String SQL_QUERY_DELETE_BY_TAG = "DELETE FROM blog_tag_document WHERE id_tag = ? AND id_blog = ?";
    private static final String SQL_QUERY_DELETE_BY_DOC = "DELETE FROM blog_tag_document WHERE id_blog = ? ";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        int nKey = 1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Tag tag, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_TAG, plugin ) )
        {
            tag.setIdTag( newPrimaryKey( plugin ) );

            daoUtil.setInt( 1, tag.getIdTag( ) );
            daoUtil.setString( 2, tag.getName( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Tag load( int idTag, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TAG, plugin ) )
        {
            daoUtil.setInt( 1, idTag );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                Tag tag = new Tag( );

                tag.setIdTag( daoUtil.getInt( 1 ) );
                tag.setName( daoUtil.getString( 2 ) );

                daoUtil.free( );

                return tag;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Tag> loadAllTag( Plugin plugin )
    {
        List<Tag> listTag = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TAG, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Tag tag = new Tag( );

                tag.setIdTag( daoUtil.getInt( 1 ) );
                tag.setName( daoUtil.getString( 2 ) );

                listTag.add( tag );

            }
        }

        return listTag;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int idTag, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, idTag );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Tag tag, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {

            daoUtil.setInt( 1, tag.getIdTag( ) );
            daoUtil.setString( 2, tag.getName( ) );
            daoUtil.setInt( 3, tag.getIdTag( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( int nIdTag, int nIdDoc, int nPriority, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_TAG_DOC, plugin ) )
        {
            daoUtil.setInt( 1, nIdTag );
            daoUtil.setInt( 2, nIdDoc );
            daoUtil.setInt( 3, nPriority );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByTAG( int idTag, int idDoc, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_TAG, plugin ) )
        {
            daoUtil.setInt( 1, idTag );
            daoUtil.setInt( 2, idDoc );

            daoUtil.executeUpdate( );
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByDoc( int idDoc, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_DOC, plugin ) )
        {
            daoUtil.setInt( 1, idDoc );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Tag> loadByDoc( int idDoc, Plugin plugin )
    {
        List<Tag> list = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TAG_DOC, plugin ) )
        {
            daoUtil.setInt( 1, idDoc );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Tag tag = new Tag( );
                tag.setIdTag( daoUtil.getInt( 1 ) );
                tag.setName( daoUtil.getString( 2 ) );
                tag.setPriority( daoUtil.getInt( 3 ) );
                list.add( tag );
            }
        }

        return list;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectTagsReferenceList( Plugin plugin )
    {
        ReferenceList blogList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_TAG, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                blogList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return blogList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Tag> loadListTagByIdDoc( int idDoc, Plugin plugin )
    {
        List<Tag> listTag = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TAG_DOC, plugin ) )
        {
            daoUtil.setInt( 1, idDoc );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Tag tag = new Tag( );
                tag.setIdTag( daoUtil.getInt( 1 ) );
                tag.setName( daoUtil.getString( 2 ) );
                tag.setPriority( daoUtil.getInt( 3 ) );

                listTag.add( tag );

            }
        }

        return listTag;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Tag loadByName( String strName, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TAG_BY_NAME, plugin ) )
        {
            daoUtil.setString( 1, strName );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                Tag tag = new Tag( );

                tag.setIdTag( daoUtil.getInt( 1 ) );
                tag.setName( daoUtil.getString( 2 ) );

                daoUtil.free( );

                return tag;
            }
        }
        return null;
    }
}
