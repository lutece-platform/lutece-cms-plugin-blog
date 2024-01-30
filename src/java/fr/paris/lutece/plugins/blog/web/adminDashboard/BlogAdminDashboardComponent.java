package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.plugins.blog.business.BlogAdminDashboardHome;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
public class BlogAdminDashboardComponent extends AdminDashboardComponent
    {
        private static final String MARK_NUMBER_MANDATORY_TAGS = "number_mandatory_tags";
        private static final String TEMPLATE_MANDATORY_TAGS = "admin/plugins/blog/dashboard/modify_mandatory_blog_tags.html";
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
}
