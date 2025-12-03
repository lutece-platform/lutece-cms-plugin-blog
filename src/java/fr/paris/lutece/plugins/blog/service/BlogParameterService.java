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
    public static final String DSKEY_DEFAULT_EDITOR = "blog.advanced_parameters.editor";
    public static final String DSKEY_USE_UPLOAD_IMAGE_PLUGIN = "use_upload_image_plugin";
    public static final String DSKEY_USE_CONTENT_TYPE = "blog.advanced_parameters.use_content_type";
    public static final String DSKEY_ACCEPTED_FILE_TYPES = "blog.advanced_parameters.accepted_file_types";
    public static final String MARK_DEFAULT_NUMBER_MANDATORY_TAGS = "number_mandatory_tags";
    public static final String MARK_DEFAULT_DATE_END_PUBLISHING = "default_date_end_publishing";
    public static final String MARK_DEFAULT_EDITOR = "default_editor";
    public static final String MARK_USE_UPLOAD_IMAGE_PLUGIN = "use_upload_image_plugin";
    public static final String MARK_USE_CONTENT_TYPE = "use_content_type";
    public static final String MARK_ACCEPTED_FILE_TYPES = "accepted_file_types";
    public static final String DSKEY_DEFAULT_EDITOR_BACK_OFFICE = "core.backOffice.defaultEditor";
    
    public static final String DEFAULT_ACCEPTED_FILE_TYPES = "image/*,.pdf,.xls,.doc,.docx,.xlsx,.odt,.ods,.ppt,.pptx,.odp";
    
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
     * Get String default editor
     *
     * @return default editor
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
   
    /**
     * Update default editor
     *
     * @param strValue editor name
     */
    public void updateDefaultEditor(String strValue)
    {
        try
        {
            DatastoreService.setDataValue( DSKEY_DEFAULT_EDITOR, strValue );
        }
        catch (Exception e)
        {
            AppLogService.error("Error updating editor config", e );
        }
    }

     /**
     * Get String default editor name
     *
     * @return default editor name for plugin
     */
    public String getDefaultBlogEditor()
    {
        String strDefaultEditor = DatastoreService.getDataValue( DSKEY_DEFAULT_EDITOR_BACK_OFFICE, "" );
        String strBlogDefaultEditor = DatastoreService.getDataValue( DSKEY_DEFAULT_EDITOR, "" );
        if (StringUtils.isNotBlank( strBlogDefaultEditor ))
        {
            return strBlogDefaultEditor;
        }
        else
        {
            return strDefaultEditor;
        }
             
    }

    /**
     * Update the use upload image plugin setting
     *
     * @param bUseUploadImagePlugin true to enable, false to disable
     */
    public void updateUseUploadImagePlugin( boolean bUseUploadImagePlugin )
    {
        DatastoreService.setDataValue( DSKEY_USE_UPLOAD_IMAGE_PLUGIN, Boolean.toString( bUseUploadImagePlugin ) );
    }

    /**
     * Check if upload image plugin is enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isUseUploadImagePlugin()
    {
        return Boolean.parseBoolean( DatastoreService.getDataValue( DSKEY_USE_UPLOAD_IMAGE_PLUGIN, "false" ) );
    }

    /**
     * Update the use content type setting
     *
     * @param bUseContentType true to enable, false to disable
     */
    public void updateUseContentType( boolean bUseContentType )
    {
        DatastoreService.setDataValue( DSKEY_USE_CONTENT_TYPE, Boolean.toString( bUseContentType ) );
    }

    /**
     * Check if content type is enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isUseContentType()
    {
        return Boolean.parseBoolean( DatastoreService.getDataValue( DSKEY_USE_CONTENT_TYPE, "true" ) );
    }

    /**
     * Update the accepted file types for upload
     *
     * @param strAcceptedFileTypes the accepted file types (e.g., "image/*,.pdf,.doc")
     */
    public void updateAcceptedFileTypes( String strAcceptedFileTypes )
    {
        if ( StringUtils.isNotBlank( strAcceptedFileTypes ) )
        {
            DatastoreService.setDataValue( DSKEY_ACCEPTED_FILE_TYPES, strAcceptedFileTypes.trim() );
        }
        else
        {
            DatastoreService.setDataValue( DSKEY_ACCEPTED_FILE_TYPES, DEFAULT_ACCEPTED_FILE_TYPES );
        }
    }

    /**
     * Get the accepted file types for upload
     *
     * @return the accepted file types string
     */
    public String getAcceptedFileTypes()
    {
        return DatastoreService.getDataValue( DSKEY_ACCEPTED_FILE_TYPES, DEFAULT_ACCEPTED_FILE_TYPES );
    }

}
