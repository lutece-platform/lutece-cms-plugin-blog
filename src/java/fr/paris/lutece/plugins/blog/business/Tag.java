package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.rbac.RBACResource;

public class Tag implements RBACResource
{
	public static final String PROPERTY_RESOURCE_TYPE = "TAG";

    private int _nIdTag;
    private String _strName;
    private int _nPriority;
    
    
    // Perimissions
    public static final String PERMISSION_VIEW = "VIEW";
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_MODIFY = "MODIFY";


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

	@Override
	public String getResourceId() {
		
		return String.valueOf( _nIdTag );
	}

	@Override
	public String getResourceTypeCode() {
		
		return PROPERTY_RESOURCE_TYPE;
	}

}
