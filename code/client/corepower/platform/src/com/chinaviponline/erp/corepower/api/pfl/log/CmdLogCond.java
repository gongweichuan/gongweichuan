/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

/**
 * <p>文件名称：CmdLogCond.java</p>
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
public class CmdLogCond extends LogCond
{

    public static final String OPERATE_SET_OTHER_ID = "corepower.otheroperation";

    private String userName[];

    private int isSuccess[];

    private String failReason[];

    private String operateSet[];

    private String resourceDn[];

    private String resourceType[];

    private String cmdCode[];

    private String connectMode[];

    private int logRank[];

    private int isFilter[];

    public CmdLogCond()
    {
        userName = null;
        isSuccess = null;
        failReason = null;
        operateSet = null;
        resourceDn = null;
        resourceType = null;
        cmdCode = null;
        connectMode = null;
        logRank = null;
        isFilter = null;
    }

    public String[] getCmdCode()
    {
        return cmdCode;
    }

    public void setCmdCode(String cmdCode[])
    {
        this.cmdCode = cmdCode;
    }

    public String[] getConnectMode()
    {
        return connectMode;
    }

    public void setConnectMode(String connectMode[])
    {
        this.connectMode = connectMode;
    }

    public int[] getIsSuccess()
    {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess[])
    {
        this.isSuccess = isSuccess;
    }

    public String[] getFailReason()
    {
        return failReason;
    }

    public void setFailReason(String reason[])
    {
        failReason = reason;
    }

    public int[] getLogRank()
    {
        return logRank;
    }

    public void setLogRank(int logRank[])
    {
        this.logRank = logRank;
    }

    public String[] getOperateSet()
    {
        return operateSet;
    }

    public void setOperateSet(String operateSet[])
    {
        this.operateSet = operateSet;
    }

    public String[] getUserName()
    {
        return userName;
    }

    public void setUserName(String userName[])
    {
        this.userName = userName;
    }

    public String[] getResourceDn()
    {
        return resourceDn;
    }

    public String[] getResourceType()
    {
        return resourceType;
    }

    public void setResource(String resourceDn[], String resourceType[])
    {
        this.resourceDn = resourceDn;
        this.resourceType = resourceType;
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
