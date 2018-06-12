package fr.paris.lutece.plugins.blog.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import fr.paris.lutece.plugins.blog.web.BlogJspBean;


/**
 * Will unlock blog 
 * 
 */
public class BlogSessionListner implements HttpSessionListener{
	

	    private static Map<String, HttpSession> _mapSession = new ConcurrentHashMap<String, HttpSession>( );
	
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void sessionCreated( HttpSessionEvent se )
	    {
	    	_mapSession.put(se.getSession().getId(), se.getSession());
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void sessionDestroyed( HttpSessionEvent se )
	    {
	        String strSessionId = se.getSession( ).getId( );
	        BlogJspBean.unLockedBlogByIdSession(strSessionId);
	        _mapSession.remove(se.getSession().getId());
	    }
	    
	    public static Map<String, HttpSession> getMapSession( ){
	    	
	    	return _mapSession;
	    }
	    
	    
	


}
