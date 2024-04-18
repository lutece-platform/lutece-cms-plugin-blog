
<jsp:useBean id="manageAdminDashboard" scope="session" class="fr.paris.lutece.plugins.blog.web.adminDashboard.BlogAdminDashboardJspBean" />
<% String strContent = manageAdminDashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
