/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log.queryservice;

import javax.swing.JPanel;
import javax.swing.JTable;

import com.chinaviponline.erp.corepower.api.pfl.log.LogCond;
import com.chinaviponline.erp.corepower.api.pfl.log.LogMessage;

/**
 * <p>�ļ����ƣ�TablePanel.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-7-12</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�    �汾�ţ�    �޸��ˣ�    �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
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
