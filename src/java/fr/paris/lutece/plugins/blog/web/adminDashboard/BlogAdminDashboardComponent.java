package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.plugins.blog.business.BlogAdminDashboardHome;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.text.SimpleDateFormat;

public class BlogAdminDashboardComponent extends AdminDashboardComponent
    {
        private static final String DASHBOARD_PAGE = "jsp/admin/AdminTechnicalMenu.jsp";
        private static final String ACCESS_DENIED_MESSAGE = "portal.message.user.accessDenied";
        public static final String RIGHT_AVANCED_CONFIGURATION = "BLOG_AVANCED_CONFIGURATION";
        private static final String TEMPLATE_MANAGE_MAX_PUBLICATION_DATE = "admin/plugins/blog/dashboard/manage_maximum_publication_date.html";
        private static final String PARAMETER_MAX_PUBLICATION_DATE_VALUE = "maxPublicationDate";
        private static final String ACTION_MANAGE_MAX_PUBLICATION_DATE = "manageMaxPublicationDate";
        private static final String DASHBOARD_PAGE_MAX_PUBLICATION_DATE = "?tab=manageMaxPublicationDate";
        // tab
        private static final String PARAMETER_TAB = "tab";
        /**
         * Manage the maximum publication date for a blog post
         * @param request
         * @return
         * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
         * @throws java.text.ParseException
         */
        @Action( ACTION_MANAGE_MAX_PUBLICATION_DATE )
        public String manageMaxPublicationDate( HttpServletRequest request ) throws fr.paris.lutece.portal.service.admin.AccessDeniedException
        {
            AdminUser adminUser = fr.paris.lutece.portal.service.admin.AdminUserService.getAdminUser( request );
            if ( !adminUser.checkRight( RIGHT_AVANCED_CONFIGURATION ) )
            {
                String strMessage = fr.paris.lutece.portal.service.i18n.I18nService.getLocalizedString( ACCESS_DENIED_MESSAGE, request.getLocale( ) );
                throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( strMessage );
            }
            if( request.getParameter( PARAMETER_MAX_PUBLICATION_DATE_VALUE ) != null)
            {
                String strMaxPublicationDate = request.getParameter( PARAMETER_MAX_PUBLICATION_DATE_VALUE );

                BlogAdminDashboardHome.updateMaximumPublicationDate( formatStringToSqlDate(strMaxPublicationDate) );
            }
            String strUrl = fr.paris.lutece.portal.service.util.AppPathService.getBaseUrl( request ) + DASHBOARD_PAGE + DASHBOARD_PAGE_MAX_PUBLICATION_DATE;
            return strUrl;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDashboardData( AdminUser user, HttpServletRequest request )
        {
            String strTemplate = "";
            Map<String, Object> model = new HashMap<>( );

            Date maxPublicationDate =  BlogAdminDashboardHome.selectMaximumPublicationDate( );
            model.put( PARAMETER_MAX_PUBLICATION_DATE_VALUE, maxPublicationDate );
            HtmlTemplate template2 = AppTemplateService.getTemplate( TEMPLATE_MANAGE_MAX_PUBLICATION_DATE, user.getLocale( ), model );
            strTemplate += template2.getHtml( );

            return strTemplate;
        }

        public java.sql.Date formatStringToSqlDate(String strDate)
        {
            Date sqlDate = null;
            try
            {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date utilDate = sdf.parse(strDate);
                sqlDate = new Date(utilDate.getTime());
            }
            catch (java.text.ParseException e)
            {
                AppLogService.error("Error parsing date", e);
            }
            return (java.sql.Date) sqlDate;
        }
}
