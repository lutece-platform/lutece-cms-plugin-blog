/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

/**
 * This Service manages document actions (create, move, delete, validate ...) and notify listeners.
 */
public class BlogServiceSession
{

    private static BlogServiceSession _singleton = new BlogServiceSession( );
    private final static String SESSION_BLOG = "blog.serviceblog";

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
     * @param Blog
     *            The blog to save
     */
    public void saveBlogInSession( HttpSession session, Blog blog )
    {
        session.setAttribute( SESSION_BLOG + blog.getId( ), blog );
    }

    /**
     * Get the current blog form from the session
     * 
     * @param session
     *            The session of the user
     * @return The blog form
     */
    public Blog getBlogFromSession( HttpSession session, Blog blog )
    {
        return (Blog) session.getAttribute( SESSION_BLOG + blog.getId( ) );
    }

    /**
     * Get the current blog form from the session
     * 
     * @param session
     *            The session of the user
     * @return The idBlog
     */
    public Blog getBlogFromSession( HttpSession session, int idBlog )
    {
        return (Blog) session.getAttribute( SESSION_BLOG + idBlog );
    }

    /**
     * Remove any blog form responses stored in the session of the user
     * 
     * @param session
     *            The session
     */
    public void removeBlogFromSession( HttpSession session, Blog blog )
    {
        session.removeAttribute( SESSION_BLOG + blog.getId( ) );
    }

    /**
     * Remove any blog form responses stored in the session of the user
     * 
     * @param session
     *            The session
     */
    public void removeBlogFromSession( HttpSession session, int idBlog )
    {
        session.removeAttribute( SESSION_BLOG + idBlog );
    }

}
