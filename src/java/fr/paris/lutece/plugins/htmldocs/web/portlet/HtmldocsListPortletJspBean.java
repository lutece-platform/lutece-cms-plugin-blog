
package fr.paris.lutece.plugins.htmldocs.web.portlet;

import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDocHome;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmlDocPublication;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmlDocsListPortlet;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmlDocsListPortletHome;
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
 * This class provides the user interface to manage DocumentList Portlet
 */
public class HtmldocsListPortletJspBean extends PortletJspBean
{
    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LIST_HTMLDOC = "htmldoc_list";
    public static final String MARK_LIST_PAGES = "pages_list";
    public static final String MARK_LIST_HTMLDOC_PUBLISHED = "htmldoc_list_published";
    public static final String PARAMETER_ACTION_PORTLET_ADD = "add";
    public static final String PARAMETER_ACTION_PORTLET_REMOVE = "remove";
    public static final String PARAMETER_ACTION_PORTLET= "action";
    
    
    private static final String PARAMETER_PAGE_TEMPLATE_CODE = "page_template_code";

    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////////////////
    // Class attributes
    
    private HtmlDocsListPortlet _portlet;

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.htmldocdocument";
    }

    /**
     * Returns the Download portlet creation form
     *
     * @param request The http request
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
    	_portlet = new HtmlDocsListPortlet( );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        List<HtmlDoc> listHtmlDocs = HtmlDocHome.getHtmlDocsList( );
        HashMap<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LIST_HTMLDOC, listHtmlDocs );
        model.put(MARK_LIST_PAGES, HtmlDocsListPortletHome.loadPages());
        HtmlTemplate template = getCreateTemplate( strIdPage, strIdPortletType, model );
        
        return template.getHtml(  );
    }    
    /**
     * Returns the Download portlet modification form
     *
     * @param request The Http request
     * @return The HTML form
     */
    public String getModify( HttpServletRequest request )
    {
    	 String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
         int nPortletId = Integer.parseInt( strPortletId );
         _portlet = (HtmlDocsListPortlet) PortletHome.findByPrimaryKey( nPortletId );
         HashMap<String, Object> model = new HashMap<String, Object>( );
         List<HtmlDoc> listHtmlDocs = HtmlDocHome.getHtmlDocsList( );
         List<HtmlDoc> listHtmlDocsNotPublished= new ArrayList<HtmlDoc>();
         List<HtmlDoc> listHtmlDocsPublished= new ArrayList<HtmlDoc>();

         boolean bool= false;
         for(HtmlDocPublication i:_portlet.getArrayHtmlDOcs()){
         for(HtmlDoc doc:listHtmlDocs){
        	 bool= false;	 
        		 if(i.getIdDocument( ) == doc.getId()){
        			 bool=true;
        			 listHtmlDocsPublished.add(doc);
        		 }
           }
        	 
         }
         
         listHtmlDocsNotPublished.addAll(listHtmlDocs);
         listHtmlDocsNotPublished.removeAll(listHtmlDocsPublished);
         model.put(MARK_LIST_PAGES, HtmlDocsListPortletHome.loadPages());

         model.put( MARK_LIST_HTMLDOC_PUBLISHED, listHtmlDocsPublished );
         model.put( MARK_LIST_HTMLDOC, listHtmlDocsNotPublished );
         model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
         //TODO implement repopulating the form with editcomment (and others..) in case of error in doModify
         HtmlTemplate template = getModifyTemplate( _portlet, model );

         return template.getHtml( );
    }

    /**
     * Process portlet's creation
     *
     * @param request The Http request
     * @return The Jsp management URL of the process result
     */
    public String doCreate( HttpServletRequest request )
    {
        int order=1;
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = Integer.parseInt( strIdPage );

        //gets the identifier of the parent page
        String strTemplateCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, _portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        _portlet.setPageId( nIdPage );
        
        //gets the specific parameters
        _portlet.setPageTemplateDocument( Integer.parseInt(strTemplateCode) );

       
        for(HtmlDocPublication doc: _portlet.getArrayHtmlDOcs()){
        	
        	doc.setDocumentOrder(order);
        	order++;
        	
        }
        //Portlet creation
        HtmlDocsListPortletHome.getInstance(  ).create( _portlet );
        
        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request The http request
     * @return Management's Url
     */
    public String doModify( HttpServletRequest request )
    {
        int order=1;

        // recovers portlet attributes
        String strDocumentTypeCode = request.getParameter( PARAMETER_PAGE_TEMPLATE_CODE );
        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, _portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        _portlet.setPageTemplateDocument( Integer.parseInt(strDocumentTypeCode) );

        for(HtmlDocPublication doc: _portlet.getArrayHtmlDOcs()){
        	
        	doc.setDocumentOrder(order);
        	order++;
        	
        }
        // updates the portlet
        _portlet.update(  );

        // displays the page withe the potlet updated
        return getPageUrl( _portlet.getPageId(  ) );
    }
    
    
    public String UpdatePortletDocument( HttpServletRequest request )
    {
        // recovers portlet attributes
        
        String strAction= request.getParameter( PARAMETER_ACTION_PORTLET );
        String strIdDocument = request.getParameter( "idDocument" );
        String strOrderDocument = request.getParameter( "orderDocument" );
        
        int nIdDocument= Integer.parseInt(strIdDocument);
        
        HtmlDocPublication doc= new HtmlDocPublication();
        doc.setIdDocument(nIdDocument);
        
        if(strAction != null && !strAction.isEmpty() && strAction.equals(PARAMETER_ACTION_PORTLET_ADD)){
           
        	int nDocumentOrder= Integer.parseInt(strOrderDocument);
        	_portlet.addIdHtmlDocs(nDocumentOrder, doc);
        	
        }else if(strAction != null && !strAction.isEmpty() && strAction.equals(PARAMETER_ACTION_PORTLET_REMOVE)){
        	
        	_portlet.removeHtmlDocs(doc);
        	
        }
        
     
        return  JsonUtil.buildJsonResponse(new JsonResponse("SUCESS"));
    }

   
   

}
