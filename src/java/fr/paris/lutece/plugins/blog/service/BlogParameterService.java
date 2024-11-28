package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * Blog parameter service.
 */
public class BlogParameterService
{
    private static BlogParameterService _singleton = new BlogParameterService( );

    public static final String DSKEY_DEFAULT_NUMBER_MANDATORY_TAGS = "blog.advanced_parameters.number_mandatory_tags";
    public static final String MARK_DEFAULT_NUMBER_MANDATORY_TAGS = "number_mandatory_tags";

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static BlogParameterService getInstance( )
    {
        return _singleton;
    }

    /**
     * Do modify the number of mandatory tags for a blog
     *
     * @param nbMandatoryTags
     */
    public void updateNumberMandatoryTags(String nbMandatoryTags)
    {
        int valueNbMandatoryTags = 0;
        try
        {
            valueNbMandatoryTags = Integer.parseInt(nbMandatoryTags);
        }
        catch (NumberFormatException e)
        {
            AppLogService.error("Incorrect value for number mandatory tags", e);
        }

        if(valueNbMandatoryTags<0)
        {
            valueNbMandatoryTags=0;
        }

        DatastoreService.setDataValue( DSKEY_DEFAULT_NUMBER_MANDATORY_TAGS, Integer.toString(valueNbMandatoryTags));
    }

    /**
     * Get the number of mandatory tags for a blog
     *
     * @return numberMandatoryTags
     */
    public int getNumberMandatoryTags( )
    {
        try
        {
            return Integer.parseInt(DatastoreService.getDataValue( DSKEY_DEFAULT_NUMBER_MANDATORY_TAGS , "0" ));
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

}
