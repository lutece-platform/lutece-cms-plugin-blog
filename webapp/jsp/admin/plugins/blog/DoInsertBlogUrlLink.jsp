<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.blog.web.insertservice.BlogUrlInsertServiceJspBean"%>

${ pageContext.response.sendRedirect( BlogUrlInsertServiceJspBean.doInsertBlogLink( pageContext.request )) }