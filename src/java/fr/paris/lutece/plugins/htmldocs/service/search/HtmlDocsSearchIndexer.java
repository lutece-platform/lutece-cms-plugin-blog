
package fr.paris.lutece.plugins.htmldocs.service.search;


import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.service.HtmlDocService;
import fr.paris.lutece.plugins.htmldocs.service.HtmldocsPlugin;
import fr.paris.lutece.plugins.htmldocs.service.docsearch.DefaultHtmldocIndexer;
import fr.paris.lutece.plugins.htmldocs.service.docsearch.HtmlDocSearchService;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.lucene.document.Document;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Directory global indexer
 */
public class HtmlDocsSearchIndexer implements SearchIndexer
{
    public static final String INDEXER_NAME = "HtmlDocsIndexer";
    public static final String SHORT_NAME = "hdoc";
    private static final String HTMLDOCS = "htmldocs";
    private static final String INDEXER_DESCRIPTION = "Indexer service for htmldocs";
    private static final String INDEXER_VERSION = "1.0.0";
    private static final String PROPERTY_INDEXER_ENABLE = "htmldocs.globalIndexer.enable";
    private static final String ROLE_NONE = "none";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return INDEXER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription( )
    {
        return INDEXER_DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion( )
    {
        return INDEXER_VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnable( )
    {
        String strEnable = AppPropertiesService.getProperty( PROPERTY_INDEXER_ENABLE );

        return ( strEnable.equalsIgnoreCase( "true" ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListType( )
    {
        List<String> listType = new ArrayList<String>( 1 );
        listType.add( HTMLDOCS );

        return listType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSpecificSearchAppUrl( )
    {
        UrlItem url = new UrlItem( AppPathService.getPortalUrl( ) );
        url.addParameter( XPageAppService.PARAM_XPAGE_APP, HTMLDOCS );

        return url.getUrl( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> getDocuments( String strDocumentId ) throws IOException, InterruptedException, SiteMessageException
    {
        Plugin plugin = HtmldocsPlugin.getPlugin();

        int documentId;

        try
        {
        	documentId = Integer.parseInt( strDocumentId );
        }
        catch( NumberFormatException ne )
        {
            AppLogService.error( strDocumentId + " not parseable to an int", ne );

            return new ArrayList<Document>( 0 );
        }

      
        HtmlDoc htmlDoc= HtmlDocService.getInstance().loadDocument(documentId);
        Document doc = DefaultHtmldocIndexer.getDocument( htmlDoc );

        if ( doc != null )
        {
            List<Document> listDocument = new ArrayList<Document>( 1 );
            listDocument.add( doc );

            return listDocument;
        }

        return new ArrayList<Document>( 0 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexDocuments( ) throws IOException, InterruptedException, SiteMessageException
    {
    	HtmlDocSearchService.getInstance(  ).processIndexing( true );
        // DefaultHtmldocIndexer.getDocuments("");
    }

  

}
