/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileUtil;

/**
 * <p>文件名称：PropertiesService.java</p>
 * <p>文件描述：属性中心</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-11</p>
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
public class PropertiesService
{
    /**
     * 单例
     */
    private static PropertiesService instance = null;

    /**
     * 属性
     */
    private Properties props;

    /**
     * 默认属性
     */
    private Properties defaultProps;

    /**
     * 补充属性
     */
    private Properties fixedProps;

    /**
     * 绝对路径属性
     */
    private Properties propsForAbsolutePath;

    /**
     * Temp属性
     */
    private Properties tempProps;

    /**
     * 储存部署文件Map
     */
    private static HashMap deployPropFilesMap = new HashMap();

    /**
     * corepower-system.properties
     */
    private static final String DEFAULT_PROPS_FILE_NAME = "corepower-system.properties";

    /**
     * run.jar
     */
    private static final String COREPOWER_BOOT_JAR_NAME = "run.jar";

    /**
     * erp.path.home
     */
    private static final String ERP_HOMEDIR_TAG = "erp.path.home";

    /**
     * erp.path.bin
     */
    private static final String ERP_PATH_BIN_TAG = "erp.path.bin";

    /**
     * corepower.path.home
     */
    private static final String COREPOWER_HOMEDIR_TAG = "corepower.path.home";

    /**
     * deploy-default.properties
     */
    private static final String DEFAULT_DEPLOY_PROPERTY_FILE = "deploy-default.properties";

    /**
     * deploy.properties
     */
    private static final String USER_DEPLOY_PROPERTY_FILE = "deploy.properties";

    /**
     * 部署配置文件所在目录,目录下有deploy.properties
     */
    private static final String COREPOWER_DEPLOY_DIR = "deploy";

    /**
     * 日志
     */
    private static Logger log;

    // 日志 使用run.jar中的log4j.properties文件
    static
    {
        log = Logger.getLogger((PropertiesService.class).getName());
    }

    /**
     * 构造函数
     *
     */
    private PropertiesService()
    {
        props = null;
        defaultProps = new Properties();
        fixedProps = new Properties();
        propsForAbsolutePath = null;
        tempProps = new Properties();
        init();
        init2();
    }

    /**
     * 
     * <p>功能描述：单例</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static synchronized PropertiesService getInstance()
    {
        if (instance == null)
        {
            instance = new PropertiesService();
        }
        return instance;
    }

    /**
     * 
     * <p>功能描述：初始化</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void init()
    {
        ClassLoader localCl = null;
        try
        {
            String RunFileDir = getRunDir();
            String runJarName = RunFileDir + COREPOWER_BOOT_JAR_NAME;
            
            File runDir = new File(RunFileDir);
            File root = runDir.getParentFile();
            File deployDir = new File(root, COREPOWER_DEPLOY_DIR);
            URL url1 = (new File(RunFileDir)).toURL();
            URL url2 = (new File(runJarName)).toURL();
            URL url3 = deployDir.toURL();
            URL libUrls[] = {url1, url2, url3};
            localCl = new URLClassLoader(libUrls, null);
            
            // 从run.jar包中里面取
            defaultProps.load(localCl
                    .getResourceAsStream(DEFAULT_PROPS_FILE_NAME));
            
            File defaultDeployFile = new File(deployDir,
                    DEFAULT_DEPLOY_PROPERTY_FILE);
            if (defaultDeployFile.exists())
            {
                log.info("Now loading props from default-deploy.properites");
                defaultProps.load(new FileInputStream(defaultDeployFile));
                deployPropFilesMap.put(defaultDeployFile.getName(),
                        defaultDeployFile);
            }
            else
            {
                throw new ErpDeploymentException(defaultDeployFile
                        .getAbsolutePath()
                        + " does not exist. ");
            }
            File userDeployFile = new File(deployDir, USER_DEPLOY_PROPERTY_FILE);
            if (userDeployFile.exists())
            {
                log.info(" Now loading props from deploy.properties");
                defaultProps.load(new FileInputStream(userDeployFile));
                deployPropFilesMap
                        .put(userDeployFile.getName(), userDeployFile);
            }
            File deployPropFiles[] = FileUtil.getFiles(new String[] {deployDir
                    .getAbsolutePath()}, "deploy-*.properties", true);
            Arrays.sort(deployPropFiles, new Comparator()
            {

                public int compare(Object a, Object b)
                {
                    String nameA = ((File) a).getName();
                    String nameB = ((File) b).getName();
                    String nameAWithoutSuffix = nameA.substring(0, nameA
                            .lastIndexOf("."));
                    String nameBWithoutSuffix = nameB.substring(0, nameB
                            .lastIndexOf("."));
                    return nameAWithoutSuffix.compareTo(nameBWithoutSuffix);
                }

            });
            int size = deployPropFiles.length;
            for (int i = 0; i < size; i++)
            {
                if (deployPropFiles[i].exists()
                        && !deployPropFiles[i].getName().equals(
                                "deploy-default.properties"))
                {
                    log.info(" Now loading props from " + deployPropFiles[i]);
                    defaultProps.load(new FileInputStream(deployPropFiles[i]));
                    deployPropFilesMap.put(deployPropFiles[i].getName(),
                            deployPropFiles[i]);
                }
            }

            fixedProps.load(localCl.getResourceAsStream("fixed.properties"));
            Iterator it = defaultProps.entrySet().iterator();
            do
            {
                if (!it.hasNext())
                    break;
                java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
                String key = (String) entry.getKey();
                String value = ((String) entry.getValue()).trim();
                int beginIndex = value.indexOf("${");
                int endIndex = value.indexOf("}");
                if (beginIndex != -1 && endIndex != -1)
                {
                    String macroName = value
                            .substring(beginIndex + 2, endIndex).trim();
                    String macroValue = defaultProps.getProperty(macroName);
                    if (macroValue != null)
                    {
                        defaultProps.put(key, macroValue);
                        log.info("replace erp property value  ${" + macroName
                                + "} with '" + macroValue + "'");
                    }
                }
            }
            while (true);
        }
        catch (Exception ignore)
        {
            log
                    .warn(
                            "can not load properties from system properties ,it exists?unZip run.jar,check it.",
                            ignore);
        }
        props = new Properties(defaultProps);
        createDsProps(defaultProps);
    }

    /**
     * 
     * <p>功能描述：创建属性文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param aDefaultProps
     * @return
     */
    private Properties createDsProps(Properties aDefaultProps)
    {
        Properties p = new Properties();
        Set set1 = aDefaultProps.keySet();
        Set set2 = getNeededSet(set1, ".ds.");
        String begin=null;
        String end=null;
        Object value=null; //TODO value的值是什么?
        for (Iterator it = set2.iterator(); it.hasNext(); props.put(
                begin + end, value))
        {
            String s = (String) it.next();
            int i = s.indexOf(".");
            begin = s.substring(0, i);
            String subString = s.substring(i + 1);
            i = subString.indexOf(".");
            subString = subString.substring(i + 1);
            i = subString.indexOf(".");
            end = subString.substring(i);
            value = aDefaultProps.getProperty(s);
        }

        return p;
    }

    /**
     * 
     * <p>功能描述：获取属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param aSet
     * @param partkey
     * @return
     */
    private static Set getNeededSet(Set aSet, String partkey)
    {
        HashSet result = new HashSet();
        Iterator it = aSet.iterator();
        do
        {
            if (!it.hasNext())
            {
                break;
            }
            String key = (String) it.next();
            if (key.indexOf(partkey) != -1)
                result.add(key);
        }
        while (true);
        return result;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void init2()
    {
        makeErpHomeDir();
        log.info(" the erp home dir is: " + getFromTempProps(ERP_HOMEDIR_TAG));
        makeCorePowerHomeDir();
        log.info(" the corepower home dir is: "
                + getFromTempProps(COREPOWER_HOMEDIR_TAG));
        System.setProperty("user.dir", getFromTempProps(ERP_HOMEDIR_TAG));
        log.info(" the SYSTEM PROPERTY 'user.dir' is set to : "
                + System.getProperty("user.dir"));
        makeBinHomeDir();
        log.info(" the erp bin dir: " + getFromTempProps(ERP_HOMEDIR_TAG));
        propsForAbsolutePath = (Properties) props.clone();
        makeAbsolutePathProps(propsForAbsolutePath,
                getFromTempProps(ERP_PATH_BIN_TAG));
    }

    /**
     * 
     * <p>功能描述：修复的属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public Properties getFixedProps()
    {
        return fixedProps;
    }

    /**
     * 
     * <p>功能描述：默认属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public Properties getDefaultProps()
    {
        return defaultProps;
    }

    /**
     * 
     * <p>功能描述：生成绝对路径属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param p
     * @param homeDir
     */
    private void makeAbsolutePathProps(Properties p, String homeDir)
    {
        Enumeration enu = p.propertyNames();
        String key = null;
        String value = null;
        String change = null;
        while (enu.hasMoreElements())
        {
            key = (String) enu.nextElement();
            value = p.getProperty(key);
            if (key.equals(COREPOWER_HOMEDIR_TAG)
                    || key.equals(ERP_HOMEDIR_TAG)
                    || key.equals(ERP_PATH_BIN_TAG))
            {
                p.setProperty(key, getFromTempProps(key));
            }
            else
            {
                change = makeAbsolutePath(homeDir, value);
                p.setProperty(key, change);
            }
        }
    }

    /**
     * 
     * <p>功能描述：获取所需要的属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param p
     * @param s
     * @return
     */
    private String[] getNeededPathsByIndex(Properties p, String s)
    {
        Enumeration en = p.propertyNames();
        String key = null;
        TreeMap tm = new TreeMap();
        do
        {
            if (!en.hasMoreElements())
                break;
            key = (String) en.nextElement();
            if (key.indexOf(s) != -1)
                tm.put(key, getForPath(key));
        }
        while (true);
        Object obs[] = tm.values().toArray();
        int size = obs.length;
        String strs[] = new String[size];
        for (int i = 0; i < size; i++)
            strs[i] = (String) obs[i];

        return strs;
    }

    /**
     * 
     * <p>功能描述：创建ERP Home目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void makeErpHomeDir()
    {
        int level = Integer.parseInt(get("erp.level", "boot"));
        String homeDir = makeHomeDir(getClass(), level);
        setTempProps(ERP_HOMEDIR_TAG, homeDir);
    }

    /**
     * 
     * <p>功能描述：创建COREPOWER HOME目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void makeCorePowerHomeDir()
    {
        int level = Integer.parseInt(get("corepower.level", "boot"));
        String homeDir = makeHomeDir(getClass(), level);
        File file = new File(homeDir);
        String homePath = null;
        try
        {
            homePath = file.getParentFile().getCanonicalPath() + File.separator
                    + "platform" + File.separator;
        }
        catch (IOException ex)
        {
            log.error(" can not make home dir!", ex);
        }
        setTempProps(COREPOWER_HOMEDIR_TAG, homePath);
    }

    /**
     * 
     * <p>功能描述：获取BIN主目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getBinHomeDir()
    {
        return getFromTempProps(ERP_PATH_BIN_TAG);
    }

    /**
     * 
     * <p>功能描述：创建BIN主目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void makeBinHomeDir()
    {
        String erpdir = getFromTempProps(COREPOWER_HOMEDIR_TAG);
        File file = new File(erpdir);
        String binPath = null;
        try
        {
            binPath = file.getParentFile().getCanonicalPath() + File.separator
                    + "bin" + File.separator;
        }
        catch (IOException ex)
        {
            log.error(" can not make home dir!", ex);
        }
        setTempProps(ERP_PATH_BIN_TAG, binPath);
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param locateClass
     * @param backCount
     * @return
     */
    private static String makeHomeDir(Class locateClass, int backCount)
    {
        String homeDir = null;
        String path = findJarPath(locateClass);
        try
        {
            if (System.getProperty("java.version").indexOf("1.3") != 0)
            {
                path = decode(path, "UTF-8");
            }
            File file = new File(path);
            for (int i = 0; i < backCount; i++)
            {
                file = file.getParentFile();
            }

            homeDir = file.getCanonicalPath() + File.separator;
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(e);
            return "";
        }
        catch (IOException e)
        {
            log.error(e);
            return "";
        }
        return homeDir;

    }

    /**
     * 
     * <p>功能描述：解码</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param s
     * @param enc
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String decode(String s, String enc)
            throws UnsupportedEncodingException
    {
        boolean needToChange = false;
        StringBuffer sb = new StringBuffer();
        int numChars = s.length();
        int i = 0;
        if (enc.length() == 0)
        {
            throw new UnsupportedEncodingException(
                    "URLDecoder: empty string enc parameter");
        }

        do
        {
            if (i >= numChars)
            {
                break;
            }

            char c = s.charAt(i);
            switch (c)
            {
                case 43: // '+'
                    sb.append(' ');
                    i++;
                    needToChange = true;
                    break;

                case 37: // '%'
                    try
                    {
                        byte bytes[] = new byte[(numChars - i) / 3];
                        int pos = 0;
                        do
                        {
                            if (i + 2 >= numChars || c != '%')
                                break;
                            bytes[pos++] = (byte) Integer.parseInt(s.substring(
                                    i + 1, i + 3), 16);
                            if ((i += 3) < numChars)
                                c = s.charAt(i);
                        }
                        while (true);
                        if (i < numChars && c == '%')
                            throw new IllegalArgumentException(
                                    "URLDecoder: Incomplete trailing escape (%) pattern");
                        sb.append(new String(bytes, 0, pos, enc));
                    }
                    catch (NumberFormatException e)
                    {
                        throw new IllegalArgumentException(
                                "URLDecoder: Illegal hex characters in escape (%) pattern - "
                                        + e.getMessage());
                    }
                    needToChange = true;
                    break;

                default:
                    sb.append(c);
                    i++;
                    break;
            }
        }
        while (true);
        return needToChange ? sb.toString() : s;
    }

    /**
     * 
     * <p>功能描述：获取修复的路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public ArrayList getFixedSearchPaths()
    {
        ArrayList list = getPathsByKey("searchpath");
        if (list == null)
            return new ArrayList();
        Iterator iter = list.iterator();
        ArrayList absolutePathList = new ArrayList();
        String temp = null;
        for (; iter.hasNext(); absolutePathList.add(temp))
        {
            if (!File.separator.equals("/"))
            {
                temp = Util.formattedStr((String) iter.next(), "/",
                        File.separator);
            }
            else
            {
                temp = (String) iter.next();
                if (!temp.startsWith("/"))
                    temp = '/' + temp;
            }
            if (System.getProperty("java.version").indexOf("1.3") == 0)
                continue;
            try
            {
                temp = decode(temp, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                log.error(e);
            }
        }

        return absolutePathList;
    }

    /**
     * 
     * <p>功能描述：获取属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param moduleName
     * @param key
     * @return
     * @throws IllegalStateException
     */

    public String get(String moduleName, String key)
            throws IllegalStateException
    {
        String name = moduleName.trim() + "." + key.trim();
        String result = get(name);
        if (result != null)
        {
            result.trim();
            return result;
        }
        else
        {
            throw new IllegalStateException("Cann't get '" + name
                    + "' from properties table, it exsits ?");
        }
    }

    /**
     * 
     * <p>功能描述：获取路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param moduleName
     * @param key
     * @return
     * @throws IllegalStateException
     */
    public String getPath(String moduleName, String key)
            throws IllegalStateException
    {
        String name = moduleName.trim() + "." + key.trim();
        if (name.indexOf("path") == -1)
        {
            log.warn("not a path!");
        }

        String result = getForPath(name);
        if (result != null)
        {
            return result;
        }
        else
        {
            throw new IllegalStateException("Cann't get '" + name
                    + "' from properties table, it exsits ?");
        }
    }

    /**
     * 
     * <p>功能描述：获取CLASSPATH</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public ArrayList getClassPath()
    {
        return getPathsByKey("classpath.all");
    }

    /**
     * 
     * <p>功能描述：获取CLASSPATH</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param s
     * @return
     */
    public ArrayList getClassPath(String s)
    {
        return getPathsByKey(s);
    }

    /**
     * 
     * <p>功能描述：获取JBOSS路径(erp_svr中有JBoss)</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param key
     * @return
     */
    public String[] getJbossArgs(String key)
    {
        return Util.getNeededElementsByIndex(props, key);
    }

    /**
     * 
     * <p>功能描述：通过KEY获取路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param key
     * @return
     */
    private ArrayList getPathsByKey(String key)
    {
        if (key != null)
        {
            ArrayList list = new ArrayList();
            String s[] = getNeededPathsByIndex(props, key);
            int length2 = s.length;
            for (int j = 0; j < length2; j++)
            {
                list.add(s[j]);
            }

            return list;
        }
        else
        {
            log.warn(" getPathsByKey() err");
            return null;
        }
    }

    /**
     * 
     * <p>功能描述：获取部署目录的属性文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static HashMap getDeployPropFilesMap()
    {
        return deployPropFilesMap;
    }

    /**
     * 
     * <p>功能描述：获取属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param key
     * @return
     */
    public String get(String key)
    {
        String result;
        result = props.getProperty(key);
        if (result == null || "".equalsIgnoreCase(result))
        {
            if (key.indexOf(".dstype") != -1)
            {
                result = props.getProperty("default.dstype");
                if (result != null || !"".equalsIgnoreCase(result))
                {
                    result = result.trim();
                    return result;
                }
            }
            return "";
        }
        result = result.trim();
        try
        {
            result = new String(result.getBytes("ISO-8859-1"));
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(e);
            return "";
        }
        return result;

    }

    /**
     * 
     * <p>功能描述：从临时属性目录中获取属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param key
     * @return
     */
    private String getFromTempProps(String key)
    {
        String result;
        result = tempProps.getProperty(key);
        if (result == null || "".equalsIgnoreCase(result))
        {
            return "";
        }

        result.trim();
        try
        {
            result = new String(result.getBytes("ISO-8859-1"));
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(e);
            return "";
        }
        return result;

    }

    /**
     * 
     * <p>功能描述：获取KEY</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param startWith
     * @return
     */
    public String[] getKeyArray(String startWith)
    {
        ArrayList array = new ArrayList();
        Enumeration en = defaultProps.keys();
        do
        {
            if (!en.hasMoreElements())
            {
                break;                
            }
            String key = (String) en.nextElement();
            
            if (key.startsWith("erp.systemproperty"))
            {
                array.add(key);                
            }
            
        }
        while (true);
        
        return (String[]) array.toArray(new String[0]);
    }

    /**
     * 
     * <p>功能描述：获取路径信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param key
     * @return
     */
    public String getForPath(String key)
    {
        String s;
        File file;
        s = propsForAbsolutePath.getProperty(key);
        if (s == null || "".equalsIgnoreCase(s))
        {
            throw new IllegalStateException("Cann't get '" + key
                    + "' from properties table, it exsits ?");
        }

        s.trim();
        file = new File(s);
        if (!file.exists())
        {
            return "";            
        }
        
        try
        {
            s = file.getCanonicalPath();
        }
        catch (IOException e)
        {
            log.error(e);
            return "";
        }
        
        return s;
    }

    /**
     * 
     * <p>功能描述：设置属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param key
     * @param value
     */
    public void set(String key, String value)
    {
        String s = null;
        try
        {
            s = new String(value.getBytes(), "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(e);
        }
        props.setProperty(key, s.trim());
    }

    /**
     * 
     * <p>功能描述：设置</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param key
     * @param value
     */
    private void setTempProps(String key, String value)
    {
        String s = null;
        try
        {
            s = new String(value.getBytes(), "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(e);
        }
        tempProps.setProperty(key, s);
    }

    /**
     * 
     * <p>功能描述：运行时,目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    private String getRunDir()
    {
        String s;
        String path;
        s = null;
        path = findJarPath(getClass());
        try
        {
            if (System.getProperty("java.version").indexOf("1.3") != 0)
            {
                path = decode(path, "UTF-8");
            }
            File file = new File(path);
            file = file.getParentFile();
            s = file.getCanonicalPath() + File.separator;
        }
        catch (UnsupportedEncodingException e)
        {

            log.error(e);
            return "";
        }
        catch (IOException e)
        {
            log.error(e);
            return "";
        }
        return s;

    }

    /**
     * 
     * <p>功能描述：创建绝对路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param homePath
     * @param relativePath
     * @return
     */
    private static String makeAbsolutePath(String homePath, String relativePath)
    {
        String s = homePath + File.separator + relativePath;
        StringTokenizer st = new StringTokenizer(s, "\\/");
        StringBuffer sb = new StringBuffer();
        if (st.hasMoreTokens())
        {
            sb.append(st.nextToken());
        }

        for (; st.hasMoreTokens(); sb.append(st.nextToken()))
        {
            sb.append(File.separator);
        }

        String absolutePath = sb.toString();
        if (absolutePath.charAt(1) != ':')
        {
            absolutePath = File.separator + absolutePath;
        }

        return absolutePath;
    }

    /**
     * 
     * <p>功能描述：转换成UNICODE</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param result
     * @return
     */
    private String processResult(String result)
    {
        if (result != null)
        {
            result = result.trim();
            try
            {
                result = new String(result.getBytes("ISO-8859-1"));
            }
            catch (UnsupportedEncodingException ex)
            {
                log.error(ex);
                result = "";
            }
        }
        return result;
    }

    /**
     * 
     * <p>功能描述：用通配符获取属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param patten
     * @return
     */
    public String getPropByPatten(String patten)
    {
        int index = patten.indexOf("?");
        int length = patten.length();
        String result = null;
        if (index == -1)
        {
            result = defaultProps.getProperty(patten);
            return processResult(result);
        }
        Set set1;
        if (index == 0)
        {
            String sub = patten.substring(index + 1);
            set1 = getNeededSet(defaultProps.keySet(), sub);
            if (set1.size() >= 1)
            {
                result = defaultProps.getProperty((String) set1.iterator()
                        .next());
                return processResult(result);
            }
            else
            {
                return "";
            }
        }
        if (index == length - 1)
        {
            String sub = patten.substring(0, index);
            set1 = getNeededSet(defaultProps.keySet(), sub);
            if (set1.size() >= 1)
            {
                result = defaultProps.getProperty((String) set1.iterator()
                        .next());
                return processResult(result);
            }
            else
            {
                return "";
            }
        }
        String begin = patten.substring(0, index);
        set1 = getNeededSet(defaultProps.keySet(), begin);
        String end = patten.substring(index + 1);
        Set set2 = getNeededSet(set1, end);
        if (set2.size() >= 1)
        {
            result = defaultProps.getProperty((String) set2.iterator().next());
            return processResult(result);
        }
        else
        {
            return "";
        }
    }

    /**
     * 
     * <p>功能描述：获取Jar路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param klass
     * @return
     */
    private static String findJarPath(Class klass)
    {
        String fullPath = klass.getResource(
                "/" + klass.getName().replace('.', '/') + ".class").getFile();
        int protocolSeparatorIndex = fullPath.indexOf('/');
        int dotJarIndex = fullPath.indexOf(".jar");
        String jarPath = fullPath.substring(protocolSeparatorIndex,
                dotJarIndex + 4);
        return jarPath;
    }

}
