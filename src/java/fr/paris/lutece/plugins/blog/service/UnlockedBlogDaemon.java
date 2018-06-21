package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.plugins.blog.web.BlogJspBean;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;

public class UnlockedBlogDaemon extends Daemon
{

    private static final String DURATION = "blog.duration.lock";

    //MILLISECOND (10 MINUTE) 
    private static final String DEFAULT_DURATION = "600000";
	@Override
	public void run() {
		
		String duration= DatastoreService.getDataValue(DURATION, DEFAULT_DURATION);
		BlogJspBean.unLockedBlogByTime(Long.parseLong( duration ));
		
	}

}
