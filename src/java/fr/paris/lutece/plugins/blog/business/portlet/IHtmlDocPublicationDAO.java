package fr.paris.lutece.plugins.blog.business.portlet;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
public interface IHtmlDocPublicationDAO {

	


    /**
     * Insert a list of doc for a specified htmlDocPublication
     * @param htmlDocPublication the HtmlDocPublication to insert
     */
    void  insertHtmlsDocsId( HtmlDocPublication htmlDocPublication, Plugin plugin  );


    /**
     * Delete docs for the specified portlet
     * @param nPortletId The doc identifier
     */
    void deleteHtmlsDocsId( int nDocId, Plugin plugin  );

    /**
     * Load a list of HtmlDocPublication
     * @param nDocId
     * @return List of IdDoc
     */
    List<HtmlDocPublication> loadHtmlsDocsId( int nDocId, Plugin plugin  );
    /**
     * load a list HtmlDocPublication by the portlet id
     * @param nIdPortlet The protlet id
     * @return list of HtmlDocPublication
     */
    List<HtmlDocPublication> loadHtmlsDocsByPortlet( int nIdPortlet, Plugin plugin  );
    /**
     * Delete the HtmlDocPublication by portlet id
     * @param nIdPortlet The portlet id
     */
    void deleteHtmlsDocByIdPortlet( int nIdPortlet, Plugin plugin  );
    /**
     * load a HtmlDocPublication by  htmldoc id and portlet id
     * @param nDocId The htmldocs id
     * @param nPortletId The portlet id
     * @return HtmlDocPublication
     */
    HtmlDocPublication loadHtmlsDocsPublication( int nDocId, int nPortletId, Plugin plugin  );
    /**
     * Update an instance of the HtmlDocPublication
     * @param htmlDocPub
     */
    void store( HtmlDocPublication htmlDocPublication, Plugin plugin );
    /**
     * Remove HtmlDocPublication by primary kley
     * @param nDocId
     * @param nIdPortlet
     * @param plugin
     */
    void remove( int nDocId, int nIdPortlet, Plugin plugin  );
    /**
     * Load all  HtmlDocPublication
     * @param plugin
     * @return The list of HtmlDocPublication 
     */
    List<HtmlDocPublication>  loadAllHtmlsDocsPublication( Plugin plugin  );
    /**
     * Find the list of {@link DocumentPublication} objects specified the status and published at or after the specified date
     * @param datePublishing The publication end date
     * @param dateEndPublishing The publication date
     * @param nStatus The status
     * @return The {@link DocumentPublication} objects {@link Collection} ordered by documentOrder ascending. The list is empty if no objects found.
     */
    Collection<HtmlDocPublication> selectSinceDatePublishingAndStatus( Date datePublishing, Date dateEndPublishing, int nStatus, Plugin plugin );
    /**
     * Get the list of id of published htmldocs associated with a given
     * collection of portlets.
     * @param nPortletsIds The list of portlet ids.
     * @param datePublishing TODO
     * @param datePublishing The publication end date
     * @param plugin The document plugin
     * @return The list of documents id.
     */
    List<Integer> getPublishedDocumentsIdsListByPortletIds( int[] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin );
}
