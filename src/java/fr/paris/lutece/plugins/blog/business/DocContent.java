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

import java.io.Serializable;
import java.util.List;

/**
 * @author eahuma
 *
 */
public class DocContent implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int _nIdDocContent;
    private String _strTextValue;
    private byte [ ] _bytes;
    private String _strValueContentType;
    private ContentType _contentType;
    private List<Blog> _blogs;
    private int _nPriority;

    /**
     * Returns the IdDocContent
     *
     * @return The _nIdDocContent
     */
    public int getId( )
    {
        return _nIdDocContent;
    }

    /**
     * Sets the _nIdDocContent
     *
     * @param nIdDocumentContent
     *            The _nIdDocContent
     */
    public void setId( int nIdDocumentContent )
    {
        _nIdDocContent = nIdDocumentContent;
    }

    /**
     * Sets the value
     * 
     * @param strTextValue
     *            The value
     */
    public void setTextValue( String strTextValue )
    {
        _strTextValue = strTextValue;
    }

    /**
     * Gets the value
     * 
     * @return The value
     */
    public String getTextValue( )
    {
        return _strTextValue;
    }

    /**
     * Sets the value
     * 
     * @param bytes
     *            The value
     */
    public void setBinaryValue( byte [ ] bytes )
    {
        _bytes = bytes;
    }

    /**
     * Gets the value
     * 
     * @return The value
     */
    public byte [ ] getBinaryValue( )
    {
        return _bytes;
    }

    /**
     * Sets the content type value
     * 
     * @param strValueContentType
     *            The content type value
     */
    public void setValueContentType( String strValueContentType )
    {
        _strValueContentType = strValueContentType;
    }

    /**
     * Gets the content type value
     * 
     * @return The content type value
     */
    public String getValueContentType( )
    {
        return _strValueContentType;
    }

    /**
     * Sets the ContentType
     * 
     * @param type
     *            The ContentType
     */
    public void setContentType( ContentType type )
    {
        _contentType = type;
    }

    /**
     * Gets the ContentType
     * 
     * @return The ContentType
     */
    public ContentType getContentType( )
    {
        return _contentType;
    }

    /**
     * Gets the blogs
     * 
     * @return the blogs
     */
    public List<Blog> getBlogs( )
    {
        return _blogs;
    }

    /**
     * Sets the blogs
     * 
     * @param blogs
     *            the blogs
     */
    public void setBlogs( List<Blog> blogs )
    {
        this._blogs = blogs;
    }

    /**
     * Gets the priority
     * 
     * @return the priority
     */
    public int getPriority( )
    {
        return _nPriority;
    }

    /**
     * Sets the priority
     * 
     * @param nPriority
     *            the priority to set
     */
    public void setPriority( int nPriority )
    {
        this._nPriority = nPriority;
    }
}
