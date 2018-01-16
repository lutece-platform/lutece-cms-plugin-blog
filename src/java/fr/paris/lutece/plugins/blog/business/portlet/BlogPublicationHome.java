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
     * @param BlogPub
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
     * load a BlogPublication by Blog id and portlet id
     * 
     * @param nDocId
     *            The Blogs id
     * @param nPortletId
     *            The portlet id
     * @return BlogPublication
     */
    public static BlogPublication findDocPublicationByPimaryKey( int nDocId, int nPortletId )
    {
        return _dao.loadBlogsPublication( nDocId, nPortletId, _plugin );

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
