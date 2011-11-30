/*
 * Created on 2007-5-16
 */
package com.coolsql.system.action;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.AutoCsAction;
import com.coolsql.gui.ExtraJarsManageDialog;

/**
 * @author liu_xlin
 *退出系统触发的事件处理
 */
public class ExtraFileManageAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;
	
	public ExtraFileManageAction()
	{
		super();
	}
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        new ExtraJarsManageDialog().setVisible(true);
    }

}
