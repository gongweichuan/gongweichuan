/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tray.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;
import com.chinaviponline.erp.corepower.tray.i.IExtensionsFromDB;
import com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions;

/**
 * <p>文件名称：TExtensionsFromDBImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-11-9</p>
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
public class TExtensionsFromDBImpl implements IExtensionsFromDB
{
    private final Logger log=Logger.getLogger(this.getClass());
    
    private List extensionPoints;
    private String extensionBean;
    private IOperateTableExtensions operate;
    
    public void init()
    {
        if(extensionPoints!=null&& extensionPoints.size()>0)
        {
            for(int i=0;i<extensionPoints.size();i++)
            {
                if(extensionPoints.get(i) instanceof Integer)
                {
                    log.debug("registerExtensionPoint: "+extensionPoints.get(i));
                    boolean b=this.registerExtensionPoint((Integer)extensionPoints.get(i), extensionBean);
                    log.debug("registerExtensionPoint: "+b);
                }
                else
                {
                    log.warn("Not instanceof Integer");
                }
                
            }
        }
    }
    
    /**
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.tray.i.IExtensionsFromDB#findExtensions(java.lang.Integer, java.lang.String, java.lang.Class)
     */
    public List findExtensions(Integer extensionPoint, String extensionBean,
            Class  instanceSuper)
    {
        List retList=new ArrayList();
        
        Map map=new HashMap();
        map.put("extensionPoint", extensionPoint);
        map.put("extensionBean", extensionBean);
        
        List tmpL=operate.selectElementExtensionPoint(map);
        if(tmpL==null || tmpL.size()<1)
        {
            return retList;
        }
        
        ISpringBeanLoader sbl=ServiceAccess.getSpringService();
        
        for(int index=0;index<tmpL.size();index++)
        {
            if(tmpL.get(index) instanceof Map)
            {
                Map m=(Map)tmpL.get(index);
                String bean=(String)m.get("extensionBean");
                log.debug("find bean from DB:"+bean);
                
               Object o= sbl.getBean("bean");
               if(o!=null )//&& (o.isInstance instanceSuper)) 本意是要传一个Class的类名、比如JMenu
               {
                 
                   retList.add(o);//有效的、然后保存备用【保存的Bean的实例】       
                   log.debug("bean superclass is:"+o.getClass().getSuperclass().getName());
               }
               else
               {
                   log.warn(bean+" is not exist");
                   operate.deleteFromTableExtensions(map);//无效的
               }
            }
            else
            {
                log.warn("tmpL not instanceof Map");
            }
        }
        
        
        return retList;
    }

    /**
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.tray.i.IExtensionsFromDB#registerExtensionPoint(java.lang.Integer, java.lang.String)
     */
    public boolean registerExtensionPoint(Integer extensionPoint,
            String extensionBean)
    {
        Map map=new HashMap();
        map.put("extensionpoint", extensionPoint);
        map.put("extensionbean", extensionBean);
        
        
        if(!operate.existTableExtensions())//表是否存在？
        {
            log.debug("registerExtensionPoint table creating");
            operate.createTableExtensions();            
        }
        else
        {
            log.info("registerExtensionPoint table exist");
        }     
        
        
        if(operate.countElemntExtensionPoint(map)==0)//数据是否存在？
        {
            operate.insertIntoTableExtensions(map);
        }
        else
        {
            log.debug("aready exist "+extensionPoint+" "+extensionBean);
        }      
        
        return true;
    }

    public List findExtensions(Integer extensionPoint)
    {
        List retList=new ArrayList();
        
        Map map=new HashMap();
        map.put("extensionpoint", extensionPoint);
//        map.put("extensionBean", extensionBean);
        
        List tmpL=operate.selectElementExtensionPoint(map);
        if(tmpL==null || tmpL.size()<1)
        {
            return retList;
        }
               
        return tmpL;
    }

    public List getExtensionPoints()
    {
        return extensionPoints;
    }

    public void setExtensionPoints(List extensionPoints)
    {
        this.extensionPoints = extensionPoints;
    }

    public String getExtensionBean()
    {
        return extensionBean;
    }

    public void setExtensionBean(String extensionBean)
    {
        this.extensionBean = extensionBean;
    }

    public IOperateTableExtensions getOperate()
    {
        return operate;
    }

    public void setOperate(IOperateTableExtensions operate)
    {
        this.operate = operate;
    }

}
