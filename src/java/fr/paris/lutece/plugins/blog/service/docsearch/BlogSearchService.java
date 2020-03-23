/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.blog.service.docsearch;

import fr.paris.lutece.plugins.blog.business.BlogSearchFilter;
import fr.paris.lutece.plugins.blog.business.IndexerAction;
import fr.paris.lutece.plugins.blog.business.IndexerActionFilter;
import fr.paris.lutece.plugins.blog.business.IndexerActionHome;
import fr.paris.lutece.plugins.blog.service.BlogPlugin;
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
public final class BlogSearchService
{
    private static final String BEAN_SEARCH_ENGINE = "blog.blogSearchEngine";
    private static final String PATH_INDEX = "blog.internalIndexer.lucene.indexPath";
    private static final String PROPERTY_WRITER_MERGE_FACTOR = "blog.internalIndexer.lucene.writer.mergeFactor";
    private static final String PROPERTY_ANALYSER_CLASS_NAME = "blog.internalIndexer.lucene.analyser.className";

    // Default values
    private static final int DEFAULT_WRITER_MERGE_FACTOR = 20;

    // Constants corresponding to the variables defined in the lutece.properties file
    private static volatile BlogSearchService _singleton;
    private volatile String _strIndex;
    private Analyzer _analyzer;
    private IBlogSearchIndexer _indexer;
    private int _nWriterMergeFactor;

    /**
     * Creates a new instance of DirectorySearchService
     */
    private BlogSearchService( )
    {
        // Read configuration properties
        String strIndex = getIndex( );

        if ( StringUtils.isEmpty( strIndex ) )
        {
            throw new AppException( "Lucene index path not found in blog.properties", null );
        }

        _nWriterMergeFactor = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MERGE_FACTOR, DEFAULT_WRITER_MERGE_FACTOR );

        String strAnalyserClassName = AppPropertiesService.getProperty( PROPERTY_ANALYSER_CLASS_NAME );

        if ( ( strAnalyserClassName == null ) || ( strAnalyserClassName.equals( "" ) ) )
        {
            throw new AppException( "Analyser class name not found in blogs.properties", null );
        }

        _indexer = SpringContextService.getBean( "blog.blogIndexer" );

        try
        {
            _analyzer = (Analyzer) Class.forName( strAnalyserClassName ).newInstance( );
        }
        catch( Exception e )
        {
            throw new AppException( "Failed to load Lucene Analyzer class", e );
        }
    }

    /**
     * Get the HelpdeskSearchService instance
     * 
     * @return The {@link BlogSearchService}
     */
    public static BlogSearchService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new BlogSearchService( );
        }

        return _singleton;
    }

    /**
     * Return search results
     * 
     * @param filter
     *            The search filter
     * @param nPageNumber
     *            The current page
     * @param nItemsPerPage
     *            The number of items per page to get
     * @param listIdAnnounces
     *            Results as a collection of id of announces
     * @return The total number of items found
     */
    public int getSearchResults( BlogSearchFilter filter, List<Integer> listIdBlog )
    {
        int nNbItems = 0;

        try
        {
            IBlogSearchEngine engine = SpringContextService.getBean( BEAN_SEARCH_ENGINE );
            List<SearchResult> listResults = new ArrayList<>( );
            nNbItems = engine.getSearchResults( filter, PluginService.getPlugin( BlogPlugin.PLUGIN_NAME ), listResults );

            for ( SearchResult searchResult : listResults )
            {
                if ( searchResult.getId( ) != null )
                {
                    listIdBlog.add( Integer.parseInt( searchResult.getId( ) ) );
                }
            }
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            // If an error occurred clean result list
            listIdBlog.clear( );
        }

        return nNbItems;
    }

    /**
     * return searcher
     * 
     * @return searcher
     */
    public IndexSearcher getSearcher( )
    {
        IndexSearcher searcher = null;

        try
        {
            IndexReader ir = DirectoryReader.open( FSDirectory.open( Paths.get( getIndex( ) ) ) );
            searcher = new IndexSearcher( ir );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        return searcher;
    }

    /**
     * Process indexing
     * 
     * @param bCreate
     *            true for start full indexing false for begin incremental indexing
     * @return the log
     */
    public String processIndexing( boolean bCreate )
    {
        StringBuffer sbLogs = new StringBuffer( );
        IndexWriter writer = null;
        boolean bCreateIndex = bCreate;

        try
        {
            sbLogs.append( "\r\nIndexing all contents ...\r\n" );

            Directory dir = FSDirectory.open( Paths.get( getIndex( ) ) );

            // Nouveau
            if ( !DirectoryReader.indexExists( dir ) )
            { // init index
                bCreateIndex = true;
            }

            Date start = new Date( );

            IndexWriterConfig conf = new IndexWriterConfig( _analyzer );
            LogMergePolicy mergePolicy = new LogDocMergePolicy( );
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

            start = new Date( );

            sbLogs.append( "\r\n<strong>Indexer : " );
            sbLogs.append( _indexer.getName( ) );
            sbLogs.append( " - " );
            sbLogs.append( _indexer.getDescription( ) );
            sbLogs.append( "</strong>\r\n" );
            _indexer.processIndexing( writer, bCreateIndex, sbLogs );

            Date end = new Date( );

            sbLogs.append( "Duration of the treatment : " );
            sbLogs.append( end.getTime( ) - start.getTime( ) );
            sbLogs.append( " milliseconds\r\n" );

        }
        catch( Exception e )
        {
            sbLogs.append( " caught a " );
            sbLogs.append( e.getClass( ) );
            sbLogs.append( "\n with message: " );
            sbLogs.append( e.getMessage( ) );
            sbLogs.append( "\r\n" );
            AppLogService.error( "Indexing error : " + e.getMessage( ), e );
        }
        finally
        {
            try
            {
                if ( writer != null )
                {
                    writer.close( );
                }
            }
            catch( IOException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }

        return sbLogs.toString( );
    }

    /**
     * Add Indexer Action to perform on a record
     * 
     * @param nIdBlog
     *            Blog id
     * @param nIdTask
     *            the key of the action to do
     * @param plugin
     *            the plugin
     */
    public void addIndexerAction( int nIdBlog, int nIdTask, Plugin plugin )
    {
        IndexerAction indexerAction = new IndexerAction( );
        indexerAction.setIdBlog( nIdBlog );
        indexerAction.setIdTask( nIdTask );
        IndexerActionHome.create( indexerAction );
    }

    /**
     * Remove a Indexer Action
     * 
     * @param nIdAction
     *            the key of the action to remove
     * @param plugin
     *            the plugin
     */
    public void removeIndexerAction( int nIdAction, Plugin plugin )
    {
        IndexerActionHome.remove( nIdAction );
    }

    /**
     * return a list of IndexerAction by task key
     * 
     * @param nIdTask
     *            the task key
     * @param plugin
     *            the plugin
     * @return a list of IndexerAction
     */
    public List<IndexerAction> getAllIndexerActionByTask( int nIdTask, Plugin plugin )
    {
        IndexerActionFilter filter = new IndexerActionFilter( );
        filter.setIdTask( nIdTask );

        return IndexerActionHome.getList( filter );
    }

    /**
     * Get the path to the index of the search service
     * 
     * @return The path to the index of the search service
     */
    private String getIndex( )
    {
        if ( _strIndex == null )
        {
            _strIndex = AppPathService.getPath( PATH_INDEX );
        }

        return _strIndex;
    }

    /**
     * Get the analyzed of this search service
     * 
     * @return The analyzer of this search service
     */
    public Analyzer getAnalyzer( )
    {
        return _analyzer;
    }

}
