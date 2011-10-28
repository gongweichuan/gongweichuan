/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * <p>文件名称：DeployedPar.java</p>
 * <p>文件描述：自定义AntTask,解析deploy.xml</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-6</p>
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
public class DeployedPar extends Task
{
    /**
     * 日志 忽略
     */
    private static Logger log;

    /**
     * 文件集合
     */
    protected Vector filesets;

    /**
     * 
     */
    private LinkedList list;

    /**
     * 路径集合
     */
    private LinkedList fsPathList;

    /**
     * 文件集合
     */
    private LinkedList filesList;

    /**
     * Par包,插件集合
     */
    private LinkedList fsParIdList;

    /**
     * 保存配置
     */
    private static DeploymentInit instance = DeploymentInit.getSingleton();

    static
    {
                log = Logger.getLogger(DeployedPar.class);
    }

    /**
     * 构造函数
     *
     */
    public DeployedPar()
    {
        filesets = new Vector();
        list = new LinkedList();
        fsPathList = new LinkedList();
        filesList = new LinkedList();
        fsParIdList = new LinkedList();
    }

    /**
     * 
     * <p>功能描述：读取FileSet属性</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-6</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param set
     */
    public void addFileset(FileSet set)
    {
        filesets.addElement(set);
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() throws BuildException
    {
        String baseDir = null;
        String srcDirs[] = null;
        LinkedList tempList = null;
        FileSet fs = null;
        AntDirectoryScanner ds = null;
        String srcFiles[] = null;
        File baseFSPath = null;
        try
        {
            String buildErrPrefix = "Can't include file:";
            for (int i = 0; i < filesets.size(); i++)
            {
                fs = (FileSet) filesets.elementAt(i);
                baseFSPath = fs.getDir(fs.getProject());
                fsPathList.add(baseFSPath);
                ds = new AntDirectoryScanner();
                fs.setupDirectoryScanner(ds, getProject());
                ds.setFollowSymlinks(fs.isFollowSymlinks());
                ds.scan();
                baseDir = ds.getBasedir().getCanonicalPath();
                srcFiles = ds.getIncludedFiles();
                if (srcFiles.length > 0)
                    throw new BuildException("Can't include file:"
                            + srcFiles[0]);
                srcDirs = ds.getIncludedDirectories();
                tempList = new LinkedList();
                List parIdList = new LinkedList();
                for (int j = 0; j < srcDirs.length; j++)
                {
                    parIdList.add(srcDirs[j]);
                    srcDirs[j] = baseDir + File.separator + srcDirs[j];
                    list.add(srcDirs[j]);
                    tempList.add(srcDirs[j]);
                }

                fsParIdList.add(parIdList);
                filesList.add(tempList);
            }

            if (instance == null)
            {
                log.info("DeploymentInit instance == null....");                
            }
            instance.setDeployedDirs(list);
            instance.setFileSets(filesList);
            instance.setFsParIdList(fsParIdList);
            File files[] = (File[]) fsPathList.toArray(new File[fsPathList
                    .size()]);
            instance.setFSPaths(files);
        }
        catch (BuildException e)
        {
            log.error("", e);
            instance.setDeployedDirs(null);
            instance.setFileSets(null);
            instance.setFSPaths(null);
            instance.setFsParIdList(null);
            throw e;
        }
        catch (IOException ex)
        {
            instance.setDeployedDirs(null);
            instance.setFileSets(null);
            instance.setFSPaths(null);
            instance.setFsParIdList(null);
            throw new BuildException(ex);
        }
    }

}
