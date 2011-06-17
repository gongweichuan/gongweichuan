/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

/**
 * <p>�ļ����ƣ�CmdLogCond.java</p>
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
