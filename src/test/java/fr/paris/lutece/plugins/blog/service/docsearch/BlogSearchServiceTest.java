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
import fr.paris.lutece.test.LuteceTestCase;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * BlogSearchService Test
 */
public class BlogSearchServiceTest extends LuteceTestCase
{
    private int _nIdBlog;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        Blog blog = TestUtils.createTestArticle( );
        _nIdBlog = blog.getId( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        super.tearDown( );
        BlogHome.remove( _nIdBlog );
    }

    /**
     * Test of processIndexing method, of class BlogSearchService.
     */
    @Test
    public void testProcessIndexing( )
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

}
