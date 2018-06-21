package fr.paris.lutece.plugins.blog.utils;

import java.util.Calendar;

public class BlogLock
{

    private String _strSessionId;
    private long _nTime = Calendar.getInstance( ).getTimeInMillis( );

    public BlogLock( String strSessionId, long nTime )
    {

        _strSessionId = strSessionId;
        _nTime = nTime;
    }

    public String getSessionId( )
    {

        return _strSessionId;
    }

    public void setSessionId( String strSessionId )
    {

        _strSessionId = strSessionId;
    }

    public long getTime( )
    {

        return _nTime;
    }

    public void setTime( long nTime )
    {

        _nTime = nTime;
    }

}
