<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<jsp:useBean id="managetags" scope="session" class="fr.paris.lutece.plugins.blog.web.TagJspBean" />
<%
	LocalVariables.setLocal( config, request, response );
%>
<%= managetags.doCreateTag(request) %>