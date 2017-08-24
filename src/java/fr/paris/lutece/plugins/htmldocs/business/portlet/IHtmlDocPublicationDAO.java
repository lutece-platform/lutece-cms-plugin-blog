package fr.paris.lutece.plugins.htmldocs.business.portlet;

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
    List<HtmlDocPublication> loadHtmlsDocsByPortlet( int nIdPortlet, Plugin plugin  );

    void deleteHtmlsDocByIdPortlet( int nIdPortlet, Plugin plugin  );
	
    HtmlDocPublication loadHtmlsDocsPublication( int nDocId, int nPortletId, Plugin plugin  );
    
    void store( HtmlDocPublication htmlDocPublication, Plugin plugin );
    void remove( int nDocId, int nIdPortlet, Plugin plugin  );
    List<HtmlDocPublication>  loadAllHtmlsDocsPublication( Plugin plugin  );

}
