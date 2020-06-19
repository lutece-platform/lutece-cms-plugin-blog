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
import fr.paris.lutece.plugins.blog.service.BlogPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.search.LuceneSearchEngine;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.search.SearchResult;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.Term;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * LuceneSearchEngine
 */
public class BlogLuceneSearchEngine implements IBlogSearchEngine
{

    private static final String WILDCARD = "*";

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSearchResults( BlogSearchFilter filter, Plugin plugin, List<SearchResult> listSearchResult )
    {
        ArrayList<SearchItem> listResults = new ArrayList<>( );
        IndexSearcher searcher;
        boolean bDateAfter = false;
        boolean bDateBefore = false;

        int nNbResults = 0;
        try
        {
            searcher = BlogSearchService.getInstance( ).getSearcher( );

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
                    strAfter = new BytesRef( DateTools.dateToString( filter.getUpdateDateAfter( ), Resolution.MINUTE ) );
                    bDateAfter = true;
                }

                if ( filter.getUpdateDateBefor( ) != null )
                {
                    Date dateBefore = filter.getUpdateDateBefor( );
                    strBefore = new BytesRef( DateTools.dateToString( dateBefore, Resolution.MINUTE ) );
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

            Query queryMulti = MultiFieldQueryParser.parse( queries.toArray( new String [ queries.size( )] ), sectors.toArray( new String [ sectors.size( )] ),
                    flags.toArray( new BooleanClause.Occur [ flags.size( )] ), BlogSearchService.getInstance( ).getAnalyzer( ) );

            Sort sorter = new Sort( );
            String field = BlogSearchItem.FIELD_DATE_UPDATE;
            Type type = Type.LONG;
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
