/**
 * Create date:2008-4-30
 */
package com.coolsql.view.resultset.action;

/**
 * Update data table row.
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-30 create
 */
public class UpdateRowActioin extends ModifyDataAction {

	private static final long serialVersionUID = 1L;
	
	public UpdateRowActioin() {
		super(ModifyDataAction.UPDATE);
		initMenuDefinitionById("UpdateRowActioin");
	}


}
