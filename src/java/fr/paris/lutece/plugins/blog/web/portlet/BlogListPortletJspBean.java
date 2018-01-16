package fr.paris.lutece.plugins.blog.web.portlet;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortlet;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


    // //////////////////////////////////////////////////////////////////////////
    // Constants
    private static final long serialVersionUID = 1L;

    // //////////////////////////////////////////////////////////////////////////
    // Class attributes

    private BlogListPortlet _portlet;

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
     * Returns the Download portlet creation form
     *
     * @param request
     *            The http request
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
        _portlet = new BlogListPortlet( );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        List<Blog> listBlog = BlogHome.getBlogsList( );
        HashMap<String, Object> model = new HashMap<String, Object>( );
        
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LIST_HTMLDOC, listBlog );
        model.put( MARK_LIST_PAGES, BlogListPortletHome.loadPages( BlogListPortlet.RESOURCE_ID ) );
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
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        _portlet = (BlogListPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HashMap<String, Object> model = new HashMap<String, Object>( );
        List<Blog> listBlog = BlogHome.getBlogsList( );
        List<Blog> listBlogNotPublished = new ArrayList<Blog>( );
        List<Blog> listBlogPublished = new ArrayList<Blog>( );

        for ( BlogPublication i : _portlet.getArrayBlogs( ) )
        {
            for ( Blog doc : listBlog )
            {
                if ( i.getIdDocument( ) == doc.getId( ) )
                {
                	listBlogPublished.add( doc );
                }
            }

        }

        listBlogNotPublished.addAll( listBlog );
        listBlogNotPublished.removeAll( listBlogPublished );
        model.put( MARK_LIST_PAGES, BlogListPortletHome.loadPages( BlogListPortlet.RESOURCE_ID ) );

        model.put( MARK_LIST_HTMLDOC_PUBLISHED, listBlogPublished );
        model.put( MARK_LIST_HTMLDOC, listBlogNotPublished );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
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
        int order = 1;
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = Integer.parseInt( strIdPage );

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

}
