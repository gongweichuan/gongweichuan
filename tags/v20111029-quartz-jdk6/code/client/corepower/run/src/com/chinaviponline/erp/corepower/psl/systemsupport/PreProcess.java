/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileScanner;
import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileUtil;

/**
 * <p>文件名称：PreProcess.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-16</p>
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
public final class PreProcess
{
    private static final String GLOBAL_PREPROCESS_FILE = "global-preprocess.xml";

    public static final String MODIFIED_CHECK_PROPERTY_FILE = "file-modify-check.properties";

    private static final String START_MODE = "startmode";

    private static final String SUFFIX = "*tmpl";

    private static final String USER_DEFINED_PREFFIX = "userdefined-";

    private static final String JNDI_PROPERTIES = "jndi.properties";

    private static final String JNDI_PROPERTIES_TMPL = "jndi.properties.tmpl";

    private static final String JBOSS_SERVICE_XML = "jboss-service.xml";

    private static final String JBOSS_SERVICE_XML_TMPL = "jboss-service.xml.tmpl";

    private File global_file;

    private Properties globalProps;

    private Properties fixedProps;

    private Properties modifyCheckProps;

    private Properties currentTimestampProp;

    private ArrayList modules;

    private static final String DEFAULT_MODULE_NAME = "default";

    private static final String SUFFIXES[] = {"-ds.xml", "-service.xml",
            ".properties"};

    private boolean jmsDisposal;

    private boolean jbossServiceDisposal;

    private boolean kernelBinDisposal;

    private File checkFile;

    private boolean doPreProcess;

    private String startMode;

    private String lastStartMode;

    private static final PropertiesService PS;

    private static final String TEMPPATH;

    private static Logger log;

    // 初始化
    static
    {
        PS = PropertiesService.getInstance();
        TEMPPATH = PS.getPath("erp.path", "home") + File.separator + "temp";
        log = Logger.getLogger((PreProcess.class).getName());
    }

    /**
     * 构造函数
     * @param mode
     */
    public PreProcess(String mode)
    {
        global_file = null;
        globalProps = null;
        fixedProps = null;
        modifyCheckProps = null;
        currentTimestampProp = new Properties();
        modules = new ArrayList();
        jmsDisposal = false;
        jbossServiceDisposal = false;
        kernelBinDisposal = false;
        checkFile = null;
        doPreProcess = false;
        startMode = null;
        lastStartMode = null;
        startMode = mode;
    }

    /**
     * 
     * <p>功能描述：注册文件是否修改过</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    private boolean checkIfModify()
    {
        boolean result = false;
        try
        {
            checkFile = new File(TEMPPATH, "file-modify-check.properties");
            if (!checkFile.exists())
            {
                result = true;
                modifyCheckProps = new Properties();
            }
            else
            {
                modifyCheckProps = Util.getPropertiesFormFile(TEMPPATH,
                        "file-modify-check.properties");
            }
            String temp = modifyCheckProps.getProperty("startmode");
            currentTimestampProp.put("startmode", startMode);
            if (temp != null)
            {
                if (!temp.equals(startMode))
                {
                    result = true;                    
                }
            }
            else
            {
                result = true;
            }
            HashMap deployPropFilesMap = PropertiesService
                    .getDeployPropFilesMap();
            boolean proChange = checkFileChange(deployPropFilesMap);
            File deployPropFiles[] = FileUtil.getFiles(new String[] {PS
                    .getPath("erp.path", "deploy")}, "deploy*.xml", true);
            HashMap deployXmlFilesMap = new HashMap();
            int tempsize = deployPropFiles.length;
            for (int i = 0; i < tempsize; i++)
                deployXmlFilesMap.put(deployPropFiles[i].getName(),
                        deployPropFiles[i]);

            boolean deployChange = checkFileChange(deployXmlFilesMap);
            File pdeployPropFiles[] = FileUtil
                    .getFiles(new String[] {PS.getPath("erp.path", "deploy")},
                            "partialdeploy*.xml", true);
            HashMap pdeployXmlFilesMap = new HashMap();
            int pdsize = pdeployPropFiles.length;
            for (int i = 0; i < pdsize; i++)
                pdeployXmlFilesMap.put(pdeployPropFiles[i].getName(),
                        pdeployPropFiles[i]);

            boolean pdeployChange = checkFileChange(pdeployXmlFilesMap);
            //            File licenseFile = new File(LicenseCheck.licenseFilePath);
            //TODO 暂不进行注册文件验证

            File licenseFile =new File( "./deploy/license.file");        //TODO 临时解决方案
            HashMap licenseFileMap = new HashMap();
            licenseFileMap.put(licenseFile.getName(), licenseFile);
            boolean licenseChange = checkFileChange(licenseFileMap);
            if (proChange || deployChange || pdeployChange || licenseChange)
            {
                result = true;
            }
            if (currentTimestampProp.size() != modifyCheckProps.size())
            {
                result = true;
            }
        }
        catch (IOException ex)
        {
            log.info(ex);
            result = true;
        }
        return result;
    }

    /**
     * 
     * <p>功能描述：检查文件是否改变</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param checkFilesMap
     * @return
     */
    private boolean checkFileChange(HashMap checkFilesMap)
    {
        boolean result = false;
        Iterator it = checkFilesMap.values().iterator();
        
        do
        {
            if (!it.hasNext())
            {
                break;                
            }
            
            File value = (File) it.next();
            String key = value.getName() + "timestamp";
            long currentTimestamp = value.lastModified();
            currentTimestampProp.setProperty(key, Long
                    .toString(currentTimestamp));
            String lastTimestamp = modifyCheckProps.getProperty(key);
            if (lastTimestamp != null)
            {
                if (currentTimestamp != Long.parseLong(lastTimestamp))
                {
                    result = true;                    
                }
            }
            else
            {
                result = true;
            }
        }
        while (true);
        
        return result;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    private boolean checkPreProcessProperty()
    {
        boolean isNeedPreProcess = false;
        String removeCacheStr = PS.get("erp.scanfiles.removeCache");
        
        if (removeCacheStr == null)
        {
            log
                    .info("[PreProcess]erp.scanfiles.removeCache property does not exist in deploy*.properties, means erp.scanfiles.removeCache=false");
        }
        else if (removeCacheStr.equalsIgnoreCase("true"))
        {
            isNeedPreProcess = true;            
        }
        
        return isNeedPreProcess;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param isSave
     * @throws IOException
     */
    public void save(boolean isSave) throws IOException
    {
        FileOutputStream fos;
        if (!isSave)
        {
            return;
        }

        File runtimeDir = new File(TEMPPATH);

        if (!runtimeDir.exists())
        {
            runtimeDir.mkdir();
        }

        if (!checkFile.exists())
        {
            checkFile.createNewFile();
        }

        fos = null;
        fos = new FileOutputStream(checkFile);
        currentTimestampProp.store(fos, "deployFileTimestamp");
        if (fos != null)
        {
            fos.close();
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    public void setDoPreProcess()
    {
        if (checkIfModify())
        {
            doPreProcess = true;            
        }
        
        if (checkPreProcessProperty())
        {
            doPreProcess = true;            
        }
        
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public boolean getDoPreProcess()
    {
        return doPreProcess;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    private static File[] getPreprocessFiles()
    {
        File files[] = FileScanner.getFiles("*tmpl");
        return files;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void initProperties()
    {
        globalProps = PS.getDefaultProps();
        fixedProps = PS.getFixedProps();
        createDsProps();
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    private Properties createDsProps()
    {
        Properties p = new Properties();
        Set set1 = globalProps.keySet();
        Set set2 = getNeededSet(set1, ".ds.");
        String begin;
        String end;
        Object value;
        for (Iterator it = set2.iterator(); it.hasNext(); globalProps.put(begin
                + end, value))
        {
            String s = (String) it.next();
            int i = s.indexOf(".");
            begin = s.substring(0, i);
            String subString = s.substring(i + 1);
            i = subString.indexOf(".");
            subString = subString.substring(i + 1);
            i = subString.indexOf(".");
            end = subString.substring(i);
            value = globalProps.get(s);
        }

        return p;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param set
     * @param partkey
     * @return
     */
    private static Set getNeededSet(Set set, String partkey)
    {
        HashSet result = new HashSet();
        Iterator it = set.iterator();
        do
        {
            if (!it.hasNext())
            {
                break;                
            }
            
            String key = (String) it.next();
            if (key.indexOf(partkey) != -1)
            {
                result.add(key);                
            }
            
        }
        while (true);
        
        return result;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws FileNotFoundException
     */
    public Properties getDeployProps() throws FileNotFoundException
    {
        if (globalProps == null)
        {
            initProperties();            
        }
        
        return globalProps;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param files
     * @throws PropNotFoundException
     */
    private void modifyPreProcessXml(File files[]) throws PropNotFoundException
    {
        File file = null;
        try
        {
            Util.deleteFile(TEMPPATH, "global-preprocess.xml");
            global_file = new File(TEMPPATH, "global-preprocess.xml");
            Document doc = new Document();
            Element root = new Element("project");
            root.setAttribute("name", "ERP PreProcess");
            root.setAttribute("basedir", ".");
            root.setAttribute("default", "main");
            initProperties();
            if (!startMode.equals("client"))
            {
                checkNeededProps(globalProps);                
            }
            
            String srcFileName = null;
            String dstFileName = null;
            String moduleName = null;
            String dir = null;
            String dirName = null;
            int temp = -1;
            int index = 0;
            Element par = null;
            Element copy = null;
            Element fileset = null;
            Element include = null;
            Element filterset = null;
            String dstype = null;
            String jndiStr = "jndi";
            String execStr = "Execute the ";
            String workStr = " work.";
            String dstypeStr = ".dstype";
            for (int i = 0; i < files.length; i++)
            {
                file = files[i];
                dir = file.getParentFile().getAbsolutePath();
                dirName = file.getParentFile().getName();
                srcFileName = file.getName();
                int j = 0;
                do
                {
                    if (j >= SUFFIXES.length)
                    {
                        break;                        
                    }
                    
                    temp = srcFileName.lastIndexOf(SUFFIXES[j]);
                    if (temp > 0)
                    {
                        if (SUFFIXES[j].equals(".properties")
                                && srcFileName.lastIndexOf("jndi") == -1)
                        {
                            index = 0;                            
                        }
                        else if (SUFFIXES[j].equals("-service.xml"))
                        {
                            index = 0;                            
                        }
                        else
                        {
                            index = temp;                            
                        }
                        break;
                    }
                    index = 0;
                    j++;
                }
                while (true);
                
                int index1 = srcFileName.lastIndexOf(".");
                dstFileName = srcFileName.substring(0, index1);
                if (index > 0)
                {
                    moduleName = srcFileName.substring(0, index);                    
                }
                else if (srcFileName.equals("jboss-service.xml.tmpl"))
                {
                    moduleName = Util.findPar(dir);
                }
                else
                {
                    int n = dirName.lastIndexOf(".par");
                    if (n > -1)
                    {
                        moduleName = "userdefined-" + dirName.substring(0, n);                        
                    }
                    else if (Util.findPar(dir) == null)
                    {
                        int index2 = dstFileName.lastIndexOf(".");
                        String tempName = srcFileName.substring(0, index2);
                        moduleName = "userdefined-" + tempName;
                    }
                    else
                    {
                        moduleName = "userdefined-" + Util.findPar(dir);
                    }
                }
                if (moduleName == null)
                {
                    continue;                    
                }
                if (moduleName.equals("jndi") && !startMode.equals("client"))
                {
                    int mark = dir.lastIndexOf(startMode);
                    if (mark <= 0 || !dirName.equals(startMode))
                    {
                        continue;                        
                    }
                }
                
                String parName;
                for (parName = moduleName; modules.contains(parName); parName = parName + '1')
                {
                    ;                    
                }
                modules.add(parName);
                par = new Element("target");
                par.setAttribute("name", parName);
                
                par.setAttribute("description", "Execute the " + moduleName
                        + " work.");
                copy = new Element("copy");
                copy.setAttribute("tofile", dir + File.separator + dstFileName);
                copy.setAttribute("filtering", "yes");
                copy.setAttribute("overwrite", "true");
                fileset = new Element("fileset");
                fileset.setAttribute("dir", dir);
                include = new Element("include");
                include.setAttribute("name", srcFileName);
                fileset.addContent(include);
                copy.addContent(fileset);
                filterset = new Element("filterset");
                String tokens[] = null;
                if (moduleName.equals("jndi"))
                {
                    tokens = getModuleTokens(moduleName);
                    for (int l = 0; l < tokens.length; l++)
                    {
                        addFilterElement(filterset, moduleName, tokens[l]);                        
                    }

                }
                else if (moduleName.startsWith("userdefined-"))
                {
                    tokens = getModuleTokens(moduleName);
                    for (int k = 0; k < tokens.length; k++)
                    {
                        addFilterElement(filterset, moduleName, tokens[k]);                        
                    }

                }
                else if (moduleName.startsWith("tomcat")
                        || moduleName.startsWith("jetty"))
                {
                    tokens = getModuleTokens(moduleName);
                    for (int k = 0; k < tokens.length; k++)
                    {
                        addFilterElement(filterset, moduleName, tokens[k]);                        
                    }

                }
                else
                {
                    addFilterElement(filterset, moduleName, "drivertitle");
                    addFilterElement(filterset, moduleName, "driverclass");
                    addFilterElement(filterset, moduleName, "attribute");
                    addFilterElement(filterset, moduleName, "ip");
                    addFilterElement(filterset, moduleName, "port");
                    addFilterElement(filterset, moduleName, "user");
                    addFilterElement(filterset, moduleName, "password");
                    addFilterElement(filterset, moduleName, "databasename");
                    addFilterElement(filterset, moduleName,
                            "DelExceptionConnFlag");
                    addFilterElement(filterset, moduleName, "MinSize");
                    addFilterElement(filterset, moduleName, "MaxSize");
                    addFilterElement(filterset, moduleName,
                            "BlockingTimeoutMillis");
                    addFilterElement(filterset, moduleName,
                            "IdleTimeoutMinutes");
                }
                if (globalProps.get(moduleName + ".dstype") != null)
                {
                    dstype = (String) globalProps.get(moduleName + ".dstype");                    
                }
                else
                {
                    dstype = (String) globalProps.get("default.dstype");                    
                }
                if (!moduleName.equals("jndi")
                        && !moduleName.startsWith("userdefined-"))
                {
                    addFixedFilter(filterset, dstype);                    
                }
                
                copy.addContent(filterset);
                par.addContent(copy);
                root.addContent(par);
            }

            String jmsDsType;
            if (globalProps.get("jms.dstype") != null)
            {
                jmsDsType = (String) globalProps.get("jms.dstype");                
            }
            else
            {
                jmsDsType = (String) globalProps.get("default.dstype");                
            }
            
            String tokens2[] = null;
            
            if (jmsDsType != null)
            {
                Element jms = new Element("target");
                jms.setAttribute("name", "jms");
                jms.setAttribute("description", "Execute the jms work.");
                copy = new Element("copy");
                String jbossDefPath = PS.getPath("corepower.path", "jboss.default");
                copy.setAttribute("tofile", jbossDefPath + File.separator
                        + "deploy" + File.separator + "jms" + File.separator
                        + "jbossmqfor" + jmsDsType + "-service.xml");
                copy.setAttribute("filtering", "yes");
                copy.setAttribute("overwrite", "true");
                fileset = new Element("fileset");
                fileset.setAttribute("dir", jbossDefPath + File.separator
                        + "template" + File.separator + "jms");
                include = new Element("include");
                include.setAttribute("name", "jbossmqfor" + jmsDsType
                        + "-service.xml.tmpl");
                fileset.addContent(include);
                copy.addContent(fileset);
                filterset = new Element("filterset");
                tokens2 = getModuleTokens("jms");
                Set set2 = new HashSet();
                
                for (int i = 0; i < tokens2.length; i++)
                {
                    set2.add(tokens2[i]);                    
                }

                if (!set2.contains("drivertitle"))
                {
                    set2.add("drivertitle");                    
                }
                
                if (!set2.contains("driverclass"))
                {
                    set2.add("driverclass");                    
                }
                
                if (!set2.contains("attribute"))
                {
                    set2.add("attribute");                    
                }
                
                if (!set2.contains("ip"))
                {
                    set2.add("ip");                    
                }
                
                if (!set2.contains("port"))
                {
                    set2.add("port");                    
                }
                
                if (!set2.contains("user"))
                {
                    set2.add("user");                    
                }
                
                if (!set2.contains("password"))
                {
                    set2.add("password");                    
                }
                
                if (!set2.contains("databasename"))
                {
                    set2.add("databasename");                    
                }
                
                tokens2 = (String[]) set2.toArray(new String[0]);
                
                for (int k = 0; k < tokens2.length; k++)
                {
                    addFilterElement(filterset, "jms", tokens2[k]);                    
                }

                copy.addContent(filterset);
                jms.addContent(copy);
                root.addContent(jms);
                jmsDisposal = true;
            }
            else
            {
                jmsDisposal = false;
            }
            if (!startMode.equals("client"))
            {
                Element kernelJndi = new Element("target");
                kernelJndi.setAttribute("name", "kernelJndi");
                kernelJndi.setAttribute("description",
                        "Execute the kernel-bin jndi work.");
                copy = new Element("copy");
                dir = PS.getPath("jboss.path", "home") + File.separator + "bin";
                copy.setAttribute("tofile", dir + File.separator
                        + "jndi.properties");
                copy.setAttribute("filtering", "yes");
                copy.setAttribute("overwrite", "true");
                fileset = new Element("fileset");
                fileset.setAttribute("dir", dir);
                include = new Element("include");
                include.setAttribute("name", "jndi.properties.tmpl");
                fileset.addContent(include);
                copy.addContent(fileset);
                filterset = new Element("filterset");
                tokens2 = getModuleTokens("jndi");
                
                for (int m = 0; m < tokens2.length; m++)
                {
                    addFilterElement(filterset, "jndi", tokens2[m]);                    
                }

                copy.addContent(filterset);
                kernelJndi.addContent(copy);
                root.addContent(kernelJndi);
                kernelBinDisposal = true;
            }
            else
            {
                kernelBinDisposal = false;
            }
            
            if (startMode.equals("all") || startMode.equals("jndi"))
            {
                Element jbossService = new Element("target");
                jbossService.setAttribute("name", "jbossService");
                jbossService.setAttribute("description",
                        "Execute the jboss-service.xml work.");
                copy = new Element("copy");
                String defaultDir = PS.getPath("corepower.path", "jboss.default");
                if (startMode.equals("client"))
                {
                    dir = defaultDir + File.separator + "conf";                    
                }
                else
                {
                    dir = defaultDir + File.separator + "conf" + File.separator
                            + startMode;                    
                }
                
                copy.setAttribute("tofile", dir + File.separator
                        + "jboss-service.xml");
                copy.setAttribute("filtering", "yes");
                copy.setAttribute("overwrite", "true");
                fileset = new Element("fileset");
                fileset.setAttribute("dir", dir);
                include = new Element("include");
                include.setAttribute("name", "jboss-service.xml.tmpl");
                fileset.addContent(include);
                copy.addContent(fileset);
                filterset = new Element("filterset");
                tokens2 = getModuleTokens("jndi");
                
                for (int m = 0; m < tokens2.length; m++)
                {
                    addFilterElement(filterset, "jndi", tokens2[m]);                    
                }

                copy.addContent(filterset);
                jbossService.addContent(copy);
                root.addContent(jbossService);
                jbossServiceDisposal = true;
            }
            else
            {
                jbossServiceDisposal = false;
            }
            Element main = new Element("target");
            main.setAttribute("name", "main");
            main.setAttribute("description", "Do all jobs");
            StringBuffer depend = null;
            if (jmsDisposal)
            {
                depend = new StringBuffer("jms,");                
            }
            else
            {
                depend = new StringBuffer();                
            }
            
            for (Iterator iter = modules.iterator(); iter.hasNext(); depend
                    .append(","))
            {
                depend.append((String) iter.next());                
            }

            if (kernelBinDisposal)
            {
                depend.append("kernelJndi,");                
            }
            if (jbossServiceDisposal)
            {
                depend.append("jbossService,");                
            }
            String dependStr = null;
            
            if (depend.length() > 0)
            {
                dependStr = depend.substring(0, depend.length() - 1);                
            }
            else
            {
                dependStr = depend.toString();                
            }
            
            main.setAttribute("depends", dependStr);
            root.addContent(main);
            doc.setRootElement(root);
            XMLOutputter outp = new XMLOutputter(" ", true, "GB2312");
            outp.output(doc, new FileOutputStream(global_file));
        }
        catch (IOException ex)
        {
            log.info(ex);
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param moduleName
     * @return
     */
    private String[] getModuleTokens(String moduleName)
    {
        Enumeration en = globalProps.propertyNames();
        String key = null;
        String token = null;
        ArrayList list = new ArrayList();
        do
        {
            if (!en.hasMoreElements())
            {
                break;                
            }
            key = (String) en.nextElement();
            
            if (key.startsWith(moduleName))
            {
                token = key.substring(key.lastIndexOf(".") + 1);
                list.add(token);
            }
        }
        while (true);
        
        String s[] = new String[list.size()];
        list.toArray(s);
        return s;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param tokenName
     * @param moduleName
     * @param properties
     * @return
     * @throws PropNotFoundException
     */
    private static Element constructModuleFilter(String tokenName,
            String moduleName, Properties properties)
            throws PropNotFoundException
    {
        Element param = new Element("filter");
        param.setAttribute("token", tokenName);
        String key = moduleName + "." + tokenName;
        String temp = properties.getProperty(key);
        
        if (temp == null)
        {
            throw new PropNotFoundException(key);
        }
        else
        {
            temp = temp.trim();
            param.setAttribute("value", temp);
            return param;
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param elem
     * @param moduleName
     * @param tokenName
     * @throws PropNotFoundException
     */
    private void addFilterElement(Element elem, String moduleName,
            String tokenName) throws PropNotFoundException
    {
        if (globalProps.get(moduleName + "." + tokenName) != null)
        {
            elem.addContent(constructModuleFilter(tokenName, moduleName,
                    globalProps));            
        }
        else
        {
            elem.addContent(constructModuleFilter(tokenName, "default",
                    globalProps));            
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param tokenName
     * @param dstype
     * @return
     * @throws PropNotFoundException
     */
    private Element constructFixedFilter(String tokenName, String dstype)
            throws PropNotFoundException
    {
        Element param = new Element("filter");
        param.setAttribute("token", tokenName);
        String key = dstype + "." + tokenName;
        String temp = fixedProps.getProperty(dstype + "." + tokenName);
        if (temp == null)
        {
            throw new PropNotFoundException(key);
        }
        else
        {
            temp = temp.trim();
            param.setAttribute("value", temp);
            return param;
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param elem
     * @param dstype
     * @throws PropNotFoundException
     */
    private void addFixedFilter(Element elem, String dstype)
            throws PropNotFoundException
    {
        elem.addContent(constructFixedFilter("ipschama", dstype));
        elem.addContent(constructFixedFilter("nameschama", dstype));
        elem.addContent(constructFixedFilter("attrschama", dstype));
        elem
                .addContent(constructFixedFilter("ExceptionSorterClassName",
                        dstype));
        elem.addContent(constructFixedFilter("dstype", dstype));
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param properties
     * @param key
     * @throws PropNotFoundException
     */
    private static void checkNeededProperty(Properties properties, String key)
            throws PropNotFoundException
    {
        if (properties.getProperty(key) == null)
        {
            throw new PropNotFoundException(key);            
        }
        else
        {
            return;            
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param properties
     * @throws PropNotFoundException
     */
    private static void checkNeededProps(Properties properties)
            throws PropNotFoundException
    {
        checkNeededProperty(properties, "default.dstype");
        checkNeededProperty(properties, "default.drivertitle");
        checkNeededProperty(properties, "default.driverclass");
        checkNeededProperty(properties, "default.attribute");
        checkNeededProperty(properties, "default.ip");
        checkNeededProperty(properties, "default.port");
        checkNeededProperty(properties, "default.user");
        checkNeededProperty(properties, "default.password");
        checkNeededProperty(properties, "default.databasename");
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param needDel
     */
    public void delCachedFiles(boolean needDel)
    {
        if (needDel)
        {
            int failDelNum = 0;
            LinkedList deledFileList = new LinkedList();
            failDelNum = Util.delTree(TEMPPATH, failDelNum, deledFileList);
            
            if (failDelNum > 0)
            {
                log.info(failDelNum + " files in dirctory " + TEMPPATH
                        + " del failed!");                
            }
            
            String defaultDir = PS.getPath("corepower.path", "jboss.default");
            String deployDir = defaultDir + File.separator + "deploy";
            String deployPaths[] = {deployDir};
            File tmpls[] = FileUtil.getFiles(deployPaths,
                    "jbossmqfor*-service.xml", true);
            String errPostfix = " can not be deleted...";
            
            for (int i = 0; i < tmpls.length; i++)
            {
                if (!tmpls[i].delete())
                {
                    System.err.println(tmpls[i].getAbsolutePath()
                            + " can not be deleted...");                    
                }                
            }

        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws ErpDeploymentException
     */
    public String preExecute() throws ErpDeploymentException
    {
        String fileName = null;
        try
        {
            if (getDoPreProcess())
            {
                File files[] = getPreprocessFiles();
                modifyPreProcessXml(files);
                fileName = global_file.getCanonicalPath();
            }
            else
            {
                String filePath = PS.getPath("erp.path", "deploy");
                fileName = filePath + File.separator + "global-preprocess.xml";
            }
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("[PreProcess] error: " + ex.getMessage()
                    + " not founded....");
            throw new ErpDeploymentException(ex);
        }
        catch (PropNotFoundException ex)
        {
            System.err.println("[PreProcess] error: " + ex.getMessage()
                    + " Property not founded....");
            throw new ErpDeploymentException(ex);
        }
        catch (IOException ex)
        {
            System.err.println("[PreProcess] error: " + ex.getMessage());
            throw new ErpDeploymentException(ex);
        }
        return fileName;
    }

}
