package fr.paris.lutece.plugins.blog.business;

import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

public interface ITagDAO
{

    /**
     * Create an instance of the tag class
     * 
     * @param tag
     *            The instance of the tag which contains the informations to store
     * @param plugin
     *            the plugin
     * @return The instance of tag which has been created with its primary key.
     */
    void insert( Tag tag, Plugin plugin );

    /**
     * Returns an instance of a tag whose identifier is specified in parameter
     * 
     * @param nKey
     *            The tag primary key
     * @param plugin
     *            the plugin
     * @return an instance of Tag
     */
    Tag load( int idTag, Plugin plugin );

    /**
     * Returns an instance of a tag whose name is specified in parameter
     * 
     * @param strName
     *            The tag name
     * @param plugin
     *            the plugin
     * @return an instance of Tag
     */
    Tag loadByName( String strName, Plugin plugin );

    /**
     * Load the data of all the tag objects and returns them as a list
     * 
     * @param plugin
     *            the plugin
     * @return the list which contains the data of all the tag objects
     */
    List<Tag> loadAllTag( Plugin plugin );

    /**
     * Remove the tag whose identifier is specified in parameter
     * 
     * @param nKey
     *            The tag Id
     * @param plugin
     *            the plugin
     */
    void delete( int idTag, Plugin plugin );

    /**
     * Update of the tag which is specified in parameter
     * 
     * @param tag
     *            The instance of the tag which contains the data to store
     * @param plugin
     *            the plugin
     * @return The instance of the tag which has been updated
     */
    void store( Tag tag, Plugin plugin );

    /**
     * Associating a tag with a document
     * 
     * @param nIdTag
     *            the Tag id
     * @param nIdocument
     *            The document identifiant
     * @param plugin
     *            the plugin
     * @param nPriority
     *            The priority of the document
     */
    void insert( int idTag, int idDoc, int nPriority, Plugin plugin );

    /**
     * Delete Association a tag with a document whose identifier is specified in parameter
     * 
     * @param idTag
     *            Id Tag
     * @param idDoc
     *            Id Document
     * @param plugin
     *            the plugin
     */
    void deleteByTAG( int idTag, int idDoc, Plugin plugin );

    /**
     * Delete Association a tag with a document whose identifier is specified in parameter
     * 
     * @param nIdDoc
     *            The Id Document
     * @param plugin
     *            the plugin
     */
    void deleteByDoc( int idDoc, Plugin plugin );

    /**
     * Load the data of the tag objects whose identifier is specified in parameter and returns them as a list
     * 
     * @param nIdDco
     *            The document identifiant
     * @param plugin
     *            the plugin
     * @return returns them as a list of the tag objects
     */
    List<Tag> loadByDoc( int idDoc, Plugin plugin );

    /**
     * Load the Tags associated with the document whose identifier is specified in parameter
     * 
     * @param idDoc
     *            Id Document
     * @param plugin
     *            the plugin
     * @return list of Tag
     */
    List<Tag> loadListTagByIdDoc( int idDoc, Plugin plugin );

    /**
     * Load the data of all the tag objects and returns them as a list
     * 
     * @param plugin
     *            the plugin
     * @return the list which contains the data of all the tag objects
     */
    ReferenceList selectTagsReferenceList( Plugin plugin );

}
