/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.blog.service;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.blog.business.DocContent;
import fr.paris.lutece.plugins.blog.business.DocContentHome;
import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogHome;
import fr.paris.lutece.plugins.blog.business.IndexerAction;
import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.business.TagHome;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublicationHome;
import fr.paris.lutece.plugins.blog.service.docsearch.BlogSearchService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.TransactionManager;

/**
 * This Service manages document actions (create, move, delete, validate ...) and notify listeners.
 */
public class BlogService
{

    private static BlogService _singleton = new BlogService( );

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static BlogService getInstance( )
    {
        return _singleton;
    }

    /**
     * Create an blog
     * 
     * @param blog
     *            The Blog
     * @param docContent
     *            The Doc content
     */
    public void createDocument( Blog blog, DocContent docContent )

    {

        TransactionManager.beginTransaction( BlogPlugin.getPlugin( ) );

        try
        {
            BlogHome.addInitialVersion( blog );
            for ( Tag tag : blog.getTag( ) )
            {

                TagHome.create( tag.getIdTag( ), blog.getId( ), tag.getPriority( ) );
            }
            if ( docContent != null )
            {

                docContent.setIdBlog( blog.getId( ) );
                DocContentHome.create( docContent );

            }
            TransactionManager.commitTransaction( BlogPlugin.getPlugin( ) );
        }
        catch( Exception e )
        {
            TransactionManager.rollBack( BlogPlugin.getPlugin( ) );
            throw new AppException( e.getMessage( ), e );
        }

        BlogSearchService.getInstance( ).addIndexerAction( blog.getId( ), IndexerAction.TASK_CREATE, BlogPlugin.getPlugin( ) );

    }

    /**
     * Remvove an blog
     * 
     * @param nId
     *            The blog id
     */
    public void deleteDocument( int nId )

    {
        TransactionManager.beginTransaction( BlogPlugin.getPlugin( ) );

        try
        {
            TagHome.removeTagDoc( nId );
            DocContentHome.remove( nId );
            BlogHome.remove( nId );
            BlogHome.removeVersions( nId );
            TransactionManager.commitTransaction( BlogPlugin.getPlugin( ) );
        }
        catch( Exception e )
        {
            TransactionManager.rollBack( BlogPlugin.getPlugin( ) );
            throw new AppException( e.getMessage( ), e );
        }
        BlogSearchService.getInstance( ).addIndexerAction( nId, IndexerAction.TASK_DELETE, BlogPlugin.getPlugin( ) );

    }

    /**
     * Create an blog
     * 
     * @param blog
     *            The Blog
     * @param docContent
     *            The Doc content
     */
    public void createDocument( Blog blog, List<DocContent> docContent )

    {

        TransactionManager.beginTransaction( BlogPlugin.getPlugin( ) );

        try
        {
            BlogHome.addInitialVersion( blog );
            for ( Tag tag : blog.getTag( ) )
            {

                TagHome.create( tag.getIdTag( ), blog.getId( ), tag.getPriority( ) );
            }
            if ( docContent != null )
            {
            	for(DocContent docCont:docContent){
            		
            		docCont.setIdBlog( blog.getId( ) );
                    DocContentHome.create( docCont );
            	}
                

            }
            TransactionManager.commitTransaction( BlogPlugin.getPlugin( ) );
        }
        catch( Exception e )
        {
            TransactionManager.rollBack( BlogPlugin.getPlugin( ) );
            throw new AppException( e.getMessage( ), e );
        }

        BlogSearchService.getInstance( ).addIndexerAction( blog.getId( ), IndexerAction.TASK_CREATE, BlogPlugin.getPlugin( ) );

    }

    /**
     * Update an Blog
     * 
     * @param blog
     *            The Ht-mlDoc
     * @param docContent
     *            The Doc Content
     */
    public void updateDocument( Blog blog, DocContent docContent )

    {
        TransactionManager.beginTransaction( BlogPlugin.getPlugin( ) );

        try
        {
            BlogHome.addNewVersion( blog );
            if ( docContent != null && DocContentHome.getDocsContent( blog.getId( ) ) != null )
            {

                docContent.setIdBlog( blog.getId( ) );
                DocContentHome.update( docContent );

            }
            else
                if ( docContent != null )
                {

                    docContent.setIdBlog( blog.getId( ) );
                    DocContentHome.create( docContent );
                }
            TagHome.removeTagDoc( blog.getId( ) );
            for ( Tag tag : blog.getTag( ) )
            {

                TagHome.create( tag.getIdTag( ), blog.getId( ), tag.getPriority( ) );
            }
            TransactionManager.commitTransaction( BlogPlugin.getPlugin( ) );
        }
        catch( Exception e )
        {
            TransactionManager.rollBack( BlogPlugin.getPlugin( ) );
            throw new AppException( e.getMessage( ), e );
        }

        BlogSearchService.getInstance( ).addIndexerAction( blog.getId( ), IndexerAction.TASK_MODIFY, BlogPlugin.getPlugin( ) );

    }
    /**
     * Update an Blog
     * 
     * @param blog
     *            The Blog
     * @param docContent
     *            The Doc Content
     */
    public void updateDocument( Blog blog, List<DocContent> docContent )

