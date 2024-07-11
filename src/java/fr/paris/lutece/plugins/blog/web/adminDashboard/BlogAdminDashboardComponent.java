package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.plugins.blog.business.BlogAdminDashboardHome;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.plugins.blog.web.utils.BlogConstant;
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
    private static final String TEMPLATE_MANAGE_AVANCED_CONFIGURATIONS= "admin/plugins/blog/dashboard/manageAvancedConfigurations.html";
    private static final String PARAMETER_MAX_PUBLICATION_DATE_VALUE = "maxPublicationDate";
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {     String strTemplate = "";

        Map<String, Object> model = new HashMap<>( );
        String idDashboardStr = AppPropertiesService.getProperty(BlogConstant.PROPERTY_ADVANCED_MAIN_DASHBOARD_ID);
        int idDashboard = (idDashboardStr != null) ? Integer.parseInt(idDashboardStr) : 1;
        model.put( MARK_NUMBER_MANDATORY_TAGS, BlogAdminDashboardHome.selectNumberMandatoryTags( idDashboard ) );
        java.util.Date maxPublicationDate =  BlogAdminDashboardHome.selectMaximumPublicationDate( idDashboard );
        model.put( PARAMETER_MAX_PUBLICATION_DATE_VALUE, maxPublicationDate );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_AVANCED_CONFIGURATIONS, user.getLocale( ), model );
        strTemplate += template.getHtml( );
        return strTemplate;
    }


}
