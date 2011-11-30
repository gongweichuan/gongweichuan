/*
 * CommitAction.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.coolsql.view.sqleditor.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.coolsql.action.framework.AutoCsAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.view.log.LogProxy;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-5-25 create
 */
public class CommitAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(CommitAction.class);
	public void executeAction(ActionEvent e)
	{
		super.executeAction(e);
		
		Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
		if(!bookmark.isConnected())
			return;
		try {
			bookmark.getConnection().commit();
			JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(), 
					stringMgr.getString("sqleditor.action.commit.success"),
					"Information",JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e1) {
			LogProxy.errorReport(e1);
		}
	}
}
