/**
 * 
 */
package com.coolsql.system.menu.loadlistener;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.coolsql.action.framework.CsAction;
import com.coolsql.system.menubuild.IMenuLoadListener;
import com.coolsql.view.ViewManage;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-20 create
 */
public class SQLMenuLoadListener implements IMenuLoadListener{

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(JMenuItem item) {
		Action tmp=ViewManage.getInstance().getSqlEditor().getEditorPane().getShowPromptPopAction();
		if(tmp!=null&&tmp instanceof CsAction&&item instanceof JMenu)
		{
			((JMenu)item).addSeparator();
			item.add(((CsAction)tmp).getMenuItem());
		}
	}

}
