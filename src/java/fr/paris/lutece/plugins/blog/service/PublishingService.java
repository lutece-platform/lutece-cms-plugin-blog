/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogFilter;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.IndexerAction;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPortletHome;
import fr.paris.lutece.plugins.blog.service.docsearch.BlogSearchService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Publishing service
 */
public class PublishingService
{
    private static PublishingService _singleton = new PublishingService( );

    /**
     * Get the unique instance of the service
     * 
     * @return The unique instance
     */
    public static PublishingService getInstance( )
    {
        return _singleton;
    }

    /**
     * Assign {@link Document} to a {@link Portlet}
     *
     * @param nDocumentId
     *            The {@link Document} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     */
    public void assign( int nDocumentId, int nPortletId )
    {
        BlogPublication documentPublication = new BlogPublication( );
        documentPublication.setIdPortlet( nPortletId );
        documentPublication.setIdDocument( nDocumentId );
        BlogPublicationHome.create( documentPublication );
    }

    /**
     * Publishing documents assigned to a portlet at the begin of the list
     *
     * @param nDocumentId
     *            the Document id
     * @param nPortletId
     *            the portlet identifier
     */
    public void publish( int nDocumentId, int nPortletId )
    {
        // Publishing of document : set status to Published
        BlogPublication documentPublication = BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nDocumentId );

        if ( documentPublication != null )
        {
            documentPublication.setIdPortlet( nPortletId );
            documentPublication.setIdDocument( nDocumentId );
            BlogPublicationHome.update( documentPublication );

        }

        BlogSearchService.getInstance( ).addIndexerAction( nDocumentId, IndexerAction.TASK_MODIFY, BlogPlugin.getPlugin( ) );

    }

    /**
     * unAssign {@link Document} to a {@link Portlet}
     *
     * @param nDocumentId
     *            The {@link Document} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     */
    public void unAssign( int nDocumentId, int nPortletId )
    {
        BlogPublicationHome.remove( nPortletId, nDocumentId );
    }

    /**
     * Check if the specified {@link Document} is published into the specified {@link Portlet}
     * 
     * @param nDocumentId
     *            The {@link Document} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     * @return True if {@link Document} is published, false else (unpublished or not assigned)
     */
    public boolean isPublished( int nDocumentId, int nPortletId )
    {
        BlogPublication documentPublication = BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nDocumentId );

        return documentPublication != null ? true : false;
    }

    /**
     * Check if the specified {@link Document} is assigned (unpublished or published) into at least one {@link Portlet}
     * 
     * @param nDocumentId
     *            The {@link Document} identifier
     * @return True if {@link Document} is assigned (published or unpublished), false else (not assigned)
     */
    public boolean isAssigned( int nDocumentId )
    {
        Collection<BlogPublication> listDocumentPublication = BlogPublicationHome.getDocPublicationByIdDoc( nDocumentId );

        return ( listDocumentPublication.size( ) > 0 );
    }

    /**
     * Check if the specified {@link Document} is assigned (unpublished or published) into the specified {@link Portlet}
     * 
     * @param nDocumentId
     *            The {@link Document} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     * @return True if {@link Document} is assigned (published or unpublished), false else (not assigned)
     */
    public boolean isAssigned( int nDocumentId, int nPortletId )
    {
        BlogPublication documentPublication = BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nDocumentId );

        return ( documentPublication != null );
    }

    /**
     * Return a {@link DocumentPublication} from a {@link Portlet} identifier and {@link Document} identifier
     * 
     * @param nPortletId
     *            the {@link Portlet} identifier
     * @param nDocumentId
     *            the {@link Document} identifier
     * @return a {@link DocumentPublication} or null if no object match
     */
    public BlogPublication getDocumentPublication( int nPortletId, int nDocumentId )
    {
        return BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nDocumentId );
    }

    /**
     * Loads the list of portlets
     *
     * @return the {@link Collection} of the portlets
     */
    public Collection<Portlet> getBlogsPortlets( )
    {
        Plugin plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        Collection<Portlet> listPortletsAll = new ArrayList<Portlet>( );

        for ( PortletType portletType : plugin.getPortletTypes( ) )
        {
            listPortletsAll.addAll( PortletHome.findByType( portletType.getId( ) ) );
        }

        return listPortletsAll;
    }

    /**
     * Loads the list of portlets blogs empty and blogslist
     *
     * @return the {@link Collection} of the portlets
     */
    public Collection<Portlet> getBlogsPortletstoPublish( )
    {
        Plugin plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        Collection<Portlet> listPortletsAll = new ArrayList<Portlet>( );

        for ( PortletType portletType : plugin.getPortletTypes( ) )
        {
            List<Portlet> listPortlet = PortletHome.findByType( portletType.getId( ) );
            String className = BlogPortletHome.class.getName( );
            String strPortletTypeId = PortletTypeHome.getPortletTypeId( className );

            if ( portletType.getId( ).equals( strPortletTypeId ) )
            {
                for ( Portlet pt : listPortlet )
                {
                    if ( BlogPublicationHome.getDocPublicationByPortlet( pt.getId( ) ).size( ) == 0 )
                    {

                        listPortletsAll.addAll( listPortlet );
                    }

                }

            }
            else
            {

                listPortletsAll.addAll( listPortlet );
            }
        }

        return listPortletsAll;
    }

    /**
     * Loads the list of the portlets whoes contain blog specified by id
     *
     * @param strDocumentId
     *            the blog identifier
     * @return the {@link Collection} of the portlets
     */
    public Collection<Portlet> getPortletsByDocumentId( String strDocumentId )
    {
        Collection<BlogPublication> listDocumentPublication = BlogPublicationHome.getDocPublicationByIdDoc( Integer.parseInt( strDocumentId ) );
        Collection<Portlet> listPortlets = new ArrayList<Portlet>( );

        for ( BlogPublication documentPublication : listDocumentPublication )
        {
            listPortlets.add( PortletHome.findByPrimaryKey( documentPublication.getIdPortlet( ) ) );
        }

        return listPortlets;
    }

    /**
     * Loads the list of the blog whose filter and date publication is specified Return published documents since the publication date. The is also filtered
     * with the documentFilter
     *
     * @param datePublishing
     *            The start publication date
     * @param dateEndPublishing
     *            The end publication date
     * @param documentFilter
     *            The filter for the published blog. The filter can be null or empty. The array of Ids will not be taked in account.
     * @param locale
     *            The locale is used to get the list of blogs with the findByFilter method
     * @return the list of the blog in form of a List. return null if datePublishing is null
     */
    public Collection<Blog> getPublishedDocumentsSinceDate( Date datePublishing, Date dateEndPublishing, BlogFilter documentFilter, Locale locale )
    {
        if ( datePublishing == null )
        {
            return null;
        }

        Collection<BlogPublication> listDocumentPublication = BlogPublicationHome.findSinceDatePublishingAndStatus( datePublishing, dateEndPublishing, 1 );

        if ( ( listDocumentPublication == null ) || ( listDocumentPublication.size( ) == 0 ) )
        {
            return new ArrayList<Blog>( );
        }

        Integer [ ] arrayIds = new Integer [ listDocumentPublication.size( )];
        int i = 0;
        BlogFilter publishedDocumentFilter = documentFilter;

        if ( publishedDocumentFilter == null )
        {
            publishedDocumentFilter = new BlogFilter( );
        }

        for ( BlogPublication documentPublication : listDocumentPublication )
        {
            arrayIds [i++] = documentPublication.getIdDocument( );
        }

        publishedDocumentFilter.setIds( arrayIds );

        Collection<Blog> listDocuments = BlogHome.findByFilter( publishedDocumentFilter, locale );

        return listDocuments;
    }

    /**
     * Get the list of id of published blogs associated with a given collection of portlets.
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param datePublishing
     *            TODO
     * @param plugin
     *            The document plugin
     * @return The list of documents id.
     */
    public static List<Integer> getPublishedDocumentsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        return BlogPublicationHome.getPublishedDocumentsIdsListByPortletIds( nPortletsIds, datePublishing, dateEndPublishing, plugin );
    }

}
