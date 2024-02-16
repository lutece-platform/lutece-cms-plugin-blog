package fr.paris.lutece.plugins.blog.business.insertservice;

import fr.paris.lutece.plugins.blog.business.Blog;

/**
 * Object containing the values set by a user when creating a link for a Blog or Blog Resource
 */
public class BlogLinkPOJO
{
    // Text shown on the link
    private String _strLinkText;

    // Title of the HTML link
    private String _strLinkTitle;

    // Link's targeted window
    private String _strLinkTargetedWindow;

    // Blog object corresponding to the link
    private Blog _objBlog;

    /**
     * Default Constructor
     * 
     * @param blogLinkText
     *            Text of the link
     * @param blogLinkTitle
     *            Title of the link
     * @param blogLinkTargetedWindow
     *            Windows targeted by the link
     * @param blog
     *            Blog object being linked to
     */
    public BlogLinkPOJO( String blogLinkText, String blogLinkTitle, String blogLinkTargetedWindow, Blog blog )
    {
        _strLinkText = blogLinkText;
        _strLinkTitle = blogLinkTitle;
        _strLinkTargetedWindow = blogLinkTargetedWindow;
        _objBlog = blog;
    }

    public String getBlogLinkText( )
    {
        return _strLinkText;
    }

    public void setBlogLinkText( String blogLinkText )
    {
        _strLinkText = blogLinkText;
    }

    public String getBlogLinkTitle( )
    {
        return _strLinkTitle;
    }

    public void setBlogLinkTitle( String blogLinkTitle )
    {
        _strLinkTitle = blogLinkTitle;
    }

    public String getBlogLinkTargetedWindow( )
    {
        return _strLinkTargetedWindow;
    }

    public void setBlogLinkTargetedWindow( String blogLinkTargetedWindow )
    {
        _strLinkTargetedWindow = blogLinkTargetedWindow;
    }

    public Blog getBlog( )
    {
        return _objBlog;
    }

    public void setBlog( Blog blog )
    {
        _objBlog = blog;
    }
}
