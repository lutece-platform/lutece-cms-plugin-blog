<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../insert/InsertServiceHeader.jsp" />

<jsp:useBean id="blogUrlInsertService" scope="session" class="fr.paris.lutece.plugins.blog.web.insertservice.BlogUrlInsertServiceJspBean" />

<%= blogUrlInsertService.doSearchBlogLink( request ) %>