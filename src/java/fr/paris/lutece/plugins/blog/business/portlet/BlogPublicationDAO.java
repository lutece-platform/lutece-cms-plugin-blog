package fr.paris.lutece.plugins.blog.business.portlet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class BlogPublicationDAO implements IBlogPublicationDAO
{

    // Category
    private static final String SQL_QUERY_INSERT_BLOGS_PORTLET = "INSERT INTO blog_list_portlet_htmldocs ( id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order ) VALUES ( ? , ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE_BLOGS_PORTLET = " DELETE FROM blog_list_portlet_htmldocs WHERE id_blog = ? ";
    private static final String SQL_QUERY_DELETE_BLOGS_PORTLET_BY_ID_PORTLET = " DELETE FROM blog_list_portlet_htmldocs WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_CATEGORY_PORTLET = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs WHERE id_blog = ? order by document_order ";
    private static final String SQL_QUERY_REMOVE_BLOGS_PORTLET = " DELETE FROM blog_list_portlet_htmldocs WHERE id_portlet = ? and id_blog= ?";

    private static final String SQL_QUERY_UPDATE_BLOGS_PORTLET = "UPDATE blog_list_portlet_htmldocs set id_portlet= ?, id_blog= ?, date_begin_publishing= ?, date_end_publishing= ?, status= ?, document_order= ? WHERE  id_blog= ?";
    private static final String SQL_QUERY_SELECT_PUBLICATION_PORTLET = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs WHERE id_blog = ? and id_portlet = ? order by document_order";
    private static final String SQL_QUERY_SELECT_PUBLICATION_ALL = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs order by document_order";
    private static final String SQL_QUERY_SELECT_DOC_PUBLICATION_BY_PORTLET = "SELECT id_portlet , id_blog, date_begin_publishing, date_end_publishing, status, document_order FROM blog_list_portlet_htmldocs WHERE id_portlet = ? order by document_order ";
    private static final String SQL_QUERY_SELECT_BY_DATE_PUBLISHING_AND_STATUS = " SELECT id_portlet, id_blog, document_order, date_begin_publishing FROM blog_list_portlet_htmldocs WHERE date_begin_publishing <= ? AND date_end_publishing >= ? AND status = ? ORDER BY document_order ";
    private static final String SQL_QUERY_SELECT_BY_PORTLET_ID_AND_STATUS = " SELECT DISTINCT pub.id_blog FROM blog_list_portlet_htmldocs pub WHERE  pub.status = ? AND pub.date_begin_publishing <= ? AND  pub.date_end_publishing >= ? AND pub.id_portlet IN ";

    private static final String SQL_FILTER_BEGIN = " (";
    private static final String SQL_TAGS_END = ") ";
    private static final String CONSTANT_QUESTION_MARK = "?";
    private static final String CONSTANT_COMMA = ",";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * {@inheritDoc }
     */
    @Override
    public void insertBlogsId( BlogPublication blogPublication, Plugin plugin )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_BLOGS_PORTLET, plugin );

        daoUtil.setInt( 1, blogPublication.getIdPortlet( ) );
        daoUtil.setInt( 2, blogPublication.getIdDocument( ) );
        daoUtil.setDate( 3, blogPublication.getDateBeginPublishing( ) );
        daoUtil.setDate( 4, blogPublication.getDateEndPublishing( ) );
        daoUtil.setInt( 5, blogPublication.getStatus( ) );
        daoUtil.setInt( 6, blogPublication.getDocumentOrder( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( BlogPublication blogPublication, Plugin plugin )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_BLOGS_PORTLET, plugin );
        daoUtil.setInt( 1, blogPublication.getIdPortlet( ) );
        daoUtil.setInt( 2, blogPublication.getIdDocument( ) );
        daoUtil.setDate( 3, blogPublication.getDateBeginPublishing( ) );
        daoUtil.setDate( 4, blogPublication.getDateEndPublishing( ) );
        daoUtil.setInt( 5, blogPublication.getStatus( ) );
        daoUtil.setInt( 6, blogPublication.getDocumentOrder( ) );

        daoUtil.setInt( 7, blogPublication.getIdDocument( ) );

        daoUtil.executeUpdate( );

        daoUtil.free( );

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteBlogsId( int nDocId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BLOGS_PORTLET, plugin );
        daoUtil.setInt( 1, nDocId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteBlogByIdPortlet( int nIdPortlet, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BLOGS_PORTLET_BY_ID_PORTLET, plugin );
        daoUtil.setInt( 1, nIdPortlet );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void remove( int nDocId, int nIdPortlet, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BLOGS_PORTLET, plugin );
        daoUtil.setInt( 1, nIdPortlet );
        daoUtil.setInt( 2, nDocId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BlogPublication> loadBlogsId( int nDocId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CATEGORY_PORTLET, plugin );
        daoUtil.setInt( 1, nDocId );
        daoUtil.executeQuery( );

        List<BlogPublication> nListIdCategory = new ArrayList<BlogPublication>( );

        while ( daoUtil.next( ) )
        {

            BlogPublication blogPub = new BlogPublication( );
            blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
            blogPub.setIdDocument( daoUtil.getInt( 2 ) );
            blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
            blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
            blogPub.setStatus( daoUtil.getInt( 5 ) );
            blogPub.setDocumentOrder( daoUtil.getInt( 6 ) );

            nListIdCategory.add( blogPub );
        }

        daoUtil.free( );

        return nListIdCategory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BlogPublication> loadBlogsByPortlet( int nIdPortlet, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOC_PUBLICATION_BY_PORTLET, plugin );
        daoUtil.setInt( 1, nIdPortlet );
        daoUtil.executeQuery( );

        List<BlogPublication> nListIdCategory = new ArrayList<BlogPublication>( );

        while ( daoUtil.next( ) )
        {

            BlogPublication blogPub = new BlogPublication( );
            blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
            blogPub.setIdDocument( daoUtil.getInt( 2 ) );
            blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
            blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
            blogPub.setStatus( daoUtil.getInt( 5 ) );
            blogPub.setDocumentOrder( daoUtil.getInt( 6 ) );

            nListIdCategory.add( blogPub );
        }

        daoUtil.free( );

        return nListIdCategory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public BlogPublication loadBlogsPublication( int nDocId, int nPortletId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLICATION_PORTLET, plugin );
        daoUtil.setInt( 1, nDocId );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.executeQuery( );

        BlogPublication blogPub = new BlogPublication( );

        if ( daoUtil.next( ) )
        {

            blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
            blogPub.setIdDocument( daoUtil.getInt( 2 ) );
            blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
            blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
            blogPub.setStatus( daoUtil.getInt( 5 ) );
            blogPub.setDocumentOrder( daoUtil.getInt( 6 ) );

        }

        daoUtil.free( );

        return blogPub;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BlogPublication> loadAllBlogsPublication( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLICATION_ALL, plugin );
        daoUtil.executeQuery( );

        List<BlogPublication> nListIdCategory = new ArrayList<BlogPublication>( );

        while ( daoUtil.next( ) )
        {

            BlogPublication blogPub = new BlogPublication( );
            blogPub.setIdPortlet( daoUtil.getInt( 1 ) );
            blogPub.setIdDocument( daoUtil.getInt( 2 ) );
            blogPub.setDateBeginPublishing( daoUtil.getDate( 3 ) );
            blogPub.setDateEndPublishing( daoUtil.getDate( 4 ) );
            blogPub.setStatus( daoUtil.getInt( 5 ) );
            blogPub.setDocumentOrder( daoUtil.getInt( 6 ) );

            nListIdCategory.add( blogPub );
        }

        daoUtil.free( );

        return nListIdCategory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<BlogPublication> selectSinceDatePublishingAndStatus( Date datePublishing, Date dateEndPublication, int nStatus, Plugin plugin )
    {
        Collection<BlogPublication> listDocumentPublication = new ArrayList<BlogPublication>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DATE_PUBLISHING_AND_STATUS, plugin );
        daoUtil.setTimestamp( 1, new Timestamp( datePublishing.getTime( ) ) );
        daoUtil.setTimestamp( 2, new Timestamp( dateEndPublication.getTime( ) ) );

        daoUtil.setInt( 3, nStatus );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            BlogPublication documentPublication = new BlogPublication( );
            documentPublication.setIdPortlet( daoUtil.getInt( 1 ) );
            documentPublication.setIdDocument( daoUtil.getInt( 2 ) );
            documentPublication.setDocumentOrder( daoUtil.getInt( 3 ) );
            documentPublication.setStatus( nStatus );
            documentPublication.setDateBeginPublishing( daoUtil.getDate( 4 ) );
            listDocumentPublication.add( documentPublication );
        }

        daoUtil.free( );

        return listDocumentPublication;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getPublishedDocumentsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        List<Integer> listIds = new ArrayList<Integer>( );
        if ( nPortletsIds == null || nPortletsIds.length == 0 )
        {
            return listIds;
        }
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_BY_PORTLET_ID_AND_STATUS );
        sbSql.append( SQL_FILTER_BEGIN );

        for ( int i = 0; i < nPortletsIds.length; i++ )
        {
            sbSql.append( CONSTANT_QUESTION_MARK );
            if ( i + 1 < nPortletsIds.length )
            {
                sbSql.append( CONSTANT_COMMA );
            }
        }
        sbSql.append( SQL_TAGS_END + "order by document_order" );

        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, 1 );
        daoUtil.setTimestamp( nIndex++, new Timestamp( datePublishing.getTime( ) ) );
        daoUtil.setTimestamp( nIndex++, new Timestamp( dateEndPublishing.getTime( ) ) );

        for ( int nPortletId : nPortletsIds )
        {
            daoUtil.setInt( nIndex++, nPortletId );
        }
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listIds.add( daoUtil.getInt( 1 ) );
        }
        daoUtil.free( );
        return listIds;

    }

}
