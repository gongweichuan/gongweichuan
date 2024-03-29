/*
 * Created on 2007-5-25
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *撤销菜单事件处理,该处理针对sql编辑组件
 */
public class UndoMenuAction extends CsAction {
	private static final long serialVersionUID = 1L;

	public UndoMenuAction()
	{
		super();
		initMenuDefinitionById("UndoMenuAction");
	}
	/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        ViewManage.getInstance().getSqlEditor().getEditorPane().getDocument().undo();
        
        ViewManage.getInstance().getSqlEditor().getEditorPane().requestFocusInWindow();
    }

}
