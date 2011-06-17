/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

import java.io.Serializable;
import java.util.Calendar;

/**
 * <p>�ļ����ƣ�LoginResult.java</p>
 * <p>�ļ���������½�����Ϣ</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-9-24</p>
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
public class LoginResult implements Serializable
{

    private static final long serialVersionUID = 0x54ca7d3eae657d74L;

    private int errID;

    private int userID;

    private String errDesc;

    private short sessionID;

    private long heartbeat_interval;

    private int heartbeat_fail_count;

    private int server_port;

    private String userName;

    private Calendar loginTime;

    public LoginResult()
    {
        errID = -1;
        userID = -1;
        errDesc = "";
        sessionID = -1;
        heartbeat_interval = 30000L;
        heartbeat_fail_count = 6;
        server_port = 2166;
        userName = "";
    }

    public void setErrDesc(String errDesc)
    {
        this.errDesc = errDesc;
    }

    public String getErrDesc()
    {
        return errDesc;
    }

    public int getErrID()
    {
        return errID;
    }

    public void setErrID(int errID)
    {
        this.errID = errID;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int uid)
    {
        userID = uid;
    }

    public void setSessionID(short sid)
    {
        sessionID = sid;
    }

    public short getSessionID()
    {
        return sessionID;
    }

    public void setHeartBeatInterval(long interval)
    {
        heartbeat_interval = interval;
    }

    public long getHeartBeatInterval()
    {
        return heartbeat_interval;
    }

    public void setHeartBeatFailCount(int count)
    {
        heartbeat_fail_count = count;
    }

    public int getHeartBeatFailCount()
    {
        return heartbeat_fail_count;
    }

    public int getServerListenPort()
    {
        return server_port;
    }

    public void setServerListenPort(int port)
    {
        server_port = port;
    }

    public void setUserName(String name)
    {
        userName = name;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setLoginTime(Calendar calendar)
    {
        loginTime = calendar;
    }

    public Calendar getLoginTime()
    {
        return loginTime;
    }

}
