<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="blogResourceInsertService" scope="session" class="fr.paris.lutece.plugins.blog.web.insertservice.BlogResourceInsertServiceJspBean" />

<%
	response.sendRedirect( blogResourceInsertService.doInsertBlogLink( request ) );
%>