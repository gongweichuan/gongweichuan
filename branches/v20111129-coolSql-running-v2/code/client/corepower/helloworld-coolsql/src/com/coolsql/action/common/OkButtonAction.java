/*
 * 创建日期 2006-6-2
 *
 *
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.coolsql.pub.component.CommonFrame;


/**
 * @author liu_xlin
 * 选择驱动程序后所做的事件处理
 */
public class OkButtonAction extends AbstractAction {
	CommonFrame frame=null;
	public OkButtonAction(CommonFrame frame)
	{
		super();
		this.frame=frame;
	}
	public void actionPerformed(ActionEvent e) {
		frame.shutDialogProcess(null);
	}

}
