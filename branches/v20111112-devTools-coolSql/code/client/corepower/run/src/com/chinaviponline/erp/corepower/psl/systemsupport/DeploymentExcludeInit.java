/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.File;
import java.util.*;
import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileUtil;

/**
 * <p>文件名称：DeploymentExcludeInit.java</p>
 * <p>文件描述：获取所有排除在外的Par</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-14</p>
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
public class DeploymentExcludeInit
{
    /**
     * 日志
     */
    private static Logger log;
    
    /**
     * 属性中心
     */
    private static final PropertiesService PS = PropertiesService.getInstance();
    
    /**
     * 单例
     */
    private static DeploymentExcludeInit instance = null;
    
    /**
     * 部署的目录
     */
    private LinkedList deployedDirs;
    
    /**
     * File Set
     */
    private LinkedList filesets;
    
    /**
     * 路径集合
     */
    private LinkedList fsParIdList;
    
    /**
     * 文件集合
     */
    private File files[];
    

    // 初始化
    static 
    {
        log = Logger.getLogger((DeploymentExcludeInit.class).getName());
    }

    /**
     * 
     *构造函数 单例
     */
    private DeploymentExcludeInit()
    {
        deployedDirs = null;
        filesets = null;
        fsParIdList = null;
        files = null;
    }

    /**
     * 
     * <p>功能描述：单例</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static DeploymentExcludeInit getSingleton()
    {
        if(instance == null)
        {
            instance = new DeploymentExcludeInit();
        }
        return instance;
    }

    /**
     * 
     * <p>功能描述：设置部署目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param deployedDirs
     */
    public void setDeployedDirs(LinkedList deployedDirs)
    {
        this.deployedDirs = deployedDirs;
    }

    /**
     * 
     * <p>功能描述：获取部署目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws erpDeploymentException
     */
    public LinkedList getDeployedDirs()
        throws ErpDeploymentException
    {
        if(deployedDirs == null)
        {
            throw new ErpDeploymentException("deployedDirs == null");            
        }
        else
        {
            return deployedDirs;            
        }
    }

    /**
     * 
     * <p>功能描述：设置路径ID</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param aList
     */
    public void setFsParIdList(LinkedList aList)
    {
        fsParIdList = aList;
    }

    /**
     * 
     * <p>功能描述：获取路径ID</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public List getFsParIdLilst()
    {
        return fsParIdList;
    }

    /**
     * 
     * <p>功能描述：设置文件集合</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param aFilesets
     */
    public void setFileSets(LinkedList aFilesets)
    {
        filesets = aFilesets;
    }

    /**
     * 
     * <p>功能描述：获得所有文件集合</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws ErpDeploymentException
     */
    public LinkedList getFileSets()
        throws ErpDeploymentException
    {
        if(filesets == null)
        {
            throw new ErpDeploymentException("deployedDirs == null");            
        }
        else
        {
            return filesets;            
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param _files
     */
    void setFSPaths(File files[])
    {
        this.files = files;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws erpDeploymentException
     */
    public File[] getFSPaths()
        throws ErpDeploymentException
    {
        if(files == null)
        {
            throw new ErpDeploymentException("deployedDirs == null");            
        }
        else
        {
            return files;            
        }
    }

    /**
     * 
     * <p>功能描述：打印路径ID</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    String showFSPaths()
    {
        StringBuffer bf = new StringBuffer();
        for(Iterator it = filesets.iterator(); it.hasNext();)
        {
            List ll = (List)it.next();
            Iterator it1 = ll.iterator();
            while(it1.hasNext()) 
            {
                bf.append(it1.next() + "\n");                
            }
        }

        return bf.toString();
    }

    /**
     * 
     * <p>功能描述：列举所有没有包含的Par</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws ErpDeploymentException
     */
    public static void collectExcludePar()
        throws ErpDeploymentException
    {
        String deployFile = PS.getPath("erp.path", "deploy") + File.separator + "partialdeploy.xml";
        log.info("partialdeploy File: " + deployFile);
        String argCmd[] = {
            "-f", deployFile
        };
        AntMainForErp antMain = new AntMainForErp();
        antMain.startAnt(argCmd, null, null);
        File deployFiles[] = FileUtil.getFiles(new String[] {PS.getPath(
                "erp.path", "deploy")}, "partialdeploy-*.xml", true);
        
        
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
        for(int i = 0; i < size; i++)
        {
            log.info("partialdeploy File[" + i + "]= " + deployFiles[i].getPath());
            String s = (new DeployExcludeXmlParser(deployFiles[i].getPath())).parserDeployXml();
            String argCmd1[] = {
                "-f", s
            };
            antMain = new AntMainForErp();
            antMain.startAnt(argCmd1, null, null);
        }

        try
        {
            DeploymentExcludeInit instance = getSingleton();
            LinkedList excludeList = instance.getFileSets();
            int excludeList_size = excludeList.size();
            if(excludeList_size > 0)
            {
                String basepar_exclude[] = new String[excludeList_size + 1];
                for(int i = 0; i < excludeList_size; i++)
                {
                    basepar_exclude[i] = excludeList.get(i).toString();
                    log.info("collectExcludePar excludeList=" + basepar_exclude[i]);
                }

            }
        }
        catch(ErpDeploymentException e)
        {
            e.printStackTrace();
        }
    }




}
