package com.chinaviponline.erp.corepower.spring;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;

/**
 * <p>文件名称：TSpringBeanLoaderImpl.java</p>
 * <p>文件描述：spring插件工具</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公   司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2010-1-24</p>
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
public  class  TSpringBeanLoaderImpl implements ISpringBeanLoader
{
    /**
     * 日志
     */
    private static final Logger log;

    /**
     * Spring的上下文
     */
    private static final ApplicationContext ctx;
    
    /**
     * 初始化Spring
     * 加载所有的*-springbean.xml
     */ 
    static
    {
        log = Logger.getLogger(TSpringBeanLoaderImpl.class);
        File springBeanFiles[] = ServiceAccess.getSystemSupportService().getFiles(ALLSPRINGBEAN);
        
        String[] springBeanVar=new String[springBeanFiles.length];
        for (int i = 0; i < springBeanFiles.length; i++)
        {
            springBeanVar[i]=springBeanFiles[i].toString();
        }
        
        //加载Spring
        if(springBeanVar.length>0)
        {
            ctx=new FileSystemXmlApplicationContext(springBeanVar);
        }
        else
        {
            log.warn("在当前目录中没有找到"+ALLSPRINGBEAN);
            String tmpStr="classpath*:*"+ALLSPRINGBEAN;
            log.info("开始在classpath中搜索"+tmpStr);
            ctx=new FileSystemXmlApplicationContext(tmpStr);
        }
        
        log.debug("Loaded Spring\r\n"+ctx);
    }

    /**
     * 
     * 功能描述：获得Sping上下文
     * @see com.chinaviponline.erp.corepower.spring.api.ISpringBeanLoader#getApplicationContext()
     */
    public  ApplicationContext getApplicationContext()
    {        
        return ctx;
    }

    /**
     * 
     * 功能描述：从Spring上下文中取bean
     * @see com.chinaviponline.erp.corepower.spring.api.ISpringBeanLoader#getBean()
     */
    public Object getBean(String beanName)
    {       
        return ctx.getBean(beanName);
    }

    /**
     * 
     * 功能描述：获得资源
     * @see com.chinaviponline.erp.corepower.spring.api.ISpringBeanLoader#getResource(java.lang.String)
     */
    public Resource getResource(String resourceName)
    {
        return ctx.getResource(resourceName);
    }
}
