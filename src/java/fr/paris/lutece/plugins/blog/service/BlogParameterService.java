package fr.paris.lutece.plugins.blog.service;

/**
 * Blog parameter service.
 */
public class BlogParameterService
{
    private static BlogParameterService _singleton = new BlogParameterService( );

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static BlogParameterService getInstance( )
    {
        return _singleton;
    }
    
}
