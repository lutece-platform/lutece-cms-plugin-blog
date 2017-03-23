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

package fr.paris.lutece.plugins.htmldocs.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for HtmlDoc objects
 */
public final class HtmlDocDAO implements IHtmlDocDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_html_doc ) FROM htmldocs";
    private static final String SQL_QUERY_NEW_PK_VERSION = "SELECT max( id_version ) FROM htmldocs_versions";
    private static final String SQL_QUERY_SELECT = "SELECT id_html_doc,  version, content_label, creation_date, update_date, html_content, user, user_creator, attached_portlet_id FROM htmldocs WHERE id_html_doc = ?";
    private static final String SQL_QUERY_SELECT_BY_NAME = "SELECT id_html_doc,  version, content_label, creation_date, update_date, html_content, user, user_creator, attached_portlet_id FROM htmldocs WHERE content_label = ?";
    private static final String SQL_QUERY_SELECT_VERSION = "SELECT id_html_doc, version, content_label, creation_date, update_date, html_content, user, user_creator, attached_portlet_id FROM htmldocs_versions WHERE id_html_doc = ? AND version = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO htmldocs ( id_html_doc,  version, content_label, creation_date, update_date, html_content, user, user_creator, attached_portlet_id ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM htmldocs WHERE id_html_doc = ?";
    private static final String SQL_QUERY_DELETE_VERSIONS = "DELETE FROM htmldocs_versions WHERE id_html_doc = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE htmldocs SET id_html_doc = ?, version = ?, content_label = ?, creation_date = ?, update_date = ?, html_content = ?, user = ?, user_creator = ?, attached_portlet_id = ? WHERE id_html_doc = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_html_doc, version, content_label, creation_date, update_date, html_content, user, user_creator, attached_portlet_id FROM htmldocs";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_html_doc FROM htmldocs";
    private static final String SQL_QUERY_SELECTALL_VERSION = "SELECT id_html_doc, version, content_label, creation_date, update_date, html_content, user, user_creator, attached_portlet_id FROM htmldocs_versions where id_html_doc = ?";
    private static final String SQL_QUERY_INSERT_VERSION = "INSERT INTO htmldocs_versions ( id_version, id_html_doc,  version, content_label, creation_date, update_date, html_content, user, user_creator, attached_portlet_id ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

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
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newVersionPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_VERSION, plugin );
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
    public void insert( HtmlDoc htmlDoc, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        htmlDoc.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, htmlDoc.getId( ) );
        daoUtil.setInt( nIndex++, htmlDoc.getVersion( ) );
        daoUtil.setString( nIndex++, htmlDoc.getContentLabel( ) );
        daoUtil.setDate( nIndex++, htmlDoc.getCreationDate( ) );
        daoUtil.setDate( nIndex++, htmlDoc.getUpdateDate( ) );
        daoUtil.setString( nIndex++, htmlDoc.getHtmlContent( ) );
        daoUtil.setString( nIndex++, htmlDoc.getUser( ) );
        daoUtil.setString( nIndex++, htmlDoc.getUserCreator( ) );
        daoUtil.setInt( nIndex++, htmlDoc.getAttachedPortletId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    public void insertVersion( HtmlDoc htmlDoc, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_VERSION, plugin );
        int nVersion = newVersionPrimaryKey( plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, nVersion );
        daoUtil.setInt( nIndex++, htmlDoc.getId( ) );
        daoUtil.setInt( nIndex++, htmlDoc.getVersion( ) );
        daoUtil.setString( nIndex++, htmlDoc.getContentLabel( ) );
        daoUtil.setDate( nIndex++, htmlDoc.getCreationDate( ) );
        daoUtil.setDate( nIndex++, htmlDoc.getUpdateDate( ) );
        daoUtil.setString( nIndex++, htmlDoc.getHtmlContent( ) );
        daoUtil.setString( nIndex++, htmlDoc.getUser( ) );
        daoUtil.setString( nIndex++, htmlDoc.getUserCreator( ) );
        daoUtil.setInt( nIndex++, htmlDoc.getAttachedPortletId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HtmlDoc load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );
        HtmlDoc htmlDoc = null;

        if ( daoUtil.next( ) )
        {
            htmlDoc = new HtmlDoc( );
            int nIndex = 1;

            htmlDoc.setId( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setVersion( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setContentLabel( daoUtil.getString( nIndex++ ) );
            htmlDoc.setCreationDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setUpdateDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setHtmlContent( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUser( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUserCreator( daoUtil.getString( nIndex++ ) );
            htmlDoc.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free( );
        return htmlDoc;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HtmlDoc loadByName( String strName, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_NAME, plugin );
        daoUtil.setString( 1, strName );
        daoUtil.executeQuery( );
        HtmlDoc htmlDoc = null;

        if ( daoUtil.next( ) )
        {
            htmlDoc = new HtmlDoc( );
            int nIndex = 1;

            htmlDoc.setId( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setVersion( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setContentLabel( daoUtil.getString( nIndex++ ) );
            htmlDoc.setCreationDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setUpdateDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setHtmlContent( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUser( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUserCreator( daoUtil.getString( nIndex++ ) );
            htmlDoc.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free( );
        return htmlDoc;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HtmlDoc loadVersion( int nId, int nVersion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VERSION, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.setInt( 2, nVersion );
        daoUtil.executeQuery( );
        HtmlDoc htmlDoc = null;

        if ( daoUtil.next( ) )
        {
            htmlDoc = new HtmlDoc( );
            int nIndex = 1;

            htmlDoc.setId( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setVersion( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setContentLabel( daoUtil.getString( nIndex++ ) );
            htmlDoc.setCreationDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setUpdateDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setHtmlContent( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUser( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUserCreator( daoUtil.getString( nIndex++ ) );
            htmlDoc.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free( );
        return htmlDoc;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteVersions( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_VERSIONS, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( HtmlDoc htmlDoc, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, htmlDoc.getId( ) );
        daoUtil.setInt( nIndex++, htmlDoc.getVersion( ) );
        daoUtil.setString( nIndex++, htmlDoc.getContentLabel( ) );
        daoUtil.setDate( nIndex++, htmlDoc.getCreationDate( ) );
        daoUtil.setDate( nIndex++, htmlDoc.getUpdateDate( ) );
        daoUtil.setString( nIndex++, htmlDoc.getHtmlContent( ) );
        daoUtil.setString( nIndex++, htmlDoc.getUser( ) );
        daoUtil.setString( nIndex++, htmlDoc.getUserCreator( ) );
        daoUtil.setInt( nIndex++, htmlDoc.getAttachedPortletId( ) );
        daoUtil.setInt( nIndex, htmlDoc.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<HtmlDoc> selectHtmlDocsList( Plugin plugin )
    {
        List<HtmlDoc> htmlDocList = new ArrayList<HtmlDoc>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            HtmlDoc htmlDoc = new HtmlDoc( );
            int nIndex = 1;

            htmlDoc.setId( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setVersion( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setContentLabel( daoUtil.getString( nIndex++ ) );
            htmlDoc.setCreationDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setUpdateDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setHtmlContent( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUser( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUserCreator( daoUtil.getString( nIndex++ ) );
            htmlDoc.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );

            htmlDocList.add( htmlDoc );
        }

        daoUtil.free( );
        return htmlDocList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<HtmlDoc> selectHtmlDocsVersionsList( int nId, Plugin plugin )
    {
        List<HtmlDoc> htmlDocVersionsList = new ArrayList<HtmlDoc>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_VERSION, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            HtmlDoc htmlDoc = new HtmlDoc( );
            int nIndex = 1;

            htmlDoc.setId( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setVersion( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setContentLabel( daoUtil.getString( nIndex++ ) );
            htmlDoc.setCreationDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setUpdateDate( daoUtil.getDate( nIndex++ ) );
            htmlDoc.setHtmlContent( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUser( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUserCreator( daoUtil.getString( nIndex++ ) );
            htmlDoc.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );

            htmlDocVersionsList.add( htmlDoc );
        }

        daoUtil.free( );
        return htmlDocVersionsList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdHtmlDocsList( Plugin plugin )
    {
        List<Integer> htmlDocList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            htmlDocList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return htmlDocList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectHtmlDocsReferenceList( Plugin plugin )
    {
        ReferenceList htmlDocList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            htmlDocList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return htmlDocList;
    }
}
