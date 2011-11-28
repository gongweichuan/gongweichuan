/*
 * 创建日期 2006-7-3
 *
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

/**
 * @author liu_xlin
 *编辑器的复制事件处理
 */
public class TextCopyAction extends AbstractAction {
	private JTextComponent _com=null;
	public TextCopyAction(JTextComponent com)
	{
	   _com=com;	
	}
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(_com!=null)
			_com.copy();
	}

}
