/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.blog.TestUtils;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * The comparison of two versions of a post under HTML auto-escaping (LUT-33153): the diff is built from the sanitized contents and written as
 * HTML, the title of the post as text
 */
public class BlogDiffRenderTest extends LuteceTestCase
{
    private static final String RIGHT_BLOG_MANAGEMENT = "BLOG_MANAGEMENT";
    private static final String TITLE_WITH_MARKUP = "Horaires <b>\"l'accueil\"</b>";
    private static final String TITLE_ESCAPED = "Horaires &lt;b&gt;&quot;l&#39;accueil&quot;&lt;/b&gt;";
    private static final String CONTENT_V1 = "<p>Accueil <strong>lundi</strong></p><script>alert(1)</script>";
    private static final String CONTENT_V2 = "<p>Accueil <strong>mardi</strong></p><p onclick=\"alert(2)\">Contact</p>";

    // BlogPublicationJspBean extends BlogJspBean: the bean is chosen by its name
    @Inject
    @Named( "blogJspBean" )
    private BlogJspBean _bean;
    private Blog _blog;

    @AfterEach
    protected void removeBlog( )
    {
        if ( _blog != null )
        {
            BlogHome.removeVersions( _blog.getId( ) );
            BlogHome.remove( _blog.getId( ) );
        }
    }

    @Test
    public void testDiffIsWrittenAsSanitizedHtml( ) throws AccessDeniedException
    {
        _blog = TestUtils.createTestBlog( );
        _blog.setContentLabel( TITLE_WITH_MARKUP );
        _blog.setHtmlContent( CONTENT_V1 );
        BlogHome.update( _blog );
        BlogHome.createVersion( _blog );
        _blog.setVersion( _blog.getVersion( ) + 1 );
        _blog.setHtmlContent( CONTENT_V2 );
        BlogHome.update( _blog );
        BlogHome.createVersion( _blog );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), RIGHT_BLOG_MANAGEMENT );
        _bean.init( request, RIGHT_BLOG_MANAGEMENT );
        request.setParameter( "id", String.valueOf( _blog.getId( ) ) );
        request.setParameter( "blog_version", String.valueOf( _blog.getVersion( ) ) );
        request.setParameter( "blog_version2", String.valueOf( _blog.getVersion( ) - 1 ) );

        String strPage = _bean.getDiffBlog( request );

        assertTrue( strPage.contains( "class=\"diff-html-" ), strPage );
        assertTrue( strPage.contains( "<strong>" ), strPage );
        assertTrue( strPage.contains( TITLE_ESCAPED ), strPage );
        assertFalse( strPage.contains( TITLE_WITH_MARKUP ), strPage );
        assertFalse( strPage.contains( "alert(1)" ), strPage );
        assertFalse( strPage.contains( "alert(2)" ), strPage );
        assertFalse( strPage.contains( "&lt;strong&gt;" ), strPage );
    }
}
