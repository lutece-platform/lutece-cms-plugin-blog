package fr.paris.lutece.plugins.blog.service.docsearch;

import fr.paris.lutece.portal.service.message.SiteMessageException;

import org.apache.lucene.index.IndexWriter;

import java.io.IOException;


/**
 *
 * IHtmldocsSearchIndexer
 *
 */
public interface IHtmldocsSearchIndexer
{
    /**
     * add to the index writer the document associate to the key specified in parameter
     * @param indexWriter lucene index writer
     * @param bCreate true for indexing all directory
     *                                   false for use incremental indexing
     * @param sbLog the buffer logger
     * @throws IOException If an IO error occured
     * @throws InterruptedException If a thread error occured
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    void processIndexing( IndexWriter indexWriter, boolean bCreate, StringBuffer sbLog )
        throws IOException, InterruptedException, SiteMessageException;

    /**
     * Returns the indexer service name
     * @return the indexer service name
     */
    String getName(  );

    /**
     * Returns the indexer service version
     * @return the indexer service version
     */
    String getVersion(  );

    /**
     * Returns the indexer service description
     * @return the indexer service description
     */
    String getDescription(  );

    /**
     * Tells whether the service is enable or not
     * @return true if enable, otherwise false
     */
    boolean isEnable(  );
}
