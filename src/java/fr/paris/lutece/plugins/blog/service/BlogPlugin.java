package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * class BlogsPlugin
 */
public class BlogPlugin extends PluginDefaultImplementation
{
    /**
     * The name of the plugin
     */
    public static final String PLUGIN_NAME = "blog";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( )
    {
        // Do nothing
    }

    /**
     * Get the blog plugin
     * 
     * @return The blog plugin
     */
    public static Plugin getPlugin( )
    {
        return PluginService.getPlugin( PLUGIN_NAME );
    }
}
