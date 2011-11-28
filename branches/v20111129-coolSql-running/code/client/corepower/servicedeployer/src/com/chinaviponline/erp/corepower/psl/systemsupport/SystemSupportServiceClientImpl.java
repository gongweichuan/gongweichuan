/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;


import java.io.*;
import java.net.*;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;

import javax.management.IntrospectionException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
//import javax.management.*;
import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.api.psl.systemsupport.Par;
import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemListenerException;
import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemStateListener;
import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemSupportService;
import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileScanner;
import com.chinaviponline.erp.corepower.psl.systemsupport.registry.RegistryFactory;

/**
 * <p>文件名称：SystemSupportServiceClientImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-20</p>
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
public class SystemSupportServiceClientImpl implements SystemSupportService
{
    
    /**
     * 日志
     */
    private static Logger log;
    
    private static MBeanServer server = null;
        
    private HashMap jarToPar;
    private static PropertiesService ps = PropertiesService.getInstance();
    private static final String KEYSTR = "at ";
    private static final String PARSTR = "*.par";
//    private static WebServerContext webServerContext = null;

    // 初始化
    static 
    {
        log = Logger.getLogger(SystemSupportServiceClientImpl.class);
    }
    
    public SystemSupportServiceClientImpl()
    {
        jarToPar = null;
    }

    public File getFile(String fileName)
    {
        return FileScanner.getFile(fileName);
    }

    public String getFilePath(String fileName)
        throws IOException
    {
        File file = getFile(fileName);
        
        if(file != null)
        {
            return file.getCanonicalPath();            
        }
        else
        {
            return null;            
        }
    }

    /*
    public SystemAccountManager getAccountManager()
    {
        throw new UnsupportedOperationException("This method can not be called from erp-clnt.");
    }
    */
    
    public File getFile(Par par, String fileName)
    {
        if(par != null)
        {
            String basePath = par.getBaseDir();
            
            return FileScanner.getFile(new String[] { basePath }, fileName);
        }
        else
        {
            return null;
        }
    }

    public String getFilePath(Par par, String fileName)
        throws IOException
    {        
        File file = getFile(par, fileName);
        
        if(file != null)
        {
            return file.getCanonicalPath();            
        }
        else
        {
            return null;            
        }
    }

    public File[] getFiles(String suffixFileName)
    {
        return FileScanner.getFiles(suffixFileName);
    }

    public File[] getFilesRefreshCache(String suffixFileName)
    {
        return FileScanner.getFilesRefreshCache(suffixFileName);
    }

    public String[] getFilePaths(String patternFileName)
        throws IOException
    {
        File fs[] = getFiles(patternFileName);
        ArrayList list = new ArrayList();
        
        for(int i = 0; i < fs.length; i++)
        {
            list.add(fs[i].getCanonicalPath());            
        }

        String ss[] = new String[list.size()];
        list.toArray(ss);
        
        return ss;
    }

    public void addSystemStateListener(SystemStateListener listener)
    throws SystemListenerException
    {
        SystemStateManager.addSystemStateListener(listener);
    }


    

    public void removeSystemStateListener(SystemStateListener listener)
        throws SystemListenerException
    {
        SystemStateManager.removeSystemStateListener(listener);
    }

    /*
    public void addLicenseStateListener(LicenseStateListener listener)
        throws SystemListenerException
    {
        LicenseCheck.addLicenseStateListener(listener);
    }

    public void removeLicenseStateListener(LicenseStateListener listener)
        throws SystemListenerException
    {
        LicenseCheck.removeSystemStateListener(listener);
    }
     */
    
    public Par[] getDeployedPars()
    {
        return ParCollector.getSequenceDeployedPars();
    }

    public Par getDeployedPar(String parId)
    {
        return ParCollector.getDeployedPar(parId);
    }

    public Par getDeployedPar(Class cls)
        throws ClassNotFoundException, IntrospectionException
    {
        String jarPath = null;
        
        if(cls.getProtectionDomain() == null || cls.getProtectionDomain().getCodeSource() == null || cls.getProtectionDomain().getCodeSource().getLocation() == null)
        {
            String classResourceName = "/" + cls.getName().replace('.', '/') + ".class";
            URL resoueceURL = cls.getResource(classResourceName);
            jarPath = resoueceURL.getFile();
        }
        else
        {
            jarPath = cls.getProtectionDomain().getCodeSource().getLocation().getFile();
        }
                
        int dotParIndex = jarPath.indexOf(".par");
        
        if(dotParIndex < 0)
        {
            throw new IllegalArgumentException("The jar '" + jarPath
                    + "' does not belong to one PAR");            
        }
                
        String path = jarPath.substring(0, dotParIndex);
        int lastFileSeperatorIndex = path.lastIndexOf("/");
        
        if(lastFileSeperatorIndex < 0)
        {
            lastFileSeperatorIndex = path.lastIndexOf(File.separator);            
        }
        
        if(lastFileSeperatorIndex < 0)
        {
            throw new IllegalArgumentException("The path of the jar '" + jarPath + "' is not valid");
        } 
        else
        {
            String parID = path.substring(lastFileSeperatorIndex + 1);
            return getDeployedPar(parID);
        }
    }

    public Par getDeployedPar()
        throws IntrospectionException, ClassNotFoundException
    {
        String callerClassName = getCaller();
        return getDeployedPar(Class.forName(callerClassName));
    }

    private static String getCaller()
    {
        try
        {
            throw new Exception("Temporary Exception");
        }
        catch(Exception ignore)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ignore.printStackTrace(pw);
            pw.flush();
            String str = sw.toString();
            int index = str.indexOf("at ");
            index = str.indexOf("at ", index + 1);
            index = str.indexOf("at ", index + 1);
            int ldx = str.indexOf('(', index);
            int ldx2 = str.lastIndexOf('.', ldx);
            return str.substring(index + 3, ldx2);
        }
    }


    private static MBeanServer getMBeanServer()
    {
        return (MBeanServer)MBeanServerFactory.findMBeanServer(null).iterator().next();
    }


    public String getERPProperty(String key)
    {
        if(key.equals("erp.home"))
        {
            return ps.getPath("erp.path", "home") + File.separator;            
        }
        
        if(key.equals("corepower.home"))
        {
            return ps.getPath("corepower.path", "home") + File.separator;            
        }
        
        if(key.equals("erp.runtime"))
        {
            return ps.getPath("erp.path", "home") + File.separator + "deploy"
                    + File.separator + "runtime" + File.separator;            
        }
        
        if(key.equals("erp.temp"))
            return ps.getPath("erp.path", "home") + File.separator + "temp" + File.separator;
        
        if(key.equals("erp.rundata"))
        {
            return ps.getPath("erp.path", "home") + File.separator + "rundata"
                    + File.separator;            
        }
        else
        {
            return ps.get(key);            
        }
    }

    public boolean isStarted()
    {
        String s_stated = System.getProperty("erp.systemproperty.systemstate");
        return s_stated != null && s_stated.equalsIgnoreCase("true");
    }

    public boolean isCluster()
    {
        return false;
    }

    public String getGlobalFileSystemRootPath()
    {
        return null;
    }

    public String getSingletonNodeAddr()
    {
        return System.getProperty("erp.systemproperty.singleton.serverjndi");
    }

    public File[] getLayers()
    {
        File files[] = null;
        LinkedList list=null;
        
        try
        {
            files = ParCollector.getFSPaths();
            File rs[] = null;
            list = new LinkedList();
            
            for(int i = 0; i < files.length; i++)
            {
                File basedirs[] = {files[i]};
                rs = getDirs(basedirs, "*.par");
                
                if(rs.length > 0)
                {
                    list.add(files[i]);                    
                }
            }

            return (File[])list.toArray(new File[list.size()]);
        }
        catch (ErpDeploymentException e)
        {
           
            log.info(e);
        }     
        
        return new File[0];
    }

    public File[] getDirs(File basedirs[], String dirName)
    {
        return FileScanner.getDirs(basedirs, dirName);
    }

    /*
    public WebServerContext getWebServerContext()
    {
        if(webServerContext != null)
            return webServerContext;
        String webserverName = null;
        String ip = null;
        String port = null;
        Par webserverPar = getDeployedPar("tomcat");
        if(webserverPar != null)
        {
            try
            {
                ip = InetAddress.getLocalHost().getHostAddress();
                webserverName = "tomcat";
            }
            catch(UnknownHostException e)
            {
                log.info(e);
            }
        } else
        {
            webserverPar = getDeployedPar("jetty");
            if(webserverPar != null)
                try
                {
                    ip = InetAddress.getLocalHost().getHostAddress();
                    webserverName = "jetty";
                }
                catch(UnknownHostException e)
                {
                    log.info(e);
                }
        }
        if(ip == null)
            return null;
        String key = "userdefined-" + webserverName + ".port";
        log.info(" key = " + key);
        port = ps.get(key);
        if(port != null)
        {
            webServerContext = new WebServerContext(ip, port);
            return webServerContext;
        } else
        {
            return null;
        }
    }
    */
    
    public Serializable getFromRegistry(String registryKey)
    {
        return getRegistry(registryKey);
    }

    public void setIntoRegistry(String registryKey, Serializable value)
        throws IOException
    {
        setRegistry(registryKey, value);
    }

    public void startedup()
        throws FileNotFoundException, IOException
    {
        RegistryFactory.getInstance().saveRegistry();
    }


