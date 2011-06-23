/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>文件名称：DeploymentInit.java</p>
 * <p>文件描述：保存获取的插件路径信息</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-6</p>
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
public class DeploymentInit
{
    /**
     * 单例
     */
    private static DeploymentInit instance = null;
    
    /**
     * 保存目录
     */
    private LinkedList deployedDirs;
    
    /**
     * 保存FileSet
     */
    private LinkedList filesets;
    
    /**
     * 保存插件Par
     */
    private LinkedList fsParIdList;
    
    
    /**
     * 
     */
    private File files[];

    /**
     * 构造函数
     *
     */
    private DeploymentInit()
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
     * <p>创建日期：2008-5-6</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static DeploymentInit getSingleton()
    {
        if(instance == null)
        {
            instance = new DeploymentInit();            
        }
        return instance;
    }

    /**
     * 
     * <p>功能描述：设置deploy目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
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
     * <p>功能描述：获得deploy目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws ErpDeploymentException
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
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
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
     * <p>功能描述：获取Par包列表</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
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
     * <p>功能描述：设置FileSet</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
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
     * <p>功能描述：获取FileSet</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
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
     * <p>功能描述：设置Path</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param files
     */
    void setFSPaths(File files[])
    {
        this.files = files;
    }

    /**
     * 
     * <p>功能描述：获取Path </p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws ErpDeploymentException
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
     * <p>功能描述：打印Path</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
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
                bf.append(it1.next() + "\n");
        }

        return bf.toString();
    }

}
