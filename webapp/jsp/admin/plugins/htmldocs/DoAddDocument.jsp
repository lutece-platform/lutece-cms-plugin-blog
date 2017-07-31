<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<jsp:useBean id="HtmldocsPortletList" scope="session" class="fr.paris.lutece.plugins.htmldocs.web.portlet.HtmldocsListPortletJspBean" />
<%
	LocalVariables.setLocal( config, request, response );
%>
<%= HtmldocsPortletList.UpdatePortletDocument(request) %>