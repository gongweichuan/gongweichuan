/*
 * Created on 2007-5-25
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *sql编辑组件的复制处理
 */
public class CopyMenuAction extends BaseEditorSelectAction {

	private static final long serialVersionUID = 1L;

	public CopyMenuAction()
	{
		super();
		initMenuDefinitionById("CopyMenuAction");
	}
	/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        ViewManage.getInstance().getSqlEditor().getEditorPane().copy();
        ViewManage.getInstance().getSqlEditor().getEditorPane().requestFocusInWindow();
    }

}
