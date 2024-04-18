package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import java.util.Date;

public final class BlogAdminDashboardDAO implements IBlogAdminDashboardDAO
{
    private static final String SQL_QUERY_UPDATE_MAXIMUM_PUBLICATION_DATE = " UPDATE blog_admin_dashboard SET maximum_publication_date = ? WHERE id_dashboard = 1";
    private static final String SQL_QUERY_SELECT_MAXIMUM_PUBLICATION_DATE = " SELECT maximum_publication_date FROM blog_admin_dashboard WHERE id_dashboard = 1";
    //blog_admin_dashboard
    @Override
    public void updateMaximumPublicationDate( java.sql.Date date, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_MAXIMUM_PUBLICATION_DATE ) )
        {
            daoUtil.setDate( 1, date );
            daoUtil.executeUpdate( );
        }
    }
    @Override
    public Date selectMaximumPublicationDate( Plugin plugin )
    {
        Date date = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAXIMUM_PUBLICATION_DATE ) )
        {
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                date = daoUtil.getDate( 1 );
            }
        }
        return date;
    }
}
