<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.blog.web.portlet.BlogPortletJspBean"%>

${ blogPortletJspBean.init( pageContext.request, BlogPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ blogPortletJspBean.getCreate( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>


