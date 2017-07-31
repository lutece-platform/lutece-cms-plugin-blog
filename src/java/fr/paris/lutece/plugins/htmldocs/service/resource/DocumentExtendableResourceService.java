package fr.paris.lutece.plugins.htmldocs.service.resource;

import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.service.HtmlDocService;
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
    private static final String MESSAGE_DOCUMENT_RESOURCE_TYPE_DESCRIPTION = "htmldocs.resource.resourceTypeDescription";
   
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strResourceType )
    {
        return HtmlDoc.PROPERTY_RESOURCE_TYPE.equals( strResourceType );
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

            return HtmlDocService.getInstance().loadDocument(nIdDocument);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceType(  )
    {
        return HtmlDoc.PROPERTY_RESOURCE_TYPE;
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
