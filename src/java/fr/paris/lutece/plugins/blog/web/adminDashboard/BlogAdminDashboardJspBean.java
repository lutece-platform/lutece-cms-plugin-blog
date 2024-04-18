package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;



/**
 * This class provides the user interface to manage general Blog feature in the site admin dashboard ( modify mandatory tag number )
 */
@Controller( controllerJsp = "ManageAdminDashboard.jsp", controllerPath = "jsp/admin/plugins/blog/", right = "BLOG_AVANCED_CONFIGURATION" )

public class BlogAdminDashboardJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = 3045411044102177294L;
    private static final String DASHBOARD_PAGE = "jsp/admin/AdminTechnicalMenu.jsp";
    private static final String ACCESS_DENIED_MESSAGE = "portal.message.user.accessDenied";
    public static final String RIGHT_AVANCED_CONFIGURATION = "BLOG_AVANCED_CONFIGURATION";


    public String getDashboardPage( HttpServletRequest request )throws AccessDeniedException
    {
        AdminUser adminUser = AdminUserService.getAdminUser( request );
        if ( !adminUser.checkRight( RIGHT_AVANCED_CONFIGURATION ) )
        {
            String strMessage = I18nService.getLocalizedString( ACCESS_DENIED_MESSAGE, request.getLocale( ) );
            throw new AccessDeniedException( strMessage );
        } else {
            return AppPathService.getBaseUrl( request ) + DASHBOARD_PAGE;
        }
    }

}
