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
package fr.paris.lutece.plugins.blog.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpSession;

/**
 * The sessions kept by BlogSessionListner: a session found invalidated is dropped at once, so that it is not kept (memory) and tried
 * again with an error at every opening of a blog.
 */
public class BlogSessionListnerTest
{
    private static final String KEY = "e2e-blog-session-key";

    @AfterEach
    public void cleanUp( )
    {
        BlogSessionListner.getMapSession( ).remove( KEY );
    }

    /**
     * An invalidated session whose id, read after invalidation, is not the key it was stored under (as on Open Liberty, where the id of
     * the creation event carries the cache prefix) is removed from the map the first time it is met.
     */
    @Test
    public void testInvalidatedSessionIsDropped( )
    {
        HttpSession dead = invalidatedSession( "jh0V6dcbvh7_other-id" );
        BlogSessionListner.getMapSession( ).put( KEY, dead );

        new BlogServiceSession( ).removeBlogFromSession( dead, 1 );

        assertFalse( BlogSessionListner.getMapSession( ).containsKey( KEY ), "invalidated session still kept" );
    }

    /**
     * A live session stays in the map.
     */
    @Test
    public void testLiveSessionIsKept( )
    {
        HttpSession live = (HttpSession) Proxy.newProxyInstance( getClass( ).getClassLoader( ), new Class<?> [ ] {
                HttpSession.class
        }, ( proxy, method, args ) -> "getId".equals( method.getName( ) ) ? KEY : null );
        BlogSessionListner.getMapSession( ).put( KEY, live );

        new BlogServiceSession( ).removeBlogFromSession( live, 1 );

        assertTrue( BlogSessionListner.getMapSession( ).containsKey( KEY ) );
    }

    private HttpSession invalidatedSession( String strId )
    {
        return (HttpSession) Proxy.newProxyInstance( getClass( ).getClassLoader( ), new Class<?> [ ] {
                HttpSession.class
        }, ( proxy, method, args ) -> {
            switch( method.getName( ) )
            {
                case "getId":
                    return strId;
                case "hashCode":
                    return System.identityHashCode( proxy );
                case "equals":
                    return proxy == args [0];
                default:
                    throw new IllegalStateException( "The method is called on an invalidated session: " + strId );
            }
        } );
    }
}
