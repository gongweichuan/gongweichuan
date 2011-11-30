/**
 * Create date:2008-5-18
 */
package com.coolsql.view.resultset;

import java.util.EventListener;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-5-18 create
 */
public interface TableModelModifyListener extends EventListener {

	/**
	 * This method will be invoked when the some cell of Data table model take a change.
	 */
	public void dataChanged(TableModelModifyEvent e);
}
