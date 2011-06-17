/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.filetransfer;

import java.io.Serializable;

import com.chinaviponline.erp.corepower.api.pfl.mainframe.RuntimeContext;

/**
 * <p>文件名称：FileTransferContext.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-28</p>
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
public class FileTransferContext implements RuntimeContext, Serializable
{

    private static final int BUFF_SIZE = 200;

    private String serverAddress;

    private int sftpserverPort;

    private int ftpserverPort;

    private String userName;

    private String userPassword;

    private String url;

    private static final long serialVersionUID = 0x18817754984ecd55L;

    private long timeout;

    public FileTransferContext(String serverAddress, int ftpsvrPort,
            int sftpsvrPort, String userName, String userPassword)
    {
        this.serverAddress = null;
        sftpserverPort = 0;
        ftpserverPort = 0;
        this.userName = null;
        this.userPassword = null;
        url = null;
        timeout = 0L;
        this.serverAddress = serverAddress;
        ftpserverPort = ftpsvrPort;
        sftpserverPort = sftpsvrPort;
        this.userName = userName;
        this.userPassword = userPassword;
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
        int filetransfer_type = FileTransferType();

        if (filetransfer_type == 2)
        {
            return sftpserverPort;
        }
        else
        {
            return ftpserverPort;
        }
    }

    public static int FileTransferType()
    {
        String s_filetransfer_type = System
                .getProperty("ums.systemproperty.filetransfer.type");
        int filetransfer_type = 1;

        if (s_filetransfer_type != null
                && s_filetransfer_type.equalsIgnoreCase("sftp"))
        {
            filetransfer_type = 2;
        }

        return filetransfer_type;
    }

    public String getLoginUrl()
    {
        if (url != null)
        {
            return url;
        }

        StringBuffer buffer = new StringBuffer(200);
        int filetransfer_type = FileTransferType();

        if (filetransfer_type == 2)
        {
            buffer.append("sftp://");
        }
        else
        {
            buffer.append("ftp://");
        }

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

        if (filetransfer_type == 2)
        {
            buffer.append(sftpserverPort);
        }
        else
        {
            buffer.append(ftpserverPort);
        }

        url = buffer.toString();
        return url;
    }

    public boolean equals(Object o)
    {
        FileTransferContext context = (FileTransferContext) o;

        if (!serverAddress.equals(context.getServerAddress()))
        {
            return false;
        }

        if (!userName.equals(context.getUserName()))
        {
            return false;
        }

        return getServerPort() == context.getServerPort();
    }

}
