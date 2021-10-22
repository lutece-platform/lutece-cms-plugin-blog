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
     */
    void insert( Tag tag, Plugin plugin );

    /**
     * Returns an instance of a tag whose identifier is specified in parameter
     * 
     * @param nIdTag
     *            The tag primary key
     * @param plugin
     *            the plugin
     * @return an instance of Tag
     */
    Tag load( int nIdTag, Plugin plugin );

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
     * @param nIdTag
     *            The tag Id
     * @param plugin
     *            the plugin
     */
    void delete( int nIdTag, Plugin plugin );

    /**
     * Update of the tag which is specified in parameter
     * 
     * @param tag
     *            The instance of the tag which contains the data to store
     * @param plugin
     *            the plugin
     */
    void store( Tag tag, Plugin plugin );

    /**
     * Associating a tag with a document
     * 
     * @param nIdTag
     *            the Tag id
     * @param nIdDocument
     *            The document identifiant
     * @param plugin
     *            the plugin
     * @param nPriority
     *            The priority of the document
     */
    void insert( int nIdTag, int nIdDocument, int nPriority, Plugin plugin );

    /**
     * Delete Association a tag with a document whose identifier is specified in parameter
     * 
     * @param nIdTag
     *            Id Tag
     * @param nIdDocument
     *            Id Document
     * @param plugin
     *            the plugin
     */
    void deleteByTAG( int nIdTag, int nIdDocument, Plugin plugin );

    /**
     * Delete Association a tag with a document whose identifier is specified in parameter
     * 
     * @param nIdDocument
     *            The Id Document
     * @param plugin
     *            the plugin
     */
    void deleteByDoc( int nIdDocument, Plugin plugin );

    /**
     * Load the data of the tag objects whose identifier is specified in parameter and returns them as a list
     * 
     * @param nIdDocument
     *            The document identifiant
     * @param plugin
     *            the plugin
     * @return returns them as a list of the tag objects
     */
    List<Tag> loadByDoc( int nIdDocument, Plugin plugin );

    /**
     * Load the Tags associated with the document whose identifier is specified in parameter
     * 
     * @param nIdDocument
     *            Id Document
     * @param plugin
     *            the plugin
     * @return list of Tag
     */
    List<Tag> loadListTagByIdDoc( int nIdDocument, Plugin plugin );

    /**
     * Load the data of all the tag objects and returns them as a list
     * 
     * @param plugin
     *            the plugin
     * @return the list which contains the data of all the tag objects
     */
    ReferenceList selectTagsReferenceList( Plugin plugin );

}
