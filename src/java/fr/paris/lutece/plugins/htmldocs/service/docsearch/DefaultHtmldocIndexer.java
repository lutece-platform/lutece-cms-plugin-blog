package fr.paris.lutece.plugins.htmldocs.service.docsearch;


import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDocHome;
import fr.paris.lutece.plugins.htmldocs.business.IndexerAction;
import fr.paris.lutece.plugins.htmldocs.business.Tag;
import fr.paris.lutece.plugins.htmldocs.service.HtmlDocService;
import fr.paris.lutece.plugins.htmldocs.service.HtmldocsPlugin;
import fr.paris.lutece.plugins.htmldocs.utils.HtmldocUtils;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;



//import org.apache.lucene.demo.html.HTMLParser;
import org.apache.lucene.document.Field;
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
public class DefaultHtmldocIndexer implements IHtmldocsSearchIndexer
{
    private static final String PROPERTY_INDEXER_NAME = "htmldocs.indexer.name";
    private static final String ENABLE_VALUE_TRUE = "1";
    private static final String PROPERTY_INDEXER_DESCRIPTION = "htmldocs.indexer.description";
    private static final String PROPERTY_INDEXER_VERSION = "htmldocs.indexer.version";
    private static final String PROPERTY_INDEXER_ENABLE = "htmldocs.indexer.enable";
    private static final String BLANK_SPACE = " ";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_DESCRIPTION );
    }

    /**
     * Index given list of record
     * @param indexWriter the indexWriter
     * @param listIdHtmlDoc The list of id htmldoc
     * @throws CorruptIndexException If the index is corrupted
     * @throws IOException If an IO Exception occurred
     * @throws InterruptedException If the indexer is interrupted
     */
    private void indexListHtmldoc( IndexWriter indexWriter, List<Integer> listIdHtmlDoc )
        throws CorruptIndexException, IOException, InterruptedException
    {
        Iterator<Integer> it = listIdHtmlDoc.iterator(  );

        while ( it.hasNext(  ) )
        {
            Integer nHtmldocId = it.next(  );
            HtmlDoc htmldoc = HtmlDocService.getInstance().loadDocument( nHtmldocId );
            indexWriter.addDocument( getDocument( htmldoc) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void processIndexing( IndexWriter indexWriter, boolean bCreate, StringBuffer sbLogs )
        throws IOException, InterruptedException, SiteMessageException
    {
        Plugin plugin = PluginService.getPlugin( HtmldocsPlugin.PLUGIN_NAME );
        List<Integer> listIdHtmldoc = new ArrayList<Integer>(  );

        if ( !bCreate )
        {
            //incremental indexing
            //delete all record which must be deleted
            for ( fr.paris.lutece.plugins.htmldocs.business.IndexerAction action : HtmlDocSearchService.getInstance(  )
                                                              .getAllIndexerActionByTask( IndexerAction.TASK_DELETE,
                    plugin ) )
            {
            	sbLogHtmldoc( sbLogs, action.getIdHtmldoc( ), IndexerAction.TASK_DELETE );

                Term term = new Term( HtmlDocsSearchItem.FIELD_ID_HTML_DOC, Integer.toString( action.getIdHtmldoc( ) ) );
                Term[] terms = { term };

                indexWriter.deleteDocuments( terms );
                HtmlDocSearchService.getInstance(  ).removeIndexerAction( action.getIdAction(  ), plugin );
            }

            //Update all record which must be updated
            for ( IndexerAction action : HtmlDocSearchService.getInstance(  )
                                                              .getAllIndexerActionByTask( IndexerAction.TASK_MODIFY,
                    plugin ) )
            {
            	sbLogHtmldoc( sbLogs, action.getIdHtmldoc( ), IndexerAction.TASK_MODIFY );

                Term term = new Term( HtmlDocsSearchItem.FIELD_ID_HTML_DOC, Integer.toString( action.getIdHtmldoc( ) ) );
                Term[] terms = { term };

                indexWriter.deleteDocuments( terms );

                listIdHtmldoc.add( action.getIdHtmldoc( ));

                HtmlDocSearchService.getInstance(  ).removeIndexerAction( action.getIdAction(  ), plugin );
            }

            this.indexListHtmldoc( indexWriter, listIdHtmldoc );

            listIdHtmldoc = new ArrayList<Integer>(  );

            //add all record which must be added
            for ( IndexerAction action : HtmlDocSearchService.getInstance(  )
                                                              .getAllIndexerActionByTask( IndexerAction.TASK_CREATE,
                    plugin ) )
            {
            	sbLogHtmldoc( sbLogs, action.getIdHtmldoc( ), IndexerAction.TASK_CREATE );
                listIdHtmldoc.add( action.getIdHtmldoc( ) );

                HtmlDocSearchService.getInstance(  ).removeIndexerAction( action.getIdAction(  ), plugin );
            }

            this.indexListHtmldoc( indexWriter, listIdHtmldoc  );
        }
        else
        {
            for ( HtmlDoc doc : HtmlDocHome.getHtmlDocsList() )
            {
                
                    sbLogs.append( "Indexing Htmldoc" );
                    sbLogs.append( "\r\n" );

                    sbLogHtmldoc( sbLogs, doc.getId(  ), IndexerAction.TASK_CREATE );

                    listIdHtmldoc.add( doc.getId(  ) );
                
            }

            this.indexListHtmldoc( indexWriter, listIdHtmldoc );
        }

        indexWriter.commit(  );
    }

 

    /**
     * Builds a document which will be used by Lucene during the indexing of the
     * announces list
     * @param announce the announce
     * @param strUrl the url
     * @param plugin the plugin
     * @throws IOException If an IO Exception occurred
     * @throws InterruptedException If the indexer is interrupted
     * @return the document
     */
    public static org.apache.lucene.document.Document getDocument( HtmlDoc htmldoc )
        throws IOException, InterruptedException
    {
        // make a new, empty document
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document(  );


        doc.add( new StringField( HtmlDocsSearchItem.FIELD_ID_HTML_DOC, Integer.toString( htmldoc.getId(  ) ),
                Field.Store.YES) );

        doc.add( new TextField( HtmlDocsSearchItem.FIELD_TAGS, getTagToIndex(htmldoc), Field.Store.YES ) );

        // Add the uid as a field, so that index can be incrementally maintained.
        // This field is not stored with question/answer, it is indexed, but it is not
        // tokenized prior to indexing.
        String strIdAnnounce = String.valueOf( htmldoc.getId(  ) );
        doc.add( new StringField( HtmlDocsSearchItem.FIELD_UID, strIdAnnounce, Field.Store.YES ) );

     
        String strContentToIndex = getContentToIndex(htmldoc);
        //NOUVEAU
        ContentHandler handler = new BodyContentHandler(  );
        Metadata metadata = new Metadata(  );

        try
        {
            new HtmlParser(  ).parse( new ByteArrayInputStream( strContentToIndex.getBytes(  ) ), handler, metadata,
                new ParseContext(  ) );
        }
        catch ( SAXException e )
        {
            throw new AppException( "Error during announce parsing." );
        }
        catch ( TikaException e )
        {
            throw new AppException( "Error during announce parsing." );
        }

        String strContent = handler.toString(  );

        // Add the tag-stripped contents as a Reader-valued Text field so it will
        // get tokenized and indexed.
        doc.add( new TextField( HtmlDocsSearchItem.FIELD_CONTENTS, strContent, Field.Store.NO ) );

        doc.add( new TextField( HtmlDocsSearchItem.FIELD_SUMMARY, htmldoc.getHtmlContent(), Field.Store.YES ) );
        // Add the subject name as a separate Text field, so that it can be searched
        // separately.
        doc.add( new StringField( HtmlDocsSearchItem.FIELD_TITLE, htmldoc.getName(), Field.Store.YES ) );

        doc.add( new StringField( HtmlDocsSearchItem.FIELD_TYPE, HtmldocsPlugin.PLUGIN_NAME, Field.Store.YES ) );

        // return the document
        return doc;
    }

    /**
     * Set the Content to index
     * @param htmldoc The {@link htmldoc} to index
     * @return The content to index
     */
    private static String getContentToIndex( HtmlDoc htmldoc )
    {
        StringBuffer sbContentToIndex = new StringBuffer(  );
        //Do not index question here
        sbContentToIndex.append( htmldoc.getName() );
        sbContentToIndex.append( BLANK_SPACE );
        sbContentToIndex.append( htmldoc.getDescription(  ) );
        sbContentToIndex.append( BLANK_SPACE );
        sbContentToIndex.append( htmldoc.getHtmlContent());
        
        return sbContentToIndex.toString(  );
    }
    
    /**
     * Set the tag to index
     * @param htmldoc The {@link htmldoc} to index
     * @return The tag to index
     */
    private static String getTagToIndex( HtmlDoc htmldoc )
    {
        StringBuffer sbContentToIndex = new StringBuffer(  );
       
        
        for(Tag tg:htmldoc.getTag()){
	        sbContentToIndex.append( BLANK_SPACE );
	        sbContentToIndex.append( tg.getName( ) );
        }
       
        return sbContentToIndex.toString(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_VERSION );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnable(  )
    {
        boolean bReturn = false;
        String strEnable = AppPropertiesService.getProperty( PROPERTY_INDEXER_ENABLE );

        if ( ( strEnable != null ) &&
                ( strEnable.equalsIgnoreCase( Boolean.TRUE.toString(  ) ) || strEnable.equals( ENABLE_VALUE_TRUE ) ) &&
                PluginService.isPluginEnable( HtmldocsPlugin.PLUGIN_NAME ) )
        {
            bReturn = true;
        }

        return bReturn;
    }

    /**
     * Indexing action performed on the recording
     * @param sbLogs the buffer log
     * @param nIdHtmldoc the id of the Htmldoc
     * @param nAction the indexer action key performed
     */
    private void sbLogHtmldoc( StringBuffer sbLogs, int nIdHtmldoc, int nAction )
    {
        sbLogs.append( "Indexing HtmlDocs:" );

        switch ( nAction )
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

        if ( nIdHtmldoc != HtmldocUtils.CONSTANT_ID_NULL )
        {
            sbLogs.append( "id_htmldoc=" );
            sbLogs.append( nIdHtmldoc );
        }

        sbLogs.append( "\r\n" );
    }
    
   
}
