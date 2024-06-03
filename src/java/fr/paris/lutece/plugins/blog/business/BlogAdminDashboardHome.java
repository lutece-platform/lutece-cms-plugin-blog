package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.sql.Date;


public final class  BlogAdminDashboardHome
{
    private static IBlogAdminDashboardDAO _dao = SpringContextService.getBean( "blog.blogAdminDashboardDAO" );
    private static fr.paris.lutece.portal.service.plugin.Plugin _plugin = fr.paris.lutece.portal.service.plugin.PluginService.getPlugin( "blog" );

    /**
     * Load the maximum publication date
     * @return the maximum publication date
     */
    public static java.util.Date selectMaximumPublicationDate(  )
    {
        return _dao.selectMaximumPublicationDate( _plugin );
    }
    /**
     * Update the maximum publication date
     * @param date the maximum publication date
     */
    public static void updateMaximumPublicationDate( Date date )
    {
        _dao.updateMaximumPublicationDate( date, _plugin );
    }
    /**
     * Load the number of mandatory tags
     * @return the number of mandatory tags
     */
    public static int selectNumberMandatoryTags(  )
    {
        return _dao.selectNumberMandatoryTags( _plugin );
    }

    /**
     * Update the number of mandatory tags
     * @param nNumberMandatoryTags the number of mandatory tags
     */
    public static void updateNumberMandatoryTags( int nNumberMandatoryTags )
    {
        _dao.updateNumberMandatoryTags( nNumberMandatoryTags, _plugin );
    }

}
