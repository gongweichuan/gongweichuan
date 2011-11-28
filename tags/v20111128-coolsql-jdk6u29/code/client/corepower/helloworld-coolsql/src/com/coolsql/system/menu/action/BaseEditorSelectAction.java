/**
 * 
 */
package com.coolsql.system.menu.action;

import com.coolsql.action.framework.CsAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.display.TextSelectionListener;
import com.coolsql.view.SqlEditorView;
import com.coolsql.view.ViewManage;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-17 create
 */
public class BaseEditorSelectAction extends CsAction implements TextSelectionListener{

	private static final long serialVersionUID = 1L;

	//Indicate whether the connection of default bookmark should be checked when selection changed.
	protected boolean isCheckConnection;
	
	public BaseEditorSelectAction()
	{
		this(false);
	}
	public BaseEditorSelectAction(boolean isCheckConnection)
	{
		super();
		SqlEditorView view=ViewManage.getInstance().getSqlEditor();
		view.getEditorPane().addSelectionListener(this);
		this.isCheckConnection=isCheckConnection;
	}
	
	public void selectionChanged(int newStart, int newEnd)
	{
		boolean isConnected=true;
		if(isCheckConnection)
		{
			Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
			if(bookmark!=null)
				isConnected=bookmark.isConnected();
			else
				isConnected=false;
		}
		this.setEnabled(isConnected&&(newEnd > newStart));
	}
}
