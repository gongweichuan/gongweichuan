/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

/**
 * <p>�ļ����ƣ�SysLogCond.java</p>
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
public class SysLogCond extends LogCond
{

    private int logRank[];

    private String taskName[];

    private String logSource[];

    private int isFilter[];

    public SysLogCond()
    {
        logRank = null;
        taskName = null;
        logSource = null;
        isFilter = null;
    }

    public int[] getLogRank()
    {
        return logRank;
    }

    public void setLogRank(int logRank[])
    {
        this.logRank = logRank;
    }

    public String[] getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName[])
    {
        this.taskName = taskName;
    }

    public String[] getLogSource()
    {
        return logSource;
    }

    public void setLogSource(String logSource[])
    {
        this.logSource = logSource;
    }

    public void setIsFilter(int filter[])
    {
        isFilter = filter;
    }

    public int[] getIsFilter()
    {
        return isFilter;
    }

}
