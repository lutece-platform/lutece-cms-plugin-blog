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
package fr.paris.lutece.plugins.blog.service;

import javax.servlet.http.HttpSession;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * This Service manages document actions (create, move, delete, validate ...) and notify listeners.
 */
public class BlogServiceSession
{

    private static BlogServiceSession _singleton = new BlogServiceSession( );
    private static final String SESSION_BLOG = "blog.serviceblog";

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static BlogServiceSession getInstance( )
    {
        return _singleton;
    }

    /**
     * Save an blog in the session of the user
     *
     * @param session
     *            The session
     * @param blog
     *            The blog to save
     */
    public void saveBlogInSession( HttpSession session, Blog blog )
    {
        try
        {

            session.setAttribute( SESSION_BLOG + blog.getId( ), blog );

        }
        catch( IllegalStateException e )
        {

            AppLogService.error( e.getMessage( ), e );
            BlogSessionListner.remove( session.getId( ) );
        }
    }

    /**
     * Get the current blog form from the session
     * 
     * @param session
     *            The session of the user
     * @param blog
     * @return The blog form
     */
    public Blog getBlogFromSession( HttpSession session, Blog blog )
    {

        try
        {
            return (Blog) session.getAttribute( SESSION_BLOG + blog.getId( ) );

        }
        catch( IllegalStateException e )
        {

            AppLogService.error( e.getMessage( ), e );
            BlogSessionListner.remove( session.getId( ) );
            return null;
        }
    }

    /**
     * Get the current blog form from the session
     * 
     * @param session
     *            The session of the user
     * @param nIdBlog
     * @return The blog post
     */
    public Blog getBlogFromSession( HttpSession session, int nIdBlog )
    {
        try
        {

            return (Blog) session.getAttribute( SESSION_BLOG + nIdBlog );

        }
        catch( IllegalStateException e )
        {

            AppLogService.error( e.getMessage( ), e );
            BlogSessionListner.remove( session.getId( ) );
            return null;
        }
    }

    /**
     * Remove any blog form responses stored in the session of the user
     * 
     * @param session
     *            The session
     * @param blog
     */
    public void removeBlogFromSession( HttpSession session, Blog blog )
    {
        try
        {

            session.removeAttribute( SESSION_BLOG + blog.getId( ) );

        }
        catch( IllegalStateException e )
        {

            AppLogService.error( e.getMessage( ), e );
            BlogSessionListner.remove( session.getId( ) );
        }
    }

    /**
     * Remove any blog form responses stored in the session of the user
     * 
     * @param session
     *            The session
     * @param idBlog
     */
    public void removeBlogFromSession( HttpSession session, int idBlog )
    {
        try
        {

            session.removeAttribute( SESSION_BLOG + idBlog );

        }
        catch( IllegalStateException e )
        {

            AppLogService.error( e.getMessage( ), e );
            BlogSessionListner.remove( session.getId( ) );
        }
    }

}
