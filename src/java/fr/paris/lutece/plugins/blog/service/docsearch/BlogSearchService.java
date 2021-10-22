/*
 * Copyright (c) 2002-2021, City of Paris
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
import fr.paris.lutece.portal.service.search.LuceneSearchEngine;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.search.SearchResult;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;

/**
 * Blog Search Service
 */
public final class BlogSearchService
{
    private static final String PROPERTY_WRITER_MERGE_FACTOR = "blog.internalIndexer.lucene.writer.mergeFactor";
    private static final String PROPERTY_ANALYSER_CLASS_NAME = "blog.internalIndexer.lucene.analyser.className";
    private static final String PATH_INDEX = "blog.internalIndexer.lucene.indexPath";
    private volatile String _strIndex;
    private static final String WILDCARD = "*";

    // Default values
    private static final int DEFAULT_WRITER_MERGE_FACTOR = 20;

    // Constants corresponding to the variables defined in the lutece.properties file
    private static BlogSearchService _singleton;

    private Analyzer _analyzer;
    private IBlogSearchIndexer _indexer;
    private int _nWriterMergeFactor;

    /**
     * Creates a new instance of DirectorySearchService
     */
    private BlogSearchService( )
    {
        _strIndex = AppPathService.getPath( PATH_INDEX );
        if ( _strIndex == null )
        {
            throw new AppException( "Index path not defined. Property : blog.internalIndexer.lucene.indexPath in blogs.properties" );
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
        catch( ClassNotFoundException | IllegalAccessException | InstantiationException e )
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
     * @param listIdBlog
     *            Results as a collection of id of blog posts
     * @return The total number of items found
     */
    public int getSearchResults( BlogSearchFilter filter, List<Integer> listIdBlog )
    {
        int nNbItems = 0;

        try
        {
            List<SearchResult> listResults = new ArrayList<>( );
            nNbItems = getSearchResultsByFilter( filter, listResults );

            for ( SearchResult searchResult : listResults )
            {
                if ( searchResult.getId( ) != null )
                {
                    listIdBlog.add( Integer.parseInt( searchResult.getId( ) ) );
                }
            }
        }
        catch( NumberFormatException e )
        {
            AppLogService.error( e.getMessage( ), e );
            // If an error occurred clean result list
            listIdBlog.clear( );
        }

        return nNbItems;
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
        StringBuilder sbLogs = new StringBuilder( );
        IndexWriter writer = null;
        boolean bCreateIndex = bCreate;

        try
        {
            sbLogs.append( "\r\nIndexing all contents ...\r\n" );

            Directory dir = FSDirectory.open( Paths.get( _strIndex ) );

            // Nouveau
            if ( !DirectoryReader.indexExists( dir ) )
            { // init index
                bCreateIndex = true;
            }

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

            Date start = new Date( );

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
     * Get search results
     * 
     * @param filter
     *            The filter
     * @param listSearchResult
     *            The list of results
     * @return The result count
     */
    private int getSearchResultsByFilter( BlogSearchFilter filter, List<SearchResult> listSearchResult )
    {
        ArrayList<SearchItem> listResults = new ArrayList<>( );

        int nNbResults = 0;
        try ( Directory dir = FSDirectory.open( Paths.get( _strIndex ) ) ; DirectoryReader reader = DirectoryReader.open( dir ) ; )
        {
            IndexSearcher searcher = new IndexSearcher( reader );

            Query queryMulti = prepareQueryForFilter( filter );

            Sort sorter = new Sort( );
            String field = BlogSearchItem.FIELD_DATE_UPDATE;
            SortField.Type type = SortField.Type.LONG;
            boolean descending = true;

            SortField sortField = new SortField( field, type, descending );

            sorter.setSort( sortField );

            TopDocs topDocs = searcher.search( queryMulti, LuceneSearchEngine.MAX_RESPONSES, sorter );
            ScoreDoc [ ] hits = topDocs.scoreDocs;
            nNbResults = hits.length;

            for ( int i = 0; i < nNbResults; i++ )
            {
                int docId = hits [i].doc;
                Document document = searcher.doc( docId );
                SearchItem si = new SearchItem( document );
                listResults.add( si );
            }
            searcher.getIndexReader( ).close( );
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        convertList( listResults, listSearchResult );
        return nNbResults;
    }

    private Query prepareQueryForFilter( BlogSearchFilter filter ) throws org.apache.lucene.queryparser.classic.ParseException
    {
        boolean bDateAfter = false;
        boolean bDateBefore = false;
        Collection<String> queries = new ArrayList<>( );
        Collection<String> sectors = new ArrayList<>( );
        Collection<BooleanClause.Occur> flags = new ArrayList<>( );

        if ( filter.getKeywords( ) != null && StringUtils.isNotBlank( filter.getKeywords( ) ) )
        {
            Term term = new Term( SearchItem.FIELD_CONTENTS, filter.getKeywords( ) );
            Query termQuery = new TermQuery( term );
            queries.add( termQuery.toString( ) );
            sectors.add( SearchItem.FIELD_CONTENTS );
            flags.add( BooleanClause.Occur.MUST );

        }
        if ( filter.getTag( ) != null )
        {
            for ( String tag : filter.getTag( ) )
            {
                Term term = new Term( BlogSearchItem.FIELD_TAGS, tag );
                Query termQuery = new TermQuery( term );
                queries.add( termQuery.toString( ) );
                sectors.add( BlogSearchItem.FIELD_TAGS );
                flags.add( BooleanClause.Occur.MUST );
            }

        }
        if ( filter.getUser( ) != null )
        {
            Term term = new Term( BlogSearchItem.FIELD_USER, filter.getUser( ) + WILDCARD );
            Query termQuery = new TermQuery( term );
            queries.add( termQuery.toString( ) );
            sectors.add( BlogSearchItem.FIELD_USER );
            flags.add( BooleanClause.Occur.MUST );

        }
        if ( filter.getUserEditedBlogVersion( ) != null )
        {
            Term term = new Term( BlogSearchItem.FIELD_USERS_EDITED_BLOG, filter.getUserEditedBlogVersion( ) );
            Query termQuery = new TermQuery( term );
            queries.add( termQuery.toString( ) );
            sectors.add( BlogSearchItem.FIELD_USERS_EDITED_BLOG );
            flags.add( BooleanClause.Occur.MUST );

        }

        if ( filter.getUpdateDateAfter( ) != null || filter.getUpdateDateBefor( ) != null )
        {
            BytesRef strAfter = null;
            BytesRef strBefore = null;

            if ( filter.getUpdateDateAfter( ) != null )
            {
                strAfter = new BytesRef( DateTools.dateToString( filter.getUpdateDateAfter( ), DateTools.Resolution.MINUTE ) );
                bDateAfter = true;
            }

            if ( filter.getUpdateDateBefor( ) != null )
            {
                Date dateBefore = filter.getUpdateDateBefor( );
                strBefore = new BytesRef( DateTools.dateToString( dateBefore, DateTools.Resolution.MINUTE ) );
                bDateBefore = true;
            }

            Query queryDate = new TermRangeQuery( SearchItem.FIELD_DATE, strAfter, strBefore, bDateAfter, bDateBefore );
            queries.add( queryDate.toString( ) );
            sectors.add( SearchItem.FIELD_DATE );
            flags.add( BooleanClause.Occur.MUST );
        }

        if ( filter.getIsUnpulished( ) )
        {
            Term term = new Term( BlogSearchItem.FIELD_UNPUBLISHED, String.valueOf( filter.getIsUnpulished( ) ) );
            Query termQuery = new TermQuery( term );
            queries.add( termQuery.toString( ) );
            sectors.add( BlogSearchItem.FIELD_UNPUBLISHED );
            flags.add( BooleanClause.Occur.MUST );

        }
        Term term = new Term( SearchItem.FIELD_TYPE, BlogPlugin.PLUGIN_NAME );
        Query termQuery = new TermQuery( term );
        queries.add( termQuery.toString( ) );
        sectors.add( SearchItem.FIELD_TYPE );
        flags.add( BooleanClause.Occur.MUST );

        return MultiFieldQueryParser.parse( queries.toArray( new String [ queries.size( )] ), sectors.toArray( new String [ sectors.size( )] ),
                flags.toArray( new BooleanClause.Occur [ flags.size( )] ), _analyzer );
    }

    /**
     * Add Indexer Action to perform on a record
     * 
     * @param nIdBlog
     *            Blog id
     * @param nIdTask
     *            the key of the action to do
     */
    public void addIndexerAction( int nIdBlog, int nIdTask )
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
     */
    public void removeIndexerAction( int nIdAction )
    {
        IndexerActionHome.remove( nIdAction );
    }

    /**
     * return a list of IndexerAction by task key
     * 
     * @param nIdTask
     *            the task key
     * @return a list of IndexerAction
     */
    public List<IndexerAction> getAllIndexerActionByTask( int nIdTask )
    {
        IndexerActionFilter filter = new IndexerActionFilter( );
        filter.setIdTask( nIdTask );

        return IndexerActionHome.getList( filter );
    }

    /**
     * Convert the SearchItem list on SearchResult list
     * 
     * @param listSource
     *            The source list
     * @param listSearchResult
     *            The result list
     */
    private void convertList( List<SearchItem> listSource, List<SearchResult> listSearchResult )
    {
        for ( SearchItem item : listSource )
        {
            SearchResult result = new SearchResult( );
            result.setId( item.getId( ) );

            try
            {
                result.setDate( DateTools.stringToDate( item.getDate( ) ) );
            }
            catch( ParseException e )
            {
                AppLogService.error( "Bad Date Format for indexed item \"" + item.getTitle( ) + "\" : " + e.getMessage( ) );
            }

            result.setUrl( item.getUrl( ) );
            result.setTitle( item.getTitle( ) );
            result.setSummary( item.getSummary( ) );
            result.setType( item.getType( ) );
            listSearchResult.add( result );
        }
    }

}
