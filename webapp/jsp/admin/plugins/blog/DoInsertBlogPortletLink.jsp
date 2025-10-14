<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.blog.web.insertservice.BlogPortletInsertServiceJspBean"%>

${ pageContext.response.sendRedirect( blogPortletInsertServiceJspBean.doInsertBlogLink( pageContext.request )) }