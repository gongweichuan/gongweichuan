/**
 * TODO 增加版权信息
 */
package com.chinaviponline.corepower.derby.impl;

import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.chinaviponline.corepower.derby.i.ICreateSysPropsTable;
import com.chinaviponline.corepower.derby.i.IEmbeddedDerbyFrame;

/**
 * <p>文件名称：TEmbeddedDerbyFrame.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-22</p>
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
public class TEmbeddedDerbyFrame implements IEmbeddedDerbyFrame
{
    private Properties derbyProps;

    private static final Logger log=Logger.getLogger(TEmbeddedDerbyFrame.class);
    

    
    /**
     * 创建系统属性表
     */
    private String createSysProps;
    
    private ICreateSysPropsTable createSysPropsTable;
    
    public void init()
    {
        
        if(derbyProps!=null&&derbyProps.size()>0)
        {
            
            for (Iterator iterator = derbyProps.entrySet().iterator(); iterator.hasNext();)
            {
                Entry e=(Entry)iterator.next();
                String  n =(String) e.getKey();
                String  v =(String)e.getValue();
                
                System.getProperties().setProperty(n, v);
            }
        }
        
        createSysPropsTable.createSysPropsTable();//Call 1.创建系统属性表  
        
        Properties sysProps=System.getProperties();
        for (Iterator iterator = sysProps.entrySet().iterator(); iterator.hasNext();)
        {
            
            Entry eSys=(Entry)iterator.next();
            String  key =(String) eSys.getKey();
            String  value =(String)eSys.getValue();
            
           //将每条属性写入DB
            String n=key.length()>300?key.substring(key.length()-300):key;
            String v=value.length()>1000? value.substring(value.length()-1000):value;
            createSysPropsTable.insertNameValue2SysProsTable(n,v);
            log.debug(key+"="+value);
        }        
        
        //classpath表
    }

    public Properties getDerbyProps()
    {
        return derbyProps;
    }

    public void setDerbyProps(Properties derbyProps)
    {
        this.derbyProps = derbyProps;
    }

    public String getCreateSysProps()
    {
        return createSysProps;
    }

    public void setCreateSysProps(String createSysProps)
    {
        this.createSysProps = createSysProps;
    }


    public ICreateSysPropsTable getCreateSysPropsTable()
    {
        return createSysPropsTable;
    }

    public void setCreateSysPropsTable(ICreateSysPropsTable createSysPropsTable)
    {
        this.createSysPropsTable = createSysPropsTable;
    }    
    
}
