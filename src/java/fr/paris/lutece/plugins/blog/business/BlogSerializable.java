package fr.paris.lutece.plugins.blog.business;

import java.io.Serializable;


public class BlogSerializable implements Serializable
{
    int id;
    String contentLabel;

    public BlogSerializable( )
    {
    }

    public BlogSerializable( int id, String contentLabel )
    {
        this.id = id;
        this.contentLabel = contentLabel;
    }

    /**
     *
     * @return id
     */
    public int getId( )
    {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId( int id )
    {
        this.id = id;
    }

    /**
     *
     * @return the content label.
     */
    public String getContentLabel( )
    {
        return contentLabel;
    }

    /**
     *
     * @param contentLabel
     */
    public void setContentLabel( String contentLabel )
    {
        this.contentLabel = contentLabel;
    }
}
