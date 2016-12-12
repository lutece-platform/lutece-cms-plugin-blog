/*
 * Copyright (c) 2002-2016, Mairie de Paris
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
 package fr.paris.lutece.plugins.htmldocs.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for HtmlDoc objects
 */
public final class HtmlDocHome
{
    // Static variable pointed at the DAO instance
    private static IHtmlDocDAO _dao = SpringContextService.getBean( "htmldocs.htmlDocDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "htmldocs" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private HtmlDocHome(  )
    {
    }

    /**
     * Create an instance of the htmlDoc class
     * @param htmlDoc The instance of the HtmlDoc which contains the informations to store
     * @return The  instance of htmlDoc which has been created with its primary key.
     */
    public static HtmlDoc create( HtmlDoc htmlDoc )
    {
        _dao.insert( htmlDoc, _plugin );

        return htmlDoc;
    }

    /**
     * Create an instance of the htmlDoc class
     * @param htmlDoc The instance of the HtmlDoc which contains the informations to store
     * @return The  instance of htmlDoc which has been created with its primary key.
     */
    public static HtmlDoc createVersion( HtmlDoc htmlDoc )
    {
        _dao.insertVersion( htmlDoc, _plugin );

        return htmlDoc;
    }

    /**
     * Update of the htmlDoc which is specified in parameter
     * @param htmlDoc The instance of the HtmlDoc which contains the data to store
     * @return The instance of the  htmlDoc which has been updated
     */
    public static HtmlDoc update( HtmlDoc htmlDoc )
    {
        _dao.store( htmlDoc, _plugin );

        return htmlDoc;
    }

    /**
     * Remove the htmlDoc whose identifier is specified in parameter
     * @param nKey The htmlDoc Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Remove the htmlDoc's versions whose identifier is specified in parameter
     * @param nKey The htmlDoc Id
     */
    public static void removeVersions( int nKey )
    {
        _dao.deleteVersions( nKey, _plugin );
    }

    /**
     * Returns an instance of a htmlDoc whose identifier is specified in parameter
     * @param nKey The htmlDoc primary key
     * @return an instance of HtmlDoc
     */
    public static HtmlDoc findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Returns an instance of a htmlDoc whose identifier is specified in parameter
     * @param nKey The htmlDoc primary key
     * @return an instance of HtmlDoc
     */
    public static HtmlDoc findByName( String strName )
    {
        return _dao.loadByName( strName, _plugin);
    }

    /**
     * Returns an instance of a htmlDoc whose identifier is specified in parameter
     * @param nKey The htmlDoc primary key
     * @return an instance of HtmlDoc
     */
    public static HtmlDoc findVersion( int nId, int nVersion )
    {
        return _dao.loadVersion( nId, nVersion,  _plugin);
    }

    /**
     * Load the data of all the htmlDoc objects and returns them as a list
     * @return the list which contains the data of all the htmlDoc objects
     */
    public static List<HtmlDoc> getHtmlDocsList( )
    {
        return _dao.selectHtmlDocsList( _plugin );
    }

    /**
     * Load the data of all the htmlDoc objects and returns them as a list
     * @return the list which contains the data of all the htmlDoc objects
     */
    public static List<HtmlDoc> getHtmlDocsVersionsList( )
    {
        return _dao.selectHtmlDocsVersionsList( _plugin );
    }
    
    /**
     * Load the id of all the htmlDoc objects and returns them as a list
     * @return the list which contains the id of all the htmlDoc objects
     */
    public static List<Integer> getIdHtmlDocsList( )
    {
        return _dao.selectIdHtmlDocsList( _plugin );
    }
    
    /**
     * Load the data of all the htmlDoc objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the htmlDoc objects
     */
    public static ReferenceList getHtmlDocsReferenceList( )
    {
        return _dao.selectHtmlDocsReferenceList(_plugin );
    }
}

