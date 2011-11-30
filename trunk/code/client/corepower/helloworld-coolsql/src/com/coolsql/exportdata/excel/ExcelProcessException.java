/*
 * 创建日期 2006-8-30
 *
 */
package com.coolsql.exportdata.excel;

/**
 * @author liu_xlin
 *excel文件处理时发生的异常类
 */
public class ExcelProcessException extends Exception {
	private Exception e=null;
    public ExcelProcessException(String name)
    {
    	super(name);
    	e=null;
    }
    public ExcelProcessException(Exception e)
    {
    	super(e.getMessage());
    	this.e=e;
    }
}
