package fr.paris.lutece.plugins.htmldocs.business;


import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IDocContentDAO {

	   void insertDocContent( DocContent docContent, Plugin plugin );

	   DocContent loadDocContent( int idDocument, Plugin plugin );
	   
	   void delete( int nDocumentId, Plugin plugin );
	    
	   void store( DocContent docContent, Plugin plugin );

	
	   
	
	
	

}
