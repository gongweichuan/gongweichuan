/*
 * 创建日期 2006-6-30
 *
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.coolsql.pub.component.CommonFrame;


/**
 * @author liu_xlin
 *上一步按钮事件处理
 */
public class PreButtonAction extends AbstractAction {
	CommonFrame frame=null;
	public PreButtonAction(CommonFrame frame)
	{
		super();
		this.frame=frame;
	}
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		frame.preButtonProcess(null);
	}

}
