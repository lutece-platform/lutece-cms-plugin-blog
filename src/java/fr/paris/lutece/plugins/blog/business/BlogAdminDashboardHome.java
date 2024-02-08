package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.spring.SpringContextService;


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
