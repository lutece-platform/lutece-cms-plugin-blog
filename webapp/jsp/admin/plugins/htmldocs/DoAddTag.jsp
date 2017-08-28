<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<jsp:useBean id="managehtmldocsHtmlDoc" scope="session" class="fr.paris.lutece.plugins.htmldocs.web.HtmlDocJspBean" />
<%
	LocalVariables.setLocal( config, request, response );
%>
<%= managehtmldocsHtmlDoc.doAddTag(request) %>