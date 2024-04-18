<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_PLUGIN_NAME" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_BLOG" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_MANAGE_MAX_PUBLICATION_DATE" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_ACTION" %>

<jsp:useBean id="manageAdminDashboard" scope="session" class="fr.paris.lutece.plugins.blog.web.adminDashboard.BlogAdminDashboardJspBean" />
<%
     if (PARAMETER_MANAGE_MAX_PUBLICATION_DATE.equals(request.getParameter(PARAMETER_ACTION)))
    {
        response.sendRedirect( manageAdminDashboard.manageMaxPublicationDate(request) );
    }
    else if(PARAMETER_BLOG.equals(request.getParameter(PARAMETER_PLUGIN_NAME)))
        {
            response.sendRedirect( manageAdminDashboard.getDashboardPage(request) );
        }
        else
        {
            response.sendRedirect( manageAdminDashboard.getDashboardPage(request) );
        }
%>
