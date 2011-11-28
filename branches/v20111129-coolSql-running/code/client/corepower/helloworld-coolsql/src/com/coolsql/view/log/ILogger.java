/**
 * 
 */
package com.coolsql.view.log;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-3-30 create
 */
public interface ILogger {

	public void appendLog(Object ob);
	
	/**
	 * Stop log . If this method have been invoked, method:appendLog() will do nothing.
	 *
	 */
	public void finishLog();
}
