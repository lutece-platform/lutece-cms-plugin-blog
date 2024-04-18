package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.plugins.blog.business.BlogAdminDashboardHome;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage general Blog feature in the site admin dashboard ( modify mandatory tag number )
 */
@Controller( controllerJsp = "ManageAdminDashboard.jsp", controllerPath = "jsp/admin/plugins/blog/", right = "BLOG_AVANCED_CONFIGURATION" )

public class BlogAdminDashboardComponent extends AdminDashboardComponent
    {
        private static final long serialVersionUID = 3045411044102177294L;
        private static final String MARK_NUMBER_MANDATORY_TAGS = "number_mandatory_tags";
        private static final String TEMPLATE_MANDATORY_TAGS = "admin/plugins/blog/dashboard/modify_mandatory_blog_tags.html";
        private static final String ACTION_UPDATE_MANDATORY_TAG_NUMBER = "updateMandatoryTagNumber";
        private static final String DASHBOARD_PAGE = "jsp/admin/AdminTechnicalMenu.jsp";
        private static final String DASHBOARD_PAGE_TAG = "?tab=modifyMandatoryBlogTagNumber";
        private static final String ACCESS_DENIED_MESSAGE = "portal.message.user.accessDenied";
        public static final String RIGHT_AVANCED_CONFIGURATION = "BLOG_AVANCED_CONFIGURATION";
        private static final String PARAMETER_NUMBER_MANDATORY_TAGS = "numberMandatoryTag";
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDashboardData( AdminUser user, HttpServletRequest request )
        {
                Map<String, Object> model = new HashMap<>( );
                model.put( MARK_NUMBER_MANDATORY_TAGS, BlogAdminDashboardHome.selectNumberMandatoryTags( ) );
                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANDATORY_TAGS, user.getLocale( ), model );

            return template.getHtml( );

        }
        @Action( ACTION_UPDATE_MANDATORY_TAG_NUMBER )
        public String updateMandatoryTagNumber( HttpServletRequest request ) throws fr.paris.lutece.portal.service.admin.AccessDeniedException
        {
            AdminUser adminUser = fr.paris.lutece.portal.service.admin.AdminUserService.getAdminUser( request );
            if ( !adminUser.checkRight( RIGHT_AVANCED_CONFIGURATION ) )
            {
                String strMessage = fr.paris.lutece.portal.service.i18n.I18nService.getLocalizedString( ACCESS_DENIED_MESSAGE, request.getLocale( ) );
                throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( strMessage );
            }
            int nNumberMandatoryTags = 0;
            if( request.getParameter( PARAMETER_NUMBER_MANDATORY_TAGS ) != null)
            {
                nNumberMandatoryTags =  Integer.parseInt( request.getParameter( PARAMETER_NUMBER_MANDATORY_TAGS ) );
            }
            BlogAdminDashboardHome.updateNumberMandatoryTags( nNumberMandatoryTags );
            String strUrl = fr.paris.lutece.portal.service.util.AppPathService.getBaseUrl( request ) + DASHBOARD_PAGE + DASHBOARD_PAGE_TAG;
            return strUrl;
        }
}
