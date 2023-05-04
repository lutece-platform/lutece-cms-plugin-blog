<jsp:include page="../../AdminHeader.jsp" />
<%-- Display the admin menu --%>
<jsp:useBean id="htmlDocPublicationJspBean" scope="session" class="fr.paris.lutece.plugins.blog.web.BlogPublicationJspBean" />
<% String strContent = htmlDocPublicationJspBean.processController ( request , response ); %>
<%@ page errorPage="../../ErrorPage.jsp" %>
<%= strContent %>
<%@ include file="../../AdminFooter.jsp" %>