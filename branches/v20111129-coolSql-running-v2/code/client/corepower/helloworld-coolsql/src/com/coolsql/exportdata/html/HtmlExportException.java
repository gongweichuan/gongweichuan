/*
 * 创建日期 2006-11-1
 */
package com.coolsql.exportdata.html;

/**
 * @author liu_xlin
 *当对数据以html进行导出时，如果发生了文件名类型不正确或者超出数据量范围时，抛出此异常
 */
public class HtmlExportException extends Exception {
   public HtmlExportException()
   {
       super();
   }
   public HtmlExportException(String message)
   {
       super(message);
   }
   public HtmlExportException(Throwable cause)
   {
       super(cause);
   }
   public HtmlExportException(String message,Throwable cause)
   {
       super(message,cause);
   }
}
