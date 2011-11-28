/**
 * Create date:2008-5-18
 */
package com.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import com.coolsql.action.framework.AutoCsAction;
import com.coolsql.view.ViewManage;
import com.coolsql.view.resultset.DataSetPanel;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-5-18 create
 */
public class ShowColumnSelectDialogAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeAction(ActionEvent e)
	{
		Component component=ViewManage.getInstance().getResultView().getDisplayComponent();
    	if(component instanceof DataSetPanel)
    	{
    		DataSetPanel dsPanel=(DataSetPanel)component;
    		dsPanel.popupColumnsSelectionDialog();
    	}
	}
}
