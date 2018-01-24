package fr.paris.lutece.plugins.blog.business;

import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IDocContentDAO
{
    /**
     * Create an instance of the DocContent class
     * 
     * @param docContent
     *            The Document Content
     * @param plugin
     *            the plugin
     */
    void insertDocContent( DocContent docContent, Plugin plugin );

    /**
     * Returns an instance of a DocContent whose identifier is specified in parameter
     * 
     * @param nIdDocument
     * @param plugin
     *            the plugin
     * @return an instance of DocContent
     */
    DocContent loadDocContent( int idDocument, Plugin plugin );
    /**
     * Returns an list of a DocContent whose htmldoc identifier  is specified in parameter
     * 
     * @param idHtmlDoc
     * @param plugin
     *            the plugin
     * @return an instance of DocContent
     */
   List <DocContent> loadDocContentByIdHtemldoc( int idHtmlDoc, Plugin plugin );

    /**
     * Remove the DocContent whose identifier is specified in parameter
     * 
     * @param nKey
     *            the Id DocContent
     * @param plugin
     *            the plugin
     */
    void delete( int nDocumentId, Plugin plugin );

    /**
     * Update of the DocContent which is specified in parameter
     * 
     * @param docContent
     *            the Document Content
     * @param plugin
     *            the plugin
     * @return the instance of DocContent updated
     */
    void store( DocContent docContent, Plugin plugin );

}
