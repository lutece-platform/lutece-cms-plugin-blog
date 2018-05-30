package fr.paris.lutece.plugins.blog.service;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

public class TagIdService extends ResourceIdService
{

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "blog.rbac.tag.resourceType";
    private static final String PROPERTY_LABEL_VIEW = "blog.rbac.tag.permission.view";
    private static final String PROPERTY_LABEL_CREATE = "blog.rbac.tag.permission.create";
    private static final String PROPERTY_LABEL_MODIFY = "blog.rbac.tag.permission.modify";
    private static final String PROPERTY_LABEL_DELETE = "blog.rbac.tag.permission.delete";

    @Override
    public void register( )
    {

        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( TagIdService.class.getName( ) );
        rt.setPluginName( BlogPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( Tag.PROPERTY_RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission( );
        p.setPermissionKey( Tag.PERMISSION_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Tag.PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Tag.PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Tag.PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );

        // for all permissions

        ResourceTypeManager.registerResourceType( rt );

    }

    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {

        List<Tag> listTags = TagHome.getTagList( );

        return ReferenceList.convert( listTags, "idTag", "name", true );
    }

    @Override
    public String getTitle( String strId, Locale locale )
    {

        Tag tag = TagHome.findByPrimaryKey( Integer.parseInt( strId ) );

        return tag.getName( );
    }

}
