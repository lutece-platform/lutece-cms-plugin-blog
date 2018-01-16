package fr.paris.lutece.plugins.blog.service.docsearch;

import fr.paris.lutece.plugins.blog.business.BlogSearchFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.search.LuceneSearchEngine;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.search.SearchResult;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

//import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * LuceneSearchEngine
 */
public class BlogLuceneSearchEngine implements IBlogSearchEngine
{

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSearchResults( BlogSearchFilter filter, Plugin plugin, List<SearchResult> listSearchResult )
    {
        ArrayList<SearchItem> listResults = new ArrayList<SearchItem>( );
        IndexSearcher searcher;

        // Searcher searcher = null;
        int nNbResults = 0;

        try
        {
            searcher = BlogSearchService.getInstance( ).getSearcher( );

            Collection<String> queries = new ArrayList<String>( );
            Collection<String> sectors = new ArrayList<String>( );
            Collection<BooleanClause.Occur> flags = new ArrayList<BooleanClause.Occur>( );

            if ( filter.getKeywords( ) != null && StringUtils.isNotBlank( filter.getKeywords( ) ) )
            {

                Term term = new Term( BlogSearchItem.FIELD_CONTENTS, filter.getKeywords( ) );
                Query termQuery = new TermQuery( term );
                queries.add( termQuery.toString( ) );
                sectors.add( BlogSearchItem.FIELD_CONTENTS );
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

                Term term = new Term( BlogSearchItem.FIELD_USER, filter.getUser( ) );
                Query termQuery = new TermQuery( term );
                queries.add( termQuery.toString( ) );
                sectors.add( BlogSearchItem.FIELD_USER );
                flags.add( BooleanClause.Occur.MUST );

            }

            Query queryMulti = MultiFieldQueryParser.parse( queries.toArray( new String [ queries.size( )] ), sectors.toArray( new String [ sectors.size( )] ),
                    flags.toArray( new BooleanClause.Occur [ flags.size( )] ), BlogSearchService.getInstance( ).getAnalyzer( ) );

            TopDocs topDocs = searcher.search( queryMulti, LuceneSearchEngine.MAX_RESPONSES );
            ScoreDoc [ ] hits = topDocs.scoreDocs;
            nNbResults = hits.length;

            for ( int i = 0; i < nNbResults; i++ )
            {
                int docId = hits [i].doc;
                Document document = searcher.doc( docId );
                SearchItem si = new SearchItem( document );
                listResults.add( si );
            }
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        convertList( listResults, listSearchResult );

        return nNbResults;
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
