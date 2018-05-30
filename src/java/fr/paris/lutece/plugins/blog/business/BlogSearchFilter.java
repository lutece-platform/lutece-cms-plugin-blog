package fr.paris.lutece.plugins.blog.business;

import java.util.Date;

/**
 * Search filter for blog
 */
public class BlogSearchFilter
{
    private int _nIdFilter;
    private String _strKeywords;
    private String [ ] _strTag;
    private String _strUser;
    private boolean _bIsUnpulished;
    private Date _dateUpdateDateAfter;
    private Date _dateUpdateDateBefor;

    /**
     * Get the id of the filter
     * 
     * @return The id of the filter
     */
    public int getIdFilter( )
    {
        return _nIdFilter;
    }

    /**
     * Set the id of the filter
     * 
     * @param nIdFilter
     *            The id of the filter
     */
    public void setIdFilter( int nIdFilter )
    {
        this._nIdFilter = nIdFilter;
    }

    /**
     * Get the keywords of the filter
     * 
     * @return The keywords of the filter
     */
    public String getKeywords( )
    {
        return _strKeywords;
    }

    /**
     * Set the keywords of the filter
     * 
     * @param strKeywords
     *            The keywords of the filter
     */
    public void setKeywords( String strKeywords )
    {
        this._strKeywords = strKeywords;
    }

    /**
     * Get the tag of the filter
     * 
     * @return The tag of the filter
     */
    public String [ ] getTag( )
    {
        return _strTag;
    }

    /**
     * Set the strTag of the filter
     * 
     * @param strTag
     *            The tag of the filter
     */
    public void setTag( String [ ] strTag )
    {
        this._strTag = strTag;
    }

    /**
     * Sets the User
     * 
     * @param strUser
     *            The User
     */
    public void setUser( String strUser )
    {
        _strUser = strUser;
    }

    /**
     * Returns the strUser
     * 
     * @return The strUser
     */
    public String getUser( )
    {
        return _strUser;
    }

    /**
     * Returns the isUnpulished
     * 
     * @return The isUnpulished
     */
    public boolean getIsUnpulished( )
    {
        return _bIsUnpulished;
    }

    /**
     * isUnpulished
     */
    public void setIsUnpulished( boolean isUnpulished )
    {
        _bIsUnpulished = isUnpulished;
    }

    /**
     * Returns the UpdateDateAfter
     * 
     * @return The UpdateDateAfter
     */
    public Date getUpdateDateAfter( )
    {
        return _dateUpdateDateAfter;
    }

    /**
     * Sets the UpdateDateAfter
     * 
     * @param dateUpdateDateAfter
     *            The UpdateDateAfter
     */
    public void setUpdateDateAfter( Date dateUpdateDateAfter )
    {
        _dateUpdateDateAfter = dateUpdateDateAfter;
    }

    /**
     * Returns the UpdateDateBefor
     * 
     * @return The UpdateDateBefor
     */
    public Date getUpdateDateBefor( )
    {
        return _dateUpdateDateBefor;
    }

    /**
     * Sets the UpdateDateBefor
     * 
     * @param dateUpdateDateBefor
     *            The UpdateDateBefor
     */
    public void setUpdateDateBefor( Date dateUpdateDateBefor )
    {
        _dateUpdateDateBefor = dateUpdateDateBefor;
    }

}
