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
package fr.paris.lutece.plugins.blog.service.docsearch;

import fr.paris.lutece.plugins.blog.TestUtils;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.BlogSearchFilter;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.test.LuteceTestCase;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.index.IndexWriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * BlogSearchService Test
 */
public class BlogSearchServiceTest extends LuteceTestCase
{
    private int _nIdBlog;

    @BeforeEach
    protected void createBlog( ) throws Exception
    {
        Blog blog = TestUtils.createTestBlog( );
        _nIdBlog = blog.getId( );
    }

    @AfterEach
    protected void removeBlog( ) throws Exception
    {
        BlogHome.remove( _nIdBlog );
    }

    /**
     * Test of processIndexing method, of class BlogSearchService.
     */
    @Test
    void testProcessIndexing( )
    {
        System.out.println( "processIndexing" );
        boolean bCreate = true;
        BlogSearchService instance = BlogSearchService.getInstance( );
        String result = instance.processIndexing( bCreate );
        System.out.println( result );

        System.out.println( "searchResults" );
        BlogSearchFilter filter = new BlogSearchFilter( );
        List<Integer> listIdBlog = new ArrayList<>( );
        int nSearchResultCount = instance.getSearchResults( filter, listIdBlog );
        assertTrue( nSearchResultCount >= 1 );
        assertTrue( listIdBlog.contains( _nIdBlog ) );

    }

    /**
     * Two concurrent index updates (e.g. two posts archived at the same time) must both reach the index: the second
     * update must wait for the first writer instead of failing on the index write lock.
     */
    @Test
    void testConcurrentUpdateDocumentKeepsBothUpdates( ) throws Exception
    {
        int nIdBlog2 = TestUtils.createTestBlog( ).getId( );
        BlogSearchService instance = BlogSearchService.getInstance( );
        Field fieldIndexer = BlogSearchService.class.getDeclaredField( "_indexer" );
        fieldIndexer.setAccessible( true );
        IBlogSearchIndexer indexer = (IBlogSearchIndexer) fieldIndexer.get( instance );
        CountDownLatch firstWriterOpen = new CountDownLatch( 1 );
        try
        {
            instance.processIndexing( true );
            BlogHome.updateBlogArchiveId( true, _nIdBlog );
            BlogHome.updateBlogArchiveId( true, nIdBlog2 );
            Blog blog1 = BlogHome.findByPrimaryKey( _nIdBlog );
            Blog blog2 = BlogHome.findByPrimaryKey( nIdBlog2 );

            // The first update keeps its writer open for a while, the second one starts meanwhile
            fieldIndexer.set( instance, new SlowIndexer( indexer, firstWriterOpen ) );
            Thread first = new Thread( ( ) -> instance.updateDocument( blog1 ) );
            first.start( );
            assertTrue( firstWriterOpen.await( 10, TimeUnit.SECONDS ) );
            Thread second = new Thread( ( ) -> instance.updateDocument( blog2 ) );
            second.start( );
            first.join( 30000 );
            second.join( 30000 );
            fieldIndexer.set( instance, indexer );

            BlogSearchFilter filter = new BlogSearchFilter( );
            filter.setIsArchived( true );
            List<Integer> listIdBlog = new ArrayList<>( );
            instance.getSearchResults( filter, listIdBlog );
            assertTrue( listIdBlog.contains( _nIdBlog ), "first archived post missing from the index" );
            assertTrue( listIdBlog.contains( nIdBlog2 ), "second archived post missing from the index" );
        }
        finally
        {
            fieldIndexer.set( instance, indexer );
            BlogHome.remove( nIdBlog2 );
        }
    }

    /**
     * Indexer that keeps the writer open during the first update
     */
    private static final class SlowIndexer implements IBlogSearchIndexer
    {
        private final IBlogSearchIndexer _delegate;
        private final CountDownLatch _writerOpen;

        SlowIndexer( IBlogSearchIndexer delegate, CountDownLatch writerOpen )
        {
            _delegate = delegate;
            _writerOpen = writerOpen;
        }

        @Override
        public void updateDocument( IndexWriter indexWriter, Blog blog ) throws IOException
        {
            _delegate.updateDocument( indexWriter, blog );
            if ( _writerOpen.getCount( ) > 0 )
            {
                _writerOpen.countDown( );
                try
                {
                    Thread.sleep( 1000 );
                }
                catch( InterruptedException e )
                {
                    Thread.currentThread( ).interrupt( );
                }
            }
        }

        @Override
        public void processIndexing( IndexWriter indexWriter, boolean bCreate, StringBuilder sbLog )
                throws IOException, InterruptedException, SiteMessageException
        {
            _delegate.processIndexing( indexWriter, bCreate, sbLog );
        }

        @Override
        public String getName( )
        {
            return _delegate.getName( );
        }

        @Override
        public String getVersion( )
        {
            return _delegate.getVersion( );
        }

        @Override
        public String getDescription( )
        {
            return _delegate.getDescription( );
        }

        @Override
        public boolean isEnable( )
        {
            return _delegate.isEnable( );
        }
    }
}
