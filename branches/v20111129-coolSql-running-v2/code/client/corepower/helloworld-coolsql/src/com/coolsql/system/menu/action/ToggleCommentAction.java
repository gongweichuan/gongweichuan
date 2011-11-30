/**
 * 
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.sql.formater.TextCommenter;
import com.coolsql.view.ViewManage;
import com.coolsql.view.sqleditor.EditorPanel;

/**
 * 对sql编辑组件EditorPanel进行注释/撤销注释的操作处理类
 * 
 * @author kenny liu
 * 
 * 2007-12-5 create
 */
public class ToggleCommentAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public ToggleCommentAction() {
		initMenuDefinitionById("ToggleCommentAction");
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		EditorPanel pane = ViewManage.getInstance().getSqlEditor()
				.getEditorPane();
		TextCommenter tc = new TextCommenter(pane);
		tc.commentSelection();

	}

}
