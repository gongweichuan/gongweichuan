package com.chinaviponline.erp.corepower.ibatis;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.psl.systemsupport.classloader.SelfAdaptClassLoader;


/**
 * <p>文件名称：TIbatisSqlmap.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2010-1-27</p>
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
public class TIbatisSqlmapFactory
{
    /**
     * 日志
     */
    private static  Logger log;
    
    /**
     * IBatis文件名的后缀
     */
    private static String mappingLocations="-sqlmap.xml";
    
    public static Resource[] getIbatisSqlmap(String mathStr)
    {
        log = Logger.getLogger(TIbatisSqlmapFactory.class);
        ClassLoader cl=Thread.currentThread().getContextClassLoader();
        if(cl instanceof URLClassLoader)
        {
            URLClassLoader urlCL=(URLClassLoader)cl;
            URL[] url=urlCL.getURLs();
            for (int i = 0; i < url.length; i++)
            {
                log.debug("url["+i+"]:"+url[i]);
            }            
        }else
        {
            log.error("cl is not URLClassLoader,is "+cl);
        }
      //========================================================================================
        
        if(mathStr!=null)
        {
            mappingLocations=mathStr;
        }
        
        File iBATISBeanFiles[] = ServiceAccess.getSystemSupportService().getFiles(mappingLocations);
        Resource[] iBATISExtendRes=new Resource[iBATISBeanFiles.length];
        for (int i = 0; i < iBATISBeanFiles.length; i++)
        {
            try
            {
                if(cl instanceof SelfAdaptClassLoader)
                {
                    SelfAdaptClassLoader urlWillAdd=(SelfAdaptClassLoader)cl;
                    urlWillAdd.addURLs(new URL[]{iBATISBeanFiles[i].getParentFile().toURI().toURL()});                    
                    log.debug("added["+i+"] is:"+iBATISBeanFiles[i].toURI().toURL());
                }                   
            }
            catch (MalformedURLException e)
            {
               log.error("iBATISBeanFiles[i].toURI().toURL() "+e.getMessage());
            }
            iBATISExtendRes[i]=new FileSystemResource(iBATISBeanFiles[i]);
        }
        //===============================================================================
        {//把ibatis的xml加入到classpath中去
            log.info("when done,the URLClassLoader:");
            ClassLoader clAdded=Thread.currentThread().getContextClassLoader();
            URLClassLoader urlCL=(URLClassLoader)clAdded;
            URL[] urlAdded=urlCL.getURLs();
            for (int i = 0; i < urlAdded.length; i++)
            {
                log.debug("url["+i+"]:"+urlAdded[i]);
            }   
        }
        
        return iBATISExtendRes;
    }

}
