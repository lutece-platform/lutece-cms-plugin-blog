package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.sql.Date;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;


public final class  BlogAdminDashboardHome
{
    private static IBlogAdminDashboardDAO _dao = SpringContextService.getBean( "blog.blogAdminDashboardDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "blog" );

    /**
     * Load the maximum publication date
     * @return the maximum publication date
     */
    public static java.util.Date selectMaximumPublicationDate( int idDashboard )
    {
        return _dao.selectMaximumPublicationDate(idDashboard, _plugin );
    }
    /**
     * Update the maximum publication date
     * @param date the maximum publication date
     */
    public static void updateMaximumPublicationDate( int idDashboard, Date date )
    {
        _dao.updateMaximumPublicationDate(idDashboard, date, _plugin );
    }
    /**
     * Load the number of mandatory tags
     * @return the number of mandatory tags
     */
    public static int selectNumberMandatoryTags(  int idDashboard )
    {
        return _dao.selectNumberMandatoryTags(idDashboard, _plugin );
    }

    /**
     * Update the number of mandatory tags
     * @param nNumberMandatoryTags the number of mandatory tags
     */
    public static void updateNumberMandatoryTags(int idDashboard, int nNumberMandatoryTags )
    {
        _dao.updateNumberMandatoryTags(idDashboard, nNumberMandatoryTags, _plugin );
    }

}
