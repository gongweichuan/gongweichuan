/*
 * 创建日期 2006-7-7
 *
 */
package com.coolsql.action.sqleditormenu;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 * 
 */
public class AutoSelectAction extends CsAction {
	private static final long serialVersionUID = 1L;
	public AutoSelectAction() {
		super();
		this.initMenuDefinitionById("autoselectAction");
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void executeAction(ActionEvent e) {
		ViewManage.getInstance().getSqlEditor().autoSelect();
	}
}
