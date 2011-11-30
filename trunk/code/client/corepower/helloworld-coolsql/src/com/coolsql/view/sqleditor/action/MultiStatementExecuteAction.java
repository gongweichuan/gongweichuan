/**
 * 
 */
package com.coolsql.view.sqleditor.action;

import java.awt.event.ActionEvent;
import java.util.List;

import com.coolsql.action.common.ActionCommand;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.system.menu.action.BaseEditorSelectAction;
import com.coolsql.view.SqlEditorView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-1 create
 */
public class MultiStatementExecuteAction extends BaseEditorSelectAction {

	private static final long serialVersionUID = 1L;
	public MultiStatementExecuteAction(){
		super();
		initMenuDefinitionById("MultiStatementExecuteAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void executeAction(ActionEvent e) {
		SqlEditorView view=ViewManage.getInstance().getSqlEditor();
		Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
		List<String> sqls=view.getQueries();
		ActionCommand command=new MultiStatementExecuteCommand(bookmark,sqls);
		try {
			command.exectue();
		} catch (Exception e1) {
			LogProxy.errorReport(e1);
		}
		
	}

}
