<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.blog.web.portlet.BlogPortletJspBean"%>

${ blogPortletJspBean.init( pageContext.request, blogPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.response.sendRedirect( blogPortletJspBean.doModify( pageContext.request )) }


