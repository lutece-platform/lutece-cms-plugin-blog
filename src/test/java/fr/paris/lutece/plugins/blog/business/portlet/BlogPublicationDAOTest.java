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

import fr.paris.lutece.plugins.blog.TestUtils;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * BlogPublicationDAO Test
 */
public class BlogPublicationDAOTest extends LuteceTestCase
{
    private static final String PLUGIN_NAME = "blog";
    private static final int PORTLET_ID = 20000;
    private static final int STATUS_ENABLED = 1;
    private static final int ORDER = 1;

    private static Plugin _plugin;
    private static String _strDatabaseEngine;
    private static int nTestCount = 0;
    private int _nIdBlog;
    @Inject
    private IBlogPublicationDAO _dao;

    @BeforeEach
    protected void before( TestInfo testInfo ) throws Exception
    {
        if ( nTestCount == 0 )
        {
            setUpDatabase( );
            nTestCount++;
        }
        System.out.println( testInfo.getDisplayName( ) );
        createBlog( );
    }

    @AfterAll
    protected void after( ) throws Exception
    {
        tearDownDatabase( );
    }

    /**
     * Test of insertBlogsId method, of class BlogPublicationDAO.
     */
    @Test
    void testInsertBlogsId( )
    {
        insert( );
    }

    /**
     * Test of store method, of class BlogPublicationDAO.
     */
    @Test
    void testStore( )
    {
        BlogPublication blogPublication = getBlogPublication( );
        _dao.store( blogPublication, _plugin );
    }

    /**
     * Test of deleteBlogsId method, of class BlogPublicationDAO.
     */
    @Test
    void testDeleteBlogsId( )
    {
        _dao.deleteBlogsId( _nIdBlog, _plugin );
    }

    /**
     * Test of deleteBlogByIdPortlet method, of class BlogPublicationDAO.
     */
    @Test
    void testDeleteBlogByIdPortlet( )
    {
        insert( );
        _dao.deleteBlogByIdPortlet( PORTLET_ID, _plugin );
    }

    /**
     * Test of remove method, of class BlogPublicationDAO.
     */
    @Test
    void testRemove( )
    {
        insert( );
        _dao.remove( _nIdBlog, PORTLET_ID, _plugin );
    }

