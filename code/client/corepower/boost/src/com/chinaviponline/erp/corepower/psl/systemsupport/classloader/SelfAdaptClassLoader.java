/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.MappedByteBuffer;

/**
 * <p>�ļ����ƣ�SelfAdaptClassLoader.java</p>
 * <p>�ļ��������������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-9</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�    �汾�ţ�    �޸��ˣ�    �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
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
     * �ɹ���
     */
    private byte sharedBytes[];

    /**
     * ���ػ���
     */
    private LoaderCache cache;

    /**
     * ��ǰ�ļ�
     */
    private MappedByteBuffer currentFileBuffer;

    /**
     * �ܹ�����ʱ��
     */
    long erpClassTotalTime;

    /**
     * �������
     */
    int erpClassCount;

    /**
     * ϵͳ���ܹ�����ʱ��
     */
    long systemClassTotalTime;

    /**
     * ϵͳ�������
     */
    int systemClassCount;

    /**
     * �ܹ���ļ���ʱ��
     */
    long totalClassTime;        // ���Ե�ʱ���ʱ�Ƚϳ�,��int��Ϊlong

    /**
     * ���캯��
     * @param initURLs ·��
     * @param parent ���������
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
     * <p>�������������classpath</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-18</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
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
     * ������������ȡ��Դ
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
     * ��������������class
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
     * <p>��������������ʱ��</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-18</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
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
     * <p>����������ֹͣѹ��</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-18</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
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
     * <p>������������ʼѹ��</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-18</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     */
    public synchronized void requestStartOptimize()
    {
        cache = new LoaderCache();
        currentFileBuffer = cache.init();
    }

    /**
     * 
     * <p>��������������class</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-18</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param info ������Ϣ
     * @param name ������
     * @return ��
     * @throws ClassNotFoundException û���ҵ���
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
