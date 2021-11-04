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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.util.ReferenceItem;

/**
 * This is the business class for the object Blog
 */
public class Blog extends ReferenceItem implements Serializable, IExtendableResource, RBACResource
{
    public static final String PROPERTY_RESOURCE_TYPE = "BLOG";

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
    @Size( max = 255, message = "#i18n{blog.validation.blog.ContentLabel.size}" )
    private String _strContentLabel;

    private Timestamp _dateCreationDate;

    private Timestamp _dateUpdateDate;

    @NotEmpty( message = "#i18n{blog.validation.blog.HtmlContent.notEmpty}" )
    @Size( message = "#i18n{blog.validation.blog.HtmlContent.size}" )
    private String _strHtmlContent;

    @NotEmpty( message = "#i18n{blog.validation.blog.User.notEmpty}" )
    @Size( max = 255, message = "#i18n{blog.validation.blog.User.size}" )
    private String _strUser;

    @NotEmpty( message = "#i18n{blog.validation.blog.UserCreator.notEmpty}" )
    @Size( max = 255, message = "#i18n{blog.validation.blog.UserCreator.size}" )
    private String _strUserCreator;

    @Size( max = 255, message = "#i18n{blog.validation.blog.EditComment.size}" )
    private String _strEditComment;

    @Size( message = "#i18n{blog.validation.description.size}" )
    private String _strDescription;

    private List<DocContent> _docContent = new ArrayList<>( );

    private String _strUrl;

    private boolean _bShareable;

    private boolean _bLocked;

    private List<Tag> _tag = new ArrayList<>( );

    private List<BlogPublication> _blogPublication = new ArrayList<>( );

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName( )
    {
        return getContentLabel( );
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
     * @param docContent
     *            the list DocContent
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
    public void addContent( DocContent docContent )
    {

        boolean isContain = _docContent.stream( ).anyMatch( dc -> dc.getTextValue( ).equals( docContent.getTextValue( ) ) );

        if ( !isContain )
        {
            _docContent.add( docContent );
        }
    }

    /**
     * delet the docContent
     *
     * @param nIdDoc
     *            The docContent Id
     */
    public void deleteDocContent( int nIdDoc )
    {
        _docContent.removeIf( dc -> dc.getId( ) == nIdDoc );

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
     * 
     * @param bShareable
     */
    public void setShareable( boolean bShareable )
    {
        _bShareable = bShareable;
    }

    /**
     * Returns the Locked
     * 
     * @return The Locked
     */
    public boolean getLocked( )
    {
        return _bLocked;
    }

    /**
     * Locked
     * 
     * @param bLocked
     */
    public void setLocked( boolean bLocked )
    {
        _bLocked = bLocked;
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
        boolean bContain = _tag.stream( ).anyMatch( tg -> tg.getIdTag( ) == tag.getIdTag( ) );
        if ( !bContain )
        {
            _tag.add( tag );
        }
    }

    /**
     * delet the tag
     *
     * @param tag
     *            The tag
     */
    public void deleteTag( Tag tag )
    {
        _tag.removeIf( tg -> tg.getIdTag( ) == tag.getIdTag( ) );

    }

    /**
     * Returns the blogc list
     *
     * @return The BlogPublication list
     */
    public List<BlogPublication> getBlogPublication( )
    {
        return _blogPublication;
    }

    /**
     * Sets the BlogPublication list
     *
     * @param blogPublication
     *            list The blogPublication list
     */
    public void setBlogPublication( List<BlogPublication> blogPublication )
    {
        _blogPublication = blogPublication;
    }

    /**
     * Sets the BlogPublication list
     *
     * @param blogPublication
     *            list The BlogPublication list
     */
    public void addBlogPublication( BlogPublication blogPublication )
    {

        boolean bContain = _blogPublication.stream( )
                .anyMatch( blogPub -> ( blogPub.getIdBlog( ) == blogPublication.getIdBlog( ) && blogPub.getIdPortlet( ) == blogPublication.getIdPortlet( ) ) );

        if ( !bContain )
        {
            _blogPublication.add( blogPublication );
        }
    }

    /**
     * delet the BlogPublication
     *
     * @param blogPublication
     *            The BlogPublication
     */
    public void deleteBlogPublication( BlogPublication blogPublication )
    {

        _blogPublication
                .removeIf( blogPub -> ( blogPub.getIdBlog( ) == blogPublication.getIdBlog( ) && blogPub.getIdPortlet( ) == blogPublication.getIdPortlet( ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceDescription( )
    {

        return getDescription( );
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

        return getContentLabel( );
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
    public String getResourceId( )
    {

        return String.valueOf( _nId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeCode( )
    {

        return PROPERTY_RESOURCE_TYPE;
    }

    /**
     * Return the user informations
     * 
     * @return AdminUser
     */
    public AdminUser getUserInfos( )
    {
        return AdminUserHome.findUserByLogin( _strUser );
    }

    /**
     * Return the user creator informations
     * 
     * @return AdminUser
     */
    public AdminUser getUserCreatorInfos( )
    {
        return AdminUserHome.findUserByLogin( _strUserCreator );
    }

}