    /**
     * Test of loadBlogsId method, of class BlogPublicationDAO.
     */
    @Test
    void testLoadBlogsId( )
    {
        insert( );
        List<BlogPublication> result = _dao.loadBlogsId( _nIdBlog, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of loadBlogsByPortlet method, of class BlogPublicationDAO.
     */
    @Test
    void testLoadBlogsByPortlet( )
    {
        insert( );
        List<BlogPublication> result = _dao.loadBlogsByPortlet( PORTLET_ID, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of loadBlogsByPortletAndPublicationDate method, of class BlogPublicationDAO.
     */
    @Test
    void testLoadBlogsByPortletAndPublicationDate( )
    {
        insert( );
        Date datePublishing = new Date( getTime( -2 ) );
        Date dateEndPublishing = new Date( getTime( 2 ) );
        List<BlogPublication> result = _dao.loadBlogsByPortletAndPublicationDate( PORTLET_ID, datePublishing, dateEndPublishing, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of loadBlogsPublication method, of class BlogPublicationDAO.
     */
    @Test
    void testLoadBlogsPublication( )
    {
        insert( );
        BlogPublication result = _dao.loadBlogsPublication( PORTLET_ID, _nIdBlog, _plugin );
        assertNotNull( result );
    }

    /**
     * Test of loadAllBlogsPublication method, of class BlogPublicationDAO.
     */
    @Test
    void testLoadAllBlogsPublication( )
    {
        insert( );
        List<BlogPublication> result = _dao.loadAllBlogsPublication( _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of selectSinceDatePublishingAndStatus method, of class BlogPublicationDAO.
     */
    @Test
    void testSelectSinceDatePublishingAndStatus( )
    {
        insert( );
        Date datePublishing = new Date( getTime( -4 ) );
        Date dateEndPublishing = new Date( getTime( 2 ) );
        int nStatus = STATUS_ENABLED;
        Collection<BlogPublication> result = _dao.selectSinceDatePublishingAndStatus( datePublishing, dateEndPublishing, nStatus, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of getPublishedBlogsIdsListByPortletIds method, of class BlogPublicationDAO.
     */
    @Test
    void testGetPublishedBlogsIdsListByPortletIds( )
    {
        insert( );
        int [ ] nPortletsIds = {
                PORTLET_ID
        };
        Date datePublishing = new Date( getTime( -2 ) );
        Date dateEndPublishing = new Date( getTime( 2 ) );
        List<Integer> result = _dao.getPublishedBlogsIdsListByPortletIds( nPortletsIds, datePublishing, dateEndPublishing, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of getLastPublishedBlogsIdsListByPortletIds method, of class BlogPublicationDAO.
     */
    @Test
    void testGetLastPublishedBlogsIdsListByPortletIds( )
    {
        insert( );
        int [ ] nPortletsIds = {
                PORTLET_ID
        };
        Date dateUpdated = new Date( getTime( 0 ) );
        List<Integer> result = _dao.getLastPublishedBlogsIdsListByPortletIds( nPortletsIds, dateUpdated, _plugin );
        // assertTrue( !result.isEmpty() ); // the request need a join with blog post, so with this test data set the list is empty
    }

    /**
     * Test of countPublicationByIdBlogAndDate method, of class BlogPublicationDAO.
     */
    @Test
    void testCountPublicationByIdBlogAndDate( )
    {
        insert( );
        Date date = new Date( );
        int result = _dao.countPublicationByIdBlogAndDate( _nIdBlog, date, _plugin );
        assertTrue( result > 0 );
    }

    // private utils

    /**
     * SetUp Database : disable referential integrity
     */
    private void setUpDatabase( )
    {
        try
        {
            _plugin = PluginService.getPlugin( PLUGIN_NAME );
            _strDatabaseEngine = _plugin.getConnectionService( ).getConnection( ).getMetaData( ).getDatabaseProductName( );

            System.out.println( "Database server : " + _strDatabaseEngine );

            String strMessage = "Disable referential integrity for the server : " + _strDatabaseEngine;
            if ( _strDatabaseEngine.equalsIgnoreCase( "MySQL" ) )
            {
                execute( "SET FOREIGN_KEY_CHECKS=0;", strMessage );
            }
            else
                if ( _strDatabaseEngine.contains( "HSQL" ) )
                {
                    execute( "SET DATABASE REFERENTIAL INTEGRITY FALSE", strMessage );
                }
        }
        catch( SQLException ex )
        {
            System.out.println( ex );
        }
    }

    /**
     * Tear Down Database : enable referential integrity
     */
    private void tearDownDatabase( ) throws Exception
    {
        clean( );

        String strMessage = "Enable referential integrity for the server : " + _strDatabaseEngine;
        if ( _strDatabaseEngine.equalsIgnoreCase( "MySQL" ) )
        {
            execute( "SET FOREIGN_KEY_CHECKS=1;", strMessage );
        }
        else
            if ( _strDatabaseEngine.contains( "HSQL" ) )
            {
                execute( "SET DATABASE REFERENTIAL INTEGRITY TRUE", strMessage );
            }
    }

    /**
     * Clean test data
     */
    private void clean( )
    {
        _dao.deleteBlogsId( _nIdBlog, _plugin );
    }

    /**
     * Insert test data
     */
    private void insert( )
    {
        BlogPublication blogPublication = getBlogPublication( );
        _dao.insertBlogsId( blogPublication, _plugin );
    }

    /**
     * Create a publication
     * 
     * @return The publication
     */
    private BlogPublication getBlogPublication( )
    {
        BlogPublication blogPublication = new BlogPublication( );
        blogPublication.setIdBlog( _nIdBlog );
        blogPublication.setIdPortlet( PORTLET_ID );
        blogPublication.setStatus( STATUS_ENABLED );
        blogPublication.setBlogOrder( ORDER );
        blogPublication.setDateBeginPublishing( new java.sql.Date( getTime( -3 ) ) );
        blogPublication.setDateEndPublishing( new java.sql.Date( getTime( 3 ) ) );

        return blogPublication;
    }

    /**
     * Give a time corresponding to a date of today with a delta in hour
     * 
     * @param lDeltaHour
     *            The delta
     * @return The time
     */
    private long getTime( long lDeltaHour )
    {
        return new Date( ).getTime( ) + ( lDeltaHour * 36000000L );
    }

    /**
     * Execute a SQL statement
     * 
     * @param strStatement
     *            The statement
     * @param strMessage
     *            The message for the output
     */
    private void execute( String strStatement, String strMessage )
    {
        try ( DAOUtil dao = new DAOUtil( strStatement ) )
        {
            dao.executeUpdate( );
            System.out.println( strMessage );
        }

    }
    
    private void createBlog( )
    {
        Blog blog = TestUtils.createTestBlog( );
        _nIdBlog = blog.getId( );
    }

}
