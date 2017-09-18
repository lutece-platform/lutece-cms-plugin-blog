package fr.paris.lutece.plugins.blog.business;

public class Tag
{

    private int _nIdTag;
    private String _strName;
    private int _nPriority;

    public Tag( )
    {

    }

    public Tag( int nIdTag, int nPriority )
    {

        this.setIdTag( nIdTag );
        this.setPriority( nPriority );
    }

    /**
     * Returns the _nIdTag
     *
     * @return The_nIdTag
     */
    public int getIdTag( )
    {
        return _nIdTag;
    }

    /**
     * Sets the nIdTag
     *
     * @param nIdTag
     *            The nIdTag
     */
    public void setIdTag( int nIdTag )
    {
        _nIdTag = nIdTag;
    }

    /**
     * Sets the Name
     * 
     * @param Name
     *            The value
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Gets the value
     * 
     * @return The value
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Returns the _nPriority
     *
     * @return The_nPriority
     */
    public int getPriority( )
    {
        return _nPriority;
    }

    /**
     * Sets the nPriority
     *
     * @param nIdTag
     *            The nPriority
     */
    public void setPriority( int nPriority )
    {
        _nPriority = nPriority;
    }

}
