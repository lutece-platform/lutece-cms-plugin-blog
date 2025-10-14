<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../insert/InsertServiceHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.blog.web.insertservice.BlogResourceInsertServiceJspBean"%>

${ blogResourceInsertServiceJspBean.doSearchBlogLink( pageContext.request ) }