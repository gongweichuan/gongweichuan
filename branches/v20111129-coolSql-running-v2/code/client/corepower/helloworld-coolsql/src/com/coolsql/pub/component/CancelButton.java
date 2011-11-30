/**
 * 
 */
package com.coolsql.pub.component;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.coolsql.pub.parse.PublicResource;

/**
 * @author kenny liu
 * 
 * 2007-11-6 create
 */
public class CancelButton extends RenderButton {

	private Window window;// 需要被关闭的窗口对象
	private boolean isPrompt;// 是否进行关闭提示(true:提示,false:不提示)
	public CancelButton(Window window, boolean isPrompt) {
		super();
		this.window = window;
		this.isPrompt = isPrompt;
		init();
	}
	/**
	 * 默认情况下，关闭窗口时不进行信息提示
	 * 
	 * @param window
	 *            --被关闭的窗口
	 */
	public CancelButton(Window window) {
		this(window, false);
	}
	private void init() {
		setText(PublicResource.getString("propertyframe.button.quit"));
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isPrompt) {
					int r = JOptionPane
							.showConfirmDialog(
									window,
									PublicResource
											.getString("system.closedialog.forcecloseconfirm"),
									"please confirm",
									JOptionPane.OK_CANCEL_OPTION);
					if (r == JOptionPane.OK_OPTION)
						window.dispose();
					else
						return;
				} else
					window.dispose();
			}
		});

	}
}
