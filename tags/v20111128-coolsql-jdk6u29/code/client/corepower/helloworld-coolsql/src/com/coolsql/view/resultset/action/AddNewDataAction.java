/**
 * 
 */
package com.coolsql.view.resultset.action;

/**
 * @author 刘孝林(kenny liu)
 * 
 * 2008-4-20 create
 */
public class AddNewDataAction extends ModifyDataAction {
	private static final long serialVersionUID = 1L;
	public AddNewDataAction()
	{
		super(ModifyDataAction.INSERT);
		initMenuDefinitionById("AddNewDataAction");
	}
}
