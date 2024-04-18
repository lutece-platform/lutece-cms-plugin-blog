<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_PLUGIN_NAME" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_BLOG" %>

<jsp:useBean id="manageAdminDashboard" scope="session" class="fr.paris.lutece.plugins.blog.web.adminDashboard.BlogAdminDashboardJspBean" />
<% String strContent = manageAdminDashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
