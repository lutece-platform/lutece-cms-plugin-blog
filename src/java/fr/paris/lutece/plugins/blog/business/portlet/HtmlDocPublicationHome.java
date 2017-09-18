package fr.paris.lutece.plugins.blog.business.portlet;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.paris.lutece.plugins.blog.service.HtmldocsPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class HtmlDocPublicationHome
{

    private static IHtmlDocPublicationDAO _dao = SpringContextService.getBean( "blog.htmlDocPublicationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( HtmldocsPlugin.PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private HtmlDocPublicationHome( )
    {
    }

    /**
     * Create an instance of the HtmlDocPublication class
     * 
     * @param htmlDocPub
     *            The instance of the HtmlDocPublication which contains the informations to store
     * @return The instance of htmlDocPub which has been created.
     */
    public static HtmlDocPublication create( HtmlDocPublication htmlDocPub )
    {
        _dao.insertHtmlsDocsId( htmlDocPub, _plugin );

        return htmlDocPub;
    }

    /**
     * Update an instance of the HtmlDocPublication
     * 
     * @param htmlDocPub
     */
    public static void update( HtmlDocPublication htmlDocPub )
    {
        _dao.store( htmlDocPub, _plugin );

    }

    /**
     * load a list HtmlDocPublication by the htmldoc identifiant
     * 
     * @param nDocId
     *            The htmldoc id
     * @return list of HtmlDocPublication
     */
    public static List<HtmlDocPublication> getDocPublicationByIdDoc( int nDocId )
    {
        return _dao.loadHtmlsDocsId( nDocId, _plugin );

    }

    /**
     * load a list HtmlDocPublication by the portlet id
     * 
     * @param nIdPortlet
     *            The protlet id
     * @return list of HtmlDocPublication
     */
    public static List<HtmlDocPublication> getDocPublicationByPortlet( int nIdPortlet )
    {
        return _dao.loadHtmlsDocsByPortlet( nIdPortlet, _plugin );

    }

    /**
     * load a HtmlDocPublication by htmldoc id and portlet id
     * 
     * @param nDocId
     *            The htmldocs id
     * @param nPortletId
     *            The portlet id
     * @return HtmlDocPublication
     */
    public static HtmlDocPublication findDocPublicationByPimaryKey( int nDocId, int nPortletId )
    {
        return _dao.loadHtmlsDocsPublication( nDocId, nPortletId, _plugin );

    }

    /**
     * Delete the HtmlDocPublication by portlet id
     * 
     * @param nIdPortlet
     *            The portlet id
     */
    public static void removeByIdPortlet( int nIdPortlet )
    {
        _dao.deleteHtmlsDocByIdPortlet( nIdPortlet, _plugin );

    }

    /**
     * Delete the HtmlDocPublication by the primary key
     * 
     * @param nIdDoc
     *            The htmldoc id
     * @param nIdPortlet
     *            The portlet id
     */
    public static void remove( int nIdDoc, int nIdPortlet )
    {
        _dao.remove( nIdDoc, nIdPortlet, _plugin );

    }

    /**
     * Load all HtmlDocPublication
     * 
     * @return list HtmlDocPublication
     */
    public static List<HtmlDocPublication> getAllDocPublication( )
    {
        return _dao.loadAllHtmlsDocsPublication( _plugin );

    }

    /**
     * Find the list of {@link DocumentPublication} objects specified the status and published at or after the specified date
     * 
     * @param datePublishing
     *            The publication date
     * @param dateEndPublishing
     *            The end publication date
     * @param nStatus
     *            The status
     * @return The {@link DocumentPublication} objects {@link Collection} ordered by documentOrder ascending. The list is empty if no objects found.
     */
    public static Collection<HtmlDocPublication> findSinceDatePublishingAndStatus( Date datePublishing, Date dateEndPublishing, int nStatus )
    {
        return _dao.selectSinceDatePublishingAndStatus( datePublishing, dateEndPublishing, nStatus, _plugin );
    }

    /**
     * Get the list of id of published htmldocs associated with a given collection of portlets.
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param datePublishing
     *            TODO
     * @param dateEndPublishing
     *            The end publication date
     * @param plugin
     *            The document plugin
     * @return The list of documents id.
     */
    public static List<Integer> getPublishedDocumentsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        return _dao.getPublishedDocumentsIdsListByPortletIds( nPortletsIds, datePublishing, dateEndPublishing, plugin );
    }

}
