package fr.paris.lutece.plugins.blog.web.admindashboard;

import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.user.AdminUserResourceIdService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;

/**
 * This class provides the user interface to manage general Blog feature in the site admin dashboard ( modify mandatory tag number )
 */
@Controller( controllerJsp = "ManageAdminDashboard.jsp", controllerPath = "jsp/admin/plugins/blog/", right = "BLOG_ADVANCED_CONFIGURATION" )
public class BlogAdminDashboardJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = 3045411044102177294L;
    private static final String UNAUTHORIZED = "Unauthorized";

    public String doModifyBlogAdvancedParameters( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS,
                getUser( ) ) )
         {
             throw new AccessDeniedException( UNAUTHORIZED );
         }

        return getAdminDashboardsUrl( request, BlogAdminDashboardComponent.ANCHOR_ADMIN_DASHBOARDS );
    }
}
