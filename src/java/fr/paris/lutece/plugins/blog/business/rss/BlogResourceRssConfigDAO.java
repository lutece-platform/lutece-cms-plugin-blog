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
package fr.paris.lutece.plugins.blog.business.rss;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * class BlogResourceRssConfigDAO
 *
 */
public class BlogResourceRssConfigDAO implements IBlogResourceRssConfigDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_rss,id_portlet " + "FROM blog_rss_cf  WHERE id_rss=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO blog_rss_cf( " + "id_rss, id_portlet )" + "VALUES (?, ?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE blog_rss_cf " + "SET id_rss=?, id_portlet=?" + " WHERE id_rss=?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM blog_rss_cf WHERE id_rss=? ";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_rss, id_portlet " + "FROM blog_rss_cf";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( BlogResourceRssConfig config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nPos = 0;

            daoUtil.setInt( ++nPos, config.getIdRss( ) );
            daoUtil.setInt( ++nPos, config.getIdPortlet( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( BlogResourceRssConfig config, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nPos = 0;

            daoUtil.setInt( ++nPos, config.getIdRss( ) );
            daoUtil.setInt( ++nPos, config.getIdPortlet( ) );

            daoUtil.setInt( ++nPos, config.getIdRss( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlogResourceRssConfig load( int nIdRss, Plugin plugin )
    {
        BlogResourceRssConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin ) )
        {
            daoUtil.setInt( 1, nIdRss );

            daoUtil.executeQuery( );

            int nPos = 0;

            if ( daoUtil.next( ) )
            {
                config = new BlogResourceRssConfig( );
                config.setIdRss( daoUtil.getInt( ++nPos ) );
                config.setIdPortlet( daoUtil.getInt( ++nPos ) );
            }
        }
        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdRss, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nIdRss );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BlogResourceRssConfig> loadAll( Plugin plugin )
    {
        List<BlogResourceRssConfig> configList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin ) )
        {
            daoUtil.executeQuery( );

            int nPos = 0;
            if ( daoUtil.next( ) )
            {
                BlogResourceRssConfig config = new BlogResourceRssConfig( );
                config.setIdRss( daoUtil.getInt( ++nPos ) );
                config.setIdPortlet( daoUtil.getInt( ++nPos ) );

                configList.add( config );
            }
        }
        return configList;
    }
}
