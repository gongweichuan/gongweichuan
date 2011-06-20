/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.systemsupport;

import java.io.*;
import java.util.*;

import javax.management.IntrospectionException;
//import javax.management.IntrospectionException;
/**
 * <p>文件名称：SystemSupportService.java</p>
 * <p>文件描述：本地文件系统代理</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-2</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：
 *  版本号：
 *  修改人：
 *  修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public interface SystemSupportService
{

    public abstract File getFile(String s);

    /*
    public abstract File getFile(Par par, String s);

    public abstract String getFilePath(String s)
        throws IOException;

    public abstract String getFilePath(Par par, String s)
        throws IOException;
     */

    public abstract File[] getFiles(String s);

    public abstract File[] getFilesRefreshCache(String s);

    public abstract String[] getFilePaths(String s)
        throws IOException;

   
    public abstract void addSystemStateListener(SystemStateListener systemstatelistener)
        throws SystemListenerException;

    /*
    public abstract void addLicenseStateListener(LicenseStateListener licensestatelistener)
        throws SystemListenerException;

    public abstract void removeSystemStateListener(SystemStateListener systemstatelistener)
        throws SystemListenerException;

    public abstract void removeLicenseStateListener(LicenseStateListener licensestatelistener)
        throws SystemListenerException;

    public abstract String getLicenseExpiration()
        throws LicenseException;

    public abstract HashMap getAllLicenseFileInfo()
        throws LicenseException;

    public abstract HashMap getAllLicenseValue()
        throws LicenseException;

*/
    public abstract Par getDeployedPar(String s);

    public abstract Par getDeployedPar(Class class1)
        throws ClassNotFoundException, IntrospectionException;

    public abstract Par[] getDeployedPars();

    public abstract String getERPProperty(String s);

    public abstract boolean isStarted();

    public abstract boolean isCluster();

    public abstract String getGlobalFileSystemRootPath();

    public abstract String getSingletonNodeAddr();

    public abstract File[] getLayers();

    public abstract File[] getDirs(File afile[], String s);

    /*
    public abstract WebServerContext getWebServerContext();
    */
    
    public abstract Serializable getFromRegistry(String s);

    public abstract void setIntoRegistry(String s, Serializable serializable)
        throws IOException;

    public abstract void startedup()
        throws FileNotFoundException, IOException;

    /*
    public abstract String getLicenseValue(String s)
        throws LicenseException;

    public abstract ArrayList getClntNoPermissionPar()
        throws LicenseException;

    public abstract ArrayList getSvrNoPermissionPar()
        throws LicenseException;

    public abstract SystemAccountManager getAccountManager();
    */

    public abstract Date getStartTime();

    public static final String ROLE = "system-support-service";
    public static final String DEPLOYED_PARS = "DeployedPars";
    public static final String SERVER_TYPE = "server";
    public static final String CLINT_TYPE = "client";
    public static final String ERP_HOME_DIR = "erp.home";
    public static final String COREPOWER_HOME_DIR = "corepower.home";
    public static final String ERP_RUNTIME_DIR = "erp.runtime";
    public static final String ERP_TEMP_DIR = "erp.temp";
    public static final String ERP_RUNDATA_DIR = "erp.rundata";
    public static final String ERP_GLOBALFILESYSTEM_DIR = "erp.globalfile";
}
