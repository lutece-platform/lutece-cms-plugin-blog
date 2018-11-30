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
package fr.paris.lutece.plugins.blog.business.rss;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;

/**
 *
 * IFormResourceRssConfigDAO
 *
 */
public interface IBlogResourceRssConfigDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param config
     *            The Instance of the object config
     * @param plugin
     *            the plugin
     */
    void insert( BlogResourceRssConfig config, Plugin plugin );

    /**
     * Update the record in the table
     *
     * @param config
     *            instance of config object to update
     * @param plugin
     *            the plugin
     */
    void store( BlogResourceRssConfig config, Plugin plugin );

    /**
     * load the data of BlogResourceRssConfig from the table
     * 
     * @param nIdConfig
     *            the config id
     * @param plugin
     *            the plugin
     * @return The Instance of the object BlogResourceRssConfig
     *
     */
    BlogResourceRssConfig load( int nIdConfig, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nIdConfig
     *            The id of object BlogResourceRssConfig
     * @param plugin
     *            le plugin
     */
    void delete( int nIdConfig, Plugin plugin );

    /**
     * Return all record
     * 
     * @param plugin
     *            le plugin
     * @return List of BlogResourceRssConfig
     */
    List<BlogResourceRssConfig> loadAll( Plugin plugin );
}
