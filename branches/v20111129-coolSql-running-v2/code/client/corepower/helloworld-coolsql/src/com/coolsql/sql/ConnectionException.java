package com.coolsql.sql;
/**
 * 
 * @author liu_xlin
 *连接异常，当对应书签连接时出现的问题时，抛出此异常．
 */

public class ConnectionException extends Exception
{
    /**
     * 连接失败的错误异常对象
     */
    private Throwable cause;
    public ConnectionException()
    {
        cause = null;
    }

    public ConnectionException(String message)
    {
        super(message);
        cause = null;
    }

    public ConnectionException(String message, Throwable cause)
    {
        super(message);
        this.cause = null;
        this.cause = cause;
    }

    public ConnectionException(Throwable cause)
    {
        super(cause.getMessage());
        this.cause = null;
        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }

    public String toString()
    {
        String base = super.toString();
        if(cause != null)
            base = base + System.getProperty("line.separator") + "Root cause:" + System.getProperty("line.separator") + cause.toString();
        return base;
    }

    
}
