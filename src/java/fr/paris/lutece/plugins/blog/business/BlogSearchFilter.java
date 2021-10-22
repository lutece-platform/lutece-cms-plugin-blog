/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
    private String _strUserEditedBlogVersion;

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
     * Sets the User Edited blog
     * 
     * @param strUser
     *            The User edited Blog
     */
    public void setUserEditedBlogVersion( String userEditedBlogVersion )
    {
        _strUserEditedBlogVersion = userEditedBlogVersion;
    }

    /**
     * Returns the strUser
     * 
     * @return The strUser
     */
    public String getUserEditedBlogVersion( )
    {
        return _strUserEditedBlogVersion;
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
