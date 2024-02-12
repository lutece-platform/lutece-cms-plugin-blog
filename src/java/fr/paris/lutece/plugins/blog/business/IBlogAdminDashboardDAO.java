package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import java.sql.Date;

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

    void updateNumberMandatoryTags( int nNumberMandatoryTags, fr.paris.lutece.portal.service.plugin.Plugin plugin );

    void updateMaximumPublicationDate( Date date, fr.paris.lutece.portal.service.plugin.Plugin plugin );

    java.util.Date selectMaximumPublicationDate( fr.paris.lutece.portal.service.plugin.Plugin plugin );
}
