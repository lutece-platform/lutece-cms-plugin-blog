package fr.paris.lutece.plugins.blog.business.portlet;

import java.sql.Date;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;

public class BlogPublication
{

    private int _nIdDocument;
    private int _nIdPortlet;
    private Date _dateBeginPublishing;
    private Date _dateEndPublishing;
    private int _nDocumentOrder;
    private int _nStatus = 1;

    /**
     * Returns the IdDocument
     * 
     * @return The IdDocument
     */
    public int getIdDocument( )
    {
        return _nIdDocument;
    }

    /**
     * Sets the IdDocument
     * 
     * @param nIdDocument
     *            The IdDocument
     */
    public void setIdDocument( int nIdDocument )
    {
        _nIdDocument = nIdDocument;
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
     * Sets the IdDocument
     * 
     * @param nIdDocument
     *            The IdDocument
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
     *            the _dateEndPublishing to set
     */
    public void setDateEndPublishing( Date datePublishing )
    {
        _dateEndPublishing = datePublishing;
    }

    /**
     * @return the _nDocumentOrder
     */
    public int getDocumentOrder( )
    {
        return _nDocumentOrder;
    }

    /**
     * @param nDocumentOrder
     *            the _nDocumentOrder to set
     */
    public void setDocumentOrder( int nDocumentOrder )
    {
        _nDocumentOrder = nDocumentOrder;
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
