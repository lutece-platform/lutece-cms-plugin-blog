/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.IndexerAction;
import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.service.BlogPlugin;
import fr.paris.lutece.plugins.blog.utils.BlogUtils;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.lucene.document.DateTools;
//import org.apache.lucene.demo.html.HTMLParser;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DefaultAnnounceIndexer
 */
public class DefaultBlogIndexer implements IBlogSearchIndexer
{
    private static final String PROPERTY_INDEXER_NAME = "blog.indexer.name";
    private static final String ENABLE_VALUE_TRUE = "1";
    private static final String PROPERTY_INDEXER_DESCRIPTION = "blog.indexer.description";
    private static final String PROPERTY_INDEXER_VERSION = "blog.indexer.version";
    private static final String PROPERTY_INDEXER_ENABLE = "blog.indexer.enable";
    private static final String BLANK_SPACE = " ";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription( )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_DESCRIPTION );
    }

    /**
     * Index given list of record
     * 
     * @param indexWriter
     *            the indexWriter
     * @param listIdBlog
     *            The list of id blog
     * @throws CorruptIndexException
     *             If the index is corrupted
     * @throws IOException
     *             If an IO Exception occurred
     * @throws InterruptedException
     *             If the indexer is interrupted
     */
    private void indexListBlog( IndexWriter indexWriter, List<Integer> listIdBlog ) throws CorruptIndexException, IOException, InterruptedException
    {
        Iterator<Integer> it = listIdBlog.iterator( );

        while ( it.hasNext( ) )
        {
            Integer nBlogId = it.next( );
            Blog blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( nBlogId );
            indexWriter.addDocument( getDocument( blog ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void processIndexing( IndexWriter indexWriter, boolean bCreate, StringBuffer sbLogs ) throws IOException, InterruptedException,
            SiteMessageException
    {
        Plugin plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        List<Integer> listIdBlog = new ArrayList<Integer>( );

        if ( !bCreate )
        {
            // incremental indexing
            // delete all record which must be deleted
            for ( fr.paris.lutece.plugins.blog.business.IndexerAction action : BlogSearchService.getInstance( ).getAllIndexerActionByTask(
                    IndexerAction.TASK_DELETE, plugin ) )
            {
                sbLogBlog( sbLogs, action.getIdBlog( ), IndexerAction.TASK_DELETE );

                Term term = new Term( BlogSearchItem.FIELD_ID_HTML_DOC, Integer.toString( action.getIdBlog( ) ) );
                Term [ ] terms = {
                    term
                };

                indexWriter.deleteDocuments( terms );
                BlogSearchService.getInstance( ).removeIndexerAction( action.getIdAction( ), plugin );
            }

            // Update all record which must be updated
            for ( IndexerAction action : BlogSearchService.getInstance( ).getAllIndexerActionByTask( IndexerAction.TASK_MODIFY, plugin ) )
            {
                sbLogBlog( sbLogs, action.getIdBlog( ), IndexerAction.TASK_MODIFY );

                Term term = new Term( BlogSearchItem.FIELD_ID_HTML_DOC, Integer.toString( action.getIdBlog( ) ) );
                Term [ ] terms = {
                    term
                };

                indexWriter.deleteDocuments( terms );
                listIdBlog = new ArrayList<Integer>( );
                listIdBlog.add( action.getIdBlog( ) );
                this.indexListBlog( indexWriter, listIdBlog );
                BlogSearchService.getInstance( ).removeIndexerAction( action.getIdAction( ), plugin );
            }

            listIdBlog = new ArrayList<Integer>( );

            // add all record which must be added
            for ( IndexerAction action : BlogSearchService.getInstance( ).getAllIndexerActionByTask( IndexerAction.TASK_CREATE, plugin ) )
            {
                sbLogBlog( sbLogs, action.getIdBlog( ), IndexerAction.TASK_CREATE );
                listIdBlog.add( action.getIdBlog( ) );

                BlogSearchService.getInstance( ).removeIndexerAction( action.getIdAction( ), plugin );
            }

            this.indexListBlog( indexWriter, listIdBlog );
        }
        else
        {
            for ( Blog doc : BlogHome.getBlogsList( ) )
            {

                sbLogs.append( "Indexing Blog" );
                sbLogs.append( "\r\n" );

                sbLogBlog( sbLogs, doc.getId( ), IndexerAction.TASK_CREATE );

                listIdBlog.add( doc.getId( ) );

            }

            this.indexListBlog( indexWriter, listIdBlog );
        }

        indexWriter.commit( );
    }

    /**
     * Builds a document which will be used by Lucene during the indexing of the announces list
     * 
     * @param announce
     *            the announce
     * @param strUrl
     *            the url
     * @param plugin
     *            the plugin
     * @throws IOException
     *             If an IO Exception occurred
     * @throws InterruptedException
     *             If the indexer is interrupted
     * @return the document
     */
    public static org.apache.lucene.document.Document getDocument( Blog blog ) throws IOException, InterruptedException
    {
        // make a new, empty document
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document( );

        doc.add( new StringField( BlogSearchItem.FIELD_ID_HTML_DOC, Integer.toString( blog.getId( ) ), Field.Store.YES ) );
        // Add the user firstName as a field, so that index can be incrementally maintained.
        doc.add( new StringField( BlogSearchItem.FIELD_USER, blog.getUser( ), Field.Store.YES ) );

        doc.add( new TextField( BlogSearchItem.FIELD_TAGS, getTagToIndex( blog ), Field.Store.YES ) );
        FieldType ft = new FieldType( StringField.TYPE_STORED );
        ft.setOmitNorms( false );
        doc.add( new Field( BlogSearchItem.FIELD_DATE, DateTools.timeToString( blog.getUpdateDate( ).getTime( ), DateTools.Resolution.MINUTE ), ft ) );
        doc.add( new NumericDocValuesField( BlogSearchItem.FIELD_DATE_UPDATE, blog.getUpdateDate( ).getTime( ) ) );
        doc.add( new TextField( BlogSearchItem.FIELD_UNPUBLISHED, ( blog.getBlogPubilcation( ).size( ) == 0 ) ? "true" : "false", Field.Store.YES ) );

        // Add the uid as a field, so that index can be incrementally maintained.
        // This field is not stored with question/answer, it is indexed, but it is not
        // tokenized prior to indexing.
        String strIdAnnounce = String.valueOf( blog.getId( ) );
        doc.add( new StringField( BlogSearchItem.FIELD_UID, strIdAnnounce, Field.Store.YES ) );

        String strContentToIndex = getContentToIndex( blog );
        // NOUVEAU
        ContentHandler handler = new BodyContentHandler( -1 );
        Metadata metadata = new Metadata( );

        try
        {
            new HtmlParser( ).parse( new ByteArrayInputStream( strContentToIndex.getBytes( ) ), handler, metadata, new ParseContext( ) );
        }
        catch( SAXException e )
        {
            throw new AppException( "Error during blog parsing. blog Id: "+ blog.getId( ), e );
        }
        catch( TikaException e )
        {
            throw new AppException( "Error during blog parsing. blog Id: "+ blog.getId( ), e );
        }

        String strContent = handler.toString( );

        // Add the tag-stripped contents as a Reader-valued Text field so it will
        // get tokenized and indexed.
        doc.add( new TextField( BlogSearchItem.FIELD_CONTENTS, strContent, Field.Store.NO ) );

        doc.add( new TextField( BlogSearchItem.FIELD_SUMMARY, blog.getHtmlContent( ), Field.Store.YES ) );
        // Add the subject name as a separate Text field, so that it can be searched
        // separately.
        doc.add( new StringField( BlogSearchItem.FIELD_TITLE, blog.getName( ), Field.Store.YES ) );

        doc.add( new StringField( BlogSearchItem.FIELD_TYPE, BlogPlugin.PLUGIN_NAME, Field.Store.YES ) );

        // return the document
        return doc;
    }

    /**
     * Set the Content to index
     * 
     * @param blog
     *            The {@link blog} to index
     * @return The content to index
     */
    private static String getContentToIndex( Blog blog )
    {
        StringBuffer sbContentToIndex = new StringBuffer( );
        // Do not index question here
        sbContentToIndex.append( blog.getName( ) );
        sbContentToIndex.append( BLANK_SPACE );
        sbContentToIndex.append( blog.getDescription( ) );
        sbContentToIndex.append( BLANK_SPACE );
        sbContentToIndex.append( blog.getHtmlContent( ) );
        sbContentToIndex.append( BLANK_SPACE );
        sbContentToIndex.append( blog.getId( ) );

        return sbContentToIndex.toString( );
    }

    /**
     * Set the tag to index
     * 
     * @param blog
     *            The {@link blog} to index
     * @return The tag to index
     */
    private static String getTagToIndex( Blog blog )
    {
        StringBuffer sbContentToIndex = new StringBuffer( );

        for ( Tag tg : blog.getTag( ) )
        {
            sbContentToIndex.append( BLANK_SPACE );
            sbContentToIndex.append( tg.getIdTag( ) );
        }

        return sbContentToIndex.toString( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion( )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_VERSION );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnable( )
    {
        boolean bReturn = false;
        String strEnable = AppPropertiesService.getProperty( PROPERTY_INDEXER_ENABLE );

        if ( ( strEnable != null ) && ( strEnable.equalsIgnoreCase( Boolean.TRUE.toString( ) ) || strEnable.equals( ENABLE_VALUE_TRUE ) )
                && PluginService.isPluginEnable( BlogPlugin.PLUGIN_NAME ) )
        {
            bReturn = true;
        }

        return bReturn;
    }

    /**
     * Indexing action performed on the recording
     * 
     * @param sbLogs
     *            the buffer log
     * @param nIdBlog
     *            the id of the Blog
     * @param nAction
     *            the indexer action key performed
     */
    private void sbLogBlog( StringBuffer sbLogs, int nIdBlog, int nAction )
    {
        sbLogs.append( "Indexing Blogs:" );

        switch( nAction )
        {
            case IndexerAction.TASK_CREATE:
                sbLogs.append( "Insert " );

                break;

            case IndexerAction.TASK_MODIFY:
                sbLogs.append( "Modify " );

                break;

            case IndexerAction.TASK_DELETE:
                sbLogs.append( "Delete " );

                break;

            default:
                break;
        }

        if ( nIdBlog != BlogUtils.CONSTANT_ID_NULL )
        {
            sbLogs.append( "id_blog=" );
            sbLogs.append( nIdBlog );
        }

        sbLogs.append( "\r\n" );
    }

}
