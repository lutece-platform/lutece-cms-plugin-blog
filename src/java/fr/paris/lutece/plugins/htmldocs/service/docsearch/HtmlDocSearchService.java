
package fr.paris.lutece.plugins.htmldocs.service.docsearch;

import fr.paris.lutece.plugins.htmldocs.business.HtmldocSearchFilter;
import fr.paris.lutece.plugins.htmldocs.business.IndexerAction;
import fr.paris.lutece.plugins.htmldocs.business.IndexerActionFilter;
import fr.paris.lutece.plugins.htmldocs.business.IndexerActionHome;
import fr.paris.lutece.plugins.htmldocs.service.HtmldocsPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.search.SearchResult;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.search.IndexSearcher;

//import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * AnnounceSearchService
 */
public final class HtmlDocSearchService
{
    private static final String BEAN_SEARCH_ENGINE = "htmldocs.htmldocSearchEngine";
    private static final String PATH_INDEX = "htmldocs.internalIndexer.lucene.indexPath";
    private static final String PROPERTY_WRITER_MERGE_FACTOR = "htmldocs.internalIndexer.lucene.writer.mergeFactor";
    private static final String PROPERTY_WRITER_MAX_FIELD_LENGTH = "htmldocs.internalIndexer.lucene.writer.maxSectorLength";
    private static final String PROPERTY_ANALYSER_CLASS_NAME = "htmldocs.internalIndexer.lucene.analyser.className";

   

    // Default values
    private static final int DEFAULT_WRITER_MERGE_FACTOR = 20;
    private static final int DEFAULT_WRITER_MAX_FIELD_LENGTH = 1000000;

    // Constants corresponding to the variables defined in the lutece.properties file
    private static volatile HtmlDocSearchService _singleton;
    private volatile String _strIndex;
    private Analyzer _analyzer;
    private IHtmldocsSearchIndexer _indexer;
    private int _nWriterMergeFactor;
    private int _nWriterMaxSectorLength;

