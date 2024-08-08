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
    int selectNumberMandatoryTags(int idDashboard, Plugin plugin );

    void updateNumberMandatoryTags(int idDashboard, int nNumberMandatoryTags, Plugin plugin );

    void updateMaximumPublicationDate(int idDashboard, Date date, Plugin plugin );

    java.util.Date selectMaximumPublicationDate(int idDashboard, Plugin plugin );

}
