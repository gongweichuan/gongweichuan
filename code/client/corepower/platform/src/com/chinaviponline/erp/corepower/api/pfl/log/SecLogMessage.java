/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.util.Date;

/**
 * <p>�ļ����ƣ�SecLogMessage.java</p>
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
public class SecLogMessage extends LogMessage
{
    private static final long serialVersionUID = 0xb6452222d219L;

    protected long id;

    protected String userName;

    protected String clientIp;

    protected String operate;

    protected String para;

    protected Date logTime;

    protected String connectMode;

    protected String reserve[];

    public SecLogMessage()
    {
        id = -1L;
        userName = "";
        clientIp = "";
        operate = "";
        para = "";
        logTime = null;
        connectMode = "";
        reserve = null;
        setLogType(3);
    }

    public SecLogMessage(long id, String user, String ip, String operate,
            String detail, Date logTime, String mode, String reserve[])
    {
        this.id = -1L;
        userName = "";
        clientIp = "";
        this.operate = "";
        para = "";
        this.logTime = null;
        connectMode = "";
        this.reserve = null;
        setLogType(3);
        this.id = id;
        userName = user;
        clientIp = ip;
        this.operate = operate;
        para = detail;
        this.logTime = logTime;
        connectMode = mode;
        this.reserve = reserve;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public String getPara()
    {
        return para;
    }

    public String getOperate()
    {
        return operate;
    }

    public String getConnectMode()
    {
        return connectMode;
    }

    public Date getLogTime()
    {
        return logTime;
    }

    public String[] getReserve()
    {
        return reserve;
    }

}
