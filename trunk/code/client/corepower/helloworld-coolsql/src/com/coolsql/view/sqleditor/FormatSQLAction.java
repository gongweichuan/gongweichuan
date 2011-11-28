package com.coolsql.view.sqleditor;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.view.ViewManage;
/**
 * 格式化处理类
 * 
 * @author kenny liu
 * 
 * 2007-12-5 create
 */
public class FormatSQLAction extends CsAction {
	private static final long serialVersionUID = 1L;

	public FormatSQLAction() {
		initMenuDefinitionById("FormatSQLAction");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		ViewManage.getInstance().getSqlEditor().getEditorPane().reformatSql();
	}

}