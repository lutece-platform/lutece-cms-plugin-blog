package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.portal.service.plugin.PluginService;


/**
 * class HtmldocsPlugin
 */
public class HtmldocsPlugin extends PluginDefaultImplementation
{
    /**
     * The name of the plugin
     */
    public static final String PLUGIN_NAME = "blog";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(  )
    {
        // Do nothing
    }

    /**
     * Get the announce plugin
     * @return The announce plugin
     */
    public static Plugin getPlugin(  )
    {
        return PluginService.getPlugin( PLUGIN_NAME );
    }
}
