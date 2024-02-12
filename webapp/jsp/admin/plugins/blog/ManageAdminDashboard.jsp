<jsp:useBean id="manageAdminDashboard" scope="session" class="fr.paris.lutece.plugins.blog.web.adminDashboard.BlogAdminDashboardJspBean" />
<%
    if("updateMandatoryTagNumber".equals(request.getParameter("action")))
    {
        response.sendRedirect( manageAdminDashboard.updateMandatoryTagNumber(request) );
    }
    else if ("manageMaxPublicationDate".equals(request.getParameter("action")))
    {
        response.sendRedirect( manageAdminDashboard.manageMaxPublicationDate(request) );
    }
    else if("blog".equals(request.getParameter("plugin_name")))
    {
        response.sendRedirect( manageAdminDashboard.getDashboardPage(request) );
    }
    else
    {
        response.sendRedirect( manageAdminDashboard.getDashboardPage(request) );
    }
%>
