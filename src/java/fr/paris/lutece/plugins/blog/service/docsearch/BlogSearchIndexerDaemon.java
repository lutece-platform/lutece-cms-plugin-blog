package fr.paris.lutece.plugins.blog.service.docsearch;

import fr.paris.lutece.portal.service.daemon.Daemon;

/**
 * BlogSearchIndexerDaemon
 */
public class BlogSearchIndexerDaemon extends Daemon
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void run( )
    {
        setLastRunLogs( BlogSearchService.getInstance( ).processIndexing( false ) );
    }
}
