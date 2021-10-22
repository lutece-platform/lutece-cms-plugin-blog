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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for Blog objects
 */
public final class DocContentHome
{
    // Static variable pointed at the DAO instance
    private static DocContentDAO _dao = SpringContextService.getBean( "blog.docContentDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "blog" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DocContentHome( )
    {
    }

    /**
     * Create an instance of the DocContent class
     * 
     * @param docContent
     *            The Document Content
     *
     */
    public static void create( DocContent docContent )
    {
        _dao.insertDocContent( docContent, _plugin );
    }

    /**
     * @param nIdBlog
     *            the blog id
     * @param nIdDocument
     *            the document id
     * @param nPriority
     *            the priority
     * 
     */
    public static void insertInBlog( int nIdBlog, int nIdDocument, int nPriority )
    {
        _dao.insertDocContentInBlog( nIdBlog, nIdDocument, nPriority, _plugin );
    }

    /**
     * Update of the DocContent which is specified in parameter
     * 
     * @param docContent
     *            the Document Content
     * @return the instance of DocContent updated
     */
    public static DocContent update( DocContent docContent )
    {
        _dao.store( docContent, _plugin );

        return docContent;
    }

    /**
     * Remove the DocContent whose identifier Blog is specified in parameter
     * 
     * @param nBlogId
     *            the Id DocContent
     */
    public static void remove( int nBlogId )
    {
        _dao.delete( nBlogId, _plugin );
    }

    /**
     * Remove the correspondance beetween the document and the blog
     * 
     * @param nDocumentId
     *            the document id
     */
    public static void removeInBlogById( int nDocumentId )
    {
        _dao.deleteInBlogById( nDocumentId, _plugin );

    }

    /**
     * Remove the DocContent whose identifier is specified in parameter
     * 
     * @param nKey
     *            the Id DocContent
     */
    public static void removeById( int nKey )
    {
        _dao.deleteById( nKey, _plugin );
    }

    /**
     * Returns an instance of a DocContent whose identifier is specified in parameter
     * 
     * @param nIdDocument
     * @return an instance of DocContent
     */
    public static DocContent getDocsContent( int nIdDocument )
    {
        return _dao.loadDocContent( nIdDocument, _plugin );
    }

    /**
     * Returns an list of a DocContent whose htmldoc identifier is specified in parameter
     * 
     * @param nIdHtmlDoc
     * @return an instance of DocContent
     */
    public static List<DocContent> getDocsContentByHtmlDoc( int nIdHtmlDoc )
    {
        return _dao.loadDocContentByIdHtemldoc( nIdHtmlDoc, _plugin );
    }

    /**
     * Returns a list of a ContentType
     * 
     * @return a list of a ContentType
     */
    public static List<ContentType> getListContentType( )
    {
        return _dao.loadListContentType( _plugin );
    }
}