//    public String getLicenseValue(String key)
//        throws LicenseException
//    {
//        return LicenseCheck.getLicenseValue(key);
//    }
//
//    public ArrayList getClntNoPermissionPar()
//        throws LicenseException
//    {
//        return LicenseCheck.getClntNoPermissionPar();
//    }
//
//    public ArrayList getSvrNoPermissionPar()
//        throws LicenseException
//    {
//        return LicenseCheck.getSvrNoPermissionPar();
//    }

    
    public static Serializable getRegistry(String registryKey)
    {
        ObjectInputStream ois=null;
        
        ois = RegistryFactory.getInstance().getFromRegistry(registryKey);
        
        if(ois == null)
        {
            return null;            
        }
        
        Serializable obj;
        try
        {
            obj = (Serializable)ois.readObject();
            return obj;
        }
        catch (IOException e)
        {
//          IOException ex;
//          ex;
          log.info(e);
        }
        catch (ClassNotFoundException e)
        {
//          return null;
//          ex;
          log.info(e);
        }
        return null;
    }

    public static void setRegistry(String registryKey, Serializable value)
        throws IOException
    {
        byte bytes[] = null;
        ObjectOutputStream oos = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            oos = null;
            bytes = baos.toByteArray();
        }
        finally
        {
            try
            {
                if(oos != null)
                {
                    oos.close();                    
                }
            }
            catch(IOException ignore) { }
        }
        RegistryFactory.getInstance().setIntoRegistry(registryKey, bytes);
    }


//    public String getLicenseExpiration()
//        throws LicenseException
//    {
//        return LicenseCheck.getExpiration();
//    }
//
//    public HashMap getAllLicenseFileInfo()
//        throws LicenseException
//    {
//        return LicenseCheck.getAllLicenseFileInfo();
//    }
//
//    public HashMap getAllLicenseValue()
//        throws LicenseException
//    {
//        return LicenseCheck.getAllLicenseValue();
//    }

    
    public Date getStartTime()
    {
        return new Date(TimeHelper.getStartTime());
    }



}
