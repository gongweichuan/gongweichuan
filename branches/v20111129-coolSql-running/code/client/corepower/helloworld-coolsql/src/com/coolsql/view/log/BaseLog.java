/*
 * 创建日期 2006-7-7
 *
 */
package com.coolsql.view.log;

/**
 * @author liu_xlin
 *日志器接口
 */
public interface BaseLog {
  /**
   * 调试信息打印，对于查询或者执行sql语句时打印的信息
   */
  public abstract void debug(Object ob);
  
  /**
   * print warning information on the log area
   */
  public void warning(Object ob);
  /**
   * 错误信息打印
   */
  public abstract void error(Object ob);
  /**
   * 一般信息打印
   */
  public abstract void info(Object ob);
}
