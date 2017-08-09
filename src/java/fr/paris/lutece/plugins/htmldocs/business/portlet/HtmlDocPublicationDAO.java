package fr.paris.lutece.plugins.htmldocs.business.portlet;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class HtmlDocPublicationDAO implements IHtmlDocPublicationDAO {

	

    //Category
    private static final String SQL_QUERY_INSERT_HTMLDOCS_PORTLET = "INSERT INTO htmldocs_list_portlet_htmldocs ( id_portlet , id_html_doc, date_begin_publishing, date_end_publishing, status ) VALUES ( ? , ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE_HTMLDOCS_PORTLET = " DELETE FROM htmldocs_list_portlet_htmldocs WHERE id_html_doc = ? ";
    private static final String SQL_QUERY_DELETE_HTMLDOCS_PORTLET_BY_ID_PORTLET = " DELETE FROM htmldocs_list_portlet_htmldocs WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_CATEGORY_PORTLET = "SELECT id_portlet , id_html_doc, date_begin_publishing, date_end_publishing, status FROM htmldocs_list_portlet_htmldocs WHERE id_html_doc = ? ";
    private static final String SQL_QUERY_REMOVE_HTMLDOCS_PORTLET = " DELETE FROM htmldocs_list_portlet_htmldocs WHERE id_portlet = ? and id_html_doc= ?";

    
    private static final String SQL_QUERY_UPDATE_HTMLDOCS_PORTLET = "UPDATE htmldocs_list_portlet_htmldocs set id_portlet= ?, id_html_doc= ?, date_begin_publishing= ?, date_end_publishing= ?, status= ? WHERE  id_html_doc= ?";
    private static final String SQL_QUERY_SELECT_PUBLICATION_PORTLET = "SELECT id_portlet , id_html_doc, date_begin_publishing, date_end_publishing, status FROM htmldocs_list_portlet_htmldocs WHERE id_html_doc = ? and id_portlet = ? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
   

    /**
     * Insert a list of doc for a specified htmlDocPublication
     * @param htmlDocPublication the HtmlDocPublication to insert
     */
    public void insertHtmlsDocsId( HtmlDocPublication htmlDocPublication, Plugin plugin  )
    {
    	
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_HTMLDOCS_PORTLET , plugin);

            
            daoUtil.setInt( 1, htmlDocPublication.getIdPortlet( ) );
            daoUtil.setInt( 2, htmlDocPublication.getIdDocument( ) );
            daoUtil.setDate(3, htmlDocPublication.getDateBeginPublishing( ));
            daoUtil.setDate(4, htmlDocPublication.getDateEndPublishing( ));
            daoUtil.setInt( 5, htmlDocPublication.getStatus( ) );
            
            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        
    }

    /**
     * Update the record in the table
     *
     * @param portlet A portlet
     */
    public void store( HtmlDocPublication htmlDocPublication, Plugin plugin )
    {
    	
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_HTMLDOCS_PORTLET );
        daoUtil.setInt( 1, htmlDocPublication.getIdPortlet( ) );
        daoUtil.setInt( 2, htmlDocPublication.getIdDocument( ) );
        daoUtil.setDate(3, htmlDocPublication.getDateBeginPublishing( ));
        daoUtil.setDate(4, htmlDocPublication.getDateEndPublishing( ));
        daoUtil.setInt( 5, htmlDocPublication.getStatus( ) );
        daoUtil.setInt( 6, htmlDocPublication.getIdDocument( ) );


        daoUtil.executeUpdate(  );

        daoUtil.free(  );

    }

    /**
     * Delete docs for the specified portlet
     * @param nDocId The portlet identifier
     */
    public void deleteHtmlsDocsId( int nDocId, Plugin plugin  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_HTMLDOCS_PORTLET, plugin );
        daoUtil.setInt( 1, nDocId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete docs for the specified portlet
     * @param nIdPortlet The portlet identifier
     */
    public void deleteHtmlsDocByIdPortlet( int nIdPortlet, Plugin plugin  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_HTMLDOCS_PORTLET_BY_ID_PORTLET, plugin );
        daoUtil.setInt( 1, nIdPortlet );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
    
    public void remove( int nDocId, int nIdPortlet, Plugin plugin  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_HTMLDOCS_PORTLET, plugin );
        daoUtil.setInt( 1, nIdPortlet );
        daoUtil.setInt( 2, nDocId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
  
    
   

    /**
     * Load a list of HtmlDocPublication
     * @param nDocId
     * @return List of IdDoc
     */
    public List<HtmlDocPublication> loadHtmlsDocsId( int nDocId, Plugin plugin  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CATEGORY_PORTLET, plugin );
        daoUtil.setInt( 1, nDocId );
        daoUtil.executeQuery(  );

        List<HtmlDocPublication> nListIdCategory = new ArrayList<HtmlDocPublication>(  );

        while ( daoUtil.next(  ) )
        {
        	
        	HtmlDocPublication htmldocPub= new HtmlDocPublication();
        	htmldocPub.setIdPortlet(daoUtil.getInt( 1 ));
        	htmldocPub.setIdDocument(daoUtil.getInt( 2 ));
        	htmldocPub.setDateBeginPublishing(daoUtil.getDate( 3 ));
        	htmldocPub.setDateEndPublishing(daoUtil.getDate( 4 ));
        	htmldocPub.setStatus(daoUtil.getInt( 5 ));
        	
        	nListIdCategory.add( htmldocPub );
        }

        daoUtil.free(  );

        return nListIdCategory;
    }
    
    /**
     * Load a  HtmlDocPublication
     * @param nDocId
     * @return IdDoc
     */
    public HtmlDocPublication loadHtmlsDocsPublication( int nDocId, int nPortletId, Plugin plugin  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLICATION_PORTLET, plugin );
        daoUtil.setInt( 1, nDocId );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

    	HtmlDocPublication htmldocPub= new HtmlDocPublication();

        if ( daoUtil.next(  ) )
        {
        	
        	htmldocPub.setIdPortlet(daoUtil.getInt( 1 ));
        	htmldocPub.setIdDocument(daoUtil.getInt( 2 ));
        	htmldocPub.setDateBeginPublishing(daoUtil.getDate( 3 ));
        	htmldocPub.setDateEndPublishing(daoUtil.getDate( 4 ));
        	htmldocPub.setStatus(daoUtil.getInt( 5 ));
        	
        	
        }

        daoUtil.free(  );

        return htmldocPub;
    }

    
  
	    
}
