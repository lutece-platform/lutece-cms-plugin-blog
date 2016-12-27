<jsp:useBean id="managehtmldocsHtmlDoc" scope="session" class="fr.paris.lutece.plugins.htmldocs.web.HtmlDocJspBean" />
<% String strContent = managehtmldocsHtmlDoc.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
