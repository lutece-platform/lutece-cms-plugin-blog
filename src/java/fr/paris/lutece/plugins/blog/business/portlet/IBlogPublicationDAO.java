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
     */
    void insertBlogsId( BlogPublication blogPublication, Plugin plugin );

    /**
     * Delete docs for the specified portlet
     * 
     * @param nPortletId
     *            The doc identifier
     */
    void deleteBlogsId( int nDocId, Plugin plugin );

    /**
     * Load a list of BLOGPublication
     * 
     * @param nDocId
     * @return List of IdDoc
     */
    List<BlogPublication> loadBlogsId( int nDocId, Plugin plugin );

    /**
     * load a list BLOGPublication by the portlet id
     * 
     * @param nIdPortlet
     *            The protlet id
     * @return list of BLOGPublication
     */
    List<BlogPublication> loadBlogsByPortlet( int nIdPortlet, Plugin plugin );

    /**
     * Delete the BLOGPublication by portlet id
     * 
     * @param nIdPortlet
     *            The portlet id
     */
    void deleteBlogByIdPortlet( int nIdPortlet, Plugin plugin );

    /**
     * load a BLOGPublication by BLOG id and portlet id
     * 
     * @param nDocId
     *            The blogs id
     * @param nPortletId
     *            The portlet id
     * @return blogPublication
     */
    BlogPublication loadBlogsPublication( int nDocId, int nPortletId, Plugin plugin );

    /**
     * Update an instance of the BLOGPublication
     * 
     * @param blogPub
     */
    void store( BlogPublication blogPublication, Plugin plugin );

    /**
     * Remove BLOGPublication by primary kley
     * 
     * @param nDocId
     * @param nIdPortlet
     * @param plugin
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
     * Find the list of {@link DocumentPublication} objects specified the status and published at or after the specified date
     * 
     * @param datePublishing
     *            The publication end date
     * @param dateEndPublishing
     *            The publication date
     * @param nStatus
     *            The status
     * @return The {@link DocumentPublication} objects {@link Collection} ordered by documentOrder ascending. The list is empty if no objects found.
     */
    Collection<BlogPublication> selectSinceDatePublishingAndStatus( Date datePublishing, Date dateEndPublishing, int nStatus, Plugin plugin );

    /**
     * Get the list of id of published BLOGs associated with a given collection of portlets.
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param datePublishing
     *            TODO
     * @param datePublishing
     *            The publication end date
     * @param plugin
     *            The document plugin
     * @return The list of documents id.
     */
    List<Integer> getPublishedDocumentsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin );
}
