package fr.paris.lutece.plugins.blog.web;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogFilter;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortlet;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPortletHome;
import fr.paris.lutece.plugins.document.utils.IntegerUtils;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.insert.InsertServiceJspBean;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectionBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlogServiceJspBean extends InsertServiceJspBean implements InsertServiceSelectionBean {


    private static final long serialVersionUID = 2694692453596836769L;

    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String REGEX_ID = "^[\\d]+$";

    // Templates
    private static final String TEMPLATE_SELECTOR_PAGE = "admin/plugins/blog/page_selector.html";
    private static final String TEMPLATE_SELECTOR_PORTLET = "admin/plugins/blog/portlet_selector.html";
    private static final String TEMPLATE_SELECTOR_BLOG = "admin/plugins/blog/blog_selector.html";
    private static final String TEMPLATE_LINK = "admin/plugins/blog/blog_link.html";

    // JSP
    private static final String JSP_SELECT_PORTLET = "SelectPortlet.jsp";
    private static final String JSP_SELECT_BLOG = "SelectBlog.jsp";

    // Parameters
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_BLOG_ID = "id";
    private static final String PARAMETER_ALT = "alt";
    private static final String PARAMETER_TARGET = "target";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_INPUT = "input";

    // Marker
    private static final String MARK_DOCUMENTS_LIST = "blog_list";
    private static final String MARK_PORTLETS_LIST = "portlets_list";
    private static final String MARK_PAGES_LIST = "pages_list";
    private static final String MARK_PORTLET_ID = "portlet_id";
    private static final String MARK_LINK_LIST_ID = "link_list_id";
    private static final String MARK_URL = "url";
    private static final String MARK_TARGET = "target";
    private static final String MARK_ALT = "alt";
    private static final String MARK_NAME = "name";

    /** The empty string */
    private static final String EMPTY_STRING = "";

    // private
    private AdminUser _user;
    private String _input;

    /**
     * Initialize data
     *
     * @param request The HTTP request
     */
    public void init( HttpServletRequest request )
    {
        _user = AdminUserService.getAdminUser( request );
        _input = request.getParameter( PARAMETER_INPUT );
    }

    @Override
    public String getInsertServiceSelectorUI(HttpServletRequest request) {
        return getSelectPage(request);
    }

    /**
     * Return the html form for page selection.
     *
     * @param request The HTTP request
     * @return The html form of the page selection page
     */
    public String getSelectPage( HttpServletRequest request )
    {
        init( request );

        int nPageId = 0;
        Collection<Page> listPages;
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );

        if ( ( strPageId != null ) && strPageId.matches( REGEX_ID ) )
        {
            nPageId = IntegerUtils.convert( strPageId );
        }

        listPages = PageHome.getChildPages( nPageId );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_PAGES_LIST, listPages );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTOR_PAGE, _user.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Select and validate the specified Page
     *
     * @param request The http request
     * @return The url of the portlet selection page
     */
    public String doSelectPage( HttpServletRequest request )
    {
        init( request );

        String strPageId = request.getParameter( PARAMETER_PAGE_ID );

        if ( ( strPageId == null ) || !strPageId.matches( REGEX_ID ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nPageId = IntegerUtils.convert( strPageId );

        Page page = PageHome.findByPrimaryKey( nPageId );

        if ( page == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        return getSelectPortletUrl( nPageId );
    }

    /**
     * Get the url of the portlet selection page with the specified page id
     *
     * @param nPageId Id of the page
     * @return The url of the portlet selection page
     */
    private String getSelectPortletUrl( int nPageId )
    {
        UrlItem url = new UrlItem( JSP_SELECT_PORTLET );
        url.addParameter( PARAMETER_PAGE_ID, nPageId );
        url.addParameter( PARAMETER_INPUT, _input );

        return url.getUrl(  );
    }


    public String getSelectPortlet( HttpServletRequest request )
    {
        init( request );

        String strPageId = request.getParameter( PARAMETER_PAGE_ID );

        if ( ( strPageId == null ) || !strPageId.matches( REGEX_ID ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Page page = PageHome.findByPrimaryKey( IntegerUtils.convert( strPageId ) );

        Collection<Portlet> listPortletsAll = page.getPortlets(  );
        Collection<Portlet> listPortlets = new ArrayList<Portlet>(  );

        for ( Portlet portlet : listPortletsAll )
        {
            if ( portlet.getPortletTypeId(  ).equals( BlogListPortletHome.getInstance(  ).getPortletTypeId(  ) ) )
            {
                listPortlets.add( portlet );
            }
            else
            {
                if ( portlet.getPortletTypeId(  ).equals( BlogPortletHome.getInstance(  ).getPortletTypeId(  ) ) )
                {
                    listPortlets.add( portlet );
                }
            }
        }


        listPortletsAll.clear(  );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_PORTLETS_LIST, listPortlets );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTOR_PORTLET, _user.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Select and validate the specified Portlet
     *
     * @param request The http request
     * @return The url of the blog selection page if porlet id is valid
     */
    public String doSelectPortlet( HttpServletRequest request )
    {
        init( request );

        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        String strInputId = request.getParameter( PARAMETER_INPUT );

        if ( ( strPortletId == null ) || !strPortletId.matches( REGEX_ID ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nPortletId = IntegerUtils.convert( strPortletId );

        Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );

        if ( portlet == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        return getSelectBlogUrl( nPortletId,strInputId );
    }

    /**
     * Get the url of the blog selection page with the specified portlet id
     *
     * @param nPortletId Id of the portlet
     * @return The url of the blog selection page
     */
    private String getSelectBlogUrl(int nPortletId, String strInputId )
    {
        UrlItem url = new UrlItem( JSP_SELECT_BLOG );
        url.addParameter( PARAMETER_PORTLET_ID, nPortletId );
        url.addParameter( PARAMETER_INPUT, strInputId );

        return url.getUrl(  );
    }

    /**
     * Return the html form for blog selection.
     *
     * @param request The HTTP request
     * @return The html form of the blog selection page
     */
    public String getSelectBlog(HttpServletRequest request ) {
        init(request);

        String strPortletId = request.getParameter(PARAMETER_PORTLET_ID);
        String strInputId = request.getParameter( PARAMETER_INPUT );

        if ((strPortletId == null) || !strPortletId.matches(REGEX_ID)) {
            return AdminMessageService.getMessageUrl(request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP);
        }

        int nPortletId = IntegerUtils.convert(strPortletId);
        BlogFilter blogFilter = new BlogFilter();
        blogFilter.setPortletId(nPortletId);
        Collection<Blog> listBlogs = BlogHome.findByFilter(blogFilter);


        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_DOCUMENTS_LIST, listBlogs );
        model.put( MARK_PORTLET_ID, strPortletId );
        model.put( MARK_LINK_LIST_ID, strInputId );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTOR_BLOG, _user.getLocale(  ), model );

        return template.getHtml(  );

    }

    /**
     * Insert the specified url into HTML content
     *
     * @param request The HTTP request
     * @return The url
     */
    public String doInsertUrl( HttpServletRequest request ) {
        init(request);

        String strBlogId = request.getParameter(PARAMETER_BLOG_ID);
        String strPortletId = request.getParameter(PARAMETER_PORTLET_ID);
        String strTarget = request.getParameter(PARAMETER_TARGET);
        String strAlt = request.getParameter(PARAMETER_ALT);
        String strName = request.getParameter(PARAMETER_NAME);

        HashMap<String, Object> model = new HashMap<String, Object>();

        if ( ( strBlogId == null ) || !strBlogId.matches( REGEX_ID ) || ( strPortletId == null ) ||
                !strPortletId.matches( REGEX_ID ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Blog blog = BlogHome.findByPrimaryKey(IntegerUtils.convert( strBlogId ));

        UrlItem url = new UrlItem( AppPathService.getPortalUrl(  ) );
        url.addParameter( PARAMETER_BLOG_ID, strBlogId );
        url.addParameter( PARAMETER_PORTLET_ID, strPortletId );
        model.put( MARK_URL, url.getUrl(  ) );
        model.put( MARK_TARGET, strTarget );
        model.put( MARK_ALT, strAlt );
        model.put( MARK_NAME, ( strName.length(  ) == 0 ) ? blog.getName() : strName );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LINK, null, model );

        return insertUrl( request, _input, StringEscapeUtils.escapeJavaScript( template.getHtml(  ) ) );
    }

}
