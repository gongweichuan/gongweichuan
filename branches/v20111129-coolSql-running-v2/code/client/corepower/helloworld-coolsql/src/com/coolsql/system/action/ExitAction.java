/*
 * Created on 2007-5-16
 */
package com.coolsql.system.action;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.main.frame.MainFrame;
import com.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 *退出系统触发的事件处理
 */
public class ExitAction extends CsAction {

	private static final long serialVersionUID = 1L;
	
	public ExitAction()
	{
		super();
		initMenuDefinitionById("ExitAction");
	}
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        ((MainFrame)GUIUtil.getMainFrame()).closeSystem();;
    }

}
