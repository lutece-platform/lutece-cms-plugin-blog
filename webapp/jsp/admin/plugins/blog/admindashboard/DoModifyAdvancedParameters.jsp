<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="blogAdminDashboard" scope="session" class="fr.paris.lutece.plugins.blog.web.admindashboard.BlogAdminDashboardJspBean" />

<%
	blogAdminDashboard.init( request, "CORE_USERS_MANAGEMENT" ) ;
	response.sendRedirect( blogAdminDashboard.doModifyBlogAdvancedParameters( request ) );
%>

