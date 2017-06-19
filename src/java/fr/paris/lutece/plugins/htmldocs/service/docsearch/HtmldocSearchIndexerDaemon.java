package fr.paris.lutece.plugins.htmldocs.service.docsearch;

import fr.paris.lutece.portal.service.daemon.Daemon;


/**
 * HtmldocSearchIndexerDaemon
 */
public class HtmldocSearchIndexerDaemon extends Daemon
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void run(  )
    {
        setLastRunLogs( HtmlDocSearchService.getInstance(  ).processIndexing( false ) );
    }
}
