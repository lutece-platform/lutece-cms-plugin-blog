<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.blog.web.admindashboard.BlogAdminDashboardJspBean"%>

${ blogAdminDashboardJspBean.init( pageContext.request, "CORE_USERS_MANAGEMENT" ) }
${ pageContext.response.sendRedirect( blogAdminDashboardJspBean.doModifyBlogAdvancedParameters( pageContext.request ) ) }

