/*
 * 创建日期 2006-6-4
 *
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin 取消向导，提示是否关闭窗口
 */
public class QuitAction extends AbstractAction implements WindowListener{
	JDialog p = null;

	public QuitAction(JDialog f) {
		p = f;
	}

	public void actionPerformed(ActionEvent e) {
        shut();

	}
	/**
	 * 关闭该窗口时所作的处理
	 *
	 */
	public void shut()
	{
		int result = JOptionPane.showConfirmDialog(p, PublicResource
				.getString("wizardbutton.quit.prompt"),"please confirm!",JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			if (p != null) {
			    for(int i=0;i<p.getComponentCount();i++)
			    {
			        JComponent com=(JComponent)p.getComponent(i);
			        com.removeAll();
			    }
			    p.removeAll();
				p.dispose();
			}
		}		
	}
	/* （非 Javadoc）
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {
		
	}

	/* （非 Javadoc）
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
           shut();
	}

	/* （非 Javadoc）
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) {
		
	}

	/* （非 Javadoc）
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) {
		
	}

	/* （非 Javadoc）
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) {
		
	}

	/* （非 Javadoc）
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent e) {
		
	}

	/* （非 Javadoc）
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) {
		
	}

}
