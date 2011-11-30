/*
 * 创建日期 2006-10-26
 */
package com.coolsql.exportdata;

import java.util.Vector;

import com.coolsql.pub.component.WaitDialogManage;
import com.coolsql.pub.display.exception.NotRegisterException;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 数据导出时，为了避免发生swing组件线程不安全的事件，编写此类来解决此问题
 */
public class ExportThread extends Thread {
    private ExportData export = null;

    /**
     * 数据导出的类型
     */
    private int processType;

    /**
     * 该类用来保存线程执行完成后所需要执行的动作
     */
    private Vector actions = new Vector();

    public ExportThread(ExportData export, int processType) {
        super("exportData");
        if (export == null) {
            throw new IllegalArgumentException("exporter is null");
        }
        if (processType != ExportData.EXPORT_TEXT
                && processType != ExportData.EXPORT_EXCEL
                && processType != ExportData.EXPORT_HTML)
            throw new IllegalArgumentException("export type is unknown:"
                    + processType);

        this.export = export;
        this.processType = processType;
    }

    public void run() {
        try {
            Thread.sleep(50);
            export.setWaiter(WaitDialogManage.getInstance().getDialogOfCurrent());
            if (processType == ExportData.EXPORT_TEXT) {

                export.exportToTxt();

            } else if (processType == ExportData.EXPORT_EXCEL) {
                export.exportToExcel();
            } else if (processType == ExportData.EXPORT_HTML) {
                export.exportToHtml();
            }
        } catch (UnifyException e) {
            LogProxy.errorReport(e);
        } catch (InterruptedException e) {
            LogProxy.outputErrorLog(e);
        } catch (NotRegisterException e) {
            LogProxy.errorReport(e);
        } finally {
            WaitDialogManage.getInstance().disposeRegister(this);
            runActions();
        }
    }

    /**
     * 添加线程执行完成后的动作事件
     * 
     * @param action
     */
    public void addAction(Actionable action) {
        synchronized (actions) {
            if (action != null)
                actions.add(action);
        }
    }

    private void runActions() {
        synchronized (actions) {
            for (int i = 0; i < actions.size(); i++) {
                Actionable action = (Actionable) actions.get(i);
                action.action();
            }
        }
    }

    /**
     * @return 返回 processType。
     */
    public int getProcessType() {
        return processType;
    }
}
