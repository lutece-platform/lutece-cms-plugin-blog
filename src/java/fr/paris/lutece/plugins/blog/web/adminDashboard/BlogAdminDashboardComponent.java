package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
public class BlogAdminDashboardComponent extends AdminDashboardComponent
    {
        private static final String TEMPLATE_MANAGE_EDITOR_CONFIG = "admin/plugins/blog/dashboard/manage_editor_config.html";
        private static final String PARAMETER_TAB = "tab";
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDashboardData( AdminUser user, HttpServletRequest request )
        {
            String strTemplate = "";
            Map<String, Object> model = new HashMap<>( );
            HtmlTemplate template3 = AppTemplateService.getTemplate( TEMPLATE_MANAGE_EDITOR_CONFIG, user.getLocale( ) );
            strTemplate += template3.getHtml( );


            return strTemplate;
        }
}
