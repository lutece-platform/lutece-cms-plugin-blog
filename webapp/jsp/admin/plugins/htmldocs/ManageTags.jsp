<jsp:useBean id="managetags" scope="session" class="fr.paris.lutece.plugins.htmldocs.web.TagJspBean" />
<% String strContent = managetags.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
