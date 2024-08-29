package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.plugins.blog.business.DocContentHome;


/**
 * Delete all docContents in the database that are not linked to any blog
 *
 */
public class RemoveDocContentsDaemon extends Daemon
{
    /**
     * {@inheritDoc }
     */
    @Override
    public void run( )
    {
        DocContentHome.removeDocContentsNotLinked( );
    }
}
