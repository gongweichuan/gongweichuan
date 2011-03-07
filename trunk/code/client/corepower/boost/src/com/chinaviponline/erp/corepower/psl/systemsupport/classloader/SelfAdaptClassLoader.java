/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.MappedByteBuffer;

/**
 * <p>文件名称：SelfAdaptClassLoader.java</p>
 * <p>文件描述：类加载器</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-9</p>
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
public class SelfAdaptClassLoader extends URLClassLoader
{

    /**
     * 100
     */
    private static final int ONE_HUNDRED = 100;

    /**
     * 10000
     */
    private static final int TEN_THOUSAND = 10000;

    /**
     * 可公用
     */
    private byte sharedBytes[];

    /**
     * 加载缓存
     */
    private LoaderCache cache;

    /**
     * 当前文件
     */
    private MappedByteBuffer currentFileBuffer;

    /**
     * 总共加载时间
     */
    long erpClassTotalTime;

    /**
     * 类的总数
     */
    int erpClassCount;

    /**
     * 系统类总共加载时间
     */
    long systemClassTotalTime;

    /**
     * 系统类的总数
     */
    int systemClassCount;

    /**
     * 总共类的加载时间
     */
    long totalClassTime;        // 调试的时候耗时比较长,从int改为long

    /**
     * 构造函数
     * @param initURLs 路径
     * @param parent 父类加载器
     */
    public SelfAdaptClassLoader(URL initURLs[], ClassLoader parent)
    {
        super(initURLs, parent);

        sharedBytes = new byte[10000];
        cache = new LoaderCache();
        currentFileBuffer = null;
        erpClassTotalTime = 0L;
        erpClassCount = 0;
        systemClassTotalTime = 0L;
        systemClassCount = 0;
        totalClassTime = 0;
        currentFileBuffer = cache.init();
    }

    /**
     * 
     * <p>功能描述：添加classpath</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param urls
     */
    public void addURLs(URL urls[])
    {
        for (int i = 0; i < urls.length; i++)
        {
            super.addURL(urls[i]);
        }
    }

    /**
     * 
     * 功能描述：获取资源
     * @see java.lang.ClassLoader#getResource(java.lang.String)
     */
    public synchronized URL getResource(String name)
    {
        if (cache != null && !name.endsWith(".class"))
        {
            URL cachedURL = cache.getResource(name);

            if (cachedURL != null)
            {
                return cachedURL;
            }

            cachedURL = super.getResource(name);
            if (cachedURL != null)
            {
                cache.addFoundResourceURL(name, cachedURL);
            }

            return cachedURL;
        }
        else
        {
            return super.getResource(name);
        }
    }

    /**
     * 
     * 功能描述：加载class
     * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
     */
    protected synchronized Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        long tt1 = System.currentTimeMillis();
        Class c = findLoadedClass(name);
        if (c == null)
        {
            if (cache != null)
            {
                ClassCacheInfo classInfo = cache.getClassCacheInfo(name);
                if (classInfo == null)
                {
                    long t1 = System.currentTimeMillis();
                    c = super.loadClass(name, false);
                    long t2 = System.currentTimeMillis();
                    ClassLoader usedClassLoader = c.getClassLoader();
                    if (usedClassLoader == this)
                    {
                        cache.addNewFoundClass(c);
                    }
                    else
                    {
                        systemClassCount++;
                        systemClassTotalTime += t2 - t1;
                    }
                }
                else
                {
                    erpClassCount++;
                    long t1 = System.currentTimeMillis();
                    c = findClass(classInfo, name);
                    long t2 = System.currentTimeMillis();
                    erpClassTotalTime += t2 - t1;
                }
            }
            else
            {
                c = super.loadClass(name, false);
            }
        }

        if (resolve)
        {
            resolveClass(c);
        }

        long tt2 = System.currentTimeMillis();

        if (cache != null)
        {
            totalClassTime += tt2 - tt1;
        }

        return c;
    }

    /**
     * 
     * <p>功能描述：启动时间</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    public synchronized void requestStartupTimeOver()
    {
        if (cache != null)
        {
            cache.requestStartupTimeOver();
            cache = null;
            currentFileBuffer = null;
        }
    }

    /**
     * 
     * <p>功能描述：停止压缩</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    public synchronized void requestStopOptimize()
    {
        if (cache != null)
        {
            cache.requestStopOptimize();
            cache = null;
            currentFileBuffer = null;
        }
        try
        {
            System.gc();
            Thread.sleep(50L);
            System.gc();
        }
        catch (Throwable ignore)
        {
        }
    }

    /**
     * 
     * <p>功能描述：开始压缩</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    public synchronized void requestStartOptimize()
    {
        cache = new LoaderCache();
        currentFileBuffer = cache.init();
    }

    /**
     * 
     * <p>功能描述：查找class</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param info 缓存信息
     * @param name 类名称
     * @return 类
     * @throws ClassNotFoundException 没有找到类
     */
    private Class findClass(ClassCacheInfo info, String name)
            throws ClassNotFoundException
    {
        int classOffset = info.offset;
        int classLength = info.len;

        if (sharedBytes.length < classLength)
        {
            sharedBytes = new byte[classLength + 100];
        }

        for (int i = 0; i < classLength; i++)
        {
            sharedBytes[i] = currentFileBuffer.get(classOffset + i);
        }

        return defineClass(name, sharedBytes, 0, classLength);
    }
}
