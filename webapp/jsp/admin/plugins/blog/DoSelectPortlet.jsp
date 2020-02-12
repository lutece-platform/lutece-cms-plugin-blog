<jsp:include page="../../insert/InsertServiceHeader.jsp" />
<jsp:useBean id="blogServiceJspBean" scope="session" class="fr.paris.lutece.plugins.blog.web.BlogServiceJspBean" />

<% response.sendRedirect( blogServiceJspBean.doSelectPortlet( request ) );%>
