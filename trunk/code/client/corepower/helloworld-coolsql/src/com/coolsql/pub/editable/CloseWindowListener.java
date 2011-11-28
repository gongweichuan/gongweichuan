/**
 * 
 */
package com.coolsql.pub.editable;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

/**
 * @author kenny liu
 *该类完成关闭窗口的动作，前提是不执行任何特定的逻辑。可以设置在关闭窗口前是否提示。
 * 2007-11-6 create
 */
public class CloseWindowListener extends WindowAdapter {
	
	private Window window; //被关闭的窗口对象
	private boolean isPrompt; //关闭时是否进行提示(true:提示,false:部提示)
	
	/**
	 * 该构造方法默认不进行信息提示。
	 * @param w --被关闭的窗口
	 */
	public CloseWindowListener(Window w)
	{
		this(w,false);
	}
	public CloseWindowListener(Window w,boolean isPrompt)
	{
		super();
		this.window=w;
		this.isPrompt=isPrompt;
	}
	public void windowClosing(WindowEvent e)
	{
		if(isPrompt)
		{
			int r=JOptionPane.showConfirmDialog(window, "","please confirm",JOptionPane.OK_CANCEL_OPTION);
			if(r==JOptionPane.OK_OPTION)
				window.dispose();
			else
				return;
		}else
			window.dispose();
	}
}
