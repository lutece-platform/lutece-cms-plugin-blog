package fr.paris.lutece.plugins.blog.service;

import fr.paris.lutece.plugins.blog.web.BlogJspBean;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;

public class UnlockedBlogDaemon extends Daemon{

    private static final String DURATION = "blog.duration.lock";

	@Override
	public void run() {
		
		String duration= DatastoreService.getDataValue(DURATION, "600000");
		BlogJspBean.unLockedBlogByTime(Long.parseLong( duration ));
		
	}

}
