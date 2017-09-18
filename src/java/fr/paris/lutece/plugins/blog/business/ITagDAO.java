package fr.paris.lutece.plugins.blog.business;

import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

public interface ITagDAO
{

    void insert( Tag tag, Plugin plugin );

    Tag load( int idTag, Plugin plugin );

    List<Tag> loadAllTag( Plugin plugin );

    void delete( int idTag, Plugin plugin );

    void store( Tag tag, Plugin plugin );

    void insert( int idTag, int idDoc, int nPriority, Plugin plugin );

    void deleteByTAG( int idTag, int idDoc, Plugin plugin );

    void deleteByDoc( int idDoc, Plugin plugin );

    List<Tag> loadByDoc( int idDoc, Plugin plugin );

    List<Tag> loadListTagByIdDoc( int idDoc, Plugin plugin );

    ReferenceList selectTagsReferenceList( Plugin plugin );

}
