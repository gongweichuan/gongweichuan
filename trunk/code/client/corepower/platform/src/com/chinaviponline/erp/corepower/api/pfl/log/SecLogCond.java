/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

/**
 * <p>文件名称：SecLogCond.java</p>
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
