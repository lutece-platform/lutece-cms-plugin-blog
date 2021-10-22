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

/**
 *
 */
public class BlogFilter
{

    // Variables declarations
    private Integer [ ] _arrayTagsId;
    private Integer [ ] _arrayId;

    // The default value is true to assure ascendant compatibility
    private boolean _bLoadBinaries = true;
    private Boolean _bIsPublished;
    private String _dateMin;
    private String _dateMax;
    private int _nPortletId;
    private boolean _bOrderInPortlet;

    /**
     * @return the _arrayTagsId
     */
    public Integer [ ] getTagsId( )
    {
        return _arrayTagsId;
    }

    /**
     * @param arrayTagsId
     *            the _arrayTagsId to set
     */
    public void setTagsId( Integer [ ] arrayTagsId )
    {
        _arrayTagsId = arrayTagsId;
    }

    /**
     * Tell if the filter contains a criteria on the tag
     * 
     * @return True if the filter contains a criteria on the tags otherwise false
     */
    public boolean containsTagsCriteria( )
    {
        return ( ( _arrayTagsId != null ) && ( _arrayTagsId.length != 0 ) );
    }

    /**
     * Tell if the filter contains a criteria on the Id
     * 
     * @return True if the filter contains a criteria on the Ids otherwise false
     */
    public boolean containsIdsCriteria( )
    {
        return ( ( _arrayId != null ) && ( _arrayId.length != 0 ) );
    }

    /**
     * @return the _arrayId
     */
    public Integer [ ] getIds( )
    {
        return _arrayId;
    }

    /**
     * @param arrayId
     *            the _arrayId to set
     */
    public void setIds( Integer [ ] arrayId )
    {
        _arrayId = arrayId;
    }

    /**
     * Get the boolean that indicates whether binaries of documents should be loaded
     * 
     * @return True if binaries should be loaded, false otherwise
     */
    public boolean getLoadBinaries( )
    {
        return _bLoadBinaries;
    }

    /**
     * Set the boolean that indicates whether binaries of documents should be loaded
     * 
     * @param bLoadBinaries
     *            True if binaries should be loaded, false otherwise
     */
    public void setLoadBinaries( boolean bLoadBinaries )
    {
        this._bLoadBinaries = bLoadBinaries;
    }

    /**
     * @return the _bIsPublished
     */
    public Boolean isPublished( )
    {
        return _bIsPublished;
    }

    /**
     * @param bIsPublished
     *            the _bIsPublished to set
     */
    public void setIsPublished( Boolean bIsPublished )
    {
        this._bIsPublished = bIsPublished;
    }

    /**
     * @return the _dateMin
     */
    public String getDateMin( )
    {
        return _dateMin;
    }

    /**
     * @param dateMin
     *            the _dateMin to set
     */
    public void setDateMin( String dateMin )
    {
        this._dateMin = dateMin;
    }

    /**
     * @return the _dateMax
     */
    public String getDateMax( )
    {
        return _dateMax;
    }

    /**
     * @param dateMax
     *            the _dateMax to set
     */
    public void setDateMax( String dateMax )
    {
        this._dateMax = dateMax;
    }

    /**
     * Get the boolean that indicates whether loaded by order in the portlet list
     * 
     * @return True if loaded by order in the portlet list, false otherwise
     */
    public boolean getOrderInPortlet( )
    {
        return _bOrderInPortlet;
    }

    /**
     * Set the boolean that indicates whether loaded by order in the portlet list
     * 
     * @param bOrder
     *            True if if loaded by order in the portlet list, false otherwise
     */
    public void setOrderInPortlet( boolean bOrder )
    {
        this._bOrderInPortlet = bOrder;
    }

    /**
     * Returns the nPortletId
     *
     * @return The nPortletId
     */
    public int getPortletId( )
    {
        return _nPortletId;
    }

    /**
     * Sets the IdPortlet
     *
     * @param nPortletId
     *            The nPortletId
     */
    public void setPortletId( int nPortletId )
    {
        _nPortletId = nPortletId;
    }
}
