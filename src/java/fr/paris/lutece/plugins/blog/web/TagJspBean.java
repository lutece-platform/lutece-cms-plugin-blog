/*
 * Copyright (c) 2002-2016, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.blog.web;

import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.service.util.AppPathService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage HtmlDoc features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageTags.jsp", controllerPath = "jsp/admin/plugins/blog/", right = "HTMLDOCS_MANAGEMENT" )
public class TagJspBean extends ManageHtmldocsJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_TAGS = "/admin/plugins/blog/tag/manage_tags.html";
    private static final String TEMPLATE_CREATE_TAG = "/admin/plugins/blog/tag/create_tag.html";
    private static final String TEMPLATE_MODIFY_TAG = "/admin/plugins/blog/tag/modify_tag.html";

    // Parameters
    private static final String PARAMETER_ID_TAG = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TAGS = "blog.manage_tags.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TAGS = "blog.modify_tags.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TAG = "blog.create_tag.pageTitle";

    // Markers
    private static final String MARK_TAG_LIST = "tag_list";
    private static final String MARK_TAG = "tags";
    private static final String MARK_WEBAPP_URL = "webapp_url";

    private static final String JSP_MANAGE_TAGS = "jsp/admin/plugins/blog/ManageTags.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_TAG = "blog.message.confirmRemoveTag";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "blog.model.entity.tag.attribute.";

    // Views
    private static final String VIEW_MANAGE_TAGS = "manageTags";
    private static final String VIEW_CREATE_TAG = "createTag";
    private static final String VIEW_MODIFY_TAG = "modifyTag";

    // ActionscreateTag
    private static final String ACTION_CREATE_TAG = "createTag";
    private static final String ACTION_MODIFY_TAG = "modifyTag";
    private static final String ACTION_REMOVE_TAG = "removeTag";
    private static final String ACTION_CONFIRM_REMOVE_TAG = "confirmRemoveTag";
    private static final String ACTION_CREATE_TAG_AJAX_REQUEST = "createTagByAjax";

    // Infos
    private static final String INFO_TAG_CREATED = "blog.info.tag.created";
    private static final String INFO_TAG_UPDATED = "blog.info.tag.updated";
    private static final String INFO_TAG_REMOVED = "blog.info.tag.removed";

    // Session variable to store working values
    private Tag _tag;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_TAGS, defaultView = true )
    public String getManageTags( HttpServletRequest request )
    {
        _tag = null;
        List<Tag> listTag = TagHome.getTagList( );

        Map<String, Object> model = getPaginatedListModel( request, MARK_TAG_LIST, listTag, JSP_MANAGE_TAGS );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_TAGS, TEMPLATE_MANAGE_TAGS, model );
    }

    /**
     * Returns the form to create a tag
     *
     * @param request
     *            The Http request
     * @return the html code of the tag form
     */
    @View( VIEW_CREATE_TAG )
    public String getCreateTag( HttpServletRequest request )
    {
        _tag = ( _tag != null ) ? _tag : new Tag( );

        Map<String, Object> model = getModel( );
        model.put( MARK_TAG, _tag );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_TAG, TEMPLATE_CREATE_TAG, model );
    }

    /**
     * Process the data capture form of a new tag
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_TAG )
    public String doCreateTag( HttpServletRequest request )
    {
        String strRequestAjax = request.getParameter( ACTION_CREATE_TAG_AJAX_REQUEST );
        _tag = ( _tag != null ) ? _tag : new Tag( );
        populate( _tag, request );

        // Check constraints
        if ( !validateBean( _tag, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_TAG );
        }

        if ( TagHome.findByName( _tag.getName( ) ) == null )
        {

            Tag tag = TagHome.create( _tag );

            if ( strRequestAjax != null && ACTION_CREATE_TAG_AJAX_REQUEST.endsWith( strRequestAjax ) )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( String.valueOf( tag.getIdTag( ) ) ) );
            }

            addInfo( INFO_TAG_CREATED, getLocale( ) );

        }
        else
            if ( strRequestAjax != null && ACTION_CREATE_TAG_AJAX_REQUEST.endsWith( strRequestAjax ) )
            {

                return JsonUtil.buildJsonResponse( new JsonResponse( "TAG_EXIST" ) );
            }

        return redirectView( request, VIEW_MANAGE_TAGS );
    }

    /**
     * Manages the removal form of a tag whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_TAG )
    public String getConfirmRemoveTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TAG ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TAG ) );
        url.addParameter( PARAMETER_ID_TAG, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TAG, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a tag
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage tag
     */
    @Action( ACTION_REMOVE_TAG )
    public String doRemoveTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TAG ) );
        TagHome.remove( nId );

        addInfo( INFO_TAG_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_TAGS );
    }

    /**
     * Returns the form to update info about a tag
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_TAG )
    public String getModifyTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TAG ) );

        if ( _tag == null || ( _tag.getIdTag( ) != nId ) )
        {

            _tag = TagHome.findByPrimaryKey( nId );

        }

        Map<String, Object> model = getModel( );
        model.put( MARK_TAG, _tag );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_TAGS, TEMPLATE_MODIFY_TAG, model );
    }

    /**
     * Process the change form of a tag
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_TAG )
    public String doModifyHtmlDoc( HttpServletRequest request )
    {
        _tag = ( _tag != null ) ? _tag : new Tag( );
        populate( _tag, request );

        // Check constraints
        if ( !validateBean( _tag, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_TAG, PARAMETER_ID_TAG, _tag.getIdTag( ) );
        }

        if ( TagHome.findByName( _tag.getName( ) ) == null )
        {

            TagHome.update( _tag );
            addInfo( INFO_TAG_UPDATED, getLocale( ) );

        }

        return redirectView( request, VIEW_MANAGE_TAGS );
    }

}
