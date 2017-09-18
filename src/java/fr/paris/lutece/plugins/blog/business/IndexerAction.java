package fr.paris.lutece.plugins.blog.business;

/**
 *
 * IndexerAction
 *
 */
public class IndexerAction
{
    /**
     * Add a document to the index
     */
    public static final int TASK_CREATE = 1;

    /**
     * Update a document of the index
     */
    public static final int TASK_MODIFY = 2;

    /**
     * Remove a document from the index
     */
    public static final int TASK_DELETE = 3;
    private int _nIdAction;
    private int _nIdTask;
    private int _nIdHtmldoc;

    /**
     * gets the action id
     * 
     * @return the action id
     */
    public int getIdAction( )
    {
        return _nIdAction;
    }

    /**
     * set the action id
     * 
     * @param nIdAction
     *            idAction
     */
    public void setIdAction( int nIdAction )
    {
        _nIdAction = nIdAction;
    }

    /**
     * gets announce id
     * 
     * @return the record Id
     */
    public int getIdHtmldoc( )
    {
        return _nIdHtmldoc;
    }

    /**
     * set the IdHtmldoc
     * 
     * @param nIdHtmldocrecord
     *            if
     */
    public void setIdHtmldoc( int nIdHtmldoc )
    {
        _nIdHtmldoc = nIdHtmldoc;
    }

    /**
     * get the task id
     * 
     * @return the task id
     */
    public int getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * set the task id
     * 
     * @param nIdTask
     *            the task id
     */
    public void setIdTask( int nIdTask )
    {
        _nIdTask = nIdTask;
    }
}
