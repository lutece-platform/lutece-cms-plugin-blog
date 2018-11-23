/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.blog.business.rss;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.service.BlogPlugin;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.service.PublishingService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.rss.IFeedResource;
import fr.paris.lutece.portal.business.rss.ResourceRss;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 * BlogResourceRss
 */
public class BlogResourceRss extends ResourceRss
{
    // templates
    private static final String TEMPLATE_PUSH_RSS_XML_BLOG = "admin/plugins/blog/rss/rss_xml_blogs.html";
    private static final String TEMPLATE_RSS_BLOG_ITEM_TITLE = "admin/plugins/blog/rss/rss_blogs_item_title.html";
    private static final String TEMPLATE_TASK_CREATE_CONFIG = "admin/plugins/blog/rss/rss_create_config.html";
    private static final String TEMPLATE_TASK_MODIFY_CONFIG = "admin/plugins/blog/rss/rss_modify_config.html";

    // JSPs

    // Markers
    private static final String MARK_RSS_ITEM_TITLE = "item_title";
    private static final String MARK_RSS_ITEM_DESCRIPTION = "item_description";

    private static final String MARK_RSS_SITE_URL = "site_url";
    private static final String MARK_RSS_FILE_LANGUAGE = "file_language";
    private static final String MARK_PORTLET_LIST = "portlet_list";
    private static final String MARK_ID_PORTLET = "id_portlet";
    private static final String MARK_RSS_ITEM_BLOG = "blog";

    // Parameters
    private static final String PARAMETER_ID_BLOG = "id_blog";
    private static final String PARAMETER_ID_PORTLET = "id_portlet";