    /**
     * Creates a new instance of DirectorySearchService
     */
    private HtmlDocSearchService(  )
    {
        // Read configuration properties
        String strIndex = getIndex(  );

        if ( StringUtils.isEmpty( strIndex ) )
        {
            throw new AppException( "Lucene index path not found in htmldocs.properties", null );
        }

        _nWriterMergeFactor = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MERGE_FACTOR,
                DEFAULT_WRITER_MERGE_FACTOR );
        _nWriterMaxSectorLength = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MAX_FIELD_LENGTH,
                DEFAULT_WRITER_MAX_FIELD_LENGTH );

        String strAnalyserClassName = AppPropertiesService.getProperty( PROPERTY_ANALYSER_CLASS_NAME );

        if ( ( strAnalyserClassName == null ) || ( strAnalyserClassName.equals( "" ) ) )
        {
            throw new AppException( "Analyser class name not found in htmldocs.properties", null );
        }

        _indexer = (IHtmldocsSearchIndexer) SpringContextService.getBean( "htmldocs.htmldocIndexer" );

        try
        {
            _analyzer = (Analyzer) Class.forName( strAnalyserClassName ).newInstance(  );
        }
        catch ( Exception e )
        {
            throw new AppException( "Failed to load Lucene Analyzer class", e );
        }
    }

    /**
     * Get the HelpdeskSearchService instance
     * @return The {@link HtmlDocSearchService}
     */
    public static HtmlDocSearchService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new HtmlDocSearchService(  );
        }

        return _singleton;
    }

    /**
     * Return search results
     * @param filter The search filter
     * @param nPageNumber The current page
     * @param nItemsPerPage The number of items per page to get
     * @param listIdAnnounces Results as a collection of id of announces
     * @return The total number of items found
     */
    public int getSearchResults( HtmldocSearchFilter filter, 
        List<Integer> listIdHtmldoc )
    {
        int nNbItems = 0;

        try
        {
            IHtmldocSearchEngine engine = SpringContextService.getBean( BEAN_SEARCH_ENGINE );
            List<SearchResult> listResults = new ArrayList<SearchResult>(  );
            nNbItems = engine.getSearchResults( filter, PluginService.getPlugin( HtmldocsPlugin.PLUGIN_NAME ),
                    listResults);

            for ( SearchResult searchResult : listResults )
            {
                if ( searchResult.getId(  ) != null )
                {
                	listIdHtmldoc.add( Integer.parseInt( searchResult.getId(  ) ) );
                }
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
            // If an error occurred clean result list
            listIdHtmldoc.clear(  );
        }

        return nNbItems;
    }

   
    /**
     * return searcher
     * @return searcher
     */
    public IndexSearcher getSearcher(  )
    {
        IndexReader dir = null;
        IndexSearcher searcher = null;

        try
        {      	  
            IndexReader ir = DirectoryReader.open( FSDirectory.open( Paths.get( getIndex(  ) ) ) );
            searcher = new IndexSearcher( ir );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );

            if ( dir != null )
            {
                try
                {
                    dir.close(  );
                }
                catch ( IOException e1 )
                {
                    AppLogService.error( e1.getMessage(  ), e );
                }
            }
        }

        return searcher;
    }

    /**
     * Process indexing
     * @param bCreate true for start full indexing
     *            false for begin incremental indexing
     * @return the log
     */
    public String processIndexing( boolean bCreate )
    {
        StringBuffer sbLogs = new StringBuffer(  );
        IndexWriter writer = null;
        boolean bCreateIndex = bCreate;

        try
        {
            sbLogs.append( "\r\nIndexing all contents ...\r\n" );

            Directory dir = FSDirectory.open( Paths.get( getIndex(  ) ) );

            //Nouveau
            if ( !DirectoryReader.indexExists( dir ) )
            { //init index
                bCreateIndex = true;
            }

             Date start = new Date(  );

             IndexWriterConfig conf = new IndexWriterConfig(  _analyzer ) ;
             LogMergePolicy mergePolicy = new LogDocMergePolicy(  );
             mergePolicy.setMergeFactor( _nWriterMergeFactor );
             conf.setMergePolicy( mergePolicy );

             if ( bCreateIndex )
             {
                 conf.setOpenMode( OpenMode.CREATE );
             }
             else
             {
                conf.setOpenMode( OpenMode.APPEND );
             }
              writer = new IndexWriter( dir, conf );

              start = new Date(  );

              sbLogs.append( "\r\n<strong>Indexer : " );
              sbLogs.append( _indexer.getName(  ) );
              sbLogs.append( " - " );
              sbLogs.append( _indexer.getDescription(  ) );
              sbLogs.append( "</strong>\r\n" );
              _indexer.processIndexing( writer, bCreateIndex, sbLogs );

              Date end = new Date(  );

              sbLogs.append( "Duration of the treatment : " );
              sbLogs.append( end.getTime(  ) - start.getTime(  ) );
              sbLogs.append( " milliseconds\r\n" );
            
        }
        catch ( Exception e )
        {
            sbLogs.append( " caught a " );
            sbLogs.append( e.getClass(  ) );
            sbLogs.append( "\n with message: " );
            sbLogs.append( e.getMessage(  ) );
            sbLogs.append( "\r\n" );
            AppLogService.error( "Indexing error : " + e.getMessage(  ), e );
        }
        finally
        {
            try
            {
                if ( writer != null )
                {
                    writer.close(  );
                }
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        return sbLogs.toString(  );
    }

    /**
     * Add Indexer Action to perform on a record
     * @param nIdHtmldoc htmldoc id
     * @param nIdTask the key of the action to do
     * @param plugin the plugin
     */
    public void addIndexerAction( int nIdHtmldoc, int nIdTask, Plugin plugin )
    {
        IndexerAction indexerAction = new IndexerAction(  );
        indexerAction.setIdHtmldoc( nIdHtmldoc );
        indexerAction.setIdTask( nIdTask );
        IndexerActionHome.create( indexerAction );
    }

    /**
     * Remove a Indexer Action
     * @param nIdAction the key of the action to remove
     * @param plugin the plugin
     */
    public void removeIndexerAction( int nIdAction, Plugin plugin )
    {
        IndexerActionHome.remove( nIdAction );
    }

    /**
     * return a list of IndexerAction by task key
     * @param nIdTask the task key
     * @param plugin the plugin
     * @return a list of IndexerAction
     */
    public List<IndexerAction> getAllIndexerActionByTask( int nIdTask, Plugin plugin )
    {
        IndexerActionFilter filter = new IndexerActionFilter(  );
        filter.setIdTask( nIdTask );

        return IndexerActionHome.getList( filter );
    }

    /**
     * Get the path to the index of the search service
     * @return The path to the index of the search service
     */
    private String getIndex(  )
    {
        if ( _strIndex == null )
        {
            _strIndex = AppPathService.getPath( PATH_INDEX );
        }

        return _strIndex;
    }

    /**
     * Get the analyzed of this search service
     * @return The analyzer of this search service
     */
    public Analyzer getAnalyzer(  )
    {
        return _analyzer;
    }

    
}
