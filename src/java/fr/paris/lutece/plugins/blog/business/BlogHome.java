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

import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class provides instances management methods (create, find, ...) for Blog objects
 */
public final class BlogHome
{
    // Static variable pointed at the DAO instance
    private static IBlogDAO _dao = SpringContextService.getBean( "blog.blogDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "blog" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private BlogHome( )
    {
    }

    /**
     * Create an instance of the blog class
     * 
     * @param blog
     *            The instance of the blog which contains the informations to store
     * @return The instance of blog which has been created with its primary key.
     */
    public static Blog create( Blog blog )
    {
        _dao.insert( blog, _plugin );

        return blog;
    }

    /**
     * Create an instance of the blog class
     * 
     * @param blog
     *            The instance of the blog which contains the informations to store
     * @return The instance of blog which has been created with its primary key.
     */
    public static Blog createVersion( Blog blog )
    {
        _dao.insertVersion( blog, _plugin );

        return blog;
    }

    /**
     * Update of the blog which is specified in parameter
     * 
     * @param blog
     *            The instance of the blog which contains the data to store
     * @return The instance of the blog which has been updated
     */
    public static Blog update( Blog blog )
    {
        _dao.store( blog, _plugin );

        return blog;
    }

    /**
     * Remove the blog whose identifier is specified in parameter
     * 
     * @param nKey
     *            The blog Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Remove the blog's versions whose identifier is specified in parameter
     * 
     * @param nKey
     *            The blog Id
     */
    public static void removeVersions( int nKey )
    {
        _dao.deleteVersions( nKey, _plugin );
    }

    /**
     * Returns an instance of a blog whose identifier is specified in parameter
     * 
     * @param nKey
     *            The blog primary key
     * @return an instance of blog
     */
    public static Blog findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a blog whose identifier is specified in parameter
     * 
     * @param nKey
     *            The blog primary key
     * @return an instance of blog
     */
    public static Blog findByName( String strName )
    {
        return _dao.loadByName( strName, _plugin );
    }

    /**
     * Returns an instance of a blog whose identifier is specified in parameter
     * 
     * @param nId
     *            The blog primary key
     * @param nVersion
     * 
     * @return an instance of blog
     */
    public static Blog findVersion( int nId, int nVersion )
    {
        return _dao.loadVersion( nId, nVersion, _plugin );
    }

    /**
     * Load the data of all the blog objects and returns them as a list
     * 
     * @return the list which contains the data of all the blog objects
     */
    public static List<Blog> getBlogsList( )
    {
        return _dao.selectBlogsList( _plugin );
    }

    /**
     * Load the data of nLimit last modified Blog objects and returns them as a list
     * 
     * @param nLimit
     *            number of Blogument
     * @return The list which contains the data of of nLimit last modified Blog objects
     */
    public static List<Blog> getLastModifiedBlogsList( int nLimit )
    {
        return _dao.selectlastModifiedBlogsList( _plugin, nLimit );
    }

    /**
     * Load the data of all the Blog objects and returns them as a list
     * 
     * @return the list which contains the data of all the Blog objects
     */
    public static List<Blog> getBlogsVersionsList( int nId )
    {
        return _dao.selectBlogsVersionsList( nId, _plugin );
    }

    /**
     * Load the data of all users edited the Blog objects and returns them as a list
     * 
     * @param nId
     *            The Id
     * @return the list which contains the data of all users edited the Blog objects
     */
    public static List<String> getUsersEditedBlogVersions( int nId )
    {
        return _dao.selectAllUsersEditedBlog( nId, _plugin );
    }

    /**
     * Load the id of all the Blog objects and returns them as a list
     * 
     * @return the list which contains the id of all the Blog objects
     */
    public static List<Integer> getIdBlogsList( )
    {
        return _dao.selectIdBlogsList( _plugin );
    }

    /**
     * Load the data of all the Blog objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the Blog objects
     */
    public static ReferenceList getBlogsReferenceList( )
    {
        return _dao.selectBlogsReferenceList( _plugin );
    }

    /**
     * Create an initial version of an Blog
     *
     * @param blog
     *            The instance of the Blog which contains the informations to store
     * @return The instance of Blog which has been created with its primary key.
     */
    public static Blog addInitialVersion( Blog blog )
    {
        BlogHome.create( blog );
        BlogHome.createVersion( blog );

        return blog;
    }

    /**
     * Adds a new version of an Blog
     *
     * @param blog
     *            The instance of the blog which contains the informations to store
     * @return The instance of the blog which has been updated
     */
    public static Blog addNewVersion( Blog blog )
    {
        BlogHome.update( blog );
        BlogHome.createVersion( blog );

        return blog;
    }

    /**
     * Returns a collection of blog objects
     * 
     * @return A collection of Blogs
     * @param filter
     *            The filter
     */
    public static List<Blog> findByFilter( BlogFilter filter )
    {
        return _dao.selectByFilter( filter );
    }

    /**
     * Load the data of Blog objects and returns them as a list
     * 
     * @param nIdTag
     *            Tag Id
     * @return The list which contains the data of Blog objects
     */
    public static List<Blog> getBlogByTag( int nIdTag )
    {
        return _dao.loadBlogByIdTag( nIdTag, _plugin );
    }

    /**
     * Returns a collection of blog objects
     * 
     * @return
     */
    public static List<Blog> selectWithoutBinaries( )
    {
        return _dao.selectWithoutBinaries( _plugin );
    }

}
