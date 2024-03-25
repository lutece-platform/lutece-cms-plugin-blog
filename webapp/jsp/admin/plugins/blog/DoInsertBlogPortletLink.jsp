<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="blogPortletInsertService" scope="session" class="fr.paris.lutece.plugins.blog.web.insertservice.BlogPortletInsertServiceJspBean" />

<%
	response.sendRedirect( blogPortletInsertService.doInsertBlogLink( request ) );
%>