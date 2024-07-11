<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_PLUGIN_NAME" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_BLOG" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_MANAGE_MAX_PUBLICATION_DATE" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_ACTION" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_MANDATORY_TAG_NUMBER" %>
<%@ page import="static fr.paris.lutece.plugins.blog.web.utils.BlogConstant.PARAMETER_ACTION" %>
<jsp:useBean id="manageAdminDashboard" scope="session" class="fr.paris.lutece.plugins.blog.web.adminDashboard.BlogAdminDashboardJspBean" />
<% String strContent = manageAdminDashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