    // Messages
    private static final String PROPERTY_SITE_LANGUAGE = "rss.language";
    private static final String PROPERTY_WEBAPP_PROD_URL = "lutece.prod.url";
    private static final String PROPERTY_DESCRIPTION_WIRE = "blog.resource_rss.description_wire";
    private static final String PROPERTY_TITLE_WIRE = "blog.resource_rss.title_wire";
    private static final String MARK_ITEM_LIST = "itemList";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contentResourceRss( )
    {

        if ( BlogPublicationHome.getAllDocPublication( ).size( ) > 0 )
        {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveConfig( HttpServletRequest request, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        String stridPortlet = request.getParameter( PARAMETER_ID_PORTLET );

        BlogResourceRssConfig config = new BlogResourceRssConfig( );
        config.setIdRss( this.getId( ) );
        config.setIdPortlet( Integer.parseInt( stridPortlet ) );

        BlogResourceRssConfigHome.create( config, plugin );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doUpdateConfig( HttpServletRequest request, Locale locale )
    {
        Plugin pluginForm = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        String stridPortlet = request.getParameter( PARAMETER_ID_PORTLET );

        BlogResourceRssConfig config = new BlogResourceRssConfig( );
        config.setIdRss( this.getId( ) );
        config.setIdPortlet( Integer.parseInt( stridPortlet ) );

        BlogResourceRssConfigHome.update( config, pluginForm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doValidateConfigForm( HttpServletRequest request, Locale locale )
    {
        this.setDescription( I18nService.getLocalizedString( PROPERTY_DESCRIPTION_WIRE, locale ) );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayCreateConfigForm( HttpServletRequest request, Locale locale )
    {

        List<Portlet> portletList = (List<Portlet>) PublishingService.getInstance( ).getBlogsPortlets( );

        ReferenceList referencelist = new ReferenceList( );
        HashMap<String, Object> model = new HashMap<String, Object>( );

        for ( Portlet portlet : portletList )
        {
            referencelist.addItem( portlet.getId( ), portlet.getName( ) );
        }

        model.put( MARK_PORTLET_LIST, referencelist );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_CREATE_CONFIG, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayModifyConfigForm( HttpServletRequest request, Locale locale )
    {
        Plugin plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        List<Portlet> portletList = (List<Portlet>) PublishingService.getInstance( ).getBlogsPortlets( );
        BlogResourceRssConfig config = BlogResourceRssConfigHome.findByPrimaryKey( this.getId( ), plugin );

        ReferenceList referencelist = new ReferenceList( );
        HashMap<String, Object> model = new HashMap<String, Object>( );

        for ( Portlet portlet : portletList )
        {
            referencelist.addItem( portlet.getId( ), portlet.getName( ) );
        }

        model.put( MARK_ID_PORTLET, config.getIdPortlet( ) );
        model.put( MARK_PORTLET_LIST, referencelist );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_MODIFY_CONFIG, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public String createHtmlRss( )
    {
        HashMap<String, Object> model = new HashMap<String, Object>( );
        Plugin plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        BlogResourceRssConfig config = BlogResourceRssConfigHome.findByPrimaryKey( this.getId( ), plugin );

        String strRssFileLanguage = AppPropertiesService.getProperty( PROPERTY_SITE_LANGUAGE );
        Locale locale = new Locale( strRssFileLanguage );

        String strWebAppUrl = AppPropertiesService.getProperty( PROPERTY_WEBAPP_PROD_URL );
        String strSiteUrl = strWebAppUrl;

        model.put( MARK_RSS_ITEM_TITLE, I18nService.getLocalizedString( PROPERTY_TITLE_WIRE, new Locale( strRssFileLanguage ) ) );
        model.put( MARK_RSS_SITE_URL, strSiteUrl );
        model.put( MARK_RSS_FILE_LANGUAGE, strRssFileLanguage );
        model.put( MARK_RSS_ITEM_DESCRIPTION, I18nService.getLocalizedString( PROPERTY_DESCRIPTION_WIRE, new Locale( strRssFileLanguage ) ) );

        Date dateNow = new Date();
        List<BlogPublication> listDocPub = BlogPublicationHome.getDocPublicationByPortletAndPlublicationDate( config.getIdPortlet( ), dateNow, dateNow );
        List<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>( );

        for ( BlogPublication dcPub : listDocPub )
        {

            HashMap<String, Object> item = new HashMap<String, Object>( );
            Blog blog = BlogService.getInstance( ).loadBlog( dcPub.getIdBlog( ) );
            item.put( MARK_RSS_ITEM_BLOG, blog );

            listItem.add( item );

        }
        model.put( MARK_ITEM_LIST, listItem );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PUSH_RSS_XML_BLOG, locale, model );

        return template.getHtml( );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IFeedResource getFeed( )
    {
        /*
         * String strRssFileLanguage = AppPropertiesService.getProperty( PROPERTY_SITE_LANGUAGE ); Locale locale = new Locale( strRssFileLanguage ); Plugin
         * plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
         * 
         * BlogResourceRssConfig config = BlogResourceRssConfigHome.findByPrimaryKey( this.getId( ), plugin );
         * 
         * String strWebAppUrl = AppPropertiesService.getProperty( PROPERTY_WEBAPP_PROD_URL ); String strSiteUrl = strWebAppUrl;
         * 
         * IFeedResource resource = new FeedResource( ); resource.setTitle( I18nService.getLocalizedString( PROPERTY_TITLE_WIRE, new Locale( strRssFileLanguage
         * ) ) ); resource.setLink( strSiteUrl ); resource.setLanguage( strRssFileLanguage ); resource.setDescription( I18nService.getLocalizedString(
         * PROPERTY_DESCRIPTION_WIRE, new Locale( strRssFileLanguage ) ) );
         * 
         * List<BlogPublication> listDocPub = BlogPublicationHome.getDocPublicationByPortlet( config.getIdPortlet( ) );
         * 
         * List<IFeedResourceItem> listItems = new ArrayList<IFeedResourceItem>( );
         * 
         * for ( BlogPublication dcPub : listDocPub ) {
         * 
         * IFeedResourceItem item = new FeedResourceItem( );
         * 
         * String strTitle; Map<String, Object> model = new HashMap<String, Object>( );
         * 
         * Blog blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( dcPub.getIdDocument( ) ); model.put( MARK_RSS_ITEM_TITLE, blog.getName( ) );
         * model.put( MARK_RSS_ITEM_DESCRIPTION, blog.getDescription( ) ); model.put( MARK_RSS_SITE_URL, strSiteUrl ); model.put( MARK_RSS_FILE_LANGUAGE,
         * strRssFileLanguage );
         * 
         * HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RSS_BLOG_ITEM_TITLE, locale, model ); strTitle = template.getHtml( );
         * 
         * item.setTitle( strTitle ); item.setLink( strSiteUrl + JSP_PAGE_BLOGS + "&id=" + blog.getId( ) ); item.setDescription( blog.getDescription( ) );
         * item.setDate( blog.getCreationDate( ) ); item.setGUID( String.valueOf( blog.getId( ) ) );
         * 
         * listItems.add( item ); }
         * 
         * resource.setItems( listItems );
         * 
         * return resource;
         */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteResourceRssConfig( int idResourceRss )
    {
        Plugin pluginForm = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        BlogResourceRssConfigHome.remove( idResourceRss, pluginForm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getParameterToApply( HttpServletRequest request )
    {
        Map<String, String> map = new HashMap<String, String>( );

        map.put( PARAMETER_ID_BLOG, request.getParameter( PARAMETER_ID_BLOG ) );

        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkResource( )
    {
        Plugin pluginForm = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
        BlogResourceRssConfig config = BlogResourceRssConfigHome.findByPrimaryKey( this.getId( ), pluginForm );

        return ( config != null );
    }
}
