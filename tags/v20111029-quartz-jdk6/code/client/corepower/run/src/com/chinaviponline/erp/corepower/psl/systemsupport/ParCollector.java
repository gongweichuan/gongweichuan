/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.api.psl.systemsupport.Par;
import com.chinaviponline.erp.corepower.api.util.I18n;
import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileScanner;
import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileUtil;

/**
 * <p>文件名称：ParCollector.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-13</p>
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
public class ParCollector
{
    /**
     * 日志
     */
    private static final Logger log;

    /**
     * temp目录
     */
    private static final String TEMP_DIR_NAME = "temp";

    /**
     * pars.cache
     */
    private static final String PAR_FILE_NAME = "pars.cache";

    /**
     * 插件列表
     */
    private static List parList = new LinkedList();

    /**
     * 文件列表
     */
    private static File files[] = null;

    /**
     * 不包含的文件
     */
    private static LinkedList exclude_files = null;

    /**
     * 插件目录
     */
    private static List parDirList = new LinkedList();

    /**
     * 插件图
     */
    private static Map parMap = new HashMap();

    /**
     * 属性服务
     */
    private static final PropertiesService PS;

    /**
     * 缓存文件名称
     */
    private static String cacheFileName;

    /**
     * 初始化
     */
    static
    {
        log = Logger.getLogger(ParCollector.class);
        PS = PropertiesService.getInstance();
        cacheFileName = PS.getPath("erp.path", "home") + File.separator
                + "temp" + File.separator + "pars.cache";
    }

    /**
     * 默认构造函数
     *
     */
    public ParCollector()
    {
        
    }

    /**
     * 
     * <p>功能描述：初始化</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param needProcess
     * @throws ErpDeploymentException
     */
    public static void initDeployedPar(boolean needProcess)
            throws ErpDeploymentException
    {
        if (!needProcess)
        {
            try
            {
                getFromCache();
            }
            catch (Exception ignore)
            {
                getNoCache();
            }
        }
        else
        {
            getNoCache();
        }
    }

    /**
     * 
     * <p>功能描述：从缓存中获取插件信息等</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws Exception
     */
    private static void getFromCache() throws Exception
    {
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(cacheFileName)));
            parList = (LinkedList) in.readObject();
            files = (File[]) in.readObject();
            exclude_files = (LinkedList) in.readObject();
            DeploymentExcludeInit.getSingleton().setFileSets(exclude_files);
            
            Par par=null;
            for (Iterator it = parList.iterator(); it.hasNext(); parMap.put(par
                    .getID(), par))
            {
                par = (Par) it.next();
                parDirList.add(par.getBaseDir());
            }

        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Exception ignore)
                {

                }
                in = null;
            }
        }
    }

    /**
     * 
     * <p>功能描述：直接读取</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws ErpDeploymentException
     */
    private static void getNoCache() throws ErpDeploymentException
    {
        collectPar();
        executeDependency();
        DeploymentExcludeInit.collectExcludePar();
        parList = ParDependencySupport.getSingleton()
                .getSequenceDeployedParList();

        for (Iterator it = parList.iterator(); it.hasNext();)
        {
            Object tempObj=null;
            
            tempObj=it.next();       // 修改过的     
            
            if(tempObj!=null)
            {            
               parDirList.add(((Par) tempObj).getBaseDir()); 
              log.debug("add "+tempObj+"to parDirList");    
            }
        }

        parMap = ParDependencySupport.getSingleton().getDeployedParMap();
        files = DeploymentInit.getSingleton().getFSPaths();
        exclude_files = DeploymentExcludeInit.getSingleton().getFileSets();
        internationalize();
        savePars();
        log.info("===========par sequence=========");
        
        String path=null;
        for (Iterator it = parDirList.iterator(); it.hasNext(); log.info(path))
        {
            path = (String) it.next();
        }

        log.info("===========layer sequence=========");
        for (int i = 0; i < files.length; i++)
        {
            String layer = files[i].getAbsolutePath();
            log.info(layer);
        }
    }

    /**
     * 
     * <p>功能描述：查找整理插件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws ErpDeploymentException
     */
    private static void collectPar() throws ErpDeploymentException
    {
        String deployFile = PS.getPath("erp.path", "deploy") + File.separator
                + "deploy.xml";
        log.info("deployFile: " + deployFile);
        String argCmd[] = {"-f", deployFile};
        AntMainForErp antMain = new AntMainForErp();
        antMain.startAnt(argCmd, null, null);
        File deployFiles[] = FileUtil.getFiles(new String[] {PS.getPath(
                "erp.path", "deploy")}, "deploy-*.xml", true);
        Arrays.sort(deployFiles, new Comparator()
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

        int size = deployFiles.length;
        for (int i = 0; i < size; i++)
        {
            log.info("deployFile: " + deployFiles[i].getPath());
            String s = (new DeployXmlParser(deployFiles[i].getPath()))
                    .parserDeployXml();
            String argCmd1[] = {"-f", s};
            antMain = new AntMainForErp();
            antMain.startAnt(argCmd1, null, null);
        }

    }

    /**
     * 
     * <p>功能描述：执行脚本</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws ErpDeploymentException
     */
    private static void executeDependency() throws ErpDeploymentException
    {
        ParDependencySupport parDS = ParDependencySupport.getSingleton();
        parDS.executeDependency();
    }

    /**
     * 
     * <p>功能描述：初始化</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private static void internationalize()
    {
        String localeString = PropertiesService.getInstance().get("erp.locale");
        int index = localeString.indexOf("_");
        String language = localeString.substring(0, index);
        String nation = localeString.substring(index + 1);
        Locale locale = new Locale(language, nation);
        Par par=null;
        String result=null;

        for (Iterator it = parList.iterator(); it.hasNext();)
        {
            par = (Par) it.next();
            
            if(par==null)   //修改过的
            {
                continue;
            }
            
            String id = par.getID();
            String paths[] = {par.getBaseDir()};
            File i18nFiles[] = FileScanner.getFiles(paths, "*-i18n.xml", true);
            int size1 = i18nFiles.length;
            result = null;
            int j = 0;
            do
            {
                if (j >= size1)
                {
                    break;
                }

                I18n aI18n=null;

                try
                {
                    aI18n = I18n.getInstance(i18nFiles[j], locale);
                }
                catch (RuntimeException e)
                {
                    log.error("The xml file '" + i18nFiles[j]
                            + " parser error!");
                    throw e;
                }

                try
                {
                    result = aI18n.getLabelValue(id);
                }
                catch (IllegalArgumentException ignore)
                {

                }
                if (result != null)
                {
                    break;
                }

                j++;
            }
            while (true);

            if (result == null)
            {
                result = id;
            }
            
            par.setName(result);    //修改过的
        }

    }

    /**
     * 
     * <p>功能描述：保存pars</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private static void savePars()
    {
        ObjectOutputStream out = null;
        try
        {
            out = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(cacheFileName)));
            out.writeObject(parList);
            out.writeObject(files);
            out.writeObject(exclude_files);
        }
        catch (Exception ignore)
        {
            log.warn("Write " + cacheFileName + " error!");
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.reset();
                }
                catch (Exception ignore)
                {
                }
                try
                {
                    out.close();
                }
                catch (Exception ignore)
                {
                }
                out = null;
            }
        }
    }

    /**
     * 
     * <p>功能描述：获取序列化的par列表</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static List getSequenceParList()
    {
        return parList;
    }

    /**
     * 
     * <p>功能描述：获取序列化Par目录列表</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static List getSequenceParDirList()
    {
        return parDirList;
    }

    /**
     * 
     * <p>功能描述：获取已经部署的par</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static Par[] getSequenceDeployedPars()
    {
        Par pars[] = new Par[parList.size()];
        parList.toArray(pars);
        return pars;
    }

    /**
     * 
     * <p>功能描述：获取已经部署的par</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param parName
     * @return
     */
    public static Par getDeployedPar(String parName)
    {
        Par par = (Par) parMap.get(parName);
        return par;
    }

    /**
     * 
     * <p>功能描述：获取文件数组</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws ErpDeploymentException
     */
    public static File[] getFSPaths() throws ErpDeploymentException
    {
        if (files == null)
        {
            throw new ErpDeploymentException("deployedDirs == null");
        }
        else
        {
            return files;
        }
    }

}
