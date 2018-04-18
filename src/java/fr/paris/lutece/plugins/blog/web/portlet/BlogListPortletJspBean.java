package fr.paris.lutece.plugins.blog.web.portlet;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortlet;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage BlogList Portlet
 */
public class BlogListPortletJspBean extends PortletJspBean
{
    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
   
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LIST_HTMLDOC = "blog_list";
    public static final String MARK_LIST_PAGES = "pages_list";
    public static final String MARK_LIST_HTMLDOC_PUBLISHED = "blog_list_published";
   
    public static final String PARAMETER_ACTION_PORTLET_ADD = "add";
    public static final String PARAMETER_ACTION_PORTLET_REMOVE = "remove";
    public static final String PARAMETER_ACTION_PORTLET = "action";
    private static final String PARAMETER_PAGE_TEMPLATE_CODE = "page_template_code";
    
    private static final String PARAMETER_DOCUMENT_ID = "idDocument";
    private static final String PARAMETER_DOCUMENT_ORDER= "orderDocument";
    private static final String PARAMETER_REFRESH_BUTTON= "orderDocument";

    
    protected static final String MARK_PAGINATOR = "paginator";
    protected static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    protected static final String MARK_ASC_SORT = "asc_sort";

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "blog.listItems.itemsPerPage";
    
    private static final String VIEW_MODIFY_PORTLET = "getModify";



    // //////////////////////////////////////////////////////////////////////////
    // Constants
    private static final long serialVersionUID = 1L;

    // //////////////////////////////////////////////////////////////////////////
    // Class attributes

    private BlogListPortlet _portlet;

