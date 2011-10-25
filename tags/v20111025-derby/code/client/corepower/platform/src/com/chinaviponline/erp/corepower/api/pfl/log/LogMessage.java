/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.io.Serializable;

/**
 * <p>文件名称：LogMessage.java</p>
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
