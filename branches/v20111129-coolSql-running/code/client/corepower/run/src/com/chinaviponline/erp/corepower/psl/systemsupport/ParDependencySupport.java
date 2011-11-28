/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

import com.chinaviponline.erp.corepower.api.psl.systemsupport.Par;

/**
 * <p>文件名称：ParDependencySupport.java</p>
 * <p>文件描述：</p>
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
public class ParDependencySupport
{
    /**
     * 单例
     */
    private static ParDependencySupport instance = null;
    
    /**
     * 部署列表
     */
    private LinkedList deployList;
    
    /**
     * 重新部署列表
     */
    private LinkedList redeployList;
    
    /**
     * par列表
     */
    private LinkedList parList;
    
    /**
     * par图
     */
    private HashMap parMap;
    
    /**
     * map
     */
    private HashMap map;
    
    /**
     * 路径转par
     */
    private HashMap parPath2ParMap;
    
    /**
     * 0
     */
    private static final int ZERO = 0;
    
    /**
     * 单例
     */
    private static DeploymentInit deployInit = DeploymentInit.getSingleton();
      
    /**
     * 日志
     */
    private static Logger log;



    // 初始化
    static 
    {
        log = Logger.getLogger((ParDependencySupport.class).getName());
    }
    
    /**
     * 构造函数 仿单例
     *
     */
    private ParDependencySupport()
    {
        deployList = new LinkedList();
        redeployList = new LinkedList();
        parList = new LinkedList();
        parMap = new HashMap();
        map = new HashMap();
        parPath2ParMap = new HashMap();
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
    public static synchronized ParDependencySupport getSingleton()
    {
        if(instance == null)
        {
            instance = new ParDependencySupport();            
        }
        
        return instance;
    }

    /**
     * 
     * <p>功能描述：构建部署</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param filesets
     */
    private void buildCommonDependency(LinkedList filesets)
    {
        Iterator iter = filesets.iterator();
        String temp = null;
        Iterator iter2 = null;
        
        while(iter.hasNext()) 
        {
            iter2 = ((LinkedList)iter.next()).iterator();
            while(iter2.hasNext()) 
            {
                temp = (String)iter2.next();
                if(temp.endsWith("common.par"))
                {
                    deployList.add(temp);                    
                }
            }
        }
    }

    /**
     * 
     * <p>功能描述：build文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param filesets
     * @throws ErpDeploymentException
     */
    private void buildFilesetDependency(LinkedList filesets)
        throws ErpDeploymentException
    {
        Iterator filesetIter = filesets.iterator();
        SAXBuilder sb = new SAXBuilder();

        do
        {// 遍历每一个fileset
            
            LinkedList lists=null;
            if (!filesetIter.hasNext())
            {
                break;
            }

            lists = new LinkedList();
            Iterator iter2 = ((LinkedList) filesetIter.next()).iterator();
            
            do
            {// 再遍历fileset中包含的每一项
                 

                if (!iter2.hasNext())
                {
                    break;
                }

                String plugdir = (String) iter2.next();
                File parinfoxml = new File(plugdir + File.separator
                        + "parinfo.xml");
                LinkedList list = new LinkedList();

                // 没有parinfo.xml文件的情况
                if (!parinfoxml.exists())
                {
                    log.error("should have a file which named "
                            + parinfoxml.getAbsolutePath());
                    break;
                }
                try
                {

                    FileInputStream fis = new FileInputStream(parinfoxml);
                    Document doc = sb.build(fis);
                    Element root = doc.getRootElement();
                    Element info = root.getChild("info");
                    String parID = info.getAttributeValue("name");
                    String parVersion = info.getAttributeValue("version");
                    boolean b = false;
                    // b = LicenseCheck.parLicenseCheck(parID);
                    //TODO 先不用LicenseCheck b=true        
                    b = true;

                    if (!b)
                    {
                        log.warn("par :" + parID
                                + " has no license, cann't deploy!");
                    }

                    Iterator dependIter = root.getChildren("depend").iterator();
                    Element depend = null;
                    do
                    {
                        if (!dependIter.hasNext())
                        {
                            break;
                        }

                        depend = (Element) dependIter.next();
                        String dependedParName = depend.getText();
                        if (!dependedParName.equals(""))
                        {
                            if (!contains(deployInit.getDeployedDirs(),
                                    dependedParName))
                            {
                                throw new ErpDeploymentException(parID
                                        + ".par depend on " + dependedParName
                                        + ",but " + dependedParName
                                        + " not found in deployed par list");
                            }

                            int index = dependedParName.lastIndexOf(".par");
                            String tmpName = null;
                            if (index > 0)
                            {
                                tmpName = dependedParName.substring(0, index);
                            }
                            else
                            {
                                tmpName = dependedParName;
                            }

                            //                            if(!LicenseCheck.parLicenseCheck(tmpName))
                            //                                throw new ErpDeploymentException(" No permission for " + parID + "'s depend par:\"" + dependedParName + "\"");
                            //TODO 先不用LicenseCheck

                            String dependedParPath = (String) map
                                    .get(dependedParName);
                            list.add(dependedParPath);
                        }
                    }
                    while (true);

                    list.add(plugdir);
                    String temp = parID.toLowerCase() + ".par";
                    if (!plugdir.toLowerCase().endsWith(temp))
                    {
                        throw new ErpDeploymentException(
                                "the NAME attribute of INFO element in parinfo.xml "
                                        + parID + " CAN NOT matched with the "
                                        + plugdir);
                    }
                    Element stateMbean = root.getChild("stateMbean");

                    Par par=null;
                    if (stateMbean != null)
                    {
                        String stateMBeanStr = stateMbean.getText();
                        par = new Par(parID, parVersion, plugdir, stateMBeanStr);
                    }
                    else
                    {
                        par = new Par(parID, parVersion, plugdir, null);
                    }
                    if (parMap.containsKey(parID)
                            && !par.getBaseDir().equals(
                                    ((Par) parMap.get(parID)).getBaseDir()))
                    {
                        throw new ErpDeploymentException(
                                "There are two pars named "
                                        + parID
                                        + " in "
                                        + par.getBaseDir()
                                        + " and "
                                        + ((Par) parMap.get(parID))
                                                .getBaseDir()
                                        + " , this can cause unpredictable error!");
                    }
                    parList.add(par);
                    parMap.put(parID, par);
                    parPath2ParMap.put(plugdir, par);
                }
                //        catch(LicenseException ex)
                //        {
                //            throw new ErpDeploymentException(ex);
                //        }
                //      TODO 先不用LicenseCheck

                catch (IOException ex)
                {
                    throw new ErpDeploymentException("parinfo.xml for "
                            + plugdir + " can not read.");
                }
                catch (JDOMException ex)
                {
                    throw new ErpDeploymentException("parinfo.xml for "
                            + plugdir + " can not be parsed by JDOM.");
                }

                String tempStr = null;
                if (list.size() == 1)
                {
                    tempStr = (String) list.get(0);
                    if (!deployList.contains(plugdir))
                    {
                        deployList.add(tempStr);                        
                    }
                }
                else if (lists.size() == 0)
                {
                    lists.add(list);
                }
                else
                {
                    int index = lists.size() - 1;
                    plug(index, lists, list);
                }                              
            }//End 再遍历fileset中包含的每一项  
            while (true);

            Iterator tempIter = lists.iterator();
            LinkedList tempList2 = null;
            for (; tempIter.hasNext(); sort(tempList2))
            {
                tempList2 = (LinkedList) tempIter.next();
            }

            if (redeployList.size() > 0)
            {
                Iterator tempIter2 = redeployList.iterator();
                StringBuffer errInfo = new StringBuffer();
                while (tempIter2.hasNext())
                {
                    LinkedList tempList3 = (LinkedList) tempIter2.next();
                    String ss[] = new String[tempList3.size()];
                    tempList3.toArray(ss);
                    int k = 0;
                    while (k < ss.length)
                    {
                        errInfo.append(ss[k]);
                        errInfo.append(" can not find dependent par. \n");
                        k++;
                    }
                }
                throw new ErpDeploymentException(
                        "Dependency relation is wrong. \n" + errInfo);
            }
        }//End 遍历每一个fileset
        while (true);
    }

    /**
     * 
     * <p>功能描述：排序</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param parDependencyList 部署par+depend parS
     */
    private void sort(LinkedList parDependencyList)
    {
        Iterator iter = parDependencyList.iterator();
        String dependedParName = null;
        int index = -1;
        String parName = (String)parDependencyList.get(parDependencyList.size() - 1);//需要部署的par
        while(iter.hasNext()) 
        {
            dependedParName = (String)iter.next();//依赖的par
            index = deployList.lastIndexOf(dependedParName);//依赖的par是否存在,索引号
            
            if(index == -1 && !dependedParName.equals(parName))//依赖自己,错误,需要重新部署
            {
                redeployList.add(parDependencyList);
                return;
            }
            
            if(index != -1)
            {
                continue;    //修改 从break到continue            
            }
            deployList.add(parName);
            int preSize=-1;
            do
            {
                if(redeployList.size() <= 0)
                {
                    break;                    
                }
                preSize = redeployList.size();
                disposeRedeployList();
            } while(preSize > redeployList.size());
        }
    }

    /**
     * 
     * <p>功能描述：取消重新部署</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void disposeRedeployList()
    {
        LinkedList templists[] = new LinkedList[redeployList.size()];
        redeployList.toArray(templists);
        LinkedList list = null;
        for(int i = 0; i < templists.length; i++)
        {
            list = templists[i];
            if(resort(list))
            {
                redeployList.remove(list);                
            }
        }

    }

    /**
     * 
     * <p>功能描述：重排序</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param list
     * @return
     */
    private boolean resort(LinkedList list)
    {
        Iterator iter = list.iterator();
        String dependedParName = null;
        String parName = (String)list.get(list.size() - 1);
        int index = -1;
        do
        {
            if(!iter.hasNext())
            {
                break;                
            }
            
            dependedParName = (String)iter.next();
            index = deployList.lastIndexOf(dependedParName);
            if(index == -1 && !dependedParName.equals(parName))
            {
                return false;                
            }
            if(index == -1)
            {
                deployList.add(parName);                
            }
        } while(true);
        return true;
    }

    /**
     * 
     * <p>功能描述：插件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param index
     * @param lists
     * @param parDependencyList
     */
    private void plug(int index, LinkedList lists, LinkedList parDependencyList)
    {
        int size = ((LinkedList)lists.get(index)).size();
        if(parDependencyList.size() >= size)
        {
            index++;
            lists.add(index, parDependencyList);
        } else
        if(--index < 0)
        {
            lists.add(0, parDependencyList);            
        }
        else
        {
            plug(index, lists, parDependencyList);            
        }
    }

    /**
     * 
     * <p>功能描述：是否包含文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param list
     * @param parName
     * @return
     */
    private boolean contains(LinkedList list, String parName)
    {
        Iterator dirs = list.iterator();
        String temp = null;
        if(map.get(parName) != null)
        {
            return true;            
        }
        
        while(dirs.hasNext()) 
        {
            temp = (String)dirs.next();
            if(temp.endsWith(parName))
            {
                map.put(parName, temp);
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * <p>功能描述：返回部署的par</p>
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
    public LinkedList getDeployedParList()
    {
        return deployList;
    }

    /**
     * 
     * <p>功能描述：返回部署数组</p>
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
    public Par[] getSequenceDeployedPars()
    {
        Par pars[] = new Par[parList.size()];
        parList.toArray(pars);
        return pars;
    }

    /**
     * 
     * <p>功能描述：返回部署的列表</p>
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
    LinkedList getSequenceDeployedParList()
    {
        return parList;
    }

    /**
     * 
     * <p>功能描述：返回部署的图</p>
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
    Map getDeployedParMap()
    {
        return parMap;
    }

    /**
     * 
     * <p>功能描述：获得已经部署的par</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param parName
     * @return
     */
    Par getDeployedPar(String parName)
    {
        Par par = (Par)parMap.get(parName);
        return par;
    }

    /**
     * 
     * <p>功能描述：执行build</p>
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
    public void executeDependency()
        throws ErpDeploymentException
    {
        LinkedList filesets = deployInit.getFileSets();
        buildCommonDependency(filesets);
        buildFilesetDependency(filesets);
        sortParList();
    }

    /**
     * 
     * <p>功能描述：排序par列表</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void sortParList()
    {
        parList = new LinkedList();
        String parPath=null;
        for(Iterator it = deployList.iterator(); it.hasNext(); parList.add(parPath2ParMap.get(parPath)))
        {
            parPath = (String)it.next();
        } 
    }
}
