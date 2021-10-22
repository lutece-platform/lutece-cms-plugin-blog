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

import fr.paris.lutece.portal.service.rbac.RBACResource;

public class Tag implements RBACResource, Serializable
{
    private static final long serialVersionUID = -3761496963604023715L;

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
     * @param strName
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
     * @param nPriority
     *            The priority
     */
    public void setPriority( int nPriority )
    {
        _nPriority = nPriority;
    }

    @Override
    public String getResourceId( )
    {

        return String.valueOf( _nIdTag );
    }

    @Override
    public String getResourceTypeCode( )
    {

        return PROPERTY_RESOURCE_TYPE;
    }

}
