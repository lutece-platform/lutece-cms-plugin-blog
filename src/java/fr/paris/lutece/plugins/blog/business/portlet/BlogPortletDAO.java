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

import java.util.Collection;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * this class provides Data Access methods for BlogsPortlet objects
 */
public final class BlogPortletDAO implements IBlogPortletDAO
{
    // Constants

    private static final String SQL_QUERY_SELECTALL = "SELECT id_portlet, name, content_id, id_page_template_document FROM blog_portlet";
    private static final String SQL_QUERY_SELECT = "SELECT id_portlet, name, content_id,id_page_template_document  FROM blog_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO blog_portlet ( id_portlet, name, content_id, id_page_template_document ) VALUES ( ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM blog_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE blog_portlet SET id_portlet = ?, name = ?, content_id = ?, id_page_template_document=? WHERE id_portlet = ? ";
    private static final String SQL_QUERY_INSERT_BLOGS_PORTLET = "INSERT INTO blog_list_portlet_htmldocs ( id_portlet , id_blog, status, document_order ) VALUES ( ? , ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_BLOGS_PORTLET_ON_UPDATE = "INSERT INTO blog_list_portlet_htmldocs ( id_portlet , id_blog, status, document_order, date_begin_publishing, date_end_publishing) VALUES ( ? , ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_SELECT_PORTLET_BY_TYPE = "SELECT DISTINCT b.id_portlet , a.name, a.date_update " + "FROM blog_portlet b "
            + "LEFT JOIN blog_list_portlet_htmldocs c ON b.id_portlet = c.id_portlet " + "INNER JOIN core_portlet a ON b.id_portlet = a.id_portlet "
            + "INNER JOIN core_page f ON a.id_page = f.id_page WHERE c.id_portlet IS NULL ";

    /**
     * Insert a new record in the table.
     *
     * @param portlet
     *            The Instance of the Portlet
     */
    @Override
    public void insert( Portlet portlet )
    {
        BlogPortlet p = (BlogPortlet) portlet;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            daoUtil.setInt( 1, p.getId( ) );
            daoUtil.setString( 2, p.getPortletName( ) );
            daoUtil.setInt( 3, p.getContentId( ) );
            daoUtil.setInt( 4, p.getPageTemplateDocument( ) );
            daoUtil.executeUpdate( );
        }
        insertBlogPublication( p );

    }

    /**
     * Delete record from table
     *
     * @param nPortletId
     *            The indentifier of the Portlet
     */
    @Override
    public void delete( int nPortletId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nPortletId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Update the record in the table
     *
     * @param portlet
     *            The reference of the portlet
     */
    @Override
    public void store( Portlet portlet )
    {
        BlogPortlet p = (BlogPortlet) portlet;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setInt( 1, p.getId( ) );
            daoUtil.setString( 2, p.getPortletName( ) );
            daoUtil.setInt( 3, p.getContentId( ) );
            daoUtil.setInt( 4, p.getPageTemplateDocument( ) );
            daoUtil.setInt( 5, p.getId( ) );

            daoUtil.executeUpdate( );
        }

        BlogPublicationHome.removeByIdPortlet( p.getId( ) );
        insertBlogPublicationOnUpdate( p );

    }

    /**
     * load the data of dbpagePortlet from the table
     * 
     * @return portlet The instance of the object portlet
     * @param nIdPortlet
     *            The identifier of the portlet
     */
    @Override
    public Portlet load( int nIdPortlet )
    {
        BlogPortlet portlet = new BlogPortlet( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nIdPortlet );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                portlet.setId( daoUtil.getInt( 1 ) );
                portlet.setPortletName( daoUtil.getString( 2 ) );
                portlet.setContentId( daoUtil.getInt( 3 ) );
                portlet.setPageTemplateDocument( daoUtil.getInt( 4 ) );
            }
        }
        return portlet;
    }

    /**
     * Insert a docPublication for a specified portlet
     * 
     * @param portlet
     *            the BlogsPortlet to insert
     */
    private void insertBlogPublication( BlogPortlet p )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_BLOGS_PORTLET ) )
        {
            daoUtil.setInt( 1, p.getId( ) );
            daoUtil.setInt( 2, p.getContentId( ) );
            daoUtil.setInt( 3, 1 );
            daoUtil.setInt( 4, 0 );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectBlogPortletReferenceList( Plugin plugin )
    {
        ReferenceList blogPortletList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                blogPortletList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return blogPortletList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<ReferenceItem> selectPortletByType( int nDocumentId, PortletOrder pOrder, PortletFilter pFilter )
    {
        StringBuilder strSQl = new StringBuilder( );
        strSQl.append( SQL_QUERY_SELECT_PORTLET_BY_TYPE );

        String strFilter = ( pFilter != null ) ? pFilter.getSQLFilter( ) : null;

        if ( strFilter != null )
        {
            strSQl.append( "AND" );
            strSQl.append( strFilter );
        }

        strSQl.append( pOrder.getSQLOrderBy( ) );

        ReferenceList list = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( strSQl.toString( ) ) )
        {
            if ( strFilter != null )
            {
                if ( pFilter.getPortletFilterType( ).equals( PortletFilter.PAGE_NAME ) )
                {
                    for ( int i = 0; i < pFilter.getPageName( ).length; i++ )
                    {
                        daoUtil.setString( i + 1, "%" + pFilter.getPageName( ) [i] + "%" );
                    }
                }
                else
                    if ( pFilter.getPortletFilterType( ).equals( PortletFilter.PORTLET_NAME ) )
                    {
                        for ( int i = 0; i < pFilter.getPortletName( ).length; i++ )
                        {
                            daoUtil.setString( i + 1, "%" + pFilter.getPortletName( ) [i] + "%" );
                        }
                    }
                    else
                        if ( pFilter.getPortletFilterType( ).equals( PortletFilter.PAGE_ID ) )
                        {
                            daoUtil.setInt( 1, pFilter.getIdPage( ) );
                        }
            }

            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }

        return list;
    }

    /**
     * Insert a blog publication for a specified portlet
     * 
     * @param portlet
     *            the BlogPortlet to insert
     */
    private void insertBlogPublicationOnUpdate( BlogPortlet portlet )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_BLOGS_PORTLET_ON_UPDATE ) )
        {
            daoUtil.setInt( 1, portlet.getId( ) );
            daoUtil.setInt( 2, portlet.getContentId( ) );
            daoUtil.setInt( 3, 1 );
            daoUtil.setInt( 4, 0 );
            daoUtil.setDate( 5, portlet.getBlogPublication( ).getDateBeginPublishing( ) );
            daoUtil.setDate( 6, portlet.getBlogPublication( ).getDateEndPublishing( ) );

            daoUtil.executeUpdate( );
        }
    }
}
