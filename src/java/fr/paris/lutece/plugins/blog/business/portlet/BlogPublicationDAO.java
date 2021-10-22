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
package fr.paris.lutece.plugins.blog.business.portlet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class BlogPublicationDAO implements IBlogPublicationDAO
{

    // Category
    private static final String SQL_QUERY_INSERT_BLOGS_PORTLET = "INSERT INTO blog_list_portlet_htmldocs ( id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order ) VALUES ( ? , ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE_BLOGS_PORTLET = " DELETE FROM blog_list_portlet_htmldocs WHERE id_blog = ? ";
    private static final String SQL_QUERY_DELETE_BLOGS_PORTLET_BY_ID_PORTLET = " DELETE FROM blog_list_portlet_htmldocs WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_CATEGORY_PORTLET = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs WHERE id_blog = ? order by document_order ";
    private static final String SQL_QUERY_REMOVE_BLOGS_PORTLET = " DELETE FROM blog_list_portlet_htmldocs WHERE id_portlet = ? and id_blog= ?";

    private static final String SQL_QUERY_UPDATE_BLOGS_PORTLET = "UPDATE blog_list_portlet_htmldocs set id_portlet= ?, id_blog= ?, date_begin_publishing= ?, date_end_publishing= ?, status= ?, document_order= ? WHERE  id_blog= ?";
    private static final String SQL_QUERY_SELECT_PUBLICATION_PORTLET = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs WHERE id_blog = ? and id_portlet = ? order by document_order";
    private static final String SQL_QUERY_SELECT_PUBLICATION_ALL = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs order by document_order";
    private static final String SQL_QUERY_SELECT_DOC_PUBLICATION_BY_PORTLET = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs WHERE id_portlet = ? order by document_order ";
    private static final String SQL_QUERY_SELECT_BY_DATE_PUBLISHING_AND_STATUS = "SELECT id_portlet, id_blog, document_order, date_begin_publishing FROM blog_list_portlet_htmldocs WHERE date_begin_publishing >= ? AND date_end_publishing >= ? AND status = ? ORDER BY document_order ";
    private static final String SQL_QUERY_SELECT_DOC_PUBLICATION_BY_PORTLET_AND_PUBLICATION_DATE = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs WHERE id_portlet = ? AND date_begin_publishing <= ? AND date_end_publishing >= ? order by document_order ";
    private static final String SQL_QUERY_SELECT_BY_PORTLET_ID_AND_STATUS = " SELECT DISTINCT pub.id_blog FROM blog_list_portlet_htmldocs pub WHERE  pub.status = ? AND pub.date_begin_publishing <= ? AND  pub.date_end_publishing >= ? AND pub.id_portlet IN ";
    private static final String SQL_QUERY_SELECT_LAST_BY_PORTLET_ID_AND_STATUS = "SELECT DISTINCT pub.id_blog FROM blog_list_portlet_htmldocs pub, blog_blog doc WHERE doc.id_blog=pub.id_blog AND pub.status=? AND doc.update_date >=? AND pub.date_begin_publishing <= ? AND pub.date_end_publishing >= ? AND pub.id_portlet IN ";

    private static final String SQL_QUERY_COUNT_DOC_PUBLICATION_BY_BLOG_AND_PUBLICATION_DATE = "SELECT count(id_blog) FROM blog_list_portlet_htmldocs WHERE id_blog = ? AND date_begin_publishing <= ? AND date_end_publishing >= ?";
    private static final String SQL_FILTER_BEGIN = " (";
    private static final String SQL_TAGS_END = ") ";
    private static final String CONSTANT_QUESTION_MARK = "?";
    private static final String CONSTANT_COMMA = ",";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * {@inheritDoc }
     */
    @Override
    public void insertBlogsId( BlogPublication blogPublication, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_BLOGS_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, blogPublication.getIdPortlet( ) );
            daoUtil.setInt( 2, blogPublication.getIdBlog( ) );
            daoUtil.setDate( 3, blogPublication.getDateBeginPublishing( ) );
            daoUtil.setDate( 4, blogPublication.getDateEndPublishing( ) );
            daoUtil.setInt( 5, blogPublication.getStatus( ) );
            daoUtil.setInt( 6, blogPublication.getBlogOrder( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( BlogPublication blogPublication, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_BLOGS_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, blogPublication.getIdPortlet( ) );
            daoUtil.setInt( 2, blogPublication.getIdBlog( ) );
            daoUtil.setDate( 3, blogPublication.getDateBeginPublishing( ) );
            daoUtil.setDate( 4, blogPublication.getDateEndPublishing( ) );
            daoUtil.setInt( 5, blogPublication.getStatus( ) );
            daoUtil.setInt( 6, blogPublication.getBlogOrder( ) );

            daoUtil.setInt( 7, blogPublication.getIdBlog( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteBlogsId( int nDocId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BLOGS_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nDocId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteBlogByIdPortlet( int nIdPortlet, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BLOGS_PORTLET_BY_ID_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nIdPortlet );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void remove( int nDocId, int nIdPortlet, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BLOGS_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nIdPortlet );
            daoUtil.setInt( 2, nDocId );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BlogPublication> loadBlogsId( int nDocId, Plugin plugin )
    {
        List<BlogPublication> nListIdCategory = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CATEGORY_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nDocId );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                BlogPublication blogPub = new BlogPublication( );
                blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
                blogPub.setIdBlog( daoUtil.getInt( 2 ) );
                blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
                blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
                blogPub.setStatus( daoUtil.getInt( 5 ) );
                blogPub.setBlogOrder( daoUtil.getInt( 6 ) );

                nListIdCategory.add( blogPub );
            }
        }
        return nListIdCategory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BlogPublication> loadBlogsByPortlet( int nIdPortlet, Plugin plugin )
    {
        List<BlogPublication> nListIdCategory = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOC_PUBLICATION_BY_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nIdPortlet );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                BlogPublication blogPub = new BlogPublication( );
                blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
                blogPub.setIdBlog( daoUtil.getInt( 2 ) );
                blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
                blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
                blogPub.setStatus( daoUtil.getInt( 5 ) );
                blogPub.setBlogOrder( daoUtil.getInt( 6 ) );

                nListIdCategory.add( blogPub );
            }
        }
        return nListIdCategory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BlogPublication> loadBlogsByPortletAndPublicationDate( int nIdPortlet, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        List<BlogPublication> nListIdCategory = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOC_PUBLICATION_BY_PORTLET_AND_PUBLICATION_DATE, plugin ) )
        {
            daoUtil.setInt( 1, nIdPortlet );
            daoUtil.setTimestamp( 2, new Timestamp( datePublishing.getTime( ) ) );
            daoUtil.setTimestamp( 3, new Timestamp( dateEndPublishing.getTime( ) ) );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                BlogPublication blogPub = new BlogPublication( );
                blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
                blogPub.setIdBlog( daoUtil.getInt( 2 ) );
                blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
                blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
                blogPub.setStatus( daoUtil.getInt( 5 ) );
                blogPub.setBlogOrder( daoUtil.getInt( 6 ) );

                nListIdCategory.add( blogPub );
            }
        }
        return nListIdCategory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public BlogPublication loadBlogsPublication( int nPortletId, int nDocId, Plugin plugin )
    {
        BlogPublication blogPub = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLICATION_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nDocId );
            daoUtil.setInt( 2, nPortletId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                blogPub = new BlogPublication( );
                blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
                blogPub.setIdBlog( daoUtil.getInt( 2 ) );
                blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
                blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
                blogPub.setStatus( daoUtil.getInt( 5 ) );
                blogPub.setBlogOrder( daoUtil.getInt( 6 ) );
            }
        }
        return blogPub;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BlogPublication> loadAllBlogsPublication( Plugin plugin )
    {
        List<BlogPublication> nListIdCategory = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLICATION_ALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                BlogPublication blogPub = new BlogPublication( );
                blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
                blogPub.setIdBlog( daoUtil.getInt( 2 ) );
                blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
                blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
                blogPub.setStatus( daoUtil.getInt( 5 ) );
                blogPub.setBlogOrder( daoUtil.getInt( 6 ) );

                nListIdCategory.add( blogPub );
            }
        }
        return nListIdCategory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<BlogPublication> selectSinceDatePublishingAndStatus( Date datePublishing, Date dateEndPublication, int nStatus, Plugin plugin )
    {
        Collection<BlogPublication> listBlogPublication = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE_PUBLISHING_AND_STATUS, plugin ) )
        {
            daoUtil.setTimestamp( 1, new Timestamp( datePublishing.getTime( ) ) );
            daoUtil.setTimestamp( 2, new Timestamp( dateEndPublication.getTime( ) ) );

            daoUtil.setInt( 3, nStatus );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                BlogPublication blogPublication = new BlogPublication( );
                blogPublication.setIdPortlet( daoUtil.getInt( 1 ) );
                blogPublication.setIdBlog( daoUtil.getInt( 2 ) );
                blogPublication.setBlogOrder( daoUtil.getInt( 3 ) );
                blogPublication.setStatus( nStatus );
                blogPublication.setDateBeginPublishing( daoUtil.getDate( 4 ) );
                listBlogPublication.add( blogPublication );
            }
        }
        return listBlogPublication;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getPublishedBlogsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        List<Integer> listIds = new ArrayList<>( );
        if ( nPortletsIds == null || nPortletsIds.length == 0 )
        {
            return listIds;
        }
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_BY_PORTLET_ID_AND_STATUS );
        sbSql.append( SQL_FILTER_BEGIN );

        for ( int i = 0; i < nPortletsIds.length; i++ )
        {
            sbSql.append( CONSTANT_QUESTION_MARK );
            if ( i + 1 < nPortletsIds.length )
            {
                sbSql.append( CONSTANT_COMMA );
            }
        }
        sbSql.append( SQL_TAGS_END );

        try ( DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, 1 );
            daoUtil.setTimestamp( nIndex++, new Timestamp( datePublishing.getTime( ) ) );
            daoUtil.setTimestamp( nIndex++, new Timestamp( dateEndPublishing.getTime( ) ) );

            for ( int nPortletId : nPortletsIds )
            {
                daoUtil.setInt( nIndex++, nPortletId );
            }
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                listIds.add( daoUtil.getInt( 1 ) );
            }
        }
        return listIds;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getLastPublishedBlogsIdsListByPortletIds( int [ ] nPortletsIds, Date dateUpdated, Plugin plugin )
    {
        List<Integer> listIds = new ArrayList<>( );
        if ( nPortletsIds == null || nPortletsIds.length == 0 )
        {
            return listIds;
        }
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_LAST_BY_PORTLET_ID_AND_STATUS );
        sbSql.append( SQL_FILTER_BEGIN );

        for ( int i = 0; i < nPortletsIds.length; i++ )
        {
            sbSql.append( CONSTANT_QUESTION_MARK );
            if ( i + 1 < nPortletsIds.length )
            {
                sbSql.append( CONSTANT_COMMA );
            }
        }
        sbSql.append( SQL_TAGS_END );

        try ( DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, 1 );
            daoUtil.setTimestamp( nIndex++, new Timestamp( dateUpdated.getTime( ) ) );
            daoUtil.setTimestamp( nIndex++, new Timestamp( System.currentTimeMillis( ) ) );
            daoUtil.setTimestamp( nIndex++, new Timestamp( System.currentTimeMillis( ) ) );

            for ( int nPortletId : nPortletsIds )
            {
                daoUtil.setInt( nIndex++, nPortletId );
            }
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                listIds.add( daoUtil.getInt( 1 ) );
            }
        }
        return listIds;

    }

    @Override
    public int countPublicationByIdBlogAndDate( int nIdBlog, Date date, Plugin plugin )
    {
        int count = 0;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_DOC_PUBLICATION_BY_BLOG_AND_PUBLICATION_DATE, plugin ) )
        {
            Timestamp timestamp = new Timestamp( date.getTime( ) );
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdBlog );
            daoUtil.setTimestamp( ++nIndex, timestamp );
            daoUtil.setTimestamp( ++nIndex, timestamp );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                count = daoUtil.getInt( 1 );
            }
        }
        return count;
    }
}
