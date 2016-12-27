
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="HtmldocsPortlet" scope="session" class="fr.paris.lutece.plugins.htmldocs.web.portlet.HtmldocsPortletJspBean" />

<% HtmldocsPortlet.init( request, HtmldocsPortlet.RIGHT_MANAGE_ADMIN_SITE); %>
<%= HtmldocsPortlet.getCreate ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>


