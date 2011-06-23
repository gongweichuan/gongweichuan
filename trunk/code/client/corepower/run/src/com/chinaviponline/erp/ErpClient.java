/**
 * 
 */
package com.chinaviponline.erp;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
//import java.net.DynamicSocksSocketImplFactory;
//import java.net.NATPlainSocketImplFactory;
//import java.net.SSHPlainSocketImplFactory; 1.5.0
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;

//import java.security.CodeSource;
//import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.chinaviponline.erp.corepower.api.psl.systemsupport.Par;
import com.chinaviponline.erp.corepower.psl.systemsupport.AntMainForErp;
import com.chinaviponline.erp.corepower.psl.systemsupport.CategoryInfo;
import com.chinaviponline.erp.corepower.psl.systemsupport.ParCollector;
import com.chinaviponline.erp.corepower.psl.systemsupport.PreProcess;
import com.chinaviponline.erp.corepower.psl.systemsupport.PropertiesService;
import com.chinaviponline.erp.corepower.psl.systemsupport.ServiceDeployerInterface;
import com.chinaviponline.erp.corepower.psl.systemsupport.SystemStateManager;
import com.chinaviponline.erp.corepower.psl.systemsupport.TimeHelper;
import com.chinaviponline.erp.corepower.psl.systemsupport.Util;
import com.chinaviponline.erp.corepower.psl.systemsupport.classloader.SelfAdaptClassLoader;
import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileScanner;
import com.chinaviponline.erp.corepower.psl.systemsupport.registry.RegistryFactory;
import com.chinaviponline.erp.corepower.util.impl.logging.CategoryStream;

/**
 * <p>文件名称：ErpClient.java</p>
 * <p>文件描述：主程序</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-8</p>
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
public class ErpClient
{
    
    /**
     * 框架类加载器 classpath
     */
    private static final String JBOSSURLS = "com.chinaviponline.erp.ErpClient.jbossurls";

    /**
     * 日志
     */
    private static final Logger log;

    /**
     * 属性中心
     */
    private static final PropertiesService propertiesService = PropertiesService.getInstance();

    /**
     * Debug前缀
     */
    private static final String DEBUG_PRN_PARID = "corepower-psl-debugprn";

    /**
     * ERP HOME DIR
     */
    public static final String ERP_HOME_DIR = "erp.home.dir";

    /**
     * 部署服务接口
     */
    private static ServiceDeployerInterface serviceDeployer = null;

    // 初始化
    static
    {
        String logSUBDIR = null;
        try
        {
            logSUBDIR = System.getProperty("system.process.instance.id");

            if (logSUBDIR != null || logSUBDIR.equalsIgnoreCase(""))
            {
                String jarPath = (BoostMain.class).getProtectionDomain()
                        .getCodeSource().getLocation().getFile();
                try
                {
                    jarPath = URLDecoder.decode(jarPath, "UTF-8");
                }
                catch (Exception e)
                {
                    System.out.println("[ErpClient]" + e.toString());
                }

                String homeDir = (new File(jarPath)).getParentFile()
                        .getParent()
                        + File.separator;

                String logDIR = homeDir + "log" + File.separator + logSUBDIR;
                System.out.println("[ErpClient] log dir: " + logDIR);

                File logfile = new File(logDIR);
                if (!logfile.exists())
                {
                    System.out.println("[ErpClient] mkdir " + logDIR);
                    logfile.mkdirs();
                }
                if (!logfile.isDirectory())
                {
                    System.out.println("[ErpClient] " + logDIR
                            + " is not a dir,please check.");
                }

            }
        }
        catch (NullPointerException e)
        {

        }
        log = Logger.getLogger(ErpClient.class);
        TimeHelper.setStartTime(System.currentTimeMillis());

        StringBuffer sbb = new StringBuffer();
        sbb
                .append("\n******************************************************************\n");
        sbb.append("                     ERP client begin starting    \n");
        sbb
                .append("******************************************************************");
        log.info(sbb.toString());
    }

    /**
     * 
     * <p>文件名称：ErpClient.java</p>
     * <p>文件描述：虚拟机关闭钩子,关闭前做些保存文件之类的事情等</p>
     * <p>版权所有： 版权所有(C)2007-2017</p>
     * <p>公   司： 与龙共舞独立工作室</p>
     * <p>内容摘要： </p>
     * <p>其他说明： </p>
     * <p>完成日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改日期：    版本号：    修改人：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @version 1.0
     * @author 龚为川
     * @email gongweichuan(AT)gmail.com
     */
    private static class ShutdownHook implements Runnable
    {

        /**
         * 构造函数 仿单例
         *
         */
        private ShutdownHook()
        {
        }

        /**
         * 
         * 功能描述：线程体
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            FileScanner.saveCache();
            ErpClient.serviceDeployer.undeployServices();
            SystemStateManager.notifySystemState(2);//TODO 替换成常量
            ErpClient.log.info("shutdown JVM");
        }

    }

    /**
     * 
     * <p>文件名称：</p>
     * <p>文件描述：</p>
     * <p>版权所有： 版权所有(C)2007-2017</p>
     * <p>公   司： 与龙共舞独立工作室</p>
     * <p>内容摘要： </p>
     * <p>其他说明： </p>
     * <p>完成日期：2008-5-15</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改日期：    版本号：    修改人：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @version 1.0
     * @author 龚为川
     * @email gongweichuan(AT)gmail.com
     */
    private static class JarFilenameFilter implements FilenameFilter
    {

        /**
         * 构造函数
         *
         */
        private JarFilenameFilter()
        {
        }

        /**
         * 
         * 功能描述：
         * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
         */
        public boolean accept(File dir, String name)
        {
            return name.endsWith(".jar");
        }
    }

    /**
     * 构造函数
     *
     */
    public ErpClient()
    {
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-15</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception
    {
        JWindow splashWindow;
        Category category = null;
        category = Category.getInstance("STDOUT");
        PrintStream out = System.out;
        CategoryStream.setSystemOutStream(out);
        System.setOut(new CategoryStream(category, Priority.INFO, out));
        category = Category.getInstance("STDERR");
        PrintStream err = System.err;
        CategoryStream.setSystemErrStream(err);
        System.setErr(new CategoryStream(category, Priority.ERROR, err));
        splashWindow = null;
        PreProcess preProcess=null;
        boolean mark=false;
        CategoryInfo infos[]=null;
        Par debugPrnPar=null;
        FileWriter writer=null;
//        String disableText = System.getProperty("user.dir")
//                + propertiesService.get("erp.wholeloginpic.enabled");
        String disableText=propertiesService.get("erp.wholeloginpic.enabled");
        
        if (disableText != null && disableText.equals("true"))
        {
            StringBuffer pathBuffer = new StringBuffer(50);
            pathBuffer.append("/corepower-wholelogin_");
            pathBuffer.append(System.getProperty("os.name").toLowerCase()
                    .indexOf("windows") < 0 ? "unix" : "windows");
            pathBuffer.append('_');
            pathBuffer.append(propertiesService.get("erp.locale"));
            pathBuffer.append(".gif");
            URL resourceURL = (ErpClient.class).getResource(pathBuffer
                    .toString());
            if (resourceURL != null)
            {
                splashWindow = new JWindow();
                splashWindow.getContentPane().add(
                        new JLabel(new ImageIcon(resourceURL)), "Center");
                splashWindow.pack();
                splashWindow.setCursor(Cursor.getPredefinedCursor(3));
                Dimension windowSize = splashWindow.getSize();
                Dimension screenSize = Toolkit.getDefaultToolkit()
                        .getScreenSize();
                splashWindow.setLocation(screenSize.width / 2
                        - windowSize.width / 2, screenSize.height / 2
                        - windowSize.height / 2);
                splashWindow.setVisible(true);
            }
        }
        
        String osName = System.getProperty("os.name");
        log.info("os name: " + osName);
        if (osName.toLowerCase().indexOf("win") != -1)
        {
            String pidpath = System
                    .getProperty("corepower.psl.systemsupport.pid.path");
            if (pidpath == null)
            {
                pidpath = propertiesService.getPath("erp.path", "bin");                
            }
            if (!pidpath.endsWith(File.separator))
            {
                pidpath = pidpath + File.separator;                
            }
            
            log.info("pid.txt path: " + pidpath);
            //        System.loadLibrary("PidNative");
            //        PidNative.createPidFile(pidpath);
            //TODO 唯一性标示,避免启动多个实例
        }
        propertiesService.set("erp.type", "client");
        log.info("the erp property of 'erp.type' is set to : client");
        String ipVersionPureV4 = propertiesService.get("erp.ip.version.purev4");
        System.setProperty("erp.ip.version.purev4", ipVersionPureV4);
        log.info("the erp property of 'erp.ip.version.purev4' is set to : "
                + ipVersionPureV4);
        String keys[] = propertiesService.getKeyArray("erp.systemproperty");
        for (int i = 0; i < keys.length; i++)
        {
            System.setProperty(keys[i], propertiesService.get(keys[i]));
            String tempkey = keys[i].substring("erp.systemproperty.".length(),
                    keys[i].length());
            System.setProperty(tempkey, propertiesService.get(keys[i]));
            log.info("the erp property of '" + keys[i] + "' and '" + tempkey
                    + "' are set to : " + propertiesService.get(keys[i]));
        }

        System
                .setProperty("corepower.psl.systemsupport.serviceimpl",
                        "com.chinaviponline.erp.corepower.psl.systemsupport.SystemSupportServiceClientImpl");
        log
                .info("the erp property of 'corepower.psl.systemsupport.serviceimpl' is set to : com.chinaviponline.erp.corepower.psl.systemsupport.SystemSupportServiceClientImpl");
        boolean useSSH = false;
        boolean useNAT = false;
        Object ssh = propertiesService.get("erp.useSSH");
        if (ssh != null)
        {
            if (((String) ssh).trim().equals("true"))
            {
//                SSHPlainSocketImplFactory factory = new SSHPlainSocketImplFactory();
//                Socket.setSocketImplFactory(factory); 1.5.0
                useSSH = true;
                System.setProperty("erp.useSSH", "true");
                System.setProperty("erp.useNAT", "true");
                log
                        .info("the erp property and system property of 'erp.useSSH' is set to : true");
            }
            else
            {
                System.setProperty("erp.useSSH", "false");
                System.setProperty("erp.useNAT", "false");
                log
                        .info("the erp property and system property of 'erp.useSSH' is set to : false");
            }
        }

        if (!useSSH)
        {
            Object s1 = propertiesService.get("erp.useNAT");
            if (s1 != null)
            {
                if (((String) s1).trim().equals("true"))
                {
//                    NATPlainSocketImplFactory factory = new NATPlainSocketImplFactory();
//                    Socket.setSocketImplFactory(factory); 1.5.0
                    useNAT = true;
                    System.setProperty("erp.useNAT", "true");
                    log
                            .info("the erp property and system property of 'erp.useNAT' is set to : true");
                }
                else
                {
                    System.setProperty("erp.useNAT", "false");
                    log
                            .info("the erp property and system property of 'erp.useNAT' is set to : false");
                }
            }
        }
        if (!useNAT && !useSSH)
        {
            Object s1 = propertiesService.get("erp.usesocks5");
            if (s1 != null && ((String) s1).trim().equals("true"))
            {
//                java.net.SocketImplFactory factory = new DynamicSocksSocketImplFactory();
//                Socket.setSocketImplFactory(factory); 1.5.0
                log
                        .info("the erp property of 'erp.usesocks5' is set to : true");
            }
        }
        
        preProcess = new PreProcess("client");
        preProcess.setDoPreProcess();
        mark = preProcess.getDoPreProcess();
        if (mark)
        {
            SelfAdaptClassLoader theClassLoader = (SelfAdaptClassLoader) Thread
                    .currentThread().getContextClassLoader();
            theClassLoader.requestStopOptimize();
        }
        
        preProcess.delCachedFiles(mark);
        
        if (mark)
        {
            (new File(propertiesService.getPath("erp.path", "home") + File.separator + "temp"
                    + File.separator)).mkdirs();
            SelfAdaptClassLoader theClassLoader = (SelfAdaptClassLoader) Thread
                    .currentThread().getContextClassLoader();
            theClassLoader.requestStartOptimize();
        }
        
        ParCollector.initDeployedPar(mark);
        
        if (!mark)
        {
            log.debug("mark is:["+mark+"], All read from Cache!");// 此处修改
        }
        else    //修改:是否所有的preExecute.xml只执行一次?
        {            
            String fileName = preProcess.preExecute();
            
            if (fileName != null)
            {
                String argCmd2s[] = {"-f", fileName};
                AntMainForErp antMain2 = new AntMainForErp();
                antMain2.startAnt(argCmd2s, null, null);
            }

            
            File preexcutes[] = FileScanner.getFiles("*-preExecute.xml", true);
            log.info("preexcutes.length: " + preexcutes.length);
            String tempFileName = null;
            
            for (int j = 0; j < preexcutes.length; j++)
            {
                tempFileName = preexcutes[j].getAbsolutePath();
                String argCmd3[] = {"-f", tempFileName};
                AntMainForErp antMain2 = new AntMainForErp();
                antMain2.startAnt(argCmd3, null, null);
            }
            
        }//第二次修改:infos为null   
        
            java.util.Properties deployProps = preProcess.getDeployProps();
            infos = Util.getCategoryInfos(deployProps);
            
            if (infos == null || infos.length == 0)
            {
                return;
            }

        
        
        // 所依赖的特殊插件,日志
        debugPrnPar = ParCollector.getDeployedPar("corepower-psl-debugprn");
        if (debugPrnPar == null)
        {
            return;
        }

        // 日志配置文件
        File debugTemplate = FileScanner.getFile(new String[] {debugPrnPar
                .getBaseDir()}, "corepower-psl-dugprn-log4j.xml");
        
        writer = null;
        if (debugTemplate!=null && debugTemplate.exists())
        {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(debugTemplate);
            Element root = doc.getRootElement();
            Element rootCategoryElem = root.getChild("root");
            
            if (rootCategoryElem != null)
            {
                rootCategoryElem.detach();                
            }
            
            Element categoryElem = null;
            Element prioElem = null;
            Element appenderElem = null;
            
            for (int j = 0; j < infos.length; j++)
            {
                categoryElem = new Element("category");
                categoryElem.setAttribute("name", infos[j].getName());
                prioElem = new Element("priority");
                prioElem.setAttribute("value", infos[j].getPriority());
                categoryElem.addContent(prioElem);
                if (infos[j].getAppender() != null
                        && infos[j].getAppender().trim().length() != 0)
                {
                    appenderElem = new Element("appender-ref");
                    appenderElem.setAttribute("ref", infos[j].getAppender());
                    categoryElem.addContent(appenderElem);
                }
                root.addContent(categoryElem);
            }

            root.addContent(rootCategoryElem);
            writer = new FileWriter(debugTemplate);
            XMLOutputter outp = new XMLOutputter(" ", true, "GB2312");
            outp.setTrimAllWhite(true);
            outp.output(doc, writer);
        }
        
        if (writer != null)
        {
            writer.close();
//            return;
        }

        try
        {
            preProcess.save(mark);
        }
        catch (IOException ex)
        {
            log.warn("file-modify-check.properties!" + ex);
        }
        
        // 读取注册服务信息
        RegistryFactory.getInstance().loadRegistry();
        
        System.setProperty("erp.home.dir", System.getProperty("user.dir"));
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
        URL urls[] = null;
        
        if (!mark)
        {
            ObjectInputStream ois = RegistryFactory.getInstance() .getFromRegistry(JBOSSURLS);
//            ObjectInputStream ois=null;
                   
            if (ois != null)
            {
                urls = (URL[]) ois.readObject();                
            }
        }
        
        if (mark || urls == null)
        {
            ArrayList systemClassPathFileList = new ArrayList(50);
            FilenameFilter jarFilenameFilter = new JarFilenameFilter();
            findJarFiles(new File(propertiesService.getPath("corepower.path", "lib")),
                    jarFilenameFilter, systemClassPathFileList);
            findJarFiles(new File(propertiesService.getPath("corepower.path", "jboss.lib")),
                    jarFilenameFilter, systemClassPathFileList);
            findJarFiles(new File(propertiesService.getPath("corepower.path", "jboss.default.lib")),
                    jarFilenameFilter, systemClassPathFileList);
            systemClassPathFileList.add(new File(propertiesService.getPath("corepower.path",
                    "jboss.conf")));
            int systemClassPathFileCount = systemClassPathFileList.size();
            urls = new URL[systemClassPathFileCount];
            for (int i = 0; i < systemClassPathFileCount; i++)
            {
                urls[i] = ((File) systemClassPathFileList.get(i)).toURL();
            }


            byte bytes[] = null;
            ObjectOutputStream oos = null;
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                oos = new ObjectOutputStream(baos);
                oos.writeObject(urls);
                oos.flush();
                oos.close();
                oos = null;
                bytes = baos.toByteArray();
            }
            finally
            {
                try
                {
                    if (oos != null)
                    {
                        oos.close();                        
                    }
                }
                catch (IOException ignore)
                {
                }
            }
            
            RegistryFactory.getInstance().setIntoRegistry(
                    JBOSSURLS, bytes);          
            
            log.debug("get url no cache");
            for (int i = 0; i < urls.length; i++)
            {
                log.debug("url: " + urls[i]);
            }


        }
        SelfAdaptClassLoader newClassLoader = (SelfAdaptClassLoader) Thread
                .currentThread().getContextClassLoader();
        newClassLoader.addURLs(urls);
        
        // 后台服务部署类
        URL tmpUrls[]=newClassLoader.getURLs();
        log.debug("newClassLoader urls is"+tmpUrls);
        
        Class serviceDeployerClass = newClassLoader
                .loadClass("com.chinaviponline.erp.corepower.psl.systemsupport.ServiceDeployer");
        serviceDeployer = (ServiceDeployerInterface) serviceDeployerClass
                .newInstance();
        serviceDeployer.deployServices();
   
        
        // LicenseCheck.setSystemStarted();
        //TODO 暂时不考虑注册码文件

        FileScanner.saveCacheWhenNoFile();
        long startupSpendTime = System.currentTimeMillis()
                - TimeHelper.getStartTime();
        StringBuffer sbf = new StringBuffer();
        String version = propertiesService.get("erp.version.display");
        if (version == null || version.length() == 0)
        {
            sbf.append(propertiesService.get("erp.version.main"));
            String patch = propertiesService.get("erp.version.patch");
            if (patch != null && !patch.trim().equals(""))
            {
                sbf.append(".");
                sbf.append(propertiesService.get("erp.version.patch"));
            }
        }
        else
        {
            sbf.append(version);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("ErpClient started!");
        sb
                .append("\n******************************************************************\n");
        sb.append("       ERP-PLATFORM (MX MicroKernel) [" + sbf.toString()
                + "] started.\n");
        if (mark)
        {
            sb.append("          Spent  " + calcHMS(startupSpendTime) + ".\n");
        }
        else
        {
            sb.append("          Spent  " + calcHMS(startupSpendTime) + "\n");
        }

        sb.append("       " + propertiesService.get("erp.copyright.en_US")
                + ". All rights reserved. \n");
        sb
                .append("******************************************************************\n");
        //    log.keyInfo(sb.toString());
        SystemStateManager.notifySystemState(1);
        //    break MISSING_BLOCK_LABEL_2535;

        String runtimedir = propertiesService.getPath("erp.path", "deploy") + File.separator
                + "temp";
        int failDelNum = 0;
        LinkedList deledFileList = new LinkedList();
        Util.delTree(runtimedir, failDelNum, deledFileList);
        
        if (failDelNum > 0)
        {
            log.warn(failDelNum + "files in deploy/temp del failed!");
        }
        //    log.error("", ex);
        if (splashWindow != null)
        {
            SwingUtilities.invokeLater(new Runnable()
            {

                public void run()
                {
                    try
                    {
                        System.exit(0);
                        Runtime.getRuntime().halt(0);
                    }
                    catch (Throwable ignroe)
                    {
                    }
                }

            });
        }
        
        if (splashWindow != null)
        {
            splashWindow.setVisible(false);
            splashWindow.dispose();
        }

    }

    /**
     * 
     * <p>功能描述：查找所在目录下的jar包</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-29</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param dir 所在目录
     * @param filter 过滤器
     * @param jarFileList 保存查找后的路径
     */
    private static void findJarFiles(File dir, FilenameFilter filter,
            ArrayList jarFileList)
    {
        File jarFiles[] = dir.listFiles(filter);
        
        // 异常处理,在指定目录中找不到*.jar文件
        if(jarFiles==null)
        {
            log.error("can not find the jars filter: "+filter.toString());
            return;
        }
        
        for (int i = 0; i < jarFiles.length; i++)
        {
            jarFileList.add(jarFiles[i]);
            log.debug("kernel jar:" + jarFiles[i].toString());
        }

    }

    /**
     * 
     * <p>功能描述：计算时间</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-6-1</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param startupSpendTime
     * @return
     */
    private static String calcHMS(long startupSpendTime)
    {
        long lTimeInMilliSeconds = startupSpendTime;
        long hours = lTimeInMilliSeconds / 0x36ee80L;
        lTimeInMilliSeconds -= hours * 3600L * 1000L;
        long minutes = lTimeInMilliSeconds / 60000L;
        lTimeInMilliSeconds -= minutes * 60L * 1000L;
        long seconds = lTimeInMilliSeconds / 1000L;
        return hours + " hour(s) " + minutes + " minute(s) " + seconds
                + " second(s)";
    }

}
