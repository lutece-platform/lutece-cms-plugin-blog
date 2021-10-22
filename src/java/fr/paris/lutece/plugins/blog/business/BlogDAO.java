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

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Blog objects
 */
public final class BlogDAO implements IBlogDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_blog ) FROM blog_blog";
    private static final String SQL_QUERY_NEW_PK_VERSION = "SELECT max( id_version ) FROM blog_versions";
    private static final String SQL_QUERY_SELECT = "SELECT id_blog,  version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description,  shareable, url FROM blog_blog WHERE id_blog = ?";
    private static final String SQL_QUERY_SELECT_LAST_DOCUMENTS = "SELECT id_blog,  version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description,  shareable, url FROM blog_blog ORDER BY update_date DESC LIMIT ?";
    private static final String SQL_QUERY_SELECT_BY_NAME = "SELECT id_blog,  version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description, shareable, url FROM blog_blog WHERE content_label = ?";
    private static final String SQL_QUERY_SELECT_VERSION = "SELECT id_blog, version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description, shareable, url FROM blog_versions WHERE id_blog = ? AND version = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO blog_blog ( id_blog,  version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description, shareable, url ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM blog_blog WHERE id_blog = ?";
    private static final String SQL_QUERY_DELETE_VERSIONS = "DELETE FROM blog_versions WHERE id_blog = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE blog_blog SET id_blog = ?, version = ?, content_label = ?, creation_date = ?, update_date = ?, html_content = ?, user_editor = ?, user_creator = ?, attached_portlet_id = ?, edit_comment = ?, description = ?, shareable = ?, url= ? WHERE id_blog = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_blog, version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description, shareable, url FROM blog_blog order by creation_date DESC";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_blog FROM blog_blog ORDER BY creation_date DESC";
    private static final String SQL_QUERY_SELECTALL_VERSION = "SELECT id_blog, version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description, shareable, url FROM blog_versions where id_blog = ?";
    private static final String SQL_QUERY_SELECTALL_USERS_EDITED_BLOG_VERSION = "SELECT distinct user_editor FROM blog_versions where id_blog = ?";

    private static final String SQL_QUERY_INSERT_VERSION = "INSERT INTO blog_versions ( id_version, id_blog,  version, content_label, creation_date, update_date, html_content, user_editor, user_creator, attached_portlet_id, edit_comment, description, shareable, url ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    private static final String SQL_QUERY_SELECT_BY_FILTER = " SELECT DISTINCT a.id_blog, a.version, a.content_label, "
            + " a.creation_date, a.update_date, a.html_content, a.user_editor, a.user_creator , a.attached_portlet_id , "
            + " a.edit_comment , a.description, a.shareable, a.url, p.document_order FROM blog_blog a "
            + " LEFT OUTER JOIN blog_tag_document f ON a.id_blog = f.id_blog" + " LEFT OUTER JOIN blog_list_portlet_htmldocs p ON  a.id_blog = p.id_blog";

    private static final String SQL_QUERY_SELECT_BLOG_BY_ID_TAG = " SELECT b.id_blog, b.version, b.content_label, b.creation_date, b.update_date, b.html_content, b.user_editor, b.user_creator, b.attached_portlet_id, b.edit_comment, b.description, b.shareable, b.url, a.id_tag FROM blog_tag_document a Inner join blog_blog b on (b.id_blog = a.id_blog) WHERE a.id_tag = ? ORDER BY priority";

    private static final String SQL_QUERY_SELECT_ALL_BLOG = " SELECT DISTINCT a.id_blog, a.version, a.content_label, a.creation_date, a.update_date, a.html_content, a.user_editor, a.user_creator , a.attached_portlet_id, a.edit_comment , a.description, a.shareable, a.url FROM blog_blog a";

    private static final String SQL_FILTER_WHERE_CLAUSE = " WHERE ";
    private static final String SQL_FILTER_AND = " AND ";
    private static final String SQL_FILTER_TAGS_BEGIN = " (";
    private static final String SQL_FILTER_TAGS = " f.id_tag = ? ";
    private static final String SQL_FILTER_TAGS_NULL = " f.id_tag IS NULL ";
    private static final String SQL_FILTER_TAGS_OR = " OR ";
    private static final String SQL_FILTER_TAGS_END = ") ";
    private static final String SQL_FILTER_ID_BEGIN = " (";
    private static final String SQL_FILTER_ID = " a.id_blog = ? ";
    private static final String SQL_FILTER_ID_OR = " OR ";
    private static final String SQL_FILTER_ID_END = ") ";
    private static final String SQL_ORDER_BY_LAST_MODIFICATION = " ORDER BY a.update_date DESC ";
    private static final String SQL_ORDER_BY_ORDER_DOCUMENT = " ORDER by p.document_order ";

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
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newVersionPrimaryKey( Plugin plugin )
    {
        int nKey = 1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_VERSION, plugin ) )
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
    public void insert( Blog blog, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            blog.setId( newPrimaryKey( plugin ) );
            int nIndex = 1;

            daoUtil.setInt( nIndex++, blog.getId( ) );
            daoUtil.setInt( nIndex++, blog.getVersion( ) );
            daoUtil.setString( nIndex++, blog.getContentLabel( ) );
            daoUtil.setTimestamp( nIndex++, blog.getCreationDate( ) );
            daoUtil.setTimestamp( nIndex++, blog.getUpdateDate( ) );
            daoUtil.setString( nIndex++, blog.getHtmlContent( ) );
            daoUtil.setString( nIndex++, blog.getUser( ) );
            daoUtil.setString( nIndex++, blog.getUserCreator( ) );
            daoUtil.setInt( nIndex++, blog.getAttachedPortletId( ) );
            daoUtil.setString( nIndex++, blog.getEditComment( ) );
            daoUtil.setString( nIndex++, blog.getDescription( ) );
            daoUtil.setBoolean( nIndex++, blog.getShareable( ) );
            daoUtil.setString( nIndex++, blog.getUrl( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insertVersion( Blog blog, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_VERSION, plugin ) )
        {
            int nVersion = newVersionPrimaryKey( plugin );
            int nIndex = 1;

            daoUtil.setInt( nIndex++, nVersion );
            daoUtil.setInt( nIndex++, blog.getId( ) );
            daoUtil.setInt( nIndex++, blog.getVersion( ) );
            daoUtil.setString( nIndex++, blog.getContentLabel( ) );
            daoUtil.setTimestamp( nIndex++, blog.getCreationDate( ) );
            daoUtil.setTimestamp( nIndex++, blog.getUpdateDate( ) );
            daoUtil.setString( nIndex++, blog.getHtmlContent( ) );
            daoUtil.setString( nIndex++, blog.getUser( ) );
            daoUtil.setString( nIndex++, blog.getUserCreator( ) );
            daoUtil.setInt( nIndex++, blog.getAttachedPortletId( ) );
            daoUtil.setString( nIndex++, blog.getEditComment( ) );
            daoUtil.setString( nIndex++, blog.getDescription( ) );
            daoUtil.setBoolean( nIndex++, blog.getShareable( ) );
            daoUtil.setString( nIndex++, blog.getUrl( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Blog load( int nKey, Plugin plugin )
    {
        Blog blog = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );

            }

        }
        return blog;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Blog loadByName( String strName, Plugin plugin )
    {
        Blog blog = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_NAME, plugin ) )
        {
            daoUtil.setString( 1, strName );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );

            }
        }
        return blog;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Blog loadVersion( int nId, int nVersion, Plugin plugin )
    {
        Blog blog = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VERSION, plugin ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.setInt( 2, nVersion );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );
            }

        }
        return blog;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteVersions( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_VERSIONS, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Blog blog, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++, blog.getId( ) );
            daoUtil.setInt( nIndex++, blog.getVersion( ) );
            daoUtil.setString( nIndex++, blog.getContentLabel( ) );
            daoUtil.setTimestamp( nIndex++, blog.getCreationDate( ) );
            daoUtil.setTimestamp( nIndex++, blog.getUpdateDate( ) );
            daoUtil.setString( nIndex++, blog.getHtmlContent( ) );
            daoUtil.setString( nIndex++, blog.getUser( ) );
            daoUtil.setString( nIndex++, blog.getUserCreator( ) );
            daoUtil.setInt( nIndex++, blog.getAttachedPortletId( ) );
            daoUtil.setString( nIndex++, blog.getEditComment( ) );
            daoUtil.setString( nIndex++, blog.getDescription( ) );
            daoUtil.setBoolean( nIndex++, blog.getShareable( ) );
            daoUtil.setString( nIndex++, blog.getUrl( ) );

            daoUtil.setInt( nIndex, blog.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Blog> selectBlogsList( Plugin plugin )
    {
        List<Blog> blogList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Blog blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );

                blogList.add( blog );
            }

        }
        return blogList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Blog> selectlastModifiedBlogsList( Plugin plugin, int nLimit )
    {
        List<Blog> blogList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_DOCUMENTS, plugin ) )
        {
            daoUtil.setInt( 1, nLimit );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Blog blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );

                blogList.add( blog );
            }

        }
        return blogList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Blog> selectBlogsVersionsList( int nId, Plugin plugin )
    {
        List<Blog> blogVersionsList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_VERSION, plugin ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Blog blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );

                blogVersionsList.add( blog );
            }
        }
        return blogVersionsList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> selectAllUsersEditedBlog( int nId, Plugin plugin )
    {
        List<String> blogUsersVersionsList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_USERS_EDITED_BLOG_VERSION, plugin ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                blogUsersVersionsList.add( daoUtil.getString( 1 ) );
            }
        }
        return blogUsersVersionsList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdBlogsList( Plugin plugin )
    {
        List<Integer> blogList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                blogList.add( daoUtil.getInt( 1 ) );
            }
        }
        return blogList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectBlogsReferenceList( Plugin plugin )
    {
        ReferenceList blogList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
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
     * Load the list of blogs
     *
     * @return The Collection of the blogs
     * @param filter
     *            The blogFilter Object
     */
    @Override
    public List<Blog> selectByFilter( BlogFilter filter )
    {
        List<Blog> listDocuments = new ArrayList<>( );
        try ( DAOUtil daoUtil = getDaoFromFilter( SQL_QUERY_SELECT_BY_FILTER, filter ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Blog blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex ) );

                if ( filter.getLoadBinaries( ) )
                {
                    blog.setDocContent( DocContentHome.getDocsContentByHtmlDoc( blog.getId( ) ) );
                }

                blog.setTag( TagHome.getTagListByDoc( blog.getId( ) ) );

                listDocuments.add( blog );
            }

            daoUtil.free( );
        }
        return listDocuments;
    }

    /**
     * Return a dao initialized with the specified filter
     * 
     * @param strQuerySelect
     *            the query
     * @param filter
     *            the DocumentFilter object
     * @return the DaoUtil
     */
    private DAOUtil getDaoFromFilter( String strQuerySelect, BlogFilter filter )
    {
        String strSQL = strQuerySelect;
        StringBuilder sbWhere = new StringBuilder( StringUtils.EMPTY );

        if ( filter.containsTagsCriteria( ) )
        {
            StringBuilder sbCategories = new StringBuilder( SQL_FILTER_TAGS_BEGIN );

            int i = 0;

            for ( int nTagId : filter.getTagsId( ) )
            {
                if ( nTagId > 0 )
                {
                    sbCategories.append( SQL_FILTER_TAGS );
                }
                else
                {
                    sbCategories.append( SQL_FILTER_TAGS_NULL );
                }

                if ( ( i + 1 ) < filter.getTagsId( ).length )
                {
                    sbCategories.append( SQL_FILTER_TAGS_OR );
                }

                i++;
            }

            sbCategories.append( SQL_FILTER_TAGS_END );
            sbWhere.append( ( sbWhere.length( ) != 0 ) ? SQL_FILTER_AND : StringUtils.EMPTY ).append( sbCategories.toString( ) );
        }

        if ( filter.containsIdsCriteria( ) )
        {
            StringBuilder sbIds = new StringBuilder( SQL_FILTER_ID_BEGIN );

            for ( int i = 0; i < filter.getIds( ).length; i++ )
            {
                sbIds.append( SQL_FILTER_ID );

                if ( ( i + 1 ) < filter.getIds( ).length )
                {
                    sbIds.append( SQL_FILTER_ID_OR );
                }
            }

            sbIds.append( SQL_FILTER_ID_END );
            sbWhere.append( ( sbWhere.length( ) != 0 ) ? SQL_FILTER_AND : StringUtils.EMPTY ).append( sbIds.toString( ) );
        }

        if ( BooleanUtils.isFalse( filter.isPublished( ) ) )
        {
            sbWhere.append( ( sbWhere.length( ) != 0 ) ? SQL_FILTER_AND : StringUtils.EMPTY )
                    .append( "a.id_blog NOT IN (SELECT DISTINCT id_blog FROM blogs_tag_document) " );
        }
        if ( filter.getPortletId( ) != 0 )
        {
            sbWhere.append( ( sbWhere.length( ) != 0 ) ? SQL_FILTER_AND : StringUtils.EMPTY ).append( "p.id_portlet=" )
                    .append( String.valueOf( filter.getPortletId( ) ) );
        }

        if ( StringUtils.isNotBlank( filter.getDateMin( ) ) && StringUtils.isNotBlank( filter.getDateMax( ) ) )
        {
            sbWhere.append( ( ( sbWhere.length( ) != 0 ) ? SQL_FILTER_AND : StringUtils.EMPTY ) ).append( "a.update_date < " ).append( '\'' )
                    .append( filter.getDateMax( ) ).append( '\'' ).append( SQL_FILTER_AND ).append( "a.update_date > " ).append( '\'' )
                    .append( filter.getDateMin( ) ).append( '\'' );
        }
        else
            if ( StringUtils.isNotBlank( filter.getDateMin( ) ) )
            {
                sbWhere.append( ( ( sbWhere.length( ) != 0 ) ? SQL_FILTER_AND : StringUtils.EMPTY ) ).append( "a.update_date > " ).append( '\'' )
                        .append( filter.getDateMin( ) ).append( '\'' );
            }
            else
                if ( StringUtils.isNotBlank( filter.getDateMax( ) ) )
                {
                    sbWhere.append( ( ( sbWhere.length( ) != 0 ) ? SQL_FILTER_AND : StringUtils.EMPTY ) ).append( "a.update_date <= " ).append( '\'' )
                            .append( filter.getDateMax( ) ).append( '\'' );
                }

        String strWhere = sbWhere.toString( );

        if ( sbWhere.length( ) != 0 )
        {
            strSQL += ( SQL_FILTER_WHERE_CLAUSE + strWhere );
        }

        if ( filter.getOrderInPortlet( ) )
        {

            strSQL += SQL_ORDER_BY_ORDER_DOCUMENT;
        }
        else
        {
            strSQL += SQL_ORDER_BY_LAST_MODIFICATION;
        }

        AppLogService.debug( "Sql query filter : " + strSQL );

        DAOUtil daoUtil = new DAOUtil( strSQL );
        int nIndex = 1;

        if ( filter.containsTagsCriteria( ) )
        {
            for ( int nCategoryId : filter.getTagsId( ) )
            {
                if ( nCategoryId > 0 )
                {
                    daoUtil.setInt( nIndex, nCategoryId );
                    AppLogService.debug( "Param" + nIndex + " (getTagsId) = " + nCategoryId );
                    nIndex++;
                }
            }
        }

        if ( filter.containsIdsCriteria( ) )
        {
            for ( int nId : filter.getIds( ) )
            {
                daoUtil.setInt( nIndex, nId );
                AppLogService.debug( "Param" + nIndex + " (getIds) = " + nId );
                nIndex++;
            }
        }

        return daoUtil;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Blog> loadBlogByIdTag( int nIdTag, Plugin plugin )
    {
        List<Blog> listBlog = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BLOG_BY_ID_TAG, plugin ) )
        {
            daoUtil.setInt( 1, nIdTag );
            daoUtil.executeQuery( );
            Blog blog;

            while ( daoUtil.next( ) )
            {
                blog = new Blog( );
                int nIndex = 1;

                blog.setId( daoUtil.getInt( nIndex++ ) );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );
                listBlog.add( blog );

            }

        }
        return listBlog;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Blog> selectWithoutBinaries( Plugin plugin )
    {
        List<Blog> listDocuments = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_BLOG, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Blog blog = new Blog( );
                int nIndex = 1;
                int idBlog = daoUtil.getInt( nIndex++ );
                blog.setId( idBlog );
                blog.setVersion( daoUtil.getInt( nIndex++ ) );
                blog.setContentLabel( daoUtil.getString( nIndex++ ) );
                blog.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
                blog.setHtmlContent( daoUtil.getString( nIndex++ ) );
                blog.setUser( daoUtil.getString( nIndex++ ) );
                blog.setUserCreator( daoUtil.getString( nIndex++ ) );
                blog.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
                blog.setEditComment( daoUtil.getString( nIndex++ ) );
                blog.setDescription( daoUtil.getString( nIndex++ ) );
                blog.setShareable( daoUtil.getBoolean( nIndex++ ) );
                blog.setUrl( daoUtil.getString( nIndex++ ) );

                listDocuments.add( blog );
            }
        }

        return listDocuments;
    }

}
