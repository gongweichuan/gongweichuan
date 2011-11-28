/**
 * Create date:2008-4-30
 */
package com.coolsql.view.resultset.action;

import com.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-30 create
 */
public class RefreshQueryAction extends SQLPageProcessAction {

	private static final long serialVersionUID = 1L;
	
	public RefreshQueryAction()
	{
		super(ResultSetDataProcess.REFRESH);
		initMenuDefinitionById("RefreshQueryAction");
	}

}
