/*
 * Created on 2007-5-25
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *sql编辑组件的全选处理
 */
public class SelectAllMenuAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public SelectAllMenuAction()
	{
		initMenuDefinitionById("SelectAllMenuAction");
	}
	/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        ViewManage.getInstance().getSqlEditor().getEditorPane().selectAll();
        ViewManage.getInstance().getSqlEditor().getEditorPane().requestFocusInWindow();
    }

}
