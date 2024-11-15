package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Blog parameter service.
 */
public class BlogParameterService
{
    private static BlogParameterService _singleton = new BlogParameterService( );

    public static final String DSKEY_DEFAULT_NUMBER_MANDATORY_TAGS = "blog.advanced_parameters.number_mandatory_tags";
    public static final String DSKEY_DEFAULT_DATE_END_PUBLISHING = "blog.advanced_parameters.default_date_end_publishing";
    public static final String MARK_DEFAULT_NUMBER_MANDATORY_TAGS = "number_mandatory_tags";
    public static final String MARK_DEFAULT_DATE_END_PUBLISHING = "default_date_end_publishing";

    private static final String FIELD_DATEFORMAT = "yyyy-MM-dd";
    private static final String DB_DATEFORMAT = "dd/MM/yyyy";

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


    /**
     * Update default end publishing date from string iso date
     *
     * @param strValue date format 'yyyy-mm-dd'
     */
    public void updateDefaultDateEndPublishing(String strValue)
    {
        if (StringUtils.isNotBlank( strValue ))
        {
            try
            {
                SimpleDateFormat formatField = new SimpleDateFormat(FIELD_DATEFORMAT);
                Date date = formatField.parse(strValue);

                SimpleDateFormat format = new SimpleDateFormat(DB_DATEFORMAT);
                String strDbDate = format.format(date);

                DatastoreService.setDataValue( DSKEY_DEFAULT_DATE_END_PUBLISHING, strDbDate );
            }
            catch (ParseException e)
            {
                AppLogService.error("Incorrect value for default date end publishing update", e);
            }
        }
    }

    /**
     * Get String default end publishing date
     *
     * @return default end publishing date
     */
    public java.sql.Date getDefaultDateEndPublishing()
    {
        SimpleDateFormat format = new SimpleDateFormat(DB_DATEFORMAT);
        try
        {
            return new java.sql.Date(format.parse(DatastoreService.getDataValue( DSKEY_DEFAULT_DATE_END_PUBLISHING , "" )).getTime());
        }
        catch (ParseException e)
        {
            return null;
        }
    }

}
