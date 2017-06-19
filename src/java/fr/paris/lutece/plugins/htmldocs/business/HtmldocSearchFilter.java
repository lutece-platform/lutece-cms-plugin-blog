package fr.paris.lutece.plugins.htmldocs.business;



/**
 * Search filter for htmldoc
 */
public class HtmldocSearchFilter
{
    private int _nIdFilter;
    private String _strKeywords;


    /**
     * Get the id of the filter
     * @return The id of the filter
     */
    public int getIdFilter(  )
    {
        return _nIdFilter;
    }

    /**
     * Set the id of the filter
     * @param nIdFilter The id of the filter
     */
    public void setIdFilter( int nIdFilter )
    {
        this._nIdFilter = nIdFilter;
    }

    /**
     * Get the keywords of the filter
     * @return The keywords of the filter
     */
    public String getKeywords(  )
    {
        return _strKeywords;
    }

    /**
     * Set the keywords of the filter
     * @param strKeywords The keywords of the filter
     */
    public void setKeywords( String strKeywords )
    {
        this._strKeywords = strKeywords;
    }

   
}
