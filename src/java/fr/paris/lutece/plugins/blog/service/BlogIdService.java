package fr.paris.lutece.plugins.blog.service;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

public class BlogIdService extends ResourceIdService
{

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "blog.rbac.blog.resourceType";
    private static final String PROPERTY_LABEL_VIEW = "blog.rbac.blog.permission.view";
    private static final String PROPERTY_LABEL_CREATE = "blog.rbac.blog.permission.create";
    private static final String PROPERTY_LABEL_MODIFY = "blog.rbac.blog.permission.modify";
    private static final String PROPERTY_LABEL_DELETE = "blog.rbac.blog.permission.delete";
    private static final String PROPERTY_LABEL_PUBLISH = "blog.rbac.blog.permission.publish";

    @Override
    public void register( )
    {

        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( BlogIdService.class.getName( ) );
        rt.setPluginName( BlogPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( Blog.PROPERTY_RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( Blog.PERMISSION_PUBLISH );
        p.setPermissionTitleKey( PROPERTY_LABEL_PUBLISH );
        rt.registerPermission( p );

        // for all permissions

        ResourceTypeManager.registerResourceType( rt );

    }

    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {

        List<Blog> listBlogs = BlogHome.getBlogsList( );

        return ReferenceList.convert( listBlogs, "id", "name", true );
    }

    @Override
    public String getTitle( String strId, Locale locale )
    {

        Blog blog = BlogHome.findByPrimaryKey( Integer.parseInt( strId ) );

        return blog.getName( );
    }

}
