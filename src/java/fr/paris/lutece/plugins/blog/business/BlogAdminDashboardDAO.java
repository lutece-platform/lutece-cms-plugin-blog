package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Date;

public final class BlogAdminDashboardDAO implements IBlogAdminDashboardDAO
{
    //blog_admin_dashboard
    private static final String SQL_QUERY_SELECT_NUMBER_MANDATORY_TAGS = " SELECT number_mandatory_tags FROM blog_admin_dashboard WHERE id_dashboard = 1";
    private static final String SQL_QUERY_UPDATE_NUMBER_MANDATORY_TAGS = " UPDATE blog_admin_dashboard SET number_mandatory_tags = ? WHERE id_dashboard = 1";
    private static final String SQL_QUERY_UPDATE_MAXIMUM_PUBLICATION_DATE = " UPDATE blog_admin_dashboard SET maximum_publication_date = ? WHERE id_dashboard = 1";
    private static final String SQL_QUERY_SELECT_MAXIMUM_PUBLICATION_DATE = " SELECT maximum_publication_date FROM blog_admin_dashboard WHERE id_dashboard = 1";
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

    //blog_admin_dashboard
    @Override
    public void updateMaximumPublicationDate( Date date, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_MAXIMUM_PUBLICATION_DATE ) )
        {
            daoUtil.setDate( 1, date );
            daoUtil.executeUpdate( );
        }
    }
    @Override
    public java.util.Date selectMaximumPublicationDate( Plugin plugin )
    {
        java.util.Date date = null;
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
