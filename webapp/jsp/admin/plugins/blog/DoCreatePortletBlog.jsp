<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.blog.web.portlet.BlogPortletJspBean"%>

${ blogPortletJspBean.init( pageContext.request, BlogPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.response.sendRedirect( blogPortletJspBean.doCreate( pageContext.request )) }


