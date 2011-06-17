/**
 * 
 */
package com.chinaviponline.erp.corepower.api;

import java.io.Serializable;

import com.chinaviponline.erp.corepower.api.pfl.mainframe.RuntimeContext;

/**
 * <p>�ļ����ƣ�FtpContext.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-28</p>
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
public class FtpContext implements RuntimeContext, Serializable
{
    public static final String JNDI_NAME = "FtpContext";

    private static final int BUFF_SIZE = 200;

    private String serverAddress;

    private int serverPort2;

    private short serverPort;

    private String userName;

    private String userPassword;

    private String url;

    private static final long serialVersionUID = 0x18817754984ecd54L;

    private long timeout;

    public FtpContext(String serverAddress, int serverPort, String userName,
            String userPassword)
    {
        this.serverAddress = null;
        serverPort2 = 0;
        this.serverPort = 0;
        this.userName = null;
        this.userPassword = null;
        url = null;
        timeout = 0L;
        this.serverAddress = serverAddress;
        serverPort2 = serverPort;
        this.userName = userName;
        this.userPassword = userPassword;
        StringBuffer buffer = new StringBuffer(200);
        buffer.append("ftp://");
        buffer.append(userName);
        buffer.append(':');
        buffer.append(userPassword);
        buffer.append('@');

        if (serverAddress.indexOf(':') != -1)
        {
            buffer.append("[" + serverAddress + "]");
        }
        else
        {
            buffer.append(serverAddress);
        }

        buffer.append(':');
        buffer.append(serverPort2);
        url = buffer.toString();
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    public String getServerAddress()
    {
        return serverAddress;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public int getServerPort()
    {
        return serverPort2;
    }

    public String getFtpLoginUrl()
    {
        return url;
    }

    public boolean equals(Object o)
    {
        FtpContext context = (FtpContext) o;

        if (!serverAddress.equals(context.getServerAddress()))
        {
            return false;
        }

        if (!userName.equals(context.getUserName()))
        {
            return false;
        }

        return serverPort2 == context.getServerPort();
    }

}
