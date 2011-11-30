/*
 * 创建日期 2006-12-13
 */
package com.coolsql.pub.display.exception;

/**
 * @author liu_xlin
 *针对等待对话框，如果线程没有向等待对话框注册，但仍然获取该线程对应的对话框，那么将抛出此异常
 */
public class NotRegisterException extends Exception {

    public NotRegisterException()
    {
        super();
    }
    public NotRegisterException(String msg)
    {
        super(msg);
    }
    public NotRegisterException(String msg,Throwable cause)
    {
        super(msg,cause);
    }
    public NotRegisterException(Throwable cause)
    {
        super(cause);
    }
}
