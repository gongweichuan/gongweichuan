/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

/**
 * <p>�ļ����ƣ�SecLogCond.java</p>
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
public class SecLogCond extends LogCond
{

    private String userName[];

    private String connectMode[];

    private String logName[];

    private int isFilter[];

    public SecLogCond()
    {
        userName = null;
        connectMode = null;
        logName = null;
        isFilter = null;
    }

    public String[] getUserName()
    {
        return userName;
    }

    public void setUserName(String userName[])
    {
        this.userName = userName;
    }

    public String[] getConnectMode()
    {
        return connectMode;
    }

    public void setConnectMode(String connectMode[])
    {
        this.connectMode = connectMode;
    }

    public String[] getLogName()
    {
        return logName;
    }

    public void setLogName(String logName[])
    {
        this.logName = logName;
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
