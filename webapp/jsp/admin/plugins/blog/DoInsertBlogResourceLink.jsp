<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.blog.web.insertservice.BlogResourceInsertServiceJspBean"%>

${ pageContext.response.sendRedirect( blogResourceInsertServiceJspBean.doInsertBlogLink( pageContext.request )) }