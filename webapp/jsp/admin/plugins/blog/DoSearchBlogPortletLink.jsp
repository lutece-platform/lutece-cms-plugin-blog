<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../insert/InsertServiceHeader.jsp" />

<jsp:useBean id="blogPortletInsertService" scope="session" class="fr.paris.lutece.plugins.blog.web.insertservice.BlogPortletInsertServiceJspBean" />

<%= blogPortletInsertService.doSearchBlogLink( request ) %>