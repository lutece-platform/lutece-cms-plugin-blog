<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_PLUGIN_NAME" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_BLOG" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_MANDATORY_TAG_NUMBER" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_ACTION" %>

<jsp:useBean id="manageAdminDashboard" scope="session" class="fr.paris.lutece.plugins.blog.web.adminDashboard.BlogAdminDashboardJspBean" />
<%
    if(PARAMETER_MANDATORY_TAG_NUMBER.equals(request.getParameter(PARAMETER_ACTION))) {
        response.sendRedirect( manageAdminDashboard.updateMandatoryTagNumber(request) );
    }
    else
    {
        response.sendRedirect( manageAdminDashboard.getDashboardPage(request) );
    }
%>
