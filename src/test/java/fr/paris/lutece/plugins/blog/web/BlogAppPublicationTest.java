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
package fr.paris.lutece.plugins.blog.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.blog.TestUtils;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.enterprise.inject.spi.CDI;

/**
 * The public page of a blog shows the current version of a published blog only
 */
public class BlogAppPublicationTest extends LuteceTestCase
{
    private static final int ID_PORTLET = 987654;

    private Blog _blog;

    @BeforeEach
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _blog = TestUtils.createTestBlog( );
        // the version 1 of the history, as when the blog is created in the back office
        BlogHome.createVersion( _blog );
    }

    @AfterEach
    @Override
    protected void tearDown( ) throws Exception
    {
        if ( _blog != null )
        {
            BlogPublicationHome.removeByBlogId( _blog.getId( ) );
            BlogHome.removeVersions( _blog.getId( ) );
            BlogHome.remove( _blog.getId( ) );
        }
        super.tearDown( );
    }

    @Test
    public void testUnpublishedBlogIsNotShown( )
    {
        assertThrows( SiteMessageException.class, ( ) -> display( null ) );
    }

    @Test
    public void testPublishedBlogIsShown( ) throws SiteMessageException
    {
        publish( LocalDate.now( ).minusDays( 1 ), LocalDate.now( ).plusDays( 10 ) );
        assertNotNull( display( null ) );
    }

    @Test
    public void testEndedPublicationIsNotShown( )
    {
        publish( LocalDate.now( ).minusDays( 10 ), LocalDate.now( ).minusDays( 1 ) );
        assertThrows( SiteMessageException.class, ( ) -> display( null ) );
    }

    @Test
    public void testArchivedBlogIsNotShown( )
    {
        publish( LocalDate.now( ).minusDays( 1 ), LocalDate.now( ).plusDays( 10 ) );
        BlogHome.updateBlogArchiveId( true, _blog.getId( ) );
        assertThrows( SiteMessageException.class, ( ) -> display( null ) );
    }

    @Test
    public void testVersionIsNotShownToVisitors( )
    {
        assertNotNull( BlogHome.findVersion( _blog.getId( ), _blog.getVersion( ) ), "the version exists" );
        publish( LocalDate.now( ).minusDays( 1 ), LocalDate.now( ).plusDays( 10 ) );
        assertThrows( SiteMessageException.class, ( ) -> display( String.valueOf( _blog.getVersion( ) ) ) );
    }

    private void publish( LocalDate begin, LocalDate end )
    {
        BlogPublication publication = new BlogPublication( );
        publication.setIdBlog( _blog.getId( ) );
        publication.setIdPortlet( ID_PORTLET );
        publication.setDateBeginPublishing( Date.valueOf( begin ) );
        publication.setDateEndPublishing( Date.valueOf( end ) );
        BlogPublicationHome.create( publication );
    }

    private Object display( String strVersion ) throws SiteMessageException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id", String.valueOf( _blog.getId( ) ) );
        if ( strVersion != null )
        {
            request.addParameter( "version", strVersion );
        }
        return CDI.current( ).select( BlogApp.class ).get( ).getBlogDetails( request );
    }
}
