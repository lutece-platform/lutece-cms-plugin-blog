/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;

import java.io.Serializable;

import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.util.ReferenceItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the business class for the object Blog
 */
public class Blog extends ReferenceItem implements Serializable, IExtendableResource, RBACResource
{
    public static final String PROPERTY_RESOURCE_TYPE = "blogs";

    private static final long serialVersionUID = 1L;
    
    // Perimissions
    public static final String PERMISSION_VIEW = "VIEW";
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_MODIFY = "MODIFY";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_PUBLISH = "PUBLISH";


    // Variables declarations
    private int _nId;

    private int _nAttachedPortletId;

    private int _nVersion;

    @NotEmpty( message = "#i18n{blog.validation.blog.ContentLabel.notEmpty}" )
    @Size( max = 50, message = "#i18n{blog.validation.blog.ContentLabel.size}" )
    private String _strContentLabel;

    private Timestamp _dateCreationDate;

    private Timestamp _dateUpdateDate;

    @NotEmpty( message = "#i18n{blog.validation.blog.HtmlContent.notEmpty}" )
    @Size( message = "#i18n{blog.validation.blog.HtmlContent.size}" )
    private String _strHtmlContent;

    @NotEmpty( message = "#i18n{blog.validation.blog.User.notEmpty}" )
    @Size( max = 100, message = "#i18n{blog.validation.blog.User.size}" )
    private String _strUser;

    @NotEmpty( message = "#i18n{blog.validation.blog.UserCreator.notEmpty}" )
    @Size( max = 100, message = "#i18n{blog.validation.blog.UserCreator.size}" )
    private String _strUserCreator;

    @Size( max = 100, message = "#i18n{blog.validation.blog.EditComment.size}" )
    private String _strEditComment;

    @Size( message = "#i18n{blog.validation.description.size}" )
    private String _strDescription;

    private List<DocContent> _docContent= new ArrayList<DocContent>( );

    private String _strUrl;
    
    private boolean _bShareable;

    private List<Tag> _tag = new ArrayList<Tag>( );

