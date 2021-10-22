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

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.test.LuteceTestCase;

import java.sql.Timestamp;

public class BlogBusinessTest extends LuteceTestCase
{
    private final static int VERSION1 = 1;
    private final static int VERSION2 = 2;
    private final static String CONTENTLABEL1 = "ContentLabel1";
    private final static String CONTENTLABEL2 = "ContentLabel2";
    private final static Timestamp CREATIONDATE1 = new Timestamp( 1000000l );
    private final static Timestamp CREATIONDATE2 = new Timestamp( 2000000l );
    private final static Timestamp UPDATEDATE1 = new Timestamp( 1000000l );
    private final static Timestamp UPDATEDATE2 = new Timestamp( 2000000l );
    private final static String HTMLCONTENT1 = "HtmlContent1";
    private final static String HTMLCONTENT2 = "HtmlContent2";
    private final static String USER1 = "User1";
    private final static String USER2 = "User2";
    private final static String USER3 = "User3";
    private final static String USER4 = "User4";
    private final static String EDITCOMMENT1 = "EditComment1";
    private final static String EDITCOMMENT2 = "EditComment2";

    public void testBusiness( )
    {
        // Initialize an object
        Blog blog = new Blog( );
        blog.setVersion( VERSION1 );
        blog.setContentLabel( CONTENTLABEL1 );
        blog.setCreationDate( CREATIONDATE1 );
        blog.setUpdateDate( UPDATEDATE1 );
        blog.setHtmlContent( HTMLCONTENT1 );
        blog.setEditComment( EDITCOMMENT1 );
        blog.setUser( USER1 );
        blog.setUserCreator( USER3 );

        // Create test
        BlogHome.create( blog );
        Blog blogStored = BlogHome.findByPrimaryKey( blog.getId( ) );
        assertEquals( blogStored.getId( ), blog.getId( ) );
        assertEquals( blogStored.getVersion( ), blog.getVersion( ) );
        assertEquals( blogStored.getContentLabel( ), blog.getContentLabel( ) );
        assertEquals( blogStored.getCreationDate( ), blog.getCreationDate( ) );
        assertEquals( blogStored.getUpdateDate( ), blog.getUpdateDate( ) );
        assertEquals( blogStored.getHtmlContent( ), blog.getHtmlContent( ) );
        assertEquals( blogStored.getEditComment( ), blog.getEditComment( ) );
        assertEquals( blogStored.getUser( ), blog.getUser( ) );
        assertEquals( blogStored.getUserCreator( ), blog.getUserCreator( ) );

        // Update test
        blog.setVersion( VERSION2 );
        blog.setContentLabel( CONTENTLABEL2 );
        blog.setCreationDate( CREATIONDATE2 );
        blog.setUpdateDate( UPDATEDATE2 );
        blog.setHtmlContent( HTMLCONTENT2 );
        blog.setEditComment( EDITCOMMENT2 );
        blog.setUser( USER2 );
        blog.setUserCreator( USER4 );
        BlogHome.update( blog );
        blogStored = BlogHome.findByPrimaryKey( blog.getId( ) );
        assertEquals( blogStored.getId( ), blog.getId( ) );
        assertEquals( blogStored.getVersion( ), blog.getVersion( ) );
        assertEquals( blogStored.getContentLabel( ), blog.getContentLabel( ) );
        assertEquals( blogStored.getCreationDate( ), blog.getCreationDate( ) );
        assertEquals( blogStored.getUpdateDate( ), blog.getUpdateDate( ) );
        assertEquals( blogStored.getHtmlContent( ), blog.getHtmlContent( ) );
        assertEquals( blogStored.getEditComment( ), blog.getEditComment( ) );
        assertEquals( blogStored.getUser( ), blog.getUser( ) );
        assertEquals( blogStored.getUserCreator( ), blog.getUserCreator( ) );

        // List test
        BlogHome.getBlogsList( );

        // Delete test
        BlogHome.remove( blog.getId( ) );
        blogStored = BlogHome.findByPrimaryKey( blog.getId( ) );
        assertNull( blogStored );

    }

}
