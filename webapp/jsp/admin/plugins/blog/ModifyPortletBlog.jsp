<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="HtmldocsPortlet" scope="session" class="fr.paris.lutece.plugins.blog.web.portlet.BlogPortletJspBean" />

<% HtmldocsPortlet.init( request, HtmldocsPortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= HtmldocsPortlet.getModify ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>