    {
        TransactionManager.beginTransaction( BlogPlugin.getPlugin( ) );

        try
        {
            BlogHome.addNewVersion( blog );
            if ( docContent != null  )
            {
            	DocContentHome.remove(blog.getId( ));
            	for(DocContent docCont: docContent){
            		
            		docCont.setIdBlog( blog.getId( ) );
                    DocContentHome.create(docCont);

            	}
                
            }
          
            TagHome.removeTagDoc( blog.getId( ) );
            for ( Tag tag : blog.getTag( ) )
            {

                TagHome.create( tag.getIdTag( ), blog.getId( ), tag.getPriority( ) );
            }
            TransactionManager.commitTransaction( BlogPlugin.getPlugin( ) );
        }
        catch( Exception e )
        {
            TransactionManager.rollBack( BlogPlugin.getPlugin( ) );
            throw new AppException( e.getMessage( ), e );
        }

        BlogSearchService.getInstance( ).addIndexerAction( blog.getId( ), IndexerAction.TASK_MODIFY, BlogPlugin.getPlugin( ) );

    }

    /**
     * Update an Blog
     * 
     * @param blog
     *            The blog
     * @param docContent
     *            The Doc Content
     */
    public void updateBlogWithoutVersion( Blog blog, List<DocContent> docContent )

    {
        TransactionManager.beginTransaction( BlogPlugin.getPlugin( ) );
        BlogHome.update( blog );
        try
        {
            if ( docContent != null  )
            {
            	DocContentHome.remove(blog.getId( ));
            	for(DocContent docCont: docContent){
            		
            		docCont.setIdBlog( blog.getId( ) );
                    DocContentHome.create(docCont);

            	}
                
            }
          
            TagHome.removeTagDoc( blog.getId( ) );
            for ( Tag tag : blog.getTag( ) )
            {

                TagHome.create( tag.getIdTag( ), blog.getId( ), tag.getPriority( ) );
            }
            TransactionManager.commitTransaction( BlogPlugin.getPlugin( ) );
        }
        catch( Exception e )
        {
            TransactionManager.rollBack( BlogPlugin.getPlugin( ) );
            throw new AppException( e.getMessage( ), e );
        }

        BlogSearchService.getInstance( ).addIndexerAction( blog.getId( ), IndexerAction.TASK_MODIFY, BlogPlugin.getPlugin( ) );

    }


    /**
     * Returns an instance of a blog whose identifier is specified in parameter
     * 
     * @param nIdDocument
     *            The blog primary key
     * @return an instance of blog
     */
    public Blog loadDocument( int nIdDocument )

    {
        Blog blog = BlogHome.findByPrimaryKey( nIdDocument );
        List<DocContent> docContent = DocContentHome.getDocsContentByHtmlDoc( nIdDocument );
        blog.setDocContent( docContent );
        blog.setTag( TagHome.loadByDoc( nIdDocument ) );

        return blog;

    }

    /**
     * Returns an instance of a blog without binairie file whose identifier is specified in parameter
     * 
     * @param nIdDocument
     *            The blog primary key
     * @return an instance of blog
     */
    public Blog findByPrimaryKeyWithoutBinaries( int nIdDocument )

    {
        Blog blog = BlogHome.findByPrimaryKey( nIdDocument );
        blog.setTag( TagHome.loadByDoc( nIdDocument ) );
        blog.setBlogPubilcation( BlogPublicationHome.getDocPublicationByIdDoc( nIdDocument ) );

        return blog;

    }

    /**
     * Load the data of all the blog objects and returns them as a list
     * 
     * @return the list which contains the data of all the blog objects
     */
    public List<Blog> getListDocWhithContent( )

    {
        List<Blog> listBlogsWhithContent = new ArrayList<Blog>( );
        List<Blog> listBlogs = BlogHome.getBlogsList( );

        for ( Blog doc : listBlogs )
        {

            listBlogsWhithContent.add( loadDocument( doc.getId( ) ) );
        }

        return listBlogsWhithContent;

    }

    /**
     * Returns a list of a blog without binairie file whose identifier is specified in parameter
     * 
     * @return the list which contains the data of all the blog objects
     */
    public List<Blog> getListDocWithoutBinaries( )

    {
        List<Blog> listBlogsWithoutBinaries = new ArrayList<Blog>( );
        List<Blog> listBlogs = BlogHome.getBlogsList( );

        for ( Blog doc : listBlogs )
        {

            listBlogsWithoutBinaries.add( findByPrimaryKeyWithoutBinaries( doc.getId( ) ) );
        }

        return listBlogsWithoutBinaries;

    }

    /**
     * Load the data of all the blog objects whose tag is specified in parameter
     * 
     * @param tag
     *            Tag param
     * @return the list which contains the data of all the blog objects
     */
    public List<Blog> searchListDocByTag( Tag tag )

    {
        List<Blog> listBlogs = BlogHome.getBlogsList( );

        for ( Blog doc : listBlogs )
        {

            doc.setTag( TagHome.getTagListByDoc( doc.getId( ) ) );
        }

        return listBlogs;

    }

}
