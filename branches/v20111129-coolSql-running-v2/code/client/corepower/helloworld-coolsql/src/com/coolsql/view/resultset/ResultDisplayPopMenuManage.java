/*
 * 创建日期 2006-10-12
 */
package com.coolsql.view.resultset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.coolsql.action.framework.CsAction;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.display.BaseTable;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.Setting;
import com.coolsql.system.menubuild.IconResource;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.resultset.action.AddNewDataAction;
import com.coolsql.view.resultset.action.CancelDeleteRowsAction;
import com.coolsql.view.resultset.action.ChangeStatusOfDataSetPanelAction;
import com.coolsql.view.resultset.action.CopyAsSqlInsertAction;
import com.coolsql.view.resultset.action.DataSetTableCopyAction;
import com.coolsql.view.resultset.action.DeleteRowsAction;
import com.coolsql.view.resultset.action.EditInDialogAction;
import com.coolsql.view.resultset.action.ExportExcelOfResultAction;
import com.coolsql.view.resultset.action.ExportHtmlOfResultAction;
import com.coolsql.view.resultset.action.ExportTxtOfResultAction;
import com.coolsql.view.resultset.action.RefreshQueryAction;
import com.coolsql.view.resultset.action.RestoreCellAction;
import com.coolsql.view.resultset.action.SaveChangeToDBAction;
import com.coolsql.view.resultset.action.ShowColumnSelectDialogAction;
import com.coolsql.view.resultset.action.UpdateRowActioin;

/**
 * @author liu_xlin 结果视图右键菜单管理类
 */
public class ResultDisplayPopMenuManage extends BaseMenuManage {
    /**
     * 复制
     */
    private JMenuItem copy;

    /**
     * 导出数据
     */
    private JMenu export;

    //导出成文本
    private JMenuItem exportTxt;

    //导出成excel文件
    private JMenuItem exportExcel;

    //导出成网页
    private JMenuItem exportHtml;

    //刷新数据(重新执行sql)
//    private JMenuItem refresh;

    /**
     * 更新数据选中的行
     */
//    private JMenuItem updateData = null;

    /**
     * 如果结果集满足相关的条件(只对一个实体进行的操作),可对该实体进行数据的插入
     */
//    private JMenuItem insertData=null;
    
    private JMenuItem preferWidth;  //将选中列设置为最佳宽度
    /**
     * 表控件的展示设置
     */
    private JMenu tableSetting = null;

    //是否可排序
    private JCheckBoxMenuItem isSortable = null;

    private JMenu tableLine = null;

    //横向线条的显示
    private JCheckBoxMenuItem horizontalLine = null;

    //竖向线条的显示
    private JCheckBoxMenuItem verticalLine = null;

    /**
     * enable/disable displaying table line number
     */
    private JCheckBoxMenuItem lineNumber;
    /**
     * @param com
     */
    public ResultDisplayPopMenuManage(JComponent com) {
        super(com);
        createPopMenu();
    }

    public void createPopMenu() {
        popMenu = new BasePopupMenu();

        //复制菜单的初始化
        Action copyAction = new DataSetTableCopyAction();
        copy = createMenuItem(PublicResource
                .getString("TextEditor.popmenu.copy"), PublicResource
                .getIcon("TextMenu.icon.copy"), copyAction);
        popMenu.add(copy);
        copy.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        
        popMenu.addSeparator();
        
        //刷新菜单项
//        refresh = createMenuItem(PublicResource
//                .getString("resultView.popmenu.refresh.label"), PublicResource
//                .getIcon("resultView.iconbutton.refresh.icon"),
//                new EditDataSetTableAction(ResultSetDataProcess.REFRESH));
        CsAction refreshAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RefreshQueryAction.class);
        popMenu.add(refreshAction.getMenuItem());

        //更新菜单项
        
        CsAction updateAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(UpdateRowActioin.class);
        popMenu.add(updateAction.getMenuItem());
        
        CsAction addAction=Setting.getInstance().getShortcutManager()
			.getActionByClass(AddNewDataAction.class);
        //向实体插入数据菜单项
        popMenu.add(addAction.getMenuItem());
        
