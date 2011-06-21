package com.chinaviponline.erp.corepower.ibatis;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.chinaviponline.erp.corepower.api.ServiceAccess;

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
    
    public static Resource[] getIbatisSqlmap(String arg)
    {
        log = Logger.getLogger(TIbatisSqlmapFactory.class);
        
        if(arg!=null)
        {
            mappingLocations=arg;
        }
        
        File iBATISBeanFiles[] = ServiceAccess.getSystemSupportService().getFiles(mappingLocations);
        
        //String[] iBATISBeanVar=new String[iBATISBeanFiles.length];
        Resource[] iBATISExtendRes=new Resource[iBATISBeanFiles.length];
        for (int i = 0; i < iBATISBeanFiles.length; i++)
        {
            //iBATISBeanVar[i]=iBATISBeanFiles[i].toString();
            iBATISExtendRes[i]=new FileSystemResource(iBATISBeanFiles[i]);
        }
                
        return iBATISExtendRes;
    }

}
