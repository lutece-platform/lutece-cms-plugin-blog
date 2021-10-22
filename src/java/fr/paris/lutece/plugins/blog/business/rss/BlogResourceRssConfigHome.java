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
package fr.paris.lutece.plugins.blog.business.rss;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;

/**
 * BlogResourceRssConfigHome
 */
public final class BlogResourceRssConfigHome
{
    // Static variable pointed at the DAO instance
    private static IBlogResourceRssConfigDAO _dao = SpringContextService.getBean( "blog.blogResourceRssConfigDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private BlogResourceRssConfigHome( )
    {
    }

    /**
     * Insert new configuration
     *
     * @param config
     *            object configuration
     * @param plugin
     *            the plugin
     */
    public static void create( BlogResourceRssConfig config, Plugin plugin )
    {
        _dao.insert( config, plugin );
    }

    /**
     * Update a configuration
     *
     * @param config
     *            object configuration
     * @param plugin
     *            the plugin
     */
    public static void update( BlogResourceRssConfig config, Plugin plugin )
    {
        _dao.store( config, plugin );
    }

    /**
     * Delete a configuration
     * 
     * @param nIdBlogResourceRssConfig
     *            id BlogResourceRssConfig
     * @param plugin
     *            the plugin
     */
    public static void remove( int nIdBlogResourceRssConfig, Plugin plugin )
    {
        _dao.delete( nIdBlogResourceRssConfig, plugin );
    }

    /**
     * Delete a configuration
     * 
     * @param nIdBlogResourceRssConfig
     *            id task
     * @param plugin
     *            the plugin
     * @return a configuration
     *
     */
    public static BlogResourceRssConfig findByPrimaryKey( int nIdBlogResourceRssConfig, Plugin plugin )
    {
        return _dao.load( nIdBlogResourceRssConfig, plugin );
    }

    /**
     * Load All BlogResourceRssConfig
     * 
     * @param plugin
     *            the plugin
     * @return a list of BlogResourceRssConfig
     *
     */
    public static List<BlogResourceRssConfig> getAll( Plugin plugin )
    {
        return _dao.loadAll( plugin );
    }
}
