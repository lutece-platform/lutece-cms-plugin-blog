package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.plugins.blog.business.BlogAdminDashboardHome;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
public class BlogAdminDashboardComponent extends AdminDashboardComponent
    {
        private static final String MARK_NUMBER_MANDATORY_TAGS = "number_mandatory_tags";
        private static final String TEMPLATE_MANDATORY_TAGS = "admin/plugins/blog/dashboard/modify_mandatory_blog_tags.html";
        private static final String TEMPLATE_MANAGE_MAX_PUBLICATION_DATE = "admin/plugins/blog/dashboard/manage_maximum_publication_date.html";
        private static final String TEMPLATE_MANAGE_EDITOR_CONFIG = "admin/plugins/blog/dashboard/manage_editor_config.html";
        private static final String PARAMETER_MAX_PUBLICATION_DATE_VALUE = "maxPublicationDate";
        // tab
        private static final String PARAMETER_TAB = "tab";
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDashboardData( AdminUser user, HttpServletRequest request )
        {
            String strTemplate = "";
            Map<String, Object> model = new HashMap<>( );
            model.put( MARK_NUMBER_MANDATORY_TAGS, BlogAdminDashboardHome.selectNumberMandatoryTags( ) );
            HtmlTemplate template1 = AppTemplateService.getTemplate( TEMPLATE_MANDATORY_TAGS, user.getLocale( ), model );
            strTemplate += template1.getHtml( );

            Date maxPublicationDate =  BlogAdminDashboardHome.selectMaximumPublicationDate( );
            model.put( PARAMETER_MAX_PUBLICATION_DATE_VALUE, maxPublicationDate );
            HtmlTemplate template2 = AppTemplateService.getTemplate( TEMPLATE_MANAGE_MAX_PUBLICATION_DATE, user.getLocale( ), model );
            strTemplate += template2.getHtml( );

            HtmlTemplate template3 = AppTemplateService.getTemplate( TEMPLATE_MANAGE_EDITOR_CONFIG, user.getLocale( ) );
            strTemplate += template3.getHtml( );




            return strTemplate;
        }
}
