package fr.paris.lutece.plugins.blog.web.adminDashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
public class BlogAdminDashboardComponent extends AdminDashboardComponent
    {
        private static final String PARAMETER_TAB = "tab";
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDashboardData( AdminUser user, HttpServletRequest request )
        {
            String strTemplate = "";
            Map<String, Object> model = new HashMap<>( );



            return strTemplate;
        }
}
