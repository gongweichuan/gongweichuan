/**
 * 
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.view.ViewManage;
import com.coolsql.view.sqleditor.EditorPanel;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-2 create
 */
public class LowerSelectedTextAction extends BaseEditorSelectAction{
	private static final long serialVersionUID = 1L;
	EditorPanel editor=ViewManage.getInstance().getSqlEditor().getEditorPane();
	public LowerSelectedTextAction()
	{
		super();
		initMenuDefinitionById("LowerSelectedTextAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		
		editor.toLowerCase();
	}
}
