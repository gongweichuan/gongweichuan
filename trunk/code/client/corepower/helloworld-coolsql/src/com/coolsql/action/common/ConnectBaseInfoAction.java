/*
 * 创建日期 2006-6-30
 *
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.view.View;
import com.coolsql.view.BookMarkwizard.ConnectPropertyDialog;


/**
 * @author liu_xlin
 *
 */
public class ConnectBaseInfoAction extends AbstractAction {
	CommonFrame frame=null;
	View view=null;
	Bookmark bookmark=null;
	public ConnectBaseInfoAction(CommonFrame frame,View view,Bookmark bookmark)
	{
		super();
		this.frame=frame;
		this.view=view;
		this.bookmark=bookmark;
	}
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		new ConnectPropertyDialog(frame,view,bookmark);
		frame.dispose();
	}

}
