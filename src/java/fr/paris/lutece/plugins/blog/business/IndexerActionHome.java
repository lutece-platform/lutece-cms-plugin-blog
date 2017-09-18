package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.plugins.blog.service.HtmldocsPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for
 * IndexerAction objects
 */
public final class IndexerActionHome
{
    // Static variable pointed at the DAO instance
    private static IIndexerActionDAO _dao = SpringContextService.getBean( "blog.htmldocIndexerActionDAO" );
    private static Plugin _plugin = PluginService.getPlugin( HtmldocsPlugin.PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private IndexerActionHome(  )
    {
    }

    /**
     * Creation of an instance of Indexer Action
     * @param indexerAction The instance of the indexer action which contains
     *            the informations to store
     */
    public static synchronized void create( IndexerAction indexerAction )
    {
        int nOppositeTask = 0;

        if ( indexerAction.getIdTask(  ) == IndexerAction.TASK_CREATE )
        {
            nOppositeTask = IndexerAction.TASK_DELETE;
        }
        else if ( indexerAction.getIdTask(  ) == IndexerAction.TASK_DELETE )
        {
            nOppositeTask = IndexerAction.TASK_CREATE;
        }

        boolean bAlreadyFound = false;

        if ( nOppositeTask > 0 )
        {
            IndexerActionFilter filter = new IndexerActionFilter(  );
            filter.setIdTask( nOppositeTask );

            List<IndexerAction> listIndexerActions = getList( filter );

            for ( IndexerAction action : listIndexerActions )
            {
                if ( action.getIdTask(  ) == nOppositeTask )
                {
                    remove( action.getIdAction(  ) );
                }
                else if ( action.getIdTask(  ) == indexerAction.getIdTask(  ) )
                {
                    if ( bAlreadyFound )
                    {
                        remove( action.getIdAction(  ) );
                    }
                    else
                    {
                        bAlreadyFound = true;
                    }
                }
            }
        }

        if ( !bAlreadyFound )
        {
            _dao.insert( indexerAction, _plugin );
        }
    }

    /**
     * Remove the indexerAction whose identifier is specified in parameter
     * @param nId The IndexerActionId
     */
    public static synchronized void remove( int nId )
    {
        _dao.delete( nId, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a IndexerAction whose identifier is specified in
     * parameter
     * @param nKey The indexerAction primary key
     * @return an instance of IndexerAction
     */
    public static IndexerAction findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Loads the data of all the IndexerAction who verify the filter and returns
     * them in a list
     * @param filter the filter
     * @return the list which contains the data of all the indexerAction
     */
    public static List<IndexerAction> getList( IndexerActionFilter filter )
    {
        return _dao.selectList( filter, _plugin );
    }
}
