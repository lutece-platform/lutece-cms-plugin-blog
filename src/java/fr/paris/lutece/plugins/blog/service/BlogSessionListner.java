package fr.paris.lutece.plugins.blog.service;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import fr.paris.lutece.plugins.blog.web.BlogJspBean;


/**
 * Will unlock blog 
 * 
 */
public class BlogSessionListner implements HttpSessionListener{
	

	
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void sessionCreated( HttpSessionEvent se )
	    {
	        // nothing to do
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void sessionDestroyed( HttpSessionEvent se )
	    {
	        String strSessionId = se.getSession( ).getId( );
	        BlogJspBean.unLockedBlogByIdSession(strSessionId);
	    }
	


}
