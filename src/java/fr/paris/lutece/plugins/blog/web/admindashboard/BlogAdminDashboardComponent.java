package fr.paris.lutece.plugins.blog.web.admindashboard;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.service.BlogParameterService;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.user.AdminUserResourceIdService;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BlogAdminDashboardComponent extends AdminDashboardComponent
{
    public static final String ANCHOR_ADMIN_DASHBOARDS = "blogAdminDashboard";
    private static final String TEMPLATE_ADMIN_DASHBOARD = "admin/plugins/blog/dashboard/blog_adminDashboard.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Blog.PROPERTY_RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, (User) user ) )
        {
            return StringUtils.EMPTY;
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        BlogParameterService blogParameterService = BlogParameterService.getInstance();
        model.put(BlogParameterService.MARK_DEFAULT_NUMBER_MANDATORY_TAGS, blogParameterService.getNumberMandatoryTags());
        model.put(BlogParameterService.MARK_DEFAULT_DATE_END_PUBLISHING, blogParameterService.getDefaultDateEndPublishing());

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_DASHBOARD, user.getLocale( ), model );

        return template.getHtml( );
    }
}
