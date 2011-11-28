/**
 * 
 */
package com.coolsql.system.lookandfeel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.UIManager;

import com.coolsql.view.log.LogProxy;

/**
 * 修改当前系统的外观
 * @author kenny liu
 *
 * 2007-11-11 create
 */
public class LookAndFeelChangeAction implements ActionListener {

	private String className=null;
	public LookAndFeelChangeAction()
	{
		this("javax.swing.plaf.metal.MetalLookAndFeel");
	}
	public LookAndFeelChangeAction(String className)
	{
		this.className=className;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(className==null||className.trim().equals(""))
			return;
		try {
			UIManager.setLookAndFeel(className.trim());
		} catch (Exception e1) {
			LogProxy.getProxy().error(e1);
		}
	}

}
