/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.helloworld.coolsql.impl;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

import com.chinaviponline.erp.corepower.helloworld.coolsql.i.ICoolSql;

/**
 * <p>文件名称：TCoolSqlImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-11-13</p>
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
public class TCoolSqlImpl implements ICoolSql
{
    private String className;
    
    private URLClassLoader newClassLoader;

    private final Logger log=Logger.getLogger(this.getClass());
    /**
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.helloworld.coolsql.i.ICoolSql#start()
     */
    @Override
    public boolean start()
    {
        if(className==null)
        {
            log.warn("className is "+className);
        }
        log.debug("will load className is "+className);
        ClassLoader p1 = Thread.currentThread().getContextClassLoader();
        
        // 这里可以设置新ClassLoader        
        Class bootClass;
        try
        {
            if(newClassLoader==null)
            {
                bootClass = p1.loadClass(className);                
            }else
            {
                Thread.currentThread().setContextClassLoader(newClassLoader);
                bootClass=newClassLoader.loadClass(className);
            }
            Method mainMethod = bootClass.getMethod("main",
                    new Class[] {java.lang.String[].class});
            String args[]=new String[1];
            args[0]="test";
            mainMethod.invoke((Object)null, new Object[] {args});
            return true;
        }
        catch (ClassNotFoundException e)
        {
           log.warn("unable to load class "+className,e);
           return false;
        }
        catch (SecurityException e)
        {
            log.warn("unable to load class "+className,e);
            return false;
        }
        catch (NoSuchMethodException e)
        {
            log.warn("unable to load class "+className,e);
            return false;
        }
        catch (IllegalArgumentException e)
        {
            log.warn("unable to load class "+className,e);
            return false;
        }
        catch (IllegalAccessException e)
        {
            log.warn("unable to load class "+className,e);
            return false;
        }
        catch (InvocationTargetException e)
        {
            log.warn("unable to load class "+className,e);
            return false;
        }
    }

    /**
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.helloworld.coolsql.i.ICoolSql#stop()
     */
    @Override
    public boolean stop()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public URLClassLoader getNewClassLoader()
    {
        return newClassLoader;
    }

    public void setNewClassLoader(URLClassLoader newClassLoader)
    {
        this.newClassLoader = newClassLoader;
    }

}
