<jsp:useBean id="managehtmldocsHtmlDoc" scope="session" class="fr.paris.lutece.plugins.blog.web.BlogJspBean" />
<% String strContent = managehtmldocsHtmlDoc.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
