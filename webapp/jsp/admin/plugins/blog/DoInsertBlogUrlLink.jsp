<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="blogUrlInsertService" scope="session" class="fr.paris.lutece.plugins.blog.web.insertservice.BlogUrlInsertServiceJspBean" />

<%
	response.sendRedirect( blogUrlInsertService.doInsertBlogLink( request ) );
%>