﻿/*
 * PreviousDefaultBookmarkAction.java
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

import com.coolsql.action.framework.AutoCsAction;
import com.coolsql.bookmarkBean.BookmarkManage;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-5-29 create
 */
public class PreviousDefaultBookmarkAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeAction(ActionEvent e)
	{
		super.executeAction(e);
		
		BookmarkManage.getInstance().previousBookmarkAsDefault();
	}
}
