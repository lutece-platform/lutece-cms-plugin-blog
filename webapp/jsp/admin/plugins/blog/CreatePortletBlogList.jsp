<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.blog.web.portlet.BlogListPortletJspBean"%>

${ blogListPortletJspBean.init( pageContext.request, BlogListPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ blogListPortletJspBean.getCreate( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>


