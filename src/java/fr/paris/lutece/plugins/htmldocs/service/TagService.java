package fr.paris.lutece.plugins.htmldocs.service;

import java.util.List;

import fr.paris.lutece.plugins.htmldocs.business.Tag;
import fr.paris.lutece.plugins.htmldocs.business.TagHome;

public class TagService {
	
    private static TagService _singleton = new TagService(  );
    
    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static TagService getInstance(  )
    {
        return _singleton;
    }
    /**
     * Get The all of Tag
     * @return list of TAG
     */
    public List<Tag>getAllTagDisplay(){
    	
    	return TagHome.getTagList();
    	
    }


}
