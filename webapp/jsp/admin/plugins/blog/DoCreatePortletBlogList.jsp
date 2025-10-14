<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.blog.web.portlet.BlogListPortletJspBean"%>

${ blogListPortletJspBean.init( pageContext.request, BlogListPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.response.sendRedirect( blogListPortletJspBean.doCreate( pageContext.request )) }