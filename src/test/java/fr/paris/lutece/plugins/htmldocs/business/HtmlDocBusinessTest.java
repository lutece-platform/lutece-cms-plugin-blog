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

import fr.paris.lutece.test.LuteceTestCase;

import java.sql.Date;

public class HtmlDocBusinessTest extends LuteceTestCase
{
    private final static int CONTENTID1 = 1;
    private final static int CONTENTID2 = 2;
    private final static int VERSION1 = 1;
    private final static int VERSION2 = 2;
    private final static String CONTENTLABEL1 = "ContentLabel1";
    private final static String CONTENTLABEL2 = "ContentLabel2";
	private final static Date CREATIONDATE1 = new Date( 1000000l );
    private final static Date CREATIONDATE2 = new Date( 2000000l );
	private final static Date UPDATEDATE1 = new Date( 1000000l );
    private final static Date UPDATEDATE2 = new Date( 2000000l );
    private final static String HTMLCONTENT1 = "HtmlContent1";
    private final static String HTMLCONTENT2 = "HtmlContent2";
    private final static String USER1 = "User1";
    private final static String USER2 = "User2";

    public void testBusiness(  )
    {
        // Initialize an object
        HtmlDoc htmlDoc = new HtmlDoc();
        htmlDoc.setContentId( CONTENTID1 );
        htmlDoc.setVersion( VERSION1 );
        htmlDoc.setContentLabel( CONTENTLABEL1 );
        htmlDoc.setCreationDate( CREATIONDATE1 );
        htmlDoc.setUpdateDate( UPDATEDATE1 );
        htmlDoc.setHtmlContent( HTMLCONTENT1 );
        htmlDoc.setUser( USER1 );

        // Create test
        HtmlDocHome.create( htmlDoc );
        HtmlDoc htmlDocStored = HtmlDocHome.findByPrimaryKey( htmlDoc.getId( ) );
        assertEquals( htmlDocStored.getContentId() , htmlDoc.getContentId( ) );
        assertEquals( htmlDocStored.getVersion() , htmlDoc.getVersion( ) );
        assertEquals( htmlDocStored.getContentLabel() , htmlDoc.getContentLabel( ) );
        assertEquals( htmlDocStored.getCreationDate() , htmlDoc.getCreationDate( ) );
        assertEquals( htmlDocStored.getUpdateDate() , htmlDoc.getUpdateDate( ) );
        assertEquals( htmlDocStored.getHtmlContent() , htmlDoc.getHtmlContent( ) );
        assertEquals( htmlDocStored.getUser() , htmlDoc.getUser( ) );

        // Update test
        htmlDoc.setContentId( CONTENTID2 );
        htmlDoc.setVersion( VERSION2 );
        htmlDoc.setContentLabel( CONTENTLABEL2 );
        htmlDoc.setCreationDate( CREATIONDATE2 );
        htmlDoc.setUpdateDate( UPDATEDATE2 );
        htmlDoc.setHtmlContent( HTMLCONTENT2 );
        htmlDoc.setUser( USER2 );
        HtmlDocHome.update( htmlDoc );
        htmlDocStored = HtmlDocHome.findByPrimaryKey( htmlDoc.getId( ) );
        assertEquals( htmlDocStored.getContentId() , htmlDoc.getContentId( ) );
        assertEquals( htmlDocStored.getVersion() , htmlDoc.getVersion( ) );
        assertEquals( htmlDocStored.getContentLabel() , htmlDoc.getContentLabel( ) );
        assertEquals( htmlDocStored.getCreationDate() , htmlDoc.getCreationDate( ) );
        assertEquals( htmlDocStored.getUpdateDate() , htmlDoc.getUpdateDate( ) );
        assertEquals( htmlDocStored.getHtmlContent() , htmlDoc.getHtmlContent( ) );
        assertEquals( htmlDocStored.getUser() , htmlDoc.getUser( ) );

        // List test
        HtmlDocHome.getHtmlDocsList();

        // Delete test
        HtmlDocHome.remove( htmlDoc.getId( ) );
        htmlDocStored = HtmlDocHome.findByPrimaryKey( htmlDoc.getId( ) );
        assertNull( htmlDocStored );
        
    }

}