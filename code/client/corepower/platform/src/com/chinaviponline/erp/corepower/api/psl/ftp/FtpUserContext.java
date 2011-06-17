/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.ftp;

import java.io.Serializable;
import java.util.*;

import com.chinaviponline.erp.corepower.api.FtpContext;

/**
 * <p>文件名称：FtpUserContext.java</p>
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
public class FtpUserContext implements Serializable
{
    private static final long serialVersionUID = 0x2e638e05e0e4eaf1L;

    public static final String JNDI_NAME = "FtpUserContext";

    private String ftpUsers[];

    private String loginURLs[];

    private String homePaths[];

    private String fileNames[];

    private HashMap fileNameToUrlMap;

    public FtpUserContext(String users[], String urls[], String paths[])
    {
        ftpUsers = new String[0];
        loginURLs = new String[0];
        homePaths = new String[0];
        fileNames = null;
        fileNameToUrlMap = null;
        ftpUsers = users;
        loginURLs = urls;
        homePaths = paths;
    }

    /**
     * @deprecated Method FtpUserContext is deprecated
     */

    public FtpUserContext(String users[], String urls[], String paths[],
            String names[])
    {
        ftpUsers = new String[0];
        loginURLs = new String[0];
        homePaths = new String[0];
        fileNames = null;
        fileNameToUrlMap = null;
        ftpUsers = users;
        loginURLs = urls;
        homePaths = paths;
        fileNames = names;
    }

    public FtpUserContext(String users[], String urls[], String paths[],
            HashMap fNameToUrlMap)
    {
        ftpUsers = new String[0];
        loginURLs = new String[0];
        homePaths = new String[0];
        fileNames = null;
        fileNameToUrlMap = null;
        ftpUsers = users;
        loginURLs = urls;
        homePaths = paths;
        fileNameToUrlMap = fNameToUrlMap;
    }

    public String[] getAllUserName()
    {
        return ftpUsers;
    }

    public String[] getAllLoginURL()
    {
        return loginURLs;
    }

    public String[] getLoginURL(String nameFilter[])
    {
        if (nameFilter == null)
        {
            return loginURLs;            
        }
        
        int length = ftpUsers.length;
        
        if (length == 0)
        {
            return new String[0];            
        }
        
        HashSet loginUrlSet = new HashSet();
        if (fileNameToUrlMap != null && fileNameToUrlMap.size() > 0)
        {
            Set tmpSet = fileNameToUrlMap.keySet();
            String tmpFileName = null;
            HashSet tmpHsSet = null;
            Iterator it = tmpSet.iterator();
            
            do
            {
                if (!it.hasNext())
                {
                    break;                    
                }
                
                tmpFileName = (String) it.next();
                
                if (isDestFile(nameFilter, tmpFileName))
                {
                    tmpHsSet = (HashSet) fileNameToUrlMap.get(tmpFileName);
                    
                    if (tmpHsSet != null && tmpHsSet.size() > 0)
                    {
                        loginUrlSet.addAll(tmpHsSet);                        
                    }
                }
            }
            while (true);
        }
        return (String[]) loginUrlSet.toArray(new String[loginUrlSet.size()]);
    }

    private boolean isDestFile(String nameFilter[], String fileName)
    {
        for (int i = 0; i < nameFilter.length; i++)
        {
            if (fileName.indexOf(nameFilter[i]) != -1)
            {
                return true;                
            }            
        }

        return false;
    }

    public String getLoginURLByName(String userName)
    {
        int length = ftpUsers.length;
        
        for (int i = 0; i < length; i++)
        {
            if (ftpUsers[i].equals(userName))
            {
                return loginURLs[i];                
            }            
        }

        return null;
    }

    public String getHomePathByName(String userName)
    {
        int length = ftpUsers.length;
        
        for (int i = 0; i < length; i++)
        {
            if (ftpUsers[i].equals(userName))
            {
                return homePaths[i];                
            }            
        }

        return null;
    }

    public FtpContext getFtpContextByUsername(String userName)
    {
        if (userName == null || userName.trim().length() == 0)
        {
            return null;            
        }
        
        String url = null;
        
        if (userName.startsWith("FtpServer.user."))
        {
            url = getLoginURLByName(userName);            
        }
        else
        {
            url = getLoginURLByName("FtpServer.user." + userName);            
        }
        
        if (url == null)
        {
            return null;            
        }
        else
        {
            return parseLoginURL(url);            
        }
        
    }

    public FtpContext[] getAllFtpContext()
    {
        if (loginURLs == null || loginURLs.length == 0)
        {
            return new FtpContext[0];            
        }
        
        FtpContext ftpCtxArray[] = new FtpContext[loginURLs.length];
        
        for (int i = 0; i < loginURLs.length; i++)
        {
            ftpCtxArray[i] = parseLoginURL(loginURLs[i]);            
        }

        return ftpCtxArray;
    }

    private FtpContext parseLoginURL(String url)
    {
        int index0 = url.indexOf("ftp://");
        index0 += 6;
        int index1 = url.indexOf(":", index0);
        int index2 = url.indexOf("@", index1);
        int index3 = url.lastIndexOf(":");
        String userName = url.substring(index0, index1);
        String userPassword = url.substring(index1 + 1, index2);
        String svrIP = url.substring(index2 + 1, index3);
        String portStr = url.substring(index3 + 1);
        int svrPort = Integer.parseInt(portStr);
        return new FtpContext(svrIP, svrPort, userName, userPassword);
    }

}
