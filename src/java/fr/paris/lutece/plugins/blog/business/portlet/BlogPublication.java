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
package fr.paris.lutece.plugins.blog.business.portlet;

import java.sql.Date;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;

public class BlogPublication
{

    private int _nIdBlog;
    private int _nIdPortlet;
    private Date _dateBeginPublishing;
    private Date _dateEndPublishing;
    private int _nBlogOrder;
    private int _nStatus = 1;

    /**
     * Returns the IdBlog
     * 
     * @return The IdBlog
     */
    public int getIdBlog( )
    {
        return _nIdBlog;
    }

    /**
     * Sets the IdBlog
     * 
     * @param nIdBlog
     *            The IdBlog
     */
    public void setIdBlog( int nIdBlog )
    {
        _nIdBlog = nIdBlog;
    }

    /**
     * Returns the IdPortlet
     * 
     * @return The IdPortlet
     */
    public int getIdPortlet( )
    {
        return _nIdPortlet;
    }

    /**
     * Sets the IdBlog
     * 
     * @param nIdPortlet
     *            The portlet Id
     */
    public void setIdPortlet( int nIdPortlet )
    {
        _nIdPortlet = nIdPortlet;
    }

    /**
     * @return the _dateBeginPublishing
     */
    public Date getDateBeginPublishing( )
    {
        return _dateBeginPublishing;
    }

    /**
     * @param datePublishing
     *            the _datePublishing to set
     */
    public void setDateBeginPublishing( Date datePublishing )
    {
        _dateBeginPublishing = datePublishing;
    }

    /**
     * @return the _dateEndPublishing
     */
    public Date getDateEndPublishing( )
    {
        return _dateEndPublishing;
    }

    /**
     * @param dateEndPublishing
     *            the End Publishing date
     */
    public void setDateEndPublishing( Date dateEndPublishing )
    {
        _dateEndPublishing = dateEndPublishing;
    }

    /**
     * @return the _nBlogOrder
     */
    public int getBlogOrder( )
    {
        return _nBlogOrder;
    }

    /**
     * @param nBlogOrder
     *            the _nBlogOrder to set
     */
    public void setBlogOrder( int nBlogOrder )
    {
        _nBlogOrder = nBlogOrder;
    }

    /**
     * @return the _nStatus
     */
    public int getStatus( )
    {
        return _nStatus;
    }

    /**
     * @param nStatus
     *            the _nStatus to set
     */
    public void setStatus( int nStatus )
    {
        _nStatus = nStatus;
    }

    /**
     * return the Portlet
     * 
     * @return
     */
    public Portlet getPortlet( )
    {
        return PortletHome.findByPrimaryKey( _nIdPortlet );
    }

}
