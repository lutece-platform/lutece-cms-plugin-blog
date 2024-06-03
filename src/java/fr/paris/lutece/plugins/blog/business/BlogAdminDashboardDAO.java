package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public final class BlogAdminDashboardDAO implements IBlogAdminDashboardDAO
{
    //blog_admin_dashboard
    private static final String SQL_QUERY_SELECT_NUMBER_MANDATORY_TAGS = " SELECT number_mandatory_tags FROM blog_admin_dashboard WHERE id_dashboard = 1";
    private static final String SQL_QUERY_UPDATE_NUMBER_MANDATORY_TAGS = " UPDATE blog_admin_dashboard SET number_mandatory_tags = ? WHERE id_dashboard = 1";
    @Override
    public int selectNumberMandatoryTags( Plugin plugin )
    {
        int nNumberMandatoryTags = 0;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NUMBER_MANDATORY_TAGS ) )
        {
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                nNumberMandatoryTags = daoUtil.getInt( 1 );
            }
        }
        return nNumberMandatoryTags;
    }
    @Override
    public void updateNumberMandatoryTags( int nNumberMandatoryTags, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_NUMBER_MANDATORY_TAGS ) )
        {
            daoUtil.setInt( 1, nNumberMandatoryTags );
            daoUtil.executeUpdate( );
        }
    }
}