        //Copy data as insert sql.
        CsAction copyAsInsertSQLAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CopyAsSqlInsertAction.class);
        popMenu.add(copyAsInsertSQLAction.getMenuItem());
        
        popMenu.addSeparator();
        
        //To change the status of data panel
        CsAction changeStatusAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(ChangeStatusOfDataSetPanelAction.class);
        popMenu.add(changeStatusAction.getMenuItem());
        
        //To restore the value of selected cell
        CsAction restoreCellAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RestoreCellAction.class);
        popMenu.add(restoreCellAction.getMenuItem());
        
        //To delete the selected rows
        CsAction deleteRowAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(DeleteRowsAction.class);
        popMenu.add(deleteRowAction.getMenuItem());
        
      //To cancel the selected rows have been marked as deleted.
        CsAction cancelDeleteRowAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CancelDeleteRowsAction.class);
        popMenu.add(cancelDeleteRowAction.getMenuItem());
        
        //To edit cell value in dialog
        CsAction editInDialogAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(EditInDialogAction.class);
        popMenu.add(editInDialogAction.getMenuItem());
        
        //Select columns as key for instant updating.
        CsAction showColumnSelectDialogAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(ShowColumnSelectDialogAction.class);
        popMenu.add(showColumnSelectDialogAction.getMenuItem());
        
        //Save cell value into database
        CsAction saveChangeToDBAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(SaveChangeToDBAction.class);
        popMenu.add(saveChangeToDBAction.getMenuItem());
        
        popMenu.addSeparator();

        //导出数据菜单
        export = new JMenu(PublicResource.getString("table.popup.export"));
        export.setIcon(IconResource.getBlankIcon());
        popMenu.add(export);

        //导出文本菜单
        exportTxt = createMenuItem(PublicResource
                .getString("table.popup.exportTxt"), PublicResource
                .getIcon("table.popup.export.txticon"),
                new ExportTxtOfResultAction());
        export.add(exportTxt);

        //导出excel菜单
        exportExcel = createMenuItem(PublicResource
                .getString("table.popup.exportExcel"), PublicResource
                .getIcon("table.popup.export.excelicon"),
                new ExportExcelOfResultAction());
        export.add(exportExcel);

        //导出网页菜单
        exportHtml = createMenuItem(PublicResource
                .getString("table.popup.exportHtml"), PublicResource
                .getIcon("table.popup.export.htmlicon"),
                new ExportHtmlOfResultAction());
        export.add(exportHtml);

        popMenu.addSeparator();
        
        //调整宽度菜单项
        Action preferWidthAction=new AbstractAction()
        {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                BaseTable table=(BaseTable)popMenu.getClientProperty(ResultSetView.DataTable);
                table.getDefaultAdjustWidthAction().actionPerformed(e);
            }
            
        };
        preferWidth=addMenuItem(
                PublicResource
                .getSQLString("rowinsert.table.popmenu.preferwidth"),
                preferWidthAction,
        PublicResource
                .getSQLIcon("rowinsert.table.popmenu.preferwidth.icon"),
        false);
        preferWidth.setAccelerator(KeyStroke.getKeyStroke("alt W"));
        
        /**
         * 表控件属性设置菜单项
         */
        tableSetting = new JMenu(PublicResource
                .getString("table.popup.tablesetting"));
        tableSetting.setIcon(IconResource.getBlankIcon());
        popMenu.add(tableSetting);

        //是否可排序
        isSortable = new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.issortable"));
        isSortable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setTableSortable(isSortable.getState());
            }

        });
        tableSetting.add(isSortable);

        //表格线设置
        tableLine = new JMenu(PublicResource.getString("table.popup.tableline"));
        tableSetting.add(tableLine);

        //横向线设置
        horizontalLine = new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.horizontal"));
        horizontalLine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                setHorizontalLine(horizontalLine.getState());
            }

        });
        tableLine.add(horizontalLine);

        //竖向线设置
        verticalLine = new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.vertical"));
        verticalLine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                setVerticalLine(verticalLine.getState());
            }

        });
        tableLine.add(verticalLine);
        
        lineNumber=new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.linenumber"));
        lineNumber.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	((BaseTable)getComponent()).setTableRowNumberVisible(lineNumber.getState());
            }

        });
        popMenu.add(lineNumber);
        
//        popMenu.addPopupMenuListener(new PopupMenuListener()
//        {
//
//			public void popupMenuCanceled(PopupMenuEvent e) {
//				
//			}
//
//			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//				popMenu.putClientProperty(ResultSetView.DataTable,null);
//				
//			}
//
//			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//				
//			}
//        	
//        }
//        );
    }

    /**
     * 设置表控件是否可排序
     * 
     * @param b
     */
    private void setTableSortable(boolean b) {
        DataSetTable tmpTable = (DataSetTable) this.getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setSortable(b);
    }

    private void setHorizontalLine(boolean b) {
        DataSetTable tmpTable = (DataSetTable) this.getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setShowHorizontalLines(b);
    }

    private void setVerticalLine(boolean b) {
        DataSetTable tmpTable = (DataSetTable) this.getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setShowVerticalLines(b);
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        if (popMenu == null)
            createPopMenu();

        DataSetTable tmpTable = (DataSetTable) this.getComponent();

        int[] selectRows = tmpTable.getSelectedRows();
        if (selectRows == null || selectRows.length == 0) //如果没有选中数据，不可用
        {
            if (copy.isEnabled())
                copy.setEnabled(false);
            GUIUtil.setComponentEnabled(false, preferWidth);
        } else //选中数据，可用
        {
            if (!copy.isEnabled())
                copy.setEnabled(true);

            GUIUtil.setComponentEnabled(true, preferWidth);
        }

        if (tmpTable.getRowCount() > 0) //表控件有数据
        {
            if (export.isEnabled())
                export.setEnabled(true);
        } else //表控件没有数据
        {
            if (export.isEnabled())
                export.setEnabled(false);
        }

        //属性设置部分
        this.isSortable.setSelected(tmpTable.isSortable());
        this.horizontalLine.setSelected(tmpTable.getShowHorizontalLines());
        this.verticalLine.setSelected(tmpTable.getShowVerticalLines());
        this.lineNumber.setSelected(tmpTable.isRowNumberVisible());
        
        super.menuCheck();
        return popMenu;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }

    /**
     * 直接获取未经菜单项可用性处理的菜单对象
     * 
     * @return
     */
    public JPopupMenu getNoRenderMenu() {
        return popMenu;
    }
}
