package fr.paris.lutece.plugins.blog.service;

/**
 * Blog parameter service.
 */
public class BlogParameterService
{
    private static PublishingService _singleton = new PublishingService( );

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static PublishingService getInstance( )
    {
        return _singleton;
    }
    
}
