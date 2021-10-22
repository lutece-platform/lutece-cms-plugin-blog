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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * IBlogDAO Interface
 */
public interface IBlogDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param blog
     *            instance of the Blog object to insert
     * @param plugin
     *            the Plugin
     */
    void insert( Blog blog, Plugin plugin );

    /**
     * Insert a new record in the table.
     * 
     * @param blog
     *            instance of the blog object to insert
     * @param plugin
     *            the Plugin
     */
    void insertVersion( Blog blog, Plugin plugin );

    /**
     * Update the record in the table
     * 
     * @param blog
     *            the reference of the blog
     * @param plugin
     *            the Plugin
     */
    void store( Blog blog, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nKey
     *            The identifier of the blog to delete
     * @param plugin
     *            the Plugin
     */
    void delete( int nKey, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nKey
     *            The identifier of the blog to delete
     * @param plugin
     *            the Plugin
     */
    void deleteVersions( int nKey, Plugin plugin );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param nKey
     *            The identifier of the blog
     * @param plugin
     *            the Plugin
     * @return The instance of the blog
     */
    Blog load( int nKey, Plugin plugin );

    /**
     * Load the data from the table
     * 
     * @param strName
     *            The name of the blog
     * @param plugin
     *            the Plugin
     * @return The instance of the blog
     */
    Blog loadByName( String strName, Plugin plugin );

    /**
     * Load the data from the table
     * 
     * @param nId
     *            The identifier of the blog
     * @param nVersion
     *            The version
     * @param plugin
     *            the Plugin
     * @return The instance of the blog
     */
    Blog loadVersion( int nId, int nVersion, Plugin plugin );

    /**
     * Load the data of all the blog objects and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @return The list which contains the data of all the blog objects
     */
    List<Blog> selectBlogsList( Plugin plugin );

    /**
     * Load the data of nLimit last modified blog objects and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @param nLimit
     *            number of blogument
     * @return The list which contains the data of of nLimit last modified blog objects
     */
    List<Blog> selectlastModifiedBlogsList( Plugin plugin, int nLimit );

    /**
     * Load the data of all the blog objects and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @return The list which contains the data of all the blog objects
     */
    List<Blog> selectBlogsVersionsList( int nId, Plugin plugin );

    /**
     * Load the data of all the users edited the blog and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @return The list which contains the data of all the users edited blog objects
     */
    List<String> selectAllUsersEditedBlog( int nId, Plugin plugin );

    /**
     * Load the id of all the Blog objects and returns them as a list
     * 
     * @param plugin
     *            the Plugin
     * @return The list which contains the id of all the Blog objects
     */
    List<Integer> selectIdBlogsList( Plugin plugin );

    /**
     * Load the data of all the Blog objects and returns them as a referenceList
     * 
     * @param plugin
     *            the Plugin
     * @return The referenceList which contains the data of all the Blog objects
     */
    ReferenceList selectBlogsReferenceList( Plugin plugin );

    /**
     * Load the list of Blogs
     * 
     * @param filter
     *            The BlogFilter Object
     * @return The Collection of the Blogss
     */
    List<Blog> selectByFilter( BlogFilter filter );

    /**
     * Load the list of Blogs objects and returns them as a list
     * 
     * @param nIdTag
     * @param plugin
     *            the Plugin
     * @return the list of Blogs objects
     */
    List<Blog> loadBlogByIdTag( int nIdTag, Plugin plugin );

    List<Blog> selectWithoutBinaries( Plugin plugin );

}
