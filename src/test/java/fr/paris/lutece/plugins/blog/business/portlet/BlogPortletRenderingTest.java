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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.blog.TestUtils;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTemplate;
import fr.paris.lutece.portal.business.portlet.PortletTemplateHome;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;

/**
 * Renders a blog portlet with every template registered in the core for the BLOG_PORTLET type
 */
public class BlogPortletRenderingTest extends LuteceTestCase
{
    private static final String PORTLET_NAME = "BlogPortletRenderingTest heading";
    private static final String MARKER_PORTLET = "portlet-blog";
    private static final int UNKNOWN_TEMPLATE_ID = 99999;

    private Blog _blog;
    private BlogPortlet _portlet;

    @BeforeEach
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        _blog = TestUtils.createTestBlog( );

        _portlet = new BlogPortlet( );
        _portlet.setContentId( _blog.getId( ) );
        _portlet.setPortletName( PORTLET_NAME );
        _portlet.setPageId( PortalService.getRootPageId( ) );
        _portlet.setStyleId( 0 );
        _portlet.setColumn( 1 );
        _portlet.setOrder( 1 );
        _portlet.setName( PORTLET_NAME );
        _portlet.setStatus( Portlet.STATUS_PUBLISHED );
        _portlet.setDisplayPortletTitle( 0 );
        _portlet.setDeviceDisplayFlags( Portlet.FLAG_DISPLAY_ON_NORMAL_DEVICE | Portlet.FLAG_DISPLAY_ON_LARGE_DEVICE | Portlet.FLAG_DISPLAY_ON_XLARGE_DEVICE );

        // published since yesterday, until 2050
        BlogPublication publication = new BlogPublication( );
        publication.setDateBeginPublishing( Date.valueOf( LocalDate.now( ).minusDays( 1 ) ) );
        publication.setDateEndPublishing( Date.valueOf( LocalDate.of( 2050, 1, 1 ) ) );
        _portlet.setBlogPublication( publication );

        BlogPortletHome.getInstance( ).create( _portlet );
        // the creation only stores the end date : the update stores the begin date too
        _portlet.update( );
    }

    @AfterEach
    @Override
    protected void tearDown( ) throws Exception
    {
        if ( _portlet != null )
        {
            // removes the publication rows before the portlet
            _portlet.remove( );
        }
        if ( _blog != null )
        {
            BlogHome.remove( _blog.getId( ) );
        }

        LocalVariables.remove( );
        super.tearDown( );
    }

    @Test
    public void testShippedTemplateRegistered( )
    {
        List<PortletTemplate> listTemplates = PortletTemplateHome.findByPortletType( BlogPortletHome.getInstance( ).getPortletTypeId( ) );

        assertEquals( 1, listTemplates.size( ), "the shipped template should be registered in the core for the BLOG_PORTLET type" );
        assertEquals( BlogPortlet.TEMPLATE_PORTLET_BLOG_DEFAULT, listTemplates.get( 0 ).getTemplatePath( ) );
    }

    @Test
    public void testTemplateStoredWithThePortlet( )
    {
        PortletTemplate template = PortletTemplateHome.findByPortletType( BlogPortletHome.getInstance( ).getPortletTypeId( ) ).get( 0 );
        _portlet.setIdTemplate( template.getId( ) );
        _portlet.update( );

        Portlet stored = PortletHome.findByPrimaryKey( _portlet.getId( ) );

        assertEquals( template.getId( ), stored.getIdTemplate( ), "the chosen template should be stored by the core with the portlet" );
        assertTrue( PortletTemplateHome.isTemplateUsed( template.getId( ) ), "a template chosen by a portlet is used" );
    }

    @Test
    public void testRenderEveryShippedTemplate( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );

        for ( PortletTemplate template : PortletTemplateHome.findByPortletType( BlogPortletHome.getInstance( ).getPortletTypeId( ) ) )
        {
            _portlet.setIdTemplate( template.getId( ) );
            String strContent = _portlet.getHtmlContent( request );

            assertTrue( strContent.contains( MARKER_PORTLET ), "template " + template.getTemplatePath( ) + " should render the portlet wrapper" );
            assertTrue( strContent.contains( "portlet_" + _portlet.getId( ) ), "template " + template.getTemplatePath( ) + " should render the portlet id" );
            assertTrue( strContent.contains( _blog.getContentLabel( ) ), "template " + template.getTemplatePath( ) + " should render the blog title" );
            assertTrue( strContent.contains( _blog.getHtmlContent( ) ), "template " + template.getTemplatePath( ) + " should render the blog content" );
            assertTrue( strContent.contains( "d-md-block" ), "template " + template.getTemplatePath( ) + " should render the device classes" );
        }
    }

    @Test
    public void testFallbackToDefaultTemplate( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );

        _portlet.setIdTemplate( UNKNOWN_TEMPLATE_ID );
        _portlet.setDisplayPortletTitle( 1 );
        String strContent = _portlet.getHtmlContent( request );

        assertTrue( strContent.contains( MARKER_PORTLET ), "the default template should render the portlet wrapper" );
        assertTrue( strContent.contains( _blog.getContentLabel( ) ), "the default template should render the blog title" );
        assertFalse( strContent.contains( PORTLET_NAME ), "a hidden portlet title should not be rendered" );
    }
}
