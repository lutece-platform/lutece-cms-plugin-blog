package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IBlogAdminDashboardDAO
{
    /**
     * Load the number of mandatory tags
     *
     * @param plugin
     *            the plugin
     * @return the number of mandatory tags
     */
    int selectNumberMandatoryTags( Plugin plugin );

    void updateNumberMandatoryTags( int nNumberMandatoryTags, Plugin plugin );

    void updateMaximumPublicationDate( java.sql.Date date, fr.paris.lutece.portal.service.plugin.Plugin plugin );

    java.util.Date selectMaximumPublicationDate( fr.paris.lutece.portal.service.plugin.Plugin plugin );

}
