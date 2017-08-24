package fr.paris.lutece.plugins.htmldocs.business.portlet;


import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class HtmlDocPublicationHome  {

	

    private static IHtmlDocPublicationDAO _dao = SpringContextService.getBean( "htmldocs.htmlDocPublicationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "htmldocs" );



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
    
    public static  void update( HtmlDocPublication htmlDocPub )
    {
        _dao.store( htmlDocPub, _plugin );

    }
    public static List<HtmlDocPublication> getDocPublicationByIdDoc( int nDocId )
    {
       return  _dao.loadHtmlsDocsId(nDocId,  _plugin );

    }
    public static List<HtmlDocPublication> getDocPublicationByPortlet( int nIdPortlet )
    {
       return  _dao.loadHtmlsDocsByPortlet(nIdPortlet,  _plugin );

    }
    
    public static HtmlDocPublication findDocPublicationByPimaryKey( int nDocId, int nPortletId )
    {
       return  _dao.loadHtmlsDocsPublication(nDocId, nPortletId,  _plugin );

    }
    
    public static void removeByIdPortlet( int nIdPortlet )
    {
         _dao.deleteHtmlsDocByIdPortlet(nIdPortlet,  _plugin );

    }
    
    public static void remove( int nIdDoc, int nIdPortlet )
    {
         _dao.remove(nIdDoc, nIdPortlet,  _plugin );

    }
    
    public static List<HtmlDocPublication> getAllDocPublication(  )
    {
       return  _dao.loadAllHtmlsDocsPublication(  _plugin );

    }
	    
}
