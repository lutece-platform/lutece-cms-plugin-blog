package fr.paris.lutece.plugins.htmldocs.business;


/**
 *
 * class IndexerActionFilter
 *
 */
public class IndexerActionFilter
{
    /**
     * Represent any integer
     */
    public static final int ALL_INT = -1;
    private int _nIdTask = ALL_INT;
    private int _nIdHtmlDoc = ALL_INT;

    /**
     * gets the id of task
     * @return the task id insert in the filter
     */
    public int getIdTask(  )
    {
        return _nIdTask;
    }

    /**
     * set the task id in the filter
     * @param idTask the task id to insert in the filter
     */
    public void setIdTask( int idTask )
    {
        _nIdTask = idTask;
    }

    /**
     * weather or not it contains the task
     * @return true if the filter contain a task id
     */
    public boolean containsIdTask(  )
    {
        return ( _nIdTask != ALL_INT );
    }

    /**
     * Get the id of the HtmlDoc
     * @return The id of the HtmlDoc
     */
    public int getIdHtmlDoc(  )
    {
        return _nIdHtmlDoc;
    }

    /**
     * Set the id of the HtmlDoc
     * @param nId The id of the HtmlDoc
     */
    public void setIdHtmlDoc( int nId )
    {
        this._nIdHtmlDoc = nId;
    }

    /**
     * Check if this filter contains an HtmlDoc id
     * @return True if this filter contains an HtmlDoc id, false otherwise
     */
    public boolean containsIdHtmlDoc(  )
    {
        return ( _nIdHtmlDoc != ALL_INT );
    }
}
