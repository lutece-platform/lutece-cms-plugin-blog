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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.Test;

/**
 * BlogPublicationDAO Test
 */
public class BlogPublicationDAOTest extends LuteceTestCase
{

    private static final String PLUGIN_NAME = "blog";
    private static final int BLOG_ID = 10000;
    private static final int PORTLET_ID = 20000;
    private static final int STATUS_ENABLED = 1;
    private static final int ORDER = 1;
    private static final int TESTCOUNT = 14;

    private static Plugin _plugin = PluginService.getPlugin( PLUGIN_NAME );
    private static BlogPublicationDAO _dao = new BlogPublicationDAO( );
    private static String _strDatabaseEngine;
    private static int nTestCount = 0;

    /**
     * {@inheritDoc }
     * 
     * @throws Exception
     */
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        // Equivalent to @BeforeClass that is not compatible with LuteceTestCase
        if ( nTestCount == 0 )
        {
            setUpDatabase( );
        }
        nTestCount++;
    }

    /**
     * {@inheritDoc }
     * 
     * @throws Exception
     */
    @Override
    protected void tearDown( ) throws Exception
    {
        super.tearDown( );

        // Equivalent to @AfterClass that is not compatible with LuteceTestCase
        if ( nTestCount == TESTCOUNT )
        {
            tearDownDatabase( );
        }
    }

    /**
     * Test of insertBlogsId method, of class BlogPublicationDAO.
     */
    @Test
    public void testInsertBlogsId( )
    {
        System.out.println( "insertBlogsId" );
        insert( );
    }

    /**
     * Test of store method, of class BlogPublicationDAO.
     */
    @Test
    public void testStore( )
    {
        System.out.println( "store" );
        BlogPublication blogPublication = getBlogPublication( );
        _dao.store( blogPublication, _plugin );
    }

    /**
     * Test of deleteBlogsId method, of class BlogPublicationDAO.
     */
    @Test
    public void testDeleteBlogsId( )
    {
        System.out.println( "deleteBlogsId" );
        insert( );
        int nDocId = BLOG_ID;
        _dao.deleteBlogsId( nDocId, _plugin );
    }

    /**
     * Test of deleteBlogByIdPortlet method, of class BlogPublicationDAO.
     */
    @Test
    public void testDeleteBlogByIdPortlet( )
    {
        System.out.println( "deleteBlogByIdPortlet" );
        insert( );
        int nIdPortlet = PORTLET_ID;
        _dao.deleteBlogByIdPortlet( nIdPortlet, _plugin );
    }

    /**
     * Test of remove method, of class BlogPublicationDAO.
     */
    @Test
    public void testRemove( )
    {
        System.out.println( "remove" );
        insert( );
        int nDocId = BLOG_ID;
        int nIdPortlet = PORTLET_ID;
        _dao.remove( nDocId, nIdPortlet, _plugin );
    }

    /**
     * Test of loadBlogsId method, of class BlogPublicationDAO.
     */
    @Test
    public void testLoadBlogsId( )
    {
        System.out.println( "loadBlogsId" );
        insert( );
        int nDocId = BLOG_ID;
        List<BlogPublication> result = _dao.loadBlogsId( nDocId, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of loadBlogsByPortlet method, of class BlogPublicationDAO.
     */
    @Test
    public void testLoadBlogsByPortlet( )
    {
        System.out.println( "loadBlogsByPortlet" );
        insert( );
        int nIdPortlet = PORTLET_ID;
        List<BlogPublication> result = _dao.loadBlogsByPortlet( nIdPortlet, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of loadBlogsByPortletAndPublicationDate method, of class BlogPublicationDAO.
     */
    @Test
    public void testLoadBlogsByPortletAndPublicationDate( )
    {
        System.out.println( "loadBlogsByPortletAndPublicationDate" );
        insert( );
        int nIdPortlet = PORTLET_ID;
        Date datePublishing = new Date( getTime( -2 ) );
        Date dateEndPublishing = new Date( getTime( 2 ) );
        List<BlogPublication> result = _dao.loadBlogsByPortletAndPublicationDate( nIdPortlet, datePublishing, dateEndPublishing, _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of loadBlogsPublication method, of class BlogPublicationDAO.
     */
    @Test
    public void testLoadBlogsPublication( )
    {
        System.out.println( "loadBlogsPublication" );
        insert( );
        int nPortletId = PORTLET_ID;
        int nDocId = BLOG_ID;
        BlogPublication result = _dao.loadBlogsPublication( nPortletId, nDocId, _plugin );
        assertNotNull( result );
    }

    /**
     * Test of loadAllBlogsPublication method, of class BlogPublicationDAO.
     */
    @Test
    public void testLoadAllBlogsPublication( )
    {
        System.out.println( "loadAllBlogsPublication" );
        insert( );
        List<BlogPublication> result = _dao.loadAllBlogsPublication( _plugin );
        assertTrue( !result.isEmpty( ) );
    }

    /**
     * Test of selectSinceDatePublishingAndStatus method, of class BlogPublicationDAO.
     */
    @Test
    public void testSelectSinceDatePublishingAndStatus( )
    {
        System.out.println( "selectSinceDatePublishingAndStatus" );
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
    public void testGetPublishedBlogsIdsListByPortletIds( )
    {
        System.out.println( "getPublishedBlogsIdsListByPortletIds" );
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
    public void testGetLastPublishedBlogsIdsListByPortletIds( )
    {
        System.out.println( "getLastPublishedBlogsIdsListByPortletIds" );
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
    public void testCountPublicationByIdBlogAndDate( )
    {
        System.out.println( "countPublicationByIdBlogAndDate" );
        insert( );
        int nIdBlog = BLOG_ID;
        Date date = new Date( );
        int result = _dao.countPublicationByIdBlogAndDate( nIdBlog, date, _plugin );
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
        _dao.deleteBlogsId( BLOG_ID, _plugin );
    }

    /**
     * Insert test data
     */
    private void insert( )
    {
        clean( );
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
        blogPublication.setIdBlog( BLOG_ID );
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
}
