/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�LogMessage.java</p>
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
public class LogMessage implements Serializable
{
    public static final int CMDLOG = 1;

    public static final int SYSLOG = 2;

    public static final int SCRTLOG = 3;

    public static final int EVTLOG = 4;

    private static final long serialVersionUID = 0xb6452221fb41L;

    private int logType;

    private int logAccount;

    private long idPageInfo[][];

    public LogMessage()
    {
        logType = 0;
        logAccount = 0;
        idPageInfo = (long[][]) null;
    }

    public void setLogType(int logtype)
    {
        if (logtype < 1 || logtype > 4)
        {
            return;
        }
        else
        {
            logType = logtype;
            return;
        }
    }

    public int getLogType()
    {
        return logType;
    }

    public void setLogAccount(int n)
    {
        logAccount = n;
    }

    public int getLogAccount()
    {
        return logAccount;
    }

    public long[][] getPageInfo()
    {
        return idPageInfo;
    }

    public void setPageInfo(long pageInfo[][])
    {
        idPageInfo = pageInfo;
    }

}
