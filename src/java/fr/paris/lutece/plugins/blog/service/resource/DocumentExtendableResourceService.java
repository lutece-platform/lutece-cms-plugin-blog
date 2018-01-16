package fr.paris.lutece.plugins.blog.service.resource;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

/**
 *
 * DocumentExtendableResourceService
 *
 */
public class DocumentExtendableResourceService implements IExtendableResourceService
{
    private static final String MESSAGE_DOCUMENT_RESOURCE_TYPE_DESCRIPTION = "blog.resource.resourceTypeDescription";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strResourceType )
    {
        return Blog.PROPERTY_RESOURCE_TYPE.equals( strResourceType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IExtendableResource getResource( String strIdResource, String strResourceType )
    {
        if ( StringUtils.isNotBlank( strIdResource ) && StringUtils.isNumeric( strIdResource ) )
        {
            int nIdDocument = Integer.parseInt( strIdResource );

            return BlogService.getInstance( ).loadDocument( nIdDocument );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceType( )
    {
        return Blog.PROPERTY_RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_DOCUMENT_RESOURCE_TYPE_DESCRIPTION, locale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceUrl( String strIdResource, String strResourceType )
    {

        return null;
    }
}
