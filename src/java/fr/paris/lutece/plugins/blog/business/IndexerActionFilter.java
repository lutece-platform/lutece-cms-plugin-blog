package fr.paris.lutece.plugins.blog.business;

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
    private int _nIdBlog = ALL_INT;

    /**
     * gets the id of task
     * 
     * @return the task id insert in the filter
     */
    public int getIdTask( )
    {
        return _nIdTask;
    }

    /**
     * set the task id in the filter
     * 
     * @param idTask
     *            the task id to insert in the filter
     */
    public void setIdTask( int idTask )
    {
        _nIdTask = idTask;
    }

    /**
     * weather or not it contains the task
     * 
     * @return true if the filter contain a task id
     */
    public boolean containsIdTask( )
    {
        return ( _nIdTask != ALL_INT );
    }

    /**
     * Get the id of the blog
     * 
     * @return The id of the blog
     */
    public int getIdBlog( )
    {
        return _nIdBlog;
    }

    /**
     * Set the id of the blog
     * 
     * @param nId
     *            The id of the Blog
     */
    public void setIdBlog( int nId )
    {
        this._nIdBlog = nId;
    }

    /**
     * Check if this filter contains an Blog id
     * 
     * @return True if this filter contains an Blog id, false otherwise
     */
    public boolean containsIdBlog( )
    {
        return ( _nIdBlog != ALL_INT );
    }
}
