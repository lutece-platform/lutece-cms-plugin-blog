package fr.paris.lutece.plugins.blog.business;

public class ContentType
{

    private int _nIdContentType;
    private String _strLabel;
    
    /**
     * Returns the IdContentType
     *
     * @return The _nIdContentType
     */
    public int getIdContentType( )
    {
        return _nIdContentType;
    }

    /**
     * Sets the IdContentType
     *
     * @param nIdDocumentContent
     *            The _nIdContentType
     */
    public void setIdContentType( int nIdContentType )
    {
    	_nIdContentType = nIdContentType;
    }

    /**
     * Sets the Label
     * 
     * @param strLabel
     *            The label
     */
    public void setLabel( String strLabel )
    {
    	_strLabel = strLabel;
    }

    /**
     * Gets the Label
     * 
     * @return The Label
     */
    public String getLabel( )
    {
        return _strLabel;
    }

}
