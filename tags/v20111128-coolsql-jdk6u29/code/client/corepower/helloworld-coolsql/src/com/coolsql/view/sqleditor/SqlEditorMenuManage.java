/*
 * Create date: 2006-7-3
 *
 */
package com.coolsql.view.sqleditor;

import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.coolsql.action.common.ExportDataOfDBAction;
import com.coolsql.action.framework.CsAction;
import com.coolsql.action.sqleditormenu.AutoSelectAction;
import com.coolsql.exportdata.ExportData;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.Setting;
import com.coolsql.system.menu.action.AddSelectedTextAsFavoriteAction;
import com.coolsql.view.sqleditor.action.CommitAction;
import com.coolsql.view.sqleditor.action.MultiStatementExecuteAction;
import com.coolsql.view.sqleditor.action.RollbackAction;
import com.coolsql.view.sqleditor.action.SQLEditorFindAction;
import com.coolsql.view.sqleditor.action.SQLExcuteAction;
import com.jidesoft.swing.JideMenu;

/**
 * Popup menu attached to sql editor view.
 * @author liu_xlin 
 */
public class SqlEditorMenuManage extends BaseMenuManage {

	private CsAction autoSelectAction;

	// sql执行
	private CsAction sqlExecute;
	/**
	 * execute sqls considered as script.
	 */
	private CsAction executeAsScript;

	private CsAction formatSQLAction;
	/**
	 * 导出数据
	 */
	private JideMenu export;

	// 导出成文本
	private JMenuItem exportTxt;

	// 导出成excel文件
	private JMenuItem exportExcel;
	
	//Add selected text in the sql editor.
	private CsAction addSelectedTextAsFavoriteAction;

	/**
	 * 用于保存是否进行了弹出菜单的初始化，调用了createPopMenu()方法后才能置为true
	 */
	private boolean isInit = false;
	public SqlEditorMenuManage(SqlPanel textPane) {
		super(textPane);
		this.popMenu = new BasePopupMenu();
		this.popMenu.addPopupMenuListener(new SQLEditorPopmenuListener());

		autoSelectAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(AutoSelectAction.class);


		formatSQLAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(FormatSQLAction.class);
		
	}

	/**
	 * 重写文本编辑组件的方法
	 */
	protected void createPopMenu() {
		if (sqlExecute == null) {
			sqlExecute = Setting.getInstance().getShortcutManager()
					.getActionByClass(SQLExcuteAction.class);
		}
		if (executeAsScript == null) {
			executeAsScript = Setting.getInstance().getShortcutManager()
					.getActionByClass(MultiStatementExecuteAction.class);
		}
		if(addSelectedTextAsFavoriteAction==null)
		{
			addSelectedTextAsFavoriteAction=Setting.getInstance().getShortcutManager()
			.getActionByClass(AddSelectedTextAsFavoriteAction.class);
		}
		
		CsAction undoAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.coolsql.system.menu.action.UndoMenuAction.class);
		CsAction redoAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.coolsql.system.menu.action.RedoMenuAction.class);
		popMenu.add(undoAction.getMenuItem());
		popMenu.add(redoAction.getMenuItem());
		
		popMenu.addSeparator();
		
		//cut
		CsAction cutAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.coolsql.system.menu.action.CutMenuAction.class);
		popMenu.add(cutAction.getMenuItem());

		//copy
		CsAction copyAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.coolsql.system.menu.action.CopyMenuAction.class);
		popMenu.add(copyAction.getMenuItem());

		//paste
		CsAction pasteAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.coolsql.system.menu.action.PasteMenuAction.class);
		popMenu.add(pasteAction.getMenuItem());

		popMenu.addSeparator();
		
		//select all
		CsAction selectAllAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(com.coolsql.system.menu.action.SelectAllMenuAction.class);
		popMenu.add(selectAllAction.getMenuItem());

		CsAction findAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(SQLEditorFindAction.class);
		popMenu.add(findAction.getMenuItem());
		
		popMenu.addSeparator();

		/**
		 * 信息提示菜单项
		 */
		EditorPanel tCom = ((SqlPanel) this.getComponent()).getEditor();
		
		popMenu.add(((CsAction)tCom.getShowPromptPopAction()).getMenuItem());

		/**
		 * Retrive the sql automatically
		 */
		popMenu.add(autoSelectAction.getMenuItem());

		popMenu.add(sqlExecute.getMenuItem());

		popMenu.add(executeAsScript.getMenuItem());
		popMenu.addSeparator();

		//commitAction
		CsAction commitAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CommitAction.class);
		popMenu.add(commitAction.getMenuItem());
		
		//rollbackAction
		CsAction rollbackAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RollbackAction.class);
		popMenu.add(rollbackAction.getMenuItem());
		
		popMenu.addSeparator();
		
		popMenu.add(formatSQLAction.getMenuItem());

		popMenu.add(addSelectedTextAsFavoriteAction.getMenuItem());
		popMenu.addSeparator();

		// 导出数据菜单
		export = new JideMenu(PublicResource
				.getString("sqlEditorView.popupmenu.export"));
		popMenu.add(export);

		// 导出文本菜单
		exportTxt = createMenuItem(PublicResource
				.getString("table.popup.exportTxt"), PublicResource
				.getIcon("table.popup.export.txticon"),
				new ExportDataOfDBAction(ExportData.EXPORT_TEXT));
		export.add(exportTxt);

		// 导出excel菜单
		exportExcel = createMenuItem(PublicResource
				.getString("table.popup.exportExcel"), PublicResource
				.getIcon("table.popup.export.excelicon"),
				new ExportDataOfDBAction(ExportData.EXPORT_EXCEL));
		export.add(exportExcel);
		isInit = true;
	}
	/**
	 * 重写文本编辑组件的方法
	 */
	public BasePopupMenu itemCheck() {
		// super.itemCheck();
		if (!isInit)
			createPopMenu();

		// --------------

		return popMenu;
	}

	/**
	 * 重写文本编辑组件的方法
	 */
	public BasePopupMenu getPopMenu() {

		return popMenu;
	}
	/**
	 * 新增的sqleditor，不能应用view视图的右键菜单处理，特别是弹出菜单的菜单项的可用性的校验。
	 * 
	 * @author kenny liu
	 * 
	 * 2007-11-26 create
	 */
	private class SQLEditorPopmenuListener implements PopupMenuListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
		 */
		public void popupMenuCanceled(PopupMenuEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
		 */
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
		 */
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			itemCheck();
		}

	}
}