    protected String _strCurrentPageIndex;
    protected int _nItemsPerPage;
    protected int _nDefaultItemsPerPage= AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 3);
    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix( )
    {
        return "portlet.blogdocument";
    }

    /**
     * Return a model that contains the list and paginator infos
     * 
     * @param request
     *            The HTTP request
     */
    protected HashMap<String, Object>  getPaginatedListModel( HttpServletRequest request )
    {
    	
    	_strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nDefaultItemsPerPage ,_nItemsPerPage  );
        List<Blog> listBlogPublished = new ArrayList<Blog>( );
        List<Blog> listBlogNotPublished = new ArrayList<Blog>( );

       
       // List<Blog> listBlog = BlogHome.getBlogsList( );
        List<Integer> listBlogsId = BlogHome.getIdBlogsList( );
        for ( BlogPublication i : _portlet.getArrayBlogs( ) )
        {
        			Blog blog=  BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries(i.getIdDocument());
                	listBlogPublished.add( blog );
                	listBlogsId.removeIf(blg -> blg.equals( blog.getId( )));
        }

        
        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<Integer>(  listBlogsId, _nItemsPerPage, getCurrentUrlFromRequest( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );
        List<Blog> listBlog = new ArrayList<Blog>( );

        for ( Integer documentId : paginator.getPageItems( ) )
        {
            Blog blog = BlogService.getInstance( ).findByPrimaryKeyWithoutBinaries( documentId );

            if ( blog != null )
            {
            	listBlog.add( blog );
            }
        }

        
        listBlogNotPublished.addAll( listBlog );
      
        HashMap<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LIST_HTMLDOC, listBlogNotPublished );
        model.put( MARK_LIST_HTMLDOC_PUBLISHED, listBlogPublished );
        
        model.put( MARK_LIST_PAGES, BlogListPortletHome.loadPages( BlogListPortlet.RESOURCE_ID ) );
       
        return model;
    }
    /**
     * Returns the Download portlet creation form
     *
     * @param request
     *            The http request
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
    	 String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
         String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        _portlet = ( _portlet != null && request.getParameter(Paginator.PARAMETER_PAGE_INDEX) != null ) ? _portlet : new BlogListPortlet( );
        HashMap<String, Object> model = getPaginatedListModel( request );
        HtmlTemplate template = getCreateTemplate( strIdPage, strIdPortletType, model );

        return template.getHtml( );
    }

    /**
     * Returns the Download portlet modification form
     *
     * @param request
     *            The Http request
     * @return The HTML form
     */
    @View( VIEW_MODIFY_PORTLET )
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        _portlet = ( _portlet != null && request.getParameter(Paginator.PARAMETER_PAGE_INDEX) != null )? _portlet : (BlogListPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HashMap<String, Object> model = getPaginatedListModel( request );

        HtmlTemplate template = getModifyTemplate( _portlet, model );

        return template.getHtml( );
    }

    /**
     * Process portlet's creation
     *
     * @param request
     *            The Http request
     * @return The Jsp management URL of the process result
     */
    public String doCreate( HttpServletRequest request )
    {
    	String button= request.getParameter( PARAMETER_REFRESH_BUTTON );
    	_nDefaultItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );
    	 String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
         int nIdPage = Integer.parseInt( strIdPage );
        		 
    	if(button != null && button.equals( PARAMETER_REFRESH_BUTTON )){
    		
    		return "../../DoCreatePortlet.jsp?portlet_type_id="+BlogListPortlet.RESOURCE_ID+"&page_id="+strIdPage+"&"+Paginator.PARAMETER_PAGE_INDEX+"=1";

    	}
    	
        int order = 1;
       

        // gets the identifier of the parent page
        String strTemplateCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, _portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        _portlet.setPageId( nIdPage );

        // gets the specific parameters
        _portlet.setPageTemplateDocument( Integer.parseInt( strTemplateCode ) );

        for ( BlogPublication doc : _portlet.getArrayBlogs( ) )
        {

            doc.setDocumentOrder( order );
            order++;

        }
        // Portlet creation
        BlogListPortletHome.getInstance( ).create( _portlet );

        // Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request
     *            The http request
     * @return Management's Url
     */
    public String doModify( HttpServletRequest request )
    {
    	_nDefaultItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );
    	String button= request.getParameter( PARAMETER_REFRESH_BUTTON );
    	if(button != null && button.equals( PARAMETER_REFRESH_BUTTON )){
    		
    		return "../../DoModifyPortlet.jsp?portlet_id="+_portlet.getId( )+"&"+Paginator.PARAMETER_PAGE_INDEX+"=1";
    	}
    	
        int order = 1;

        // recovers portlet attributes
        String strDocumentTypeCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );
        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, _portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        _portlet.setPageTemplateDocument( Integer.parseInt( strDocumentTypeCode ) );

        for ( BlogPublication doc : _portlet.getArrayBlogs( ) )
        {

            doc.setDocumentOrder( order );
            order++;

        }
        // updates the portlet
        _portlet.update( );

        // displays the page withe the potlet updated
        return getPageUrl( _portlet.getPageId( ) );
    }
    /**
     * Update blog portlet
     * @param request
     * @return Json The Json succes or echec
     */
    public String UpdatePortletDocument( HttpServletRequest request )
    {
        // recovers portlet attributes

        String strAction = request.getParameter( PARAMETER_ACTION_PORTLET );
        String strIdDocument = request.getParameter( PARAMETER_DOCUMENT_ID );
        String strOrderDocument = request.getParameter( PARAMETER_DOCUMENT_ORDER );

        int nIdDocument = Integer.parseInt( strIdDocument );

        BlogPublication doc = new BlogPublication( );
        doc.setIdDocument( nIdDocument );

        if ( strAction != null && !strAction.isEmpty( ) && strAction.equals( PARAMETER_ACTION_PORTLET_ADD ) )
        {

            int nDocumentOrder = Integer.parseInt( strOrderDocument );
            _portlet.addIdBlogs( nDocumentOrder, doc );

        }
        else
            if ( strAction != null && !strAction.isEmpty( ) && strAction.equals( PARAMETER_ACTION_PORTLET_REMOVE ) )
            {

                _portlet.removeBlogs( doc );

            }

        return JsonUtil.buildJsonResponse( new JsonResponse( "SUCESS" ) );
    }
    
 
    /**
     * 
     * @param request
     * @return
     */
    public static String getCurrentUrlFromRequest(HttpServletRequest request)
    {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null){
            
        	return requestURL.toString();
        
        }else if(request.getParameter(Paginator.PARAMETER_PAGE_INDEX) != null ){
        	
        	String[] query= queryString.split("&"+Paginator.PARAMETER_PAGE_INDEX);
        	queryString= query[0];
        }

        return requestURL.append('?').append(queryString).toString();
    }

}
