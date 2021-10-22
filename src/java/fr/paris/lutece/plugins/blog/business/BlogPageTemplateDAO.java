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

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for BlogPageTemplate objects
 */
public final class BlogPageTemplateDAO implements IBlogPageTemplateDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_page_template_document ) FROM blog_page_template";
    private static final String SQL_QUERY_SELECT = " SELECT id_page_template_document, description, page_template_path, picture_path FROM blog_page_template WHERE id_page_template_document = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO blog_page_template ( id_page_template_document, description, page_template_path, picture_path ) VALUES ( ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM blog_page_template WHERE id_page_template_document = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE blog_page_template SET id_page_template_document = ?, description = ?, page_template_path = ?, picture_path = ? "
            + " WHERE id_page_template_document = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_page_template_document , description, page_template_path, picture_path FROM blog_page_template ORDER BY id_page_template_document ";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * Generates a new primary key
     * 
     * @return The new primary key
     */
    int newPrimaryKey( )
    {
        int nKey = 1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK ) )
        {
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }
        return nKey;
    }

    /**
     * Insert a new record in the table.
     * 
     * @param blogPageTemplate
     *            The Instance of the object BlogPageTemplate
     */
    @Override
    public synchronized void insert( BlogPageTemplate blogPageTemplate )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            blogPageTemplate.setId( newPrimaryKey( ) );

            daoUtil.setInt( 1, blogPageTemplate.getId( ) );
            daoUtil.setString( 2, blogPageTemplate.getDescription( ) );
            daoUtil.setString( 3, blogPageTemplate.getFile( ) );
            daoUtil.setString( 4, blogPageTemplate.getPicture( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * load the data of BlogPageTemplate from the table
     *
     * @param nPageTemplateId
     *            The indentifier of the object BlogPageTemplate
     * @return The Instance of the object PageTemplate
     */
    @Override
    public BlogPageTemplate load( int nPageTemplateId )
    {
        BlogPageTemplate blogPageTemplate = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nPageTemplateId );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                blogPageTemplate = new BlogPageTemplate( );
                blogPageTemplate.setId( daoUtil.getInt( 1 ) );
                blogPageTemplate.setDescription( daoUtil.getString( 2 ) );
                blogPageTemplate.setFile( daoUtil.getString( 3 ) );
                blogPageTemplate.setPicture( daoUtil.getString( 4 ) );
            }
        }

        return blogPageTemplate;
    }

    /**
     * Delete a record from the table
     * 
     * @param nPageTemplateId
     *            The indentifier of the object PageTemplate
     */
    @Override
    public void delete( int nPageTemplateId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nPageTemplateId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Update the record in the table
     * 
     * @param blogPageTemplate
     *            The instance of the PageTemplate to update
     */
    @Override
    public void store( BlogPageTemplate blogPageTemplate )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setInt( 1, blogPageTemplate.getId( ) );
            daoUtil.setString( 2, blogPageTemplate.getDescription( ) );
            daoUtil.setString( 3, blogPageTemplate.getFile( ) );
            daoUtil.setString( 4, blogPageTemplate.getPicture( ) );
            daoUtil.setInt( 5, blogPageTemplate.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Returns a list of all the page templates
     * 
     * @return A list of PageTemplates objects
     */
    @Override
    public List<BlogPageTemplate> selectPageTemplatesList( )
    {
        List<BlogPageTemplate> listBlogPageTemplates = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                BlogPageTemplate blogPageTemplate = new BlogPageTemplate( );

                blogPageTemplate.setId( daoUtil.getInt( 1 ) );
                blogPageTemplate.setDescription( daoUtil.getString( 2 ) );
                blogPageTemplate.setFile( daoUtil.getString( 3 ) );
                blogPageTemplate.setPicture( daoUtil.getString( 4 ) );
                listBlogPageTemplates.add( blogPageTemplate );
            }

        }
        return listBlogPageTemplates;
    }
}
