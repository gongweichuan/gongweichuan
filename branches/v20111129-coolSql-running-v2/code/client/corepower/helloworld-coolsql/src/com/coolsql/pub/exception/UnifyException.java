/*
 * 创建日期 2006-6-1
 *
 */
package com.coolsql.pub.exception;

/**
 * @author liu_xlin
 *涉及到数据库处理问题时抛出该异常
 * 
 */
public class UnifyException extends Exception {
	private static final long serialVersionUID = 6335842122747858303L;
	public UnifyException()
	{
		super();
	}
    public UnifyException(String msg)
    {
    	super(msg);
    }
    public UnifyException(String msg,Throwable e)
    {
        super(msg,e);
    }
    public UnifyException(Throwable e)
    {
        super(e);
    }
}
