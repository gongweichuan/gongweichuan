﻿/**
 * 
 */
package com.coolsql.view.resultset.action;

import com.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-20 create
 */
public class NextPageProcessAction extends SQLPageProcessAction {

	private static final long serialVersionUID = 1L;
	
	public NextPageProcessAction()
	{
		super(ResultSetDataProcess.NEXT);
		initMenuDefinitionById("NextPageProcessAction");
	}
}