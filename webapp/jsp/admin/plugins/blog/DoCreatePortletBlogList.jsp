
<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="HtmldocsPortletList" scope="session" class="fr.paris.lutece.plugins.blog.web.portlet.BlogListPortletJspBean" />

<%
HtmldocsPortletList.init( request, HtmldocsPortletList.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( HtmldocsPortletList.doCreate( request ) );
%>

