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
 *编辑器的剪切事件
 */
public class TextCutAction extends AbstractAction {

	private JTextComponent _com=null;
	public TextCutAction(JTextComponent com)
	{
		_com=com;
	}
	
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		if(_com!=null)
			_com.cut();
	}

}
