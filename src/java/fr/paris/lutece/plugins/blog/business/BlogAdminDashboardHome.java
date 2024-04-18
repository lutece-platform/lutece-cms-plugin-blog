package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.sql.Date;


public final class  BlogAdminDashboardHome
{
    private static IBlogAdminDashboardDAO _dao = SpringContextService.getBean( "blog.blogAdminDashboardDAO" );
    private static fr.paris.lutece.portal.service.plugin.Plugin _plugin = fr.paris.lutece.portal.service.plugin.PluginService.getPlugin( "blog" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private BlogAdminDashboardHome(  )
    {

    }


}
