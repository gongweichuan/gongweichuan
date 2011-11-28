/*
 * 创建日期 2006-10-27
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JTable;

import com.coolsql.exportdata.Actionable;
import com.coolsql.exportdata.ExportData;
import com.coolsql.exportdata.ExportFactory;
import com.coolsql.exportdata.ExportThread;
import com.coolsql.exportdata.excel.ExcelUtil;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.component.WaitDialog;
import com.coolsql.pub.component.WaitDialogManage;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *导出excel文件的事件处理类
 */
public class ExportExcelOfTableAction extends PublicAction {

    private JTable table=null;
    private ExportData export=null;
    public ExportExcelOfTableAction(JTable table)
    {
       super(null);
       this.table=table;
       export=ExportFactory.createExportForTable(table);
    }
    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

        try {
            File tmpFile=export.selectFile(ExportData.getFileType(ExportData.EXPORT_EXCEL));
            if(tmpFile==null)
                return;
            export.setFile(tmpFile);
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
            return;
        }
        //创建数据导出线程
        ExportThread thread=new ExportThread(export,ExportData.EXPORT_EXCEL);
        final WaitDialog waiter = WaitDialogManage.getInstance().register(
                thread, CommonFrame.getTopOwner(table));
        waiter.addQuitAction(new Actionable()   //添加对excel生成的取消
                {

                    public void action() {
                        ExcelUtil util=ExcelUtil.getInstance();
                        if(util.isRunning()) //如果正在执行,取消excel的导出
                            util.setRun(false);
                        waiter.setPrompt(PublicResource.getString("waitdialog.prompt.quit"));
                    }
            
                }
        );
        thread.addAction(new CloseWaitDialog(waiter));
        waiter.setPrompt(PublicResource.getString("waitdialog.prompt.excel"));
        thread.start();        
        waiter.setVisible(true);
    }
    /**
     * @author liu_xlin
     *退出关闭等待对话框
     */
    protected static class CloseWaitDialog implements Actionable
    {
        private WaitDialog waiter;
        public CloseWaitDialog(WaitDialog waiter)
        {
           this.waiter=waiter;   
        }
        public void action() {
           
            waiter.dispose();
        }

    }
    /**
     * @return 返回 table。
     */
    public JTable getTable() {
        return table;
    }
    /**
     * @param table 要设置的 table。
     */
    public void setTable(JTable table) {
        this.table = table;
        export.setSource(table);
    }
}
