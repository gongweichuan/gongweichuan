/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log.queryservice;

import javax.swing.JPanel;
import javax.swing.JTable;

import com.chinaviponline.erp.corepower.api.pfl.log.LogCond;
import com.chinaviponline.erp.corepower.api.pfl.log.LogMessage;

/**
 * <p>文件名称：TablePanel.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-7-12</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public abstract class TablePanel extends JPanel
{

    protected LogCond cond;

    protected LogCond tmpCond;

    public TablePanel()
    {
        cond = null;
        tmpCond = null;
    }

    public abstract void setController(TableController tablecontroller);

    public abstract TableController getController();

    public abstract String[] getSelectedLogId();

    public abstract int getLogAccount();

    public abstract void setLogCurrentPageNo(int i);

    public abstract int getLogCurrentPageNo();

    public abstract int getLogPageAccount();

    public abstract int[] getLogColSort();

    public abstract String[] getLogColName();

    public abstract int[] getLogColWidth();

    public abstract int[] getLogPrintWidth();

    public abstract void updataLogSelectRowData(LogMessage logmessage);

    public abstract JTable getLogTable();

    public abstract void setExportLogBtnEnable(boolean flag);

    public abstract void setPrintLogBtnEnable(boolean flag);

    public abstract void setSaveCondBtnEnable(boolean flag);

    public abstract void hideExPrBtn(boolean flag);

    public abstract LogMessage[] getPanelData();

    public void setCond(LogCond logCond)
    {
        cond = logCond;
    }

    public void setTmpCond(LogCond cond)
    {
        tmpCond = cond;
    }

    public LogCond getTmpCond()
    {
        if (tmpCond == null)
            tmpCond = cond;
        return tmpCond;
    }

}
