/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.plugins.blog.business;

import fr.paris.lutece.plugins.blog.service.BlogPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Tag objects
 */
public final class TagHome
{
    // Static variable pointed at the DAO instance
    private static ITagDAO _dao = SpringContextService.getBean( "blog.tagDAO" );
    private static Plugin _plugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private TagHome( )
    {
    }

    /**
     * Create an instance of the tag class
     * 
     * @param tag
     *            The instance of the tag which contains the informations to store
     * @return The instance of tag which has been created with its primary key.
     */
    public static Tag create( Tag tag )
    {
        _dao.insert( tag, _plugin );

        return tag;
    }

    /**
     * Update of the tag which is specified in parameter
     * 
     * @param tag
     *            The instance of the tag which contains the data to store
     * @return The instance of the tag which has been updated
     */
    public static Tag update( Tag tag )
    {
        _dao.store( tag, _plugin );

        return tag;
    }

    /**
     * Remove the tag whose identifier is specified in parameter
     * 
     * @param nKey
     *            The tag Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a tag whose identifier is specified in parameter
     * 
     * @param nKey
     *            The tag primary key
     * @return an instance of Tag
     */
    public static Tag findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a tag whose name is specified in parameter
     * 
     * @param strName
     *            The tag name
     * @return an instance of Tag
     */
    public static Tag findByName( String strName )
    {
        return _dao.loadByName( strName, _plugin );
    }

    /**
     * Load the data of all the tag objects and returns them as a list
     * 
     * @return the list which contains the data of all the tag objects
     */
    public static List<Tag> getTagList( )
    {
        return _dao.loadAllTag( _plugin );
    }

    /**
     * Load the data of all the tag objects and returns them as a list
     * 
     * @return the list which contains the data of all the tag objects
     */
    public static ReferenceList getTagsReferenceList( )
    {
        return _dao.selectTagsReferenceList( _plugin );
    }

    /**
     * Load the data of the tag objects whose identifier is specified in parameter and returns them as a list
     * 
     * @param nIdDco
     *            The document identifiant
     * @return returns them as a list of the tag objects
     */
    public static List<Tag> loadByDoc( int nIdDco )
    {
        return _dao.loadByDoc( nIdDco, _plugin );
    }

    /**
     * Associating a tag with a document
     * 
     * @param nIdTag
     *            the Tag id
     * @param nIdocument
     *            The document identifiant
     * @param nPriority
     *            The priority of the document
     */
    public static void create( int nIdTag, int nIdocument, int nPriority )
    {
        _dao.insert( nIdTag, nIdocument, nPriority, _plugin );

    }

    /**
     * Delete Association a tag with a document whose identifier is specified in parameter
     * 
     * @param nIdDoc
     *            The Id Document
     */
    public static void removeTagDoc( int nIdDoc )
    {
        _dao.deleteByDoc( nIdDoc, _plugin );
    }

    /**
     * Load the Tags associated with the document whose identifier is specified in parameter
     * 
     * @param idDoc
     *            Id Document
     * @return list of Tag
     */
    public static List<Tag> getTagListByDoc( int idDoc )
    {
        return _dao.loadListTagByIdDoc( idDoc, _plugin );
    }
}
