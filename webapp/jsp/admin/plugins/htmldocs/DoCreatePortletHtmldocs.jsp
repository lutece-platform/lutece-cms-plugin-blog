
<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="HtmldocsPortlet" scope="session" class="fr.paris.lutece.plugins.htmldocs.web.portlet.HtmldocsPortletJspBean" />

<%
    HtmldocsPortlet.init( request, HtmldocsPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( HtmldocsPortlet.doCreate( request ) );
%>

