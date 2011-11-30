/**
 * 
 */
package com.coolsql.sql.execute;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.view.log.ILogger;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-3-30 create
 */
public interface IMultiStatementExecute {

	public void execute(); //execute multi sql statement.
	public void cancel();//cancel current execution.
	
	/**
	 * Switch to else bookmark. 
	 */
	public void changeBookmark(Bookmark bookmark);
	
	public void setExecuteLogger(ILogger logger);
	
	public boolean isFinished();
	public boolean isSuccess();
	public boolean isCancelExecution();
}
