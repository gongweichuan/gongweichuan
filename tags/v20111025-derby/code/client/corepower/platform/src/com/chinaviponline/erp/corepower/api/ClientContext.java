/**
 * 
 */
package com.chinaviponline.erp.corepower.api;

import com.chinaviponline.erp.corepower.api.pfl.mainframe.RuntimeContext;
import com.chinaviponline.erp.corepower.api.psl.filetransfer.FileTransferContext;
import com.chinaviponline.erp.corepower.api.psl.ftp.FtpUserContext;

import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.*;

/**
 * <p>文件名称：ClientContext.java</p>
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
public class ClientContext implements RuntimeContext
{

    public static final String SERVER_LOCALE_JNDI = "languageJNDI";

    private static ClientContext instance = null;

    private HashMap ftpContextMap;

    private HashMap sftpContextMap;

    private HashMap filetransferContextMap;

    private String loginnedServerIP;

    public static synchronized ClientContext getInstance()
    {
        if (instance == null)
        {
            ClientContext temp = new ClientContext();
            instance = temp;
        }
        return instance;
    }

    public synchronized void resetFtpContext()
    {
        ftpContextMap.remove("FtpContext");
    }

    public synchronized FtpContext getFtpContext(String moduleFtpCtxJndiName)
    {
        FtpContext ftpContext = (FtpContext) ftpContextMap
                .get(moduleFtpCtxJndiName);
        if (ftpContext == null)
        {
            loginnedServerIP = System.getProperty("java.naming.provider.url");

            if (loginnedServerIP == null)
            {
                loginnedServerIP = "";
            }

            ftpContext = initFtpContext(moduleFtpCtxJndiName);
        }
        else if (!loginnedServerIP.equals(System
                .getProperty("java.naming.provider.url")))
        {
            loginnedServerIP = System.getProperty("java.naming.provider.url");

            if (loginnedServerIP == null)
            {
                loginnedServerIP = "";
            }

            ftpContext = initFtpContext(moduleFtpCtxJndiName);
        }
        return ftpContext;
    }

    public FtpContext getFtpContext()
    {
        return getFtpContext("FtpContext");
    }

    public FtpContext getGlobalFtpContext()
    {
        return getFtpContextByUsername("globalftp");
    }

    private FtpContext getFtpContextByUsername(String name)
    {
        FtpUserContext ftpusercxt;
        FtpContext globalFtpCxt = null;
        ftpusercxt = null;

        try
        {
            Context jndiContext = new InitialContext();
            ftpusercxt = (FtpUserContext) jndiContext.lookup("FtpUserContext");
        }
        catch (NamingException e)
        {
            throw new Error("Can not get (" + name + ") ftp context", e);
        }

        return ftpusercxt.getFtpContextByUsername(name);
        //        NamingException e;
        //        e;
        //        throw new Error("Can not get (" + name + ") ftp context", e);
    }

    public String getServerLocale()
    {
//        String serverLocaleText;
//
//        try
//        {
//            Context jndiContext = new InitialContext();
//            serverLocaleText = (String) jndiContext.lookup("languageJNDI");
//        }
//        catch (NamingException e)
//        {
//            throw new Error("Can not get server locale", e);
//        }
//
//        return serverLocaleText;
        
        //先硬编码
        return "zh_CN";
    }

    private FtpContext initFtpContext(String jndiName)
    {
        FtpContext ftpContext = null;
        try
        {
            Context jndiContext = new InitialContext();
            ftpContext = (FtpContext) jndiContext.lookup(jndiName);
            ftpContextMap.put(jndiName, ftpContext);
        }
        catch (NamingException e)
        {
            throw new Error("Can not get ftp context", e);
        }
        return ftpContext;
    }

    private SFtpContext initSftpContext(String jndiName)
    {
        SFtpContext sftpContext = null;
        try
        {
            Context jndiContext = new InitialContext();
            sftpContext = (SFtpContext) jndiContext.lookup(jndiName);
            sftpContextMap.put(jndiName, sftpContext);
        }
        catch (NamingException e)
        {
            throw new Error("Can not get sftp context", e);
        }
        return sftpContext;
    }

    private SFtpContext initSftpContext(String jndiUrl, String jndiName)
    {
        SFtpContext sftpContext = null;
        try
        {
            Hashtable p = new Hashtable();
            p.put("java.naming.provider.url", jndiUrl);
            Context jndiContext = new InitialContext(p);
            sftpContext = (SFtpContext) jndiContext.lookup(jndiName);
            sftpContextMap.put(jndiUrl + jndiName, sftpContext);
        }
        catch (NamingException e)
        {
            throw new Error("Can not get sftp context", e);
        }
        return sftpContext;
    }

    public SFtpContext getSftpContext()
    {
        return getSftpContext("SftpContext");
    }

    public synchronized SFtpContext getSftpContext(String moduleSftpCtxJndiName)
    {
        SFtpContext sftpContext = (SFtpContext) sftpContextMap
                .get(moduleSftpCtxJndiName);
        if (sftpContext == null)
        {
            loginnedServerIP = System.getProperty("java.naming.provider.url");

            if (loginnedServerIP == null)
            {
                loginnedServerIP = "";
            }

            sftpContext = initSftpContext(moduleSftpCtxJndiName);
        }
        else if (!loginnedServerIP.equals(System
                .getProperty("java.naming.provider.url")))
        {
            loginnedServerIP = System.getProperty("java.naming.provider.url");

            if (loginnedServerIP == null)
            {
                loginnedServerIP = "";
            }

            sftpContext = initSftpContext(moduleSftpCtxJndiName);
        }
        return sftpContext;
    }

    public synchronized SFtpContext getSftpContext(String jndiUrl,
            String moduleSftpCtxJndiName)
    {
        SFtpContext sftpContext = (SFtpContext) sftpContextMap.get(jndiUrl
                + moduleSftpCtxJndiName);

        if (sftpContext == null)
        {
            sftpContext = initSftpContext(jndiUrl, moduleSftpCtxJndiName);
        }

        return sftpContext;
    }

    public FileTransferContext getGlobalFileTransferContext()
    {
        return getFileTransferContext("globalftp");
    }

    public FileTransferContext getFileTransferContext()
    {
        String usename = getFtpContext().getUserName();
        return getFileTransferContext(usename);
    }

    public synchronized FileTransferContext getFileTransferContext(
            String moduleFileTransferCtxJndiName)
    {
        FileTransferContext filetransferContext = (FileTransferContext) filetransferContextMap
                .get(moduleFileTransferCtxJndiName);
        if (filetransferContext == null)
        {
            loginnedServerIP = System.getProperty("java.naming.provider.url");

            if (loginnedServerIP == null)
            {
                loginnedServerIP = "";
            }

            filetransferContext = initFileTransferContext(moduleFileTransferCtxJndiName);
        }
        else if (!loginnedServerIP.equals(System
                .getProperty("java.naming.provider.url")))
        {
            loginnedServerIP = System.getProperty("java.naming.provider.url");

            if (loginnedServerIP == null)
            {
                loginnedServerIP = "";
            }

            filetransferContext = initFileTransferContext(moduleFileTransferCtxJndiName);
        }
        return filetransferContext;
    }

    public synchronized FileTransferContext getFileTransferContext(
            String jndiUrl, String moduleFileTransferCtxJndiName)
    {
        FileTransferContext filetransferContext = (FileTransferContext) filetransferContextMap
                .get(jndiUrl + moduleFileTransferCtxJndiName);

        if (filetransferContext == null)
        {
            loginnedServerIP = System.getProperty("java.naming.provider.url");

            if (loginnedServerIP == null)
            {
                loginnedServerIP = "";
            }

            filetransferContext = initFileTransferContext(jndiUrl,
                    moduleFileTransferCtxJndiName);
        }

        return filetransferContext;
    }

    public String[] getFileTransferUserLoginURL(String filter[],
            String jndi_name)
    {
        String fileTransferLogUrl[];
        int filetransfer_type;

        try
        {
            Context jndiContext = new InitialContext();
            FtpUserContext ftpUserContext = (FtpUserContext) jndiContext
                    .lookup(jndi_name);
            fileTransferLogUrl = ftpUserContext.getLoginURL(filter);

            String s_filetransfer_type = System
                    .getProperty("erp.systemproperty.filetransfer.type");
            filetransfer_type = 1;

            if (s_filetransfer_type != null
                    && s_filetransfer_type.equalsIgnoreCase("sftp"))
            {
                filetransfer_type = 2;
            }

            if (filetransfer_type == 1)
            {
                return fileTransferLogUrl;
            }

            for (int i = 0; i < fileTransferLogUrl.length; i++)
            {
                fileTransferLogUrl[i] = "s" + fileTransferLogUrl[i];
            }

            return fileTransferLogUrl;
        }
        catch (NamingException e)
        {
            return null;
        }
        //        NamingException e;
        //        e;
        //        return null;
    }

    private FileTransferContext initFileTransferContext(String jndiName)
    {
        FtpContext ftpContext = getFtpContextByUsername(jndiName);
        SFtpContext sftpContext = getSftpContext();
        FileTransferContext filetransferContext = new FileTransferContext(
                ftpContext.getServerAddress(), ftpContext.getServerPort(),
                sftpContext.getServerPort(), ftpContext.getUserName(),
                ftpContext.getUserPassword());
        return filetransferContext;
    }

    private FileTransferContext initFileTransferContext(String jndiUrl,
            String jndiName)
    {
        FtpContext ftpContext = null;
        SFtpContext sftpContext = getSftpContext(jndiUrl, "SftpContext");

        try
        {
            Hashtable p = new Hashtable();
            p.put("java.naming.provider.url", jndiUrl);
            Context jndiContext = new InitialContext(p);
            ftpContext = (FtpContext) jndiContext.lookup(jndiName);
        }
        catch (NamingException e)
        {
            throw new Error("Can not get ftp context from " + jndiUrl, e);
        }

        FileTransferContext filetransferContext = new FileTransferContext(
                ftpContext.getServerAddress(), ftpContext.getServerPort(),
                sftpContext.getServerPort(), ftpContext.getUserName(),
                ftpContext.getUserPassword());
        return filetransferContext;
    }

    private ClientContext()
    {
        ftpContextMap = new HashMap();
        sftpContextMap = new HashMap();
        filetransferContextMap = new HashMap();
        loginnedServerIP = null;
    }

}
