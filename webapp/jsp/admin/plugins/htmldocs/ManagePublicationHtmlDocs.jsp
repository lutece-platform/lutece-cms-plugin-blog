<jsp:useBean id="htmlDocPublicationJspBean" scope="session" class="fr.paris.lutece.plugins.htmldocs.web.HtmlDocPublicationJspBean" />
<% String strContent = htmlDocPublicationJspBean.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
