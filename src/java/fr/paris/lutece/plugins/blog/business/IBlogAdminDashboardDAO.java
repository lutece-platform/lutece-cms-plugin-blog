package fr.paris.lutece.plugins.blog.business;

import java.sql.Date;

public interface IBlogAdminDashboardDAO
{
    void updateMaximumPublicationDate( Date date, fr.paris.lutece.portal.service.plugin.Plugin plugin );

    java.util.Date selectMaximumPublicationDate( fr.paris.lutece.portal.service.plugin.Plugin plugin );

}
