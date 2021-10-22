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

import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IBlogPublicationDAO
{

    /**
     * Insert a list of doc for a specified blogPublication
     * 
     * @param blogPublication
     *            the blogPublication to insert
     * @param plugin
     *            the plugin
     */
    void insertBlogsId( BlogPublication blogPublication, Plugin plugin );

    /**
     * Delete docs for the specified portlet
     * 
     * @param nDocId
     *            The doc identifier
     * @param plugin
     *            the plugin
     */
    void deleteBlogsId( int nDocId, Plugin plugin );

    /**
     * Load a list of BLOGPublication
     * 
     * @param nDocId
     * @param plugin
     *            the plugin
     * @return List of IdDoc
     */
    List<BlogPublication> loadBlogsId( int nDocId, Plugin plugin );

    /**
     * load a list BLOGPublication by the portlet id
     * 
     * @param nIdPortlet
     *            The protlet id
     * @param plugin
     *            the plugin
     * @return list of BLOGPublication
     */
    List<BlogPublication> loadBlogsByPortlet( int nIdPortlet, Plugin plugin );

    /**
     * Load a list BLOGPublication by the portlet id, and published at or after the specified date.
     *
     * @param nIdPortlet
     *            The portlet id
     * @param datePublishing
     *            The publication end date
     * @param dateEndPublishing
     *            The publication date
     * @param plugin
     *            the plugin
     * @return list of BLOGPublication
     */
    List<BlogPublication> loadBlogsByPortletAndPublicationDate( int nIdPortlet, Date datePublishing, Date dateEndPublishing, Plugin plugin );

    /**
     * Delete the BLOGPublication by portlet id
     * 
     * @param nIdPortlet
     *            The portlet id
     * @param plugin
     *            the plugin
     */
    void deleteBlogByIdPortlet( int nIdPortlet, Plugin plugin );

    /**
     * load a BLOGPublication by BLOG id and portlet id
     * 
     * @param nPortletId
     *            The portlet id
     * @param nDocId
     *            The blogs id
     * @param plugin
     *            the plugin
     * @return blogPublication
     */
    BlogPublication loadBlogsPublication( int nPortletId, int nDocId, Plugin plugin );

    /**
     * Update an instance of the BLOGPublication
     * 
     * @param blogPublication
     *            the publication data
     * @param plugin
     *            the plugin
     */
    void store( BlogPublication blogPublication, Plugin plugin );

    /**
     * Remove BLOGPublication by primary kley
     * 
     * @param nDocId
     * @param nIdPortlet
     * @param plugin
     *            the plugin
     */
    void remove( int nDocId, int nIdPortlet, Plugin plugin );

    /**
     * Load all BLOGPublication
     * 
     * @param plugin
     * @return The list of BLOGPublication
     */
    List<BlogPublication> loadAllBlogsPublication( Plugin plugin );

    /**
     * Find the list of {@link BlogPublication} objects specified the status and published at or after the specified date
     * 
     * @param datePublishing
     *            The publication end date
     * @param dateEndPublishing
     *            The publication date
     * @param nStatus
     *            The status
     * @param plugin
     *            the plugin
     * @return The {@link BlogPublication} objects {@link Collection} ordered by BlogOrder ascending. The list is empty if no objects found.
     */
    Collection<BlogPublication> selectSinceDatePublishingAndStatus( Date datePublishing, Date dateEndPublishing, int nStatus, Plugin plugin );

    /**
     * Get the list of id of published BLOGs associated with a given collection of portlets.
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param datePublishing
     *            The publication date
     * @param dateEndPublishing
     *            The publication end date
     * @param plugin
     *            The blog plugin
     * @return The list of blogs id.
     */
    List<Integer> getPublishedBlogsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin );

    /**
     * Get the list of published Blogs associated with given collection of portlets. Returns only the blogs updated after given dateUpdated
     * 
     * @param nPortletsIds
     *            The list of portlets ids
     * @param dateUpdatedFrom
     *            The date from the blogs had been updated
     * @param plugin
     *            the plugin
     * @return the list of blogs id
     */
    List<Integer> getLastPublishedBlogsIdsListByPortletIds( int [ ] nPortletsIds, Date dateUpdatedFrom, Plugin plugin );

    /**
     * Counts the number of valid publication of a blog at a given date.
     * 
     * @param nIdBlog
     * @param date
     * @param plugin
     * @return
     */
    int countPublicationByIdBlogAndDate( int nIdBlog, Date date, Plugin plugin );
}
