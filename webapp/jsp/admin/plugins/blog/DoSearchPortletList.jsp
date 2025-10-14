<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<%
	LocalVariables.setLocal( config, request, response );
%>
${ blogListPortletJspBean.getSearch( pageContext.request ) }
