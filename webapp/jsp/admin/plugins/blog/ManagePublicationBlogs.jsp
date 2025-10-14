<jsp:include page="../../AdminHeader.jsp" />
<%-- Display the admin menu --%>
${ pageContext.setAttribute( 'strContent', blogPublicationJspBean.processController( pageContext.request , pageContext.response ) ) }
<%@ page errorPage="../../ErrorPage.jsp" %>
${ pageContext.getAttribute( 'strContent' ) }
<%@ include file="../../AdminFooter.jsp" %>