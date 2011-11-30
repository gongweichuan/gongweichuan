package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.ShortcutEditor;

/**
 * Edit shortcut 
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-16 create
 */

public class EditShortcutsAction
	extends CsAction
{
	private static final long serialVersionUID = 1L;

	public EditShortcutsAction()
	{
		super();
		initMenuDefinitionById("EditShortcutsAction");
	}

	public void executeAction(ActionEvent e)
	{
		ShortcutEditor editor = new ShortcutEditor(GUIUtil.getMainFrame());
		editor.showWindow();
	}
}