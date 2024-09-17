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
    DocContent loadDocContent( int nIdDocument, Plugin plugin );

    /**
     * Returns an list of a DocContent whose htmldoc identifier is specified in parameter
     * 
     * @param idHtmlDoc
     * @param plugin
     *            the plugin
     * @return an instance of DocContent
     */
    List<DocContent> loadDocContentByIdHtemldoc( int idHtmlDoc, Plugin plugin );

    /**
     * Remove the DocContent identifier Blog is specified in parameter
     * 
     * @param nBlogId
     *            the Id Blog
     * @param plugin
     *            the plugin
     */
    void delete( int nBlogId, Plugin plugin );

    /**
     * Remove the DocContent whose identifier is specified in parameter
     * 
     * @param nDocumentId
     *            the Id DocContent
     * @param plugin
     *            the plugin
     */
    void deleteById( int nDocumentId, Plugin plugin );

    /**
     * Update of the DocContent which is specified in parameter
     * 
     * @param docContent
     *            the Document Content
     * @param plugin
     *            the plugin
     */
    void store( DocContent docContent, Plugin plugin );

    /**
     * Returns an instance of a ContentType whose identifier is specified in parameter
     * 
     * @param idType
     *            The identifier
     * @param plugin
     *            the plugin
     * @return an instance of a ContentType
     */
    ContentType loadContentType( int idType, Plugin plugin );

    /**
     * Returns a list of a ContentType
     * 
     * @param plugin
     *            the plugin
     * @return a list of a ContentType
     */
    List<ContentType> loadListContentType( Plugin plugin );

    /**
     * Link a blog with a document
     * 
     * @param nIdBlog
     * 
     * @param nIdDocument
     * 
     * @param nPriority
     * 
     * @param plugin
     * 
     */
    void insertDocContentInBlog( int nIdBlog, int nIdDocument, int nPriority, Plugin plugin );

    /**
     * Remove the link between the document and the blog
     * 
     * @param nDocumentId
     *            the document Id
     * @param plugin
     *            the plugin
     */
    void deleteInBlogById( int nDocumentId, Plugin plugin );
}
