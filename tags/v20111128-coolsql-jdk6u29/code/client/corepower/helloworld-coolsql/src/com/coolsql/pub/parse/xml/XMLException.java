/*
 * 创建日期 2006-11-7
 */
package com.coolsql.pub.parse.xml;

/**
 * @author liu_xlin
 *xml解析异常
 */
public class XMLException extends Exception
{

    public XMLException(String msg)
    {
        super(msg);
    }

    public XMLException(Throwable cause)
    {
        super(cause);
    }
    public XMLException(String msg,Throwable cause)
    {
        super(msg,cause);
    }
}
