<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../insert/InsertServiceHeader.jsp" />

<jsp:useBean id="blogResourceInsertService" scope="session" class="fr.paris.lutece.plugins.blog.web.insertservice.BlogResourceInsertServiceJspBean" />

<%= blogResourceInsertService.doSearchBlogLink( request ) %>