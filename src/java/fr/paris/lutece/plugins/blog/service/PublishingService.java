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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

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
     * Assign {@link Blog} to a {@link Portlet}
     *
     * @param nBlogId
     *            The {@link Blog} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     */
    public void assign( int nBlogId, int nPortletId )
    {
        BlogPublication blogPublication = new BlogPublication( );
        blogPublication.setIdPortlet( nPortletId );
        blogPublication.setIdBlog( nBlogId );
        BlogPublicationHome.create( blogPublication );
    }

    /**
     * Publishing blogs assigned to a portlet at the begin of the list
     *
     * @param nBlogId
     *            the Blog id
     * @param nPortletId
     *            the portlet identifier
     */
    public void publish( int nBlogId, int nPortletId )
    {
        // Publishing of blog : set status to Published
        BlogPublication blogPublication = BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nBlogId );

        if ( blogPublication != null )
        {
            blogPublication.setIdPortlet( nPortletId );
            blogPublication.setIdBlog( nBlogId );
            BlogPublicationHome.update( blogPublication );

        }

        BlogSearchService.getInstance( ).addIndexerAction( nBlogId, IndexerAction.TASK_MODIFY );

    }

    /**
     * unAssign {@link Blog} to a {@link Portlet}
     *
     * @param nBlogId
     *            The {@link Blog} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     */
    public void unAssign( int nBlogId, int nPortletId )
    {
        BlogPublicationHome.remove( nPortletId, nBlogId );
    }

    /**
     * Check if the specified {@link Blog} is published into the specified {@link Portlet}
     * 
     * @param nBlogId
     *            The {@link Blog} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     * @return True if {@link Blog} is published, false else (unpublished or not assigned)
     */
    public boolean isPublished( int nBlogId, int nPortletId )
    {
        BlogPublication blogPublication = BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nBlogId );

        return blogPublication != null;
    }

    /**
     * Check if the specified {@link Blog} is assigned (unpublished or published) into at least one {@link Portlet}
     * 
     * @param nBlogId
     *            The {@link Blog} identifier
     * @return True if {@link Blog} is assigned (published or unpublished), false else (not assigned)
     */
    public boolean isAssigned( int nBlogId )
    {
        Collection<BlogPublication> listBlogPublication = BlogPublicationHome.getDocPublicationByIdDoc( nBlogId );
        return CollectionUtils.isNotEmpty( listBlogPublication );
    }

    /**
     * Check if the specified {@link Blog} is assigned (unpublished or published) into the specified {@link Portlet}
     * 
     * @param nBlogId
     *            The {@link Blog} identifier
     * @param nPortletId
     *            The {@link Portlet} identifier
     * @return True if {@link Blog} is assigned (published or unpublished), false else (not assigned)
     */
    public boolean isAssigned( int nBlogId, int nPortletId )
    {
        BlogPublication blogPublication = BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nBlogId );

        return ( blogPublication != null );
    }

    /**
     * Return a {@link BlogPublication} from a {@link Portlet} identifier and {@link Blog} identifier
     * 
     * @param nPortletId
     *            the {@link Portlet} identifier
     * @param nBlogId
     *            the {@link Blog} identifier
     * @return a {@link BlogPublication} or null if no object match
     */
    public BlogPublication getBlogPublication( int nPortletId, int nBlogId )
    {
        return BlogPublicationHome.findDocPublicationByPimaryKey( nPortletId, nBlogId );
    }

    /**
     * Loads the list of portlets
     *
     * @return the {@link Collection} of the portlets
     */
    public Collection<Portlet> getBlogsPortlets( )
    {
        Plugin plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        Collection<Portlet> listPortletsAll = new ArrayList<>( );

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
        Collection<Portlet> listPortletsAll = new ArrayList<>( );

        for ( PortletType portletType : plugin.getPortletTypes( ) )
        {
            List<Portlet> listPortlet = PortletHome.findByType( portletType.getId( ) );
            String className = BlogPortletHome.class.getName( );
            String strPortletTypeId = PortletTypeHome.getPortletTypeId( className );

            if ( portletType.getId( ).equals( strPortletTypeId ) )
            {
                for ( Portlet pt : listPortlet )
                {
                    if ( CollectionUtils.isEmpty( BlogPublicationHome.getDocPublicationByPortlet( pt.getId( ) ) ) )
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
     * @param strBlogId
     *            the blog identifier
     * @return the {@link Collection} of the portlets
     */
    public Collection<Portlet> getPortletsByBlogId( String strBlogId )
    {
        Collection<BlogPublication> listBlogPublication = BlogPublicationHome.getDocPublicationByIdDoc( Integer.parseInt( strBlogId ) );
        Collection<Portlet> listPortlets = new ArrayList<>( );

        for ( BlogPublication blogPublication : listBlogPublication )
        {
            listPortlets.add( PortletHome.findByPrimaryKey( blogPublication.getIdPortlet( ) ) );
        }

        return listPortlets;
    }

    /**
     * Loads the list of the blog whose filter and date publication is specified Return published blogs since the publication date. The is also filtered with
     * the blogFilter
     *
     * @param datePublishing
     *            The start publication date
     * @param dateEndPublishing
     *            The end publication date
     * @param blogFilter
     *            The filter for the published blog. The filter can be null or empty. The array of Ids will not be taked in account.
     * @param locale
     *            The locale is used to get the list of blogs with the findByFilter method
     * @return the list of the blog in form of a List. return null if datePublishing is null
     */
    public Collection<Blog> getPublishedBlogsSinceDate( Date datePublishing, Date dateEndPublishing, BlogFilter blogFilter, Locale locale )
    {
        if ( datePublishing == null )
        {
            return null;
        }

        Collection<BlogPublication> listBlogPublication = BlogPublicationHome.findSinceDatePublishingAndStatus( datePublishing, dateEndPublishing, 1 );

        if ( CollectionUtils.isEmpty( listBlogPublication ) )
        {
            return new ArrayList<>( );
        }

        Set<Integer> sIds = new HashSet<>( );
        BlogFilter publishedBlogFilter = blogFilter;

        if ( publishedBlogFilter == null )
        {
            publishedBlogFilter = new BlogFilter( );
        }

        for ( BlogPublication blogPublication : listBlogPublication )
        {
            sIds.add( blogPublication.getIdBlog( ) );
        }

        publishedBlogFilter.setIds( sIds.toArray( new Integer [ sIds.size( )] ) );

        return BlogHome.findByFilter( publishedBlogFilter );
    }

    /**
     * Get the list of id of published blogs associated with a given collection of portlets.
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param datePublishing
     *            The publishing date
     * @param dateEndPublishing
     *            The publishing end date
     * @param plugin
     *            The blog plugin
     * @return The list of blogs id.
     */
    public static List<Integer> getPublishedBlogsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        return BlogPublicationHome.getPublishedBlogsIdsListByPortletIds( nPortletsIds, datePublishing, dateEndPublishing, plugin );
    }

    /**
     * Get the list of id of published Blogs, associated with a given collection of porlets, which has been updated since the dateUpdated
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param dateUpdated
     *            The date from the blogs had to be updated
     * @param plugin
     *            The plugin
     * @return The list of Blogs id.
     */
    public static List<Integer> getLastPublishedBlogsIdsListByPortletIds( int [ ] nPortletsIds, Date dateUpdated, Plugin plugin )
    {
        return BlogPublicationHome.getLastPublishedDocumentsIdsListByPortletIds( nPortletsIds, dateUpdated, plugin );
    }

}
