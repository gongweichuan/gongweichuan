/*
 * 创建日期 2006-10-24
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

import com.coolsql.action.common.ExportExcelOfTableAction.CloseWaitDialog;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.exportdata.Actionable;
import com.coolsql.exportdata.ExportDBData;
import com.coolsql.exportdata.ExportData;
import com.coolsql.exportdata.ExportFactory;
import com.coolsql.exportdata.ExportThread;
import com.coolsql.exportdata.excel.ExcelUtil;
import com.coolsql.pub.component.WaitDialog;
import com.coolsql.pub.component.WaitDialogManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.ConnectionUtil;
import com.coolsql.view.SqlEditorView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.sqleditor.ScriptParser;

/**
 * @author liu_xlin 从数据库中获取数据，然后导出为文本文件
 */
public class ExportDataOfDBAction extends PublicAction {
	private static final long serialVersionUID = 1L;

	/**
     * 数据导出器
     */
    private ExportDBData export = null;

    /**
     * 处理类型
     */
    private int processType = -1;

    public ExportDataOfDBAction(int processType) {
        export = (ExportDBData) ExportFactory.createExportForSql(null, null);
        this.processType = processType;
    }

    /*
     * （非 Javadoc）
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (!updateData())
            return;

        try {
            File tmpFile = export.selectFile(ExportData
                    .getFileType(processType));
            if (tmpFile == null)
                return;
            export.setFile(tmpFile);

        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
            return;
        }

        //创建数据导出线程
        final ExportThread thread = new ExportThread(export, processType);
        final WaitDialog waiter = WaitDialogManage.getInstance().register(
                thread, GUIUtil.getMainFrame());
        waiter.addQuitAction(new Actionable() //添加对excel生成的取消
                {

                    public void action() {
                        if (processType == ExportData.EXPORT_EXCEL) {
                            ExcelUtil util = ExcelUtil.getInstance();
                            if (util.isRunning()) //如果正在执行,取消excel的导出
                                util.setRun(false);
                        } 
                        export.stopExport();

                        waiter.setPrompt(PublicResource
                                .getString("waitdialog.prompt.quit"));
                        
                        try {
                            ConnectionUtil.quitLongTimeStatement(thread);
                        } catch (SQLException e) {
                            LogProxy.errorReport(e);
                        }
                    }

                });
        thread.addAction(new CloseWaitDialog(waiter));

        waiter.setPrompt(getWaitPrompt());
        thread.start();       
        waiter.setVisible(true);

    }

    private String getWaitPrompt() {
        if (processType == ExportData.EXPORT_TEXT)
            return PublicResource.getString("waitdialog.prompt.txt");
        else if (processType == ExportData.EXPORT_EXCEL) {
            return PublicResource.getString("waitdialog.prompt.excel");
        } else if (processType == ExportData.EXPORT_HTML) {
            return PublicResource.getString("waitdialog.prompt.html");
        } else
            return "";
    }

    /**
     * 更新
     * 
     * @return
     */
    private boolean updateData() {
        SqlEditorView view = ViewManage.getInstance().getSqlEditor();
        Bookmark bookmark = BookmarkManage.getInstance().getDefaultBookmark();
        if (bookmark == null) {
            LogProxy.infoMessage(PublicResource
                    .getSQLString("export.sql.nodatabase"));
            return false;
        }
        if (!bookmark.isConnected()) {
            LogProxy.infoMessage(PublicResource
                    .getSQLString("export.sql.notconnected"));
            return false;
        }
        export.setSource(bookmark); //更新数据源

        ScriptParser sqlParser=new ScriptParser(view.getSelectedText());
        
        /**
         * 检查sql是否存在
         */
        if (sqlParser.getSize() == 0) //如果没有选中sql
        {
            LogProxy.infoMessage(PublicResource.getSQLString("export.nosql"));
            return false;
        }
        /**
         * 检查sql语句的数量
         */
        if (sqlParser.getSize() > 1) {
            LogProxy.infoMessage(PublicResource
                    .getSQLString("export.sql.outofamount"));
            return false;
        }

        /**
         * 检查sql的类型
         */
        String tmpSQL = (String) sqlParser.getCommand(0);
        if (tmpSQL != null && !tmpSQL.equals("")) {
            if (!checkSelectSQLType(tmpSQL)) {
                LogProxy.infoMessage(PublicResource
                        .getSQLString("export.sql.typeerror"));
                return false;
            }
        } else
            return false;

        export.setSql(tmpSQL);
        return true;

    }

    /**
     * 校验给出的sql语句是否为查询类型
     * 
     * @param s
     * @return
     */
    private boolean checkSelectSQLType(String s) {
        String tmp = StringUtil.trim(s).toLowerCase();
        if (tmp.startsWith("select ")) {
            return true;
        } else
            return false;
    }
}