    private List<BlogPublication> _blogPubilcation = new ArrayList<BlogPublication>( );

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    public String getName( )
    {
        return _strContentLabel;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Version
     * 
     * @return The Version
     */
    public int getVersion( )
    {
        return _nVersion;
    }

    /**
     * Sets the Version
     * 
     * @param nVersion
     *            The Version
     */
    public void setVersion( int nVersion )
    {
        _nVersion = nVersion;
    }

    /**
     * Returns the ContentLabel
     * 
     * @return The ContentLabel
     */
    public String getContentLabel( )
    {
        return _strContentLabel;
    }

    /**
     * Sets the ContentLabel
     * 
     * @param strContentLabel
     *            The ContentLabel
     */
    public void setContentLabel( String strContentLabel )
    {
        _strContentLabel = strContentLabel;
    }

    /**
     * Returns the CreationDate
     * 
     * @return The CreationDate
     */
    public Timestamp getCreationDate( )
    {
        return _dateCreationDate;
    }

    /**
     * Sets the CreationDate
     * 
     * @param dateCreationDate
     *            The CreationDate
     */
    public void setCreationDate( Timestamp dateCreationDate )
    {
        _dateCreationDate = dateCreationDate;
    }

    /**
     * Returns the UpdateDate
     * 
     * @return The UpdateDate
     */
    public Timestamp getUpdateDate( )
    {
        return _dateUpdateDate;
    }

    /**
     * Sets the UpdateDate
     * 
     * @param dateUpdateDate
     *            The UpdateDate
     */
    public void setUpdateDate( Timestamp dateUpdateDate )
    {
        _dateUpdateDate = dateUpdateDate;
    }

    /**
     * Returns the HtmlContent
     * 
     * @return The HtmlContent
     */
    public String getHtmlContent( )
    {
        return _strHtmlContent;
    }

    /**
     * Sets the HtmlContent
     * 
     * @param strHtmlContent
     *            The HtmlContent
     */
    public void setHtmlContent( String strHtmlContent )
    {
        _strHtmlContent = strHtmlContent;
    }

    /**
     * Returns the User
     * 
     * @return The User
     */
    public String getUser( )
    {
        return _strUser;
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
     * Returns the UserCreator
     * 
     * @return The UserCreator
     */
    public String getUserCreator( )
    {
        return _strUserCreator;
    }

    /**
     * Sets the UserCreator
     * 
     * @param strUserCreator
     *            The UserCreator
     */
    public void setUserCreator( String strUserCreator )
    {
        _strUserCreator = strUserCreator;
    }

    public void setAttachedPortletId( int nAttachedPortletId )
    {
        _nAttachedPortletId = nAttachedPortletId;
    }

    public int getAttachedPortletId( )
    {
        return _nAttachedPortletId;
    }

    /**
     * Returns the EditComment
     *
     * @return The EditComment
     */
    public String getEditComment( )
    {
        return _strEditComment;
    }

    /**
     * Sets the EditComment
     *
     * @param strEditComment
     *            The EditComment
     */
    public void setEditComment( String strEditComment )
    {
        _strEditComment = strEditComment;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription
     *            The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the list of DocContent
     *
     * @return The DocContent
     */
    public List<DocContent> getDocContent( )
    {
        return _docContent;
    }

    /**
     * Sets the list DocContent
     *
     * @param DocContent
     *            The DocContent
     */
    public void setDocContent( List<DocContent> docContent )
    {
        _docContent = docContent;
    }
    /**
     * Sets the docConetnt list
     *
     * @param docContent
     *            list The docContent list
     */
    public void addConetnt(  DocContent docContent )
    {
        for ( DocContent docCont : _docContent )
        {

            if ( docCont.getTextValue().equals(docContent.getTextValue( )) )
            {

                return;
            }
        }
        _docContent.add( docContent );
    }
    /**
    * delet the docContent
    *
    * @param docContent
    *            The docContent
    */
   public void deleteDocContent( String strFileName )
   {
	   DocContent docContToRemove = null;
	   for ( DocContent docCont : _docContent )
       {

           if ( docCont.getTextValue().equals( strFileName ) )
           {

        	   docContToRemove = docCont;
               break;
           }
       }

	   _docContent.remove( docContToRemove );
   }

    /**
     * Returns the Url
     *
     * @return The Url
     */
    public String getUrl( )
    {
        return _strUrl;
    }

    /**
     * Sets the Url
     *
     * @param strUrl
     *            The Url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }
    /**
     * Returns the Shareable
     * 
     * @return The Shareable
     */
    public boolean getShareable( )
    {
        return _bShareable;
    }

    /**
     * shareable
     */
    public void setShareable( boolean shareable )
    {
        _bShareable = shareable;
    }

    /**
     * Returns the tag list
     *
     * @return The tag list
     */
    public List<Tag> getTag( )
    {
        return _tag;
    }

    /**
     * Sets the tag list
     *
     * @param tag
     *            list The tag list
     */
    public void setTag( List<Tag> tag )
    {
        _tag = tag;
    }

    /**
     * Sets the tag list
     *
     * @param tag
     *            list The tag list
     */
    public void addTag( Tag tag )
    {
        for ( Tag tg : _tag )
        {

            if ( tg.getIdTag( ) == tag.getIdTag( ) )
            {

                return;
            }
        }
        _tag.add( tag );
    }

    /**
     * delet the tag
     *
     * @param tag
     *            The tag
     */
    public void deleteTag( Tag tag )
    {
        Tag tagToRemove = null;
        for ( Tag tg : _tag )
        {

            if ( tg.getIdTag( ) == tag.getIdTag( ) )
            {

                tagToRemove = tg;
                break;
            }
        }

        _tag.remove( tagToRemove );
    }

    /**
     * Returns the blogPubilcation list
     *
     * @return The BlogPubilcation list
     */
    public List<BlogPublication> getBlogPubilcation( )
    {
        return _blogPubilcation;
    }

    /**
     * Sets the BlogPublication list
     *
     * @param blogPublication
     *            list The blogPublication list
     */
    public void setBlogPubilcation( List<BlogPublication> blogPublication )
    {
        _blogPubilcation = blogPublication;
    }

    /**
     * Sets the BlogPubilcation list
     *
     * @param BlogPubilcation
     *            list The BlogPubilcation list
     */
    public void addBlogPublication( BlogPublication blogPubilcation )
    {
        for ( BlogPublication pub : _blogPubilcation )
        {

            if ( pub.getIdDocument( ) == blogPubilcation.getIdDocument( ) && pub.getIdPortlet( ) == blogPubilcation.getIdPortlet( ) )
            {

                return;
            }
        }
        _blogPubilcation.add( blogPubilcation );
    }

    /**
     * delet the BlogPubilcation
     *
     * @param BlogPubilcation
     *            The BlogPubilcation
     */
    public void deleteBlogPublication( BlogPublication blogPubilcation )
    {
        BlogPublication pubToRemove = null;

        for ( BlogPublication pub : _blogPubilcation )
        {

            if ( pub.getIdDocument( ) == blogPubilcation.getIdDocument( ) && pub.getIdPortlet( ) == blogPubilcation.getIdPortlet( ) )
            {

                pubToRemove = pub;
                break;
            }
        }

        _blogPubilcation.remove( pubToRemove );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceDescription( )
    {

        return _strDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceImageUrl( )
    {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceName( )
    {

        return _strContentLabel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceType( )
    {

        return PROPERTY_RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdExtendableResource( )
    {
        return Integer.toString( _nId );
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public String getResourceId() {
		
		return String.valueOf( _nId );
	}
	 /**
     * {@inheritDoc}
     */
	@Override
	public String getResourceTypeCode() {

		return PROPERTY_RESOURCE_TYPE;
	}
}
