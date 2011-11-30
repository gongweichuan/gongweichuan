/*
 * 创建日期 2006-10-24
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JTable;

import com.coolsql.action.common.ExportExcelOfTableAction.CloseWaitDialog;
import com.coolsql.exportdata.Actionable;
import com.coolsql.exportdata.ExportData;
import com.coolsql.exportdata.ExportFactory;
import com.coolsql.exportdata.ExportThread;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.component.WaitDialog;
import com.coolsql.pub.component.WaitDialogManage;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 公共的表控件文本导出事件处理
 */
public class ExportTextOfTableAction extends PublicAction {
    private JTable table; //被导数据的表控件

    private ExportData export; //数据导出器

    public ExportTextOfTableAction(JTable table) {
        super(null);
        this.table = table;
        export = ExportFactory.createExportForTable(table);
    }

    /*
     * （非 Javadoc）
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(!export.isRunning())
            export.launchTextRun();

        try {
            File tmpFile=export.selectFile(null);
            if(tmpFile==null)
                return;
            export.setFile(tmpFile);
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
            return;
        }
        
        //创建数据导出线程
        ExportThread thread=new ExportThread(export,ExportData.EXPORT_TEXT);
        final WaitDialog waiter = WaitDialogManage.getInstance().register(
                thread, CommonFrame.getTopOwner(table));
        waiter.addQuitAction(new Actionable()   //添加对excel生成的取消
                {

                    public void action() {
                        export.stopExport();
                        waiter.setPrompt(PublicResource.getString("waitdialog.prompt.quit"));
                    }
            
                }
        );
        thread.addAction(new CloseWaitDialog(waiter));
        
        waiter.setPrompt(PublicResource.getString("waitdialog.prompt.txt"));       
        thread.start();
        waiter.setVisible(true);
    }

    /**
     * @return 返回 table。
     */
    public JTable getTable() {
        return table;
    }

    /**
     * @param table
     *            要设置的 table。
     */
    public void setTable(JTable table) {
        this.table = table;
        export.setSource(table);
    }
}
