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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.paris.lutece.plugins.blog.service.BlogPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class BlogPublicationHome
{

    private static IBlogPublicationDAO _dao = SpringContextService.getBean( "blog.blogPublicationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private BlogPublicationHome( )
    {
    }

    /**
     * Create an instance of the BlogPublication class
     * 
     * @param blogPub
     *            The instance of the BlogPublication which contains the informations to store
     * @return The instance of BlogPub which has been created.
     */
    public static BlogPublication create( BlogPublication blogPub )
    {
        _dao.insertBlogsId( blogPub, _plugin );

        return blogPub;
    }

    /**
     * Update an instance of the BlogPublication
     * 
     * @param blogPub
     */
    public static void update( BlogPublication blogPub )
    {
        _dao.store( blogPub, _plugin );

    }

    /**
     * load a list BlogPublication by the Blog identifiant
     * 
     * @param nDocId
     *            The Blog id
     * @return list of BlogPublication
     */
    public static List<BlogPublication> getDocPublicationByIdDoc( int nDocId )
    {
        return _dao.loadBlogsId( nDocId, _plugin );

    }

    /**
     * load a list BlogPublication by the portlet id
     * 
     * @param nIdPortlet
     *            The protlet id
     * @return list of BlogPublication
     */
    public static List<BlogPublication> getDocPublicationByPortlet( int nIdPortlet )
    {
        return _dao.loadBlogsByPortlet( nIdPortlet, _plugin );

    }

    /**
     * Retrieve all blogs by Portlet and between publication dates.
     *
     * @param nIdPortlet
     *            The portlet id
     * @param datePublishing
     *            The publication date
     * @param dateEndPublishing
     *            The end publication date
     * @return list of BlogPublication
     */
    public static List<BlogPublication> getDocPublicationByPortletAndPlublicationDate( int nIdPortlet, Date datePublishing, Date dateEndPublishing )
    {
        return _dao.loadBlogsByPortletAndPublicationDate( nIdPortlet, datePublishing, dateEndPublishing, _plugin );

    }

    /**
     * load a BlogPublication by Blog id and portlet id
     * 
     * @param nPortletId
     *            The portlet id
     * @param nDocId
     *            The Blogs id
     * @return BlogPublication
     */
    public static BlogPublication findDocPublicationByPimaryKey( int nPortletId, int nDocId )
    {
        return _dao.loadBlogsPublication( nPortletId, nDocId, _plugin );

    }

    /**
     * Delete the BlogPublication by portlet id
     * 
     * @param nIdPortlet
     *            The portlet id
     */
    public static void removeByIdPortlet( int nIdPortlet )
    {
        _dao.deleteBlogByIdPortlet( nIdPortlet, _plugin );

    }

    /**
     * Delete the BlogPublication by the primary key
     * 
     * @param nIdDoc
     *            The Blog id
     * @param nIdPortlet
     *            The portlet id
     */
    public static void remove( int nIdDoc, int nIdPortlet )
    {
        _dao.remove( nIdDoc, nIdPortlet, _plugin );

    }

    /**
     * Load all BlogPublication
     * 
     * @return list BlogPublication
     */
    public static List<BlogPublication> getAllDocPublication( )
    {
        return _dao.loadAllBlogsPublication( _plugin );

    }

    /**
     * Find the list of {@link BlogPublication} objects specified the status and published at or after the specified date
     * 
     * @param datePublishing
     *            The publication date
     * @param dateEndPublishing
     *            The end publication date
     * @param nStatus
     *            The status
     * @return The {@link BlogPublication} objects {@link Collection} ordered by BlogOrder ascending. The list is empty if no objects found.
     */
    public static Collection<BlogPublication> findSinceDatePublishingAndStatus( Date datePublishing, Date dateEndPublishing, int nStatus )
    {
        return _dao.selectSinceDatePublishingAndStatus( datePublishing, dateEndPublishing, nStatus, _plugin );
    }

    /**
     * Get the list of id of published Blogs associated with a given collection of portlets.
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param datePublishing
     * @param dateEndPublishing
     *            The end publication date
     * @param plugin
     *            The Blog plugin
     * @return The list of Blogs id.
     */
    public static List<Integer> getPublishedBlogsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        return _dao.getPublishedBlogsIdsListByPortletIds( nPortletsIds, datePublishing, dateEndPublishing, plugin );
    }

    /**
     * Get the list of id of published Blogs, associated with a given collection of porlets, which has been updated since the dateUpdated
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param dateUpdated
     *            The date from the blogs had to be updated
     * @param plugin
     *            The plugin
     * @return The list of Blogs id.
     */
    public static List<Integer> getLastPublishedDocumentsIdsListByPortletIds( int [ ] nPortletsIds, Date dateUpdated, Plugin plugin )
    {
        return _dao.getLastPublishedBlogsIdsListByPortletIds( nPortletsIds, dateUpdated, plugin );
    }

    /**
     * Counts the number of valid publication of a blog at a given date.
     * 
     * @param nIdBlog
     *            The blog id
     * @param date
     *            The date
     * @return The count
     */
    public static int countPublicationByIdBlogAndDate( int nIdBlog, Date date )
    {
        return _dao.countPublicationByIdBlogAndDate( nIdBlog, date, _plugin );
    }
}
