
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="HtmldocsPortletList" scope="session" class="fr.paris.lutece.plugins.blog.web.portlet.HtmldocsListPortletJspBean" />

<% HtmldocsPortletList.init( request, HtmldocsPortletList.RIGHT_MANAGE_ADMIN_SITE); %>
<%= HtmldocsPortletList.getCreate ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>


