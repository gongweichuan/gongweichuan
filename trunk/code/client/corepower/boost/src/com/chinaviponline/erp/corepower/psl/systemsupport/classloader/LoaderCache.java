/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.classloader;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.zip.*;

/**
 * <p>文件名称：LoaderCache.java</p>
 * <p>文件描述：加载器缓存</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-18</p>
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
public final class LoaderCache
{

    /**
     * 幻数
     */
    private static final byte MAGIC = -1;

    /**
     * 版本
     */
    private static final byte VERSION = 1;

    /**
     * 50
     */
    private static final int FIFTY = 50;

    /**
     * 500
     */
    private static final int FIVE_HUNDRED = 500;

    /**
     * 3000
     */
    private static final int THREE_THOUSAND = 3000;

    /**
     * 类文件1
     */
    private final File class1File = (new File("temp/cls1.bin"))
            .getAbsoluteFile();

    /**
     * 类文件2
     */
    private final File class2File = (new File("temp/cls2.bin"))
            .getAbsoluteFile();

    /**
     * 缓存Jar信息
     */
    private final HashMap cachedJarInfoMap = new HashMap(50);

    /**
     * 缓存类信息
     */
    private final HashMap cachedClassInfoMap = new HashMap(3000);

    /**
     * 缓存的资源文件
     */
    private final HashMap cachedResourceFilesMap = new HashMap(500);

    /**
     * 新找到的类
     */
    private final LinkedList newFoundClasses = new LinkedList();

    /**
     * 缓存的URL
     */
    private final HashMap cachedURLsMap = new HashMap(500);

    /**
     * 新找到的资源URL
     */
    private final HashMap newFoundResourceURLsMap = new HashMap();

    /**
     * 类二进制文件
     */
    private File classBinFile;

    /**
     * 当前文件通道
     */
    private FileChannel currentFileChannel;

    /**
     * 当前文件缓冲流
     */
    private MappedByteBuffer currentFileBuffer;

    /**
     * 构造函数
     *
     */
    LoaderCache()
    {
        classBinFile = class1File;
        currentFileChannel = null;
        currentFileBuffer = null;
    }

    /**
     * 
     * <p>功能描述：初始化</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    MappedByteBuffer init()
    {
	 this.classBinFile = ((this.class1File.exists()) ? this.class1File
				: (this.class2File.exists()) ? this.class2File
						: (this.class1File.lastModified() > this.class2File
								.lastModified()) ? this.class1File
								: this.class2File);

		if (this.classBinFile.exists()) {
			try {
				this.currentFileChannel = new RandomAccessFile(
						this.classBinFile, "r").getChannel();
				long actualFileSize = this.currentFileChannel.size();
				this.currentFileBuffer = this.currentFileChannel.map(
						FileChannel.MapMode.READ_ONLY, 0L, actualFileSize);
				if ((this.currentFileBuffer.remaining() > 10)
						&& (this.currentFileBuffer.get() == -1)
						&& (this.currentFileBuffer.get() == 1)) {
					long savedFileSize = this.currentFileBuffer.getLong();

					if (actualFileSize == savedFileSize)
						readCacheHeadInfo();
				}
			} catch (IOException ex) {
				this.cachedJarInfoMap.clear();
				this.cachedClassInfoMap.clear();
				this.cachedResourceFilesMap.clear();
			}
		}
		if (this.cachedClassInfoMap.size() == 0) {
			closeFileChannel(this.currentFileChannel);
			this.currentFileChannel = null;
		}
		return this.currentFileBuffer;
    	
    	/*
		 * //TODO 理清逻辑 优先级 classBinFile = class1File.exists() ?
		 * class2File.exists() ? class1File .lastModified() <=
		 * class2File.lastModified() ? class2File : class1File : class1File :
		 * class2File;
		 * 
		 * if (classBinFile.exists()) { try { currentFileChannel = (new
		 * RandomAccessFile(classBinFile, "r")) .getChannel(); long
		 * actualFileSize = currentFileChannel.size();
		 * 
		 * currentFileBuffer = currentFileChannel.map(
		 * java.nio.channels.FileChannel.MapMode.READ_ONLY, 0L, actualFileSize);
		 * 
		 * if (currentFileBuffer.remaining() > 10 && currentFileBuffer.get() ==
		 * -1 && currentFileBuffer.get() == 1) { long savedFileSize =
		 * currentFileBuffer.getLong(); if (actualFileSize == savedFileSize) {
		 * readCacheHeadInfo(); } } } catch (IOException ex) {
		 * cachedJarInfoMap.clear(); cachedClassInfoMap.clear();
		 * cachedResourceFilesMap.clear(); } }
		 * 
		 * 
		 * if (cachedClassInfoMap.size() == 0) {
		 * closeFileChannel(currentFileChannel); currentFileChannel = null; }
		 * 
		 * return currentFileBuffer;
		 */
    	
    }

    /**
     * 
     * <p>功能描述：获取资源</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @return
     */
    URL getResource(String name)
    {
        URL cachedURL = (URL) cachedURLsMap.get(name);

        if (cachedURL != null)
        {
            return cachedURL;
        }

        String fileName = (String) cachedResourceFilesMap.get(name);
        if (fileName != null)
        {
            File resourceFile = new File(fileName);
            if (resourceFile.exists())
            {
                try
                {
                    cachedURL = resourceFile.toURL();
                }
                catch (Exception ignore)
                {
                }
            }
        }
        
        if (cachedURL != null)
        {
            cachedURLsMap.put(name, cachedURL);
            return cachedURL;
        }
        else
        {
            cachedURL = (URL) newFoundResourceURLsMap.get(name);
            return cachedURL;
        }
    }

    /**
     * 
     * <p>功能描述：田间新找到的资源URL</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @param newResourceURL
     */
    void addFoundResourceURL(String name, URL newResourceURL)
    {
        String path = newResourceURL.getPath();
        if (path.indexOf(".jar") > 0 && !path.endsWith(".jar"))
        {
            synchronized (this)
            {
                newFoundResourceURLsMap.put(name, newResourceURL);
            }
        }
    }

    /**
     * 
     * <p>功能描述：获得缓存类信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @return
     */
    ClassCacheInfo getClassCacheInfo(String name)
    {
        return (ClassCacheInfo) cachedClassInfoMap.get(name);
    }

    /**
     * 
     * <p>功能描述：增加新找到的类</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newClass
     */
    void addNewFoundClass(Class newClass)
    {
        newFoundClasses.add(newClass);
    }

    /**
     * 
     * <p>功能描述：开始时间</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    void requestStartupTimeOver()
    {
        cachedClassInfoMap.clear();
        cachedResourceFilesMap.clear();
        cachedURLsMap.clear();
        if (newFoundClasses.size() > 0 || newFoundResourceURLsMap.size() > 0)
        {
            Thread backThread = new Thread(new BackRunnable());
            backThread.start();
        }
        else
        {
            closeFileChannel(currentFileChannel);
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
    void requestStopOptimize()
    {
        cachedClassInfoMap.clear();
        cachedResourceFilesMap.clear();
        cachedURLsMap.clear();
        newFoundClasses.clear();
        newFoundResourceURLsMap.clear();
        if (currentFileChannel != null)
        {
            closeFileChannel(currentFileChannel);
        }

        currentFileBuffer = null;
    }

    /**
     * 
     * <p>功能描述：读取缓存头部信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void readCacheHeadInfo()
    {
        byte sharedBytes[] = new byte[10000];
        int jarCount = currentFileBuffer.getShort();
        for (int i = 0; i < jarCount; i++)
        {
            int jarBeginPosition = currentFileBuffer.position();
            int jarPathLength = currentFileBuffer.getShort();
            currentFileBuffer.get(sharedBytes, 0, jarPathLength);
            String jarPath = new String(sharedBytes, 0, jarPathLength);
            long savedTimestamp = currentFileBuffer.getLong();
            File jarFile = new File(jarPath);
            boolean jarFileValid = jarFile.exists()
                    && jarFile.lastModified() == savedTimestamp;
            int classCount = currentFileBuffer.getShort();
            int resourceCount = currentFileBuffer.getShort();
            JarCacheInfo jarInfo = new JarCacheInfo(jarFile, classCount,
                    resourceCount);
            int classByteLengthOfJar = 0;
            
            for (int j = 0; j < classCount; j++)
            {
                int classNameLength = currentFileBuffer.getShort();
                currentFileBuffer.get(sharedBytes, 0, classNameLength);
                String className = new String(sharedBytes, 0, classNameLength);
                int classOffset = currentFileBuffer.getInt();
                int classByteLength = currentFileBuffer.getInt();
                if (jarFileValid)
                {
                    ClassCacheInfo classInfo = new ClassCacheInfo(className,
                            classOffset, classByteLength);
                    cachedClassInfoMap.put(classInfo.className, classInfo);
                    classByteLengthOfJar += classByteLength;
                    jarInfo.addClassInfo(classInfo);
                }
            }

            for (int j = 0; j < resourceCount; j++)
            {
                int keyTextLength = currentFileBuffer.getShort();
                currentFileBuffer.get(sharedBytes, 0, keyTextLength);
                String key = new String(sharedBytes, 0, keyTextLength);
                int valueTextLength = currentFileBuffer.getShort();
                currentFileBuffer.get(sharedBytes, 0, valueTextLength);
                String value = new String(sharedBytes, 0, valueTextLength);

                if (jarFileValid)
                {
                    cachedResourceFilesMap.put(key, value);
                    jarInfo.putResourceInfo(key, value);
                }
            }

            if (jarFileValid)
            {
                jarInfo.headLength = currentFileBuffer.position()
                        - jarBeginPosition;
                jarInfo.classByteLengthOfJar = classByteLengthOfJar;
                cachedJarInfoMap.put(jarFile, jarInfo);
            }
        }

    }

    /**
     * 
     * <p>功能描述：新的资源URL</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    private HashMap tidyNewResourceURLs()
    {
        HashMap resourceFilesMap;
        Iterator it;
        LinkedList newResources;
        File jarFile;
        HashMap resourcePairMap;
        BufferedInputStream bis;

        resourceFilesMap = new HashMap();
        HashMap urlMap = new HashMap();

        synchronized (this)
        {
            URL nextURL;
            LinkedList list;
            for (it = newFoundResourceURLsMap.values().iterator(); it.hasNext(); list
                    .add(nextURL))
            {
                nextURL = (URL) it.next();
                jarFile = ascendJarFile(nextURL);
                list = (LinkedList) urlMap.get(jarFile);
                if (list == null)
                {
                    list = new LinkedList();
                    urlMap.put(jarFile, list);
                }
            }

        }
        
        it = urlMap.entrySet().iterator();

        if (!it.hasNext())
        {
            return null;
        }

        try
        {

            java.util.Map.Entry nextEntry = (java.util.Map.Entry) it.next();
            jarFile = (File) nextEntry.getKey();
            newResources = (LinkedList) nextEntry.getValue();
            resourcePairMap = new HashMap();
            resourceFilesMap.put(jarFile, resourcePairMap);
            bis = null;
            ZipInputStream zipIn = new ZipInputStream(new CheckedInputStream(
                    new FileInputStream(jarFile), new Adler32()));
            bis = new BufferedInputStream(zipIn);
            ZipEntry nextZipEntry = null;

            do
            {
                if ((nextZipEntry = zipIn.getNextEntry()) == null)
                {
                    break;
                }

                if (nextZipEntry.isDirectory())
                {
                    while (bis.read() != -1)
                        ;
                    continue;
                }

                String zipName = nextZipEntry.getName();

                if (!zipName.endsWith(".class"))
                {
                    URL theURL = null;
                    Iterator itr = newResources.iterator();

                    do
                    {
                        if (!itr.hasNext())
                        {
                            break;
                        }

                        Object theNext = itr.next();
                        if (!(theNext instanceof URL))
                        {
                            continue;
                        }

                        URL nextURL = (URL) theNext;
                        String fullPath = nextURL.getFile();
                        if (!fullPath.endsWith(zipName))
                        {
                            continue;
                        }

                        theURL = nextURL;
                        break; //TODO 是否有问题
                    }
                    while (true);

                    if (theURL != null)
                    {
                        File resourceFile = (new File(
                                System.getProperty("user.dir")
                                        + "/deploy/runtime/platform/corepower-common-runtime/res/"
                                        + nextZipEntry.getName()))
                                .getAbsoluteFile();

                        resourceFile.getParentFile().mkdirs();
                        if (!resourceFile.exists())
                        {
                            resourceFile.createNewFile();
                        }

                        FileOutputStream fos = null;
                        try
                        {
                            fos = new FileOutputStream(resourceFile);
                            for (int c = bis.read(); c != -1; c = bis.read())
                            {
                                fos.write(c);
                            }

                            resourcePairMap.put(zipName, resourceFile
                                    .getAbsolutePath());
                        }
                        finally
                        {
                            if (fos != null)
                            {
                                try
                                {
                                    fos.close();
                                }
                                catch (IOException ignore)
                                {
                                }
                            }
                        }
                    }
                    else
                    {
                        while (bis.read() != -1)
                            ;
                    }
                    continue;
                }
                while (bis.read() != -1)
                    ;
            }
            while (true);

            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException ignore)
                {
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            it.remove();
        }

        return resourceFilesMap;
    }

    /**
     * 
     * <p>功能描述：新的类信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    private HashMap tidyNewClassInfos()
    {
        HashMap newClassInfosMap;
        Iterator it;
        File jarFile;
        LinkedList newClassInfos;
        BufferedInputStream bis;
        java.util.Map.Entry nextEntry;

        newClassInfosMap = new HashMap();

        synchronized (this)
        {
            NewClassInfo newInfo;
            LinkedList list;
            for (it = newFoundClasses.iterator(); it.hasNext(); list
                    .add(newInfo))
            {
                Class nextClass = (Class) it.next();
                jarFile = ascendJarFile(nextClass);
                newInfo = new NewClassInfo(jarFile, nextClass);
                list = (LinkedList) newClassInfosMap.get(jarFile);
                if (list == null)
                {
                    list = new LinkedList();
                    newClassInfosMap.put(jarFile, list);
                }
            }

        }
        it = newClassInfosMap.entrySet().iterator();
        //_L2:

        if (!it.hasNext())
        {
            return null;
        }

        try
        {
            nextEntry = (java.util.Map.Entry) it.next();
            jarFile = (File) nextEntry.getKey();
            newClassInfos = (LinkedList) nextEntry.getValue();
            bis = null;
            ZipInputStream zipIn = new ZipInputStream(new CheckedInputStream(
                    new FileInputStream(jarFile), new Adler32()));
            bis = new BufferedInputStream(zipIn);
            ZipEntry nextZipEntry = null;

            do
            {
                if ((nextZipEntry = zipIn.getNextEntry()) == null)
                {
                    break;
                }

                if (nextZipEntry.isDirectory())
                {
                    while (bis.read() != -1)
                        ;
                    continue;
                }

                String zipName = nextZipEntry.getName();
                if (zipName.endsWith(".class"))
                {
                    NewClassInfo zipNewClassInfo = null;
                    String className = zipName.replace('/', '.');
                    className = className.substring(0, className
                            .lastIndexOf('.'));
                    Iterator itr = newClassInfos.iterator();
                    do
                    {
                        if (!itr.hasNext())
                        {
                            break;
                        }

                        NewClassInfo newInfo = (NewClassInfo) itr.next();

                        if (!className.equals(newInfo.theClass.getName()))
                        {
                            continue;
                        }

                        zipNewClassInfo = newInfo;
                        break;
                    }
                    while (true);

                    if (zipNewClassInfo != null)
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        for (int c = bis.read(); c != -1; c = bis.read())
                        {
                            baos.write(c);
                        }

                        ByteBuffer classByteBuffer = ByteBuffer.wrap(baos
                                .toByteArray());
                        zipNewClassInfo.byteBuffer = classByteBuffer;
                    }
                    else
                    {
                        while (bis.read() != -1)
                            ;
                    }
                    continue;
                }
                while (bis.read() != -1)
                    ;
            }
            while (true);

            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException ignore)
                {
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            it.remove();
        }

        for (it = newClassInfosMap.entrySet().iterator(); it.hasNext();)
        {
            nextEntry = (java.util.Map.Entry) it.next();
            jarFile = (File) nextEntry.getKey();
            newClassInfos = (LinkedList) nextEntry.getValue();
            Iterator itr = newClassInfos.iterator();
            while (itr.hasNext())
            {
                NewClassInfo newInfo = (NewClassInfo) itr.next();
                if (newInfo.byteBuffer == null)
                {
                    System.err.println("The '" + newInfo.theClass.getName()
                            + "' is not in " + jarFile);
                    itr.remove();
                }
            }
        }

        return newClassInfosMap;

    }

    /**
     * 
     * <p>功能描述：获取jar文件的绝对路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newClass
     * @return
     */
    private static File ascendJarFile(Class newClass)
    {
        String jarPath = newClass.getProtectionDomain().getCodeSource()
                .getLocation().getFile();
        return (new File(jarPath)).getAbsoluteFile();
    }

    /**
     * 
     * <p>功能描述：获取jar文件的绝对路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newResourceURL
     * @return
     */
    private static File ascendJarFile(URL newResourceURL)
    {
        String fullPath = newResourceURL.getPath();
        int protocolSeparatorIndex = fullPath.indexOf('/');
        int dotJarIndex = fullPath.indexOf(".jar");
        String jarPath = fullPath.substring(protocolSeparatorIndex,
                dotJarIndex + 4);
        return (new File(jarPath)).getAbsoluteFile();
    }

    /**
     * 
     * <p>功能描述：计算文件头的大小</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newClassInfosMap
     * @param resourceFilesMap
     * @return
     */
    private int calculateHeadByteCount(HashMap newClassInfosMap,
            HashMap resourceFilesMap)
    {
        Set oldJarFileSet = new HashSet(cachedJarInfoMap.size());
        int headByteCount = 12;
        JarCacheInfo nextJarByteInfo;
        for (Iterator it = cachedJarInfoMap.values().iterator(); it.hasNext(); oldJarFileSet
                .add(nextJarByteInfo.jarFile))
        {
            nextJarByteInfo = (JarCacheInfo) it.next();
            headByteCount += nextJarByteInfo.headLength;
        }

        Set newJarSet = new HashSet(newClassInfosMap.size()
                + resourceFilesMap.size());
        newJarSet.addAll(newClassInfosMap.keySet());
        newJarSet.addAll(resourceFilesMap.keySet());
        Iterator it = newJarSet.iterator();
        do
        {
            if (!it.hasNext())
                break;
            File nextNewJarFile = (File) it.next();
            LinkedList newJarInfos = (LinkedList) newClassInfosMap
                    .get(nextNewJarFile);
            HashMap resourceInfoMap = (HashMap) resourceFilesMap
                    .get(nextNewJarFile);
            if (!oldJarFileSet.contains(nextNewJarFile))
                headByteCount += nextNewJarFile.getAbsolutePath().getBytes().length + 14;
            if (newJarInfos != null)
            {
                for (Iterator itr = newJarInfos.iterator(); itr.hasNext();)
                {
                    NewClassInfo nextNewClassInfo = (NewClassInfo) itr.next();
                    headByteCount += nextNewClassInfo.theClass.getName()
                            .getBytes().length + 10;
                }

            }
            if (resourceInfoMap != null)
            {
                Iterator itr = resourceInfoMap.entrySet().iterator();
                while (itr.hasNext())
                {
                    java.util.Map.Entry pair = (java.util.Map.Entry) itr.next();
                    String resourceName = (String) pair.getKey();
                    String resourcePathName = (String) pair.getValue();
                    headByteCount += 4 + resourceName.getBytes().length
                            + resourcePathName.getBytes().length;
                }
            }
        }
        while (true);
        return headByteCount;
    }

    /**
     * 
     * <p>功能描述：计算文件体的大小</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newClassInfosMap
     * @return
     */
    private int calculateBodyByteCount(HashMap newClassInfosMap)
    {
        int bodyByteCount = 0;
        for (Iterator it = cachedJarInfoMap.values().iterator(); it.hasNext();)
        {
            JarCacheInfo nextJarByteInfo = (JarCacheInfo) it.next();
            bodyByteCount += nextJarByteInfo.classByteLengthOfJar;
        }

        for (Iterator it = newClassInfosMap.values().iterator(); it.hasNext();)
        {
            LinkedList newJarInfos = (LinkedList) it.next();
            Iterator itr = newJarInfos.iterator();
            while (itr.hasNext())
            {
                NewClassInfo nextNewClassInfo = (NewClassInfo) itr.next();
                bodyByteCount += nextNewClassInfo.byteBuffer.remaining();
            }
        }

        return bodyByteCount;
    }

    /***
     * 
     * <p>功能描述：归并</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newHeadBuffer
     * @param newBodyBuffer
     * @param newClassInfosMap
     * @param resourceFilesMap
     */
    private void merge(ByteBuffer newHeadBuffer, ByteBuffer newBodyBuffer,
            HashMap newClassInfosMap, HashMap resourceFilesMap)
    {
        HashMap resourceInfoMap;
        for (Iterator it = cachedJarInfoMap.values().iterator(); it.hasNext(); appendResourceInfos(
                newHeadBuffer, resourceInfoMap))
        {
            JarCacheInfo nextJarByteInfo = (JarCacheInfo) it.next();
            byte jarPathBytes[] = nextJarByteInfo.jarFile.getAbsolutePath()
                    .getBytes();
            newHeadBuffer.putShort((short) jarPathBytes.length);
            newHeadBuffer.put(jarPathBytes);
            newHeadBuffer.putLong(nextJarByteInfo.jarFile.lastModified());
            LinkedList newClassInfos = (LinkedList) newClassInfosMap
                    .remove(nextJarByteInfo.jarFile);
            if (newClassInfos != null)
                newHeadBuffer
                        .putShort((short) (nextJarByteInfo.classCount + newClassInfos
                                .size()));
            else
                newHeadBuffer.putShort((short) nextJarByteInfo.classCount);
            resourceInfoMap = nextJarByteInfo.resourceInfoMap;
            HashMap newResourceInfoMap = (HashMap) resourceFilesMap
                    .remove(nextJarByteInfo.jarFile);
            if (newResourceInfoMap != null)
                resourceInfoMap.putAll(newResourceInfoMap);
            newHeadBuffer.putShort((short) resourceInfoMap.size());
            for (int i = 0; i < nextJarByteInfo.classCount; i++)
            {
                ClassCacheInfo classInfo = (ClassCacheInfo) nextJarByteInfo.classByteInfos
                        .get(i);
                byte classNameBytes[] = classInfo.className.getBytes();
                newHeadBuffer.putShort((short) classNameBytes.length);
                newHeadBuffer.put(classNameBytes);
                int classOffset = newHeadBuffer.limit()
                        + newBodyBuffer.position();
                newHeadBuffer.putInt(classOffset);
                newHeadBuffer.putInt(classInfo.len);
                for (int j = 0; j < classInfo.len; j++)
                    newBodyBuffer.put(currentFileBuffer.get(classInfo.offset
                            + j));

            }

            if (newClassInfos == null)
                continue;
            NewClassInfo newClassInfo;
            for (Iterator itr = newClassInfos.iterator(); itr.hasNext(); appendNewFile(
                    newHeadBuffer, newBodyBuffer, newClassInfo))
                newClassInfo = (NewClassInfo) itr.next();

        }

    }

    /**
     * 
     * <p>功能描述：在新jar文件上追加</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newHeadBuffer
     * @param newBodyBuffer
     * @param newClassInfosMap
     * @param resourceFilesMap
     */
    private void appendNewJars(ByteBuffer newHeadBuffer,
            ByteBuffer newBodyBuffer, HashMap newClassInfosMap,
            HashMap resourceFilesMap)
    {
        Set newJarSet = new HashSet(newClassInfosMap.size()
                + resourceFilesMap.size());
        newJarSet.addAll(newClassInfosMap.keySet());
        newJarSet.addAll(resourceFilesMap.keySet());
        HashMap resourceInfoMap;
        for (Iterator it = newJarSet.iterator(); it.hasNext(); appendResourceInfos(
                newHeadBuffer, resourceInfoMap))
        {
            File nextNewJarFile = (File) it.next();
            LinkedList newJarInfos = (LinkedList) newClassInfosMap
                    .get(nextNewJarFile);
            newJarInfos = newJarInfos != null ? newJarInfos : new LinkedList();
            resourceInfoMap = (HashMap) resourceFilesMap.get(nextNewJarFile);
            resourceInfoMap = resourceInfoMap != null ? resourceInfoMap
                    : new HashMap();
            byte jarPathBytes[] = nextNewJarFile.getAbsolutePath().getBytes();
            newHeadBuffer.putShort((short) jarPathBytes.length);
            newHeadBuffer.put(jarPathBytes);
            newHeadBuffer.putLong(nextNewJarFile.lastModified());
            newHeadBuffer.putShort((short) newJarInfos.size());
            newHeadBuffer.putShort((short) resourceInfoMap.size());
            NewClassInfo nextNewClassInfo;
            for (Iterator itr = newJarInfos.iterator(); itr.hasNext(); appendNewFile(
                    newHeadBuffer, newBodyBuffer, nextNewClassInfo))
                nextNewClassInfo = (NewClassInfo) itr.next();

        }

    }

    /**
     * 
     * <p>功能描述：在新class文件上追加</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newHeadBuffer
     * @param newBodyBuffer
     * @param newInfo
     */
    private void appendNewFile(ByteBuffer newHeadBuffer,
            ByteBuffer newBodyBuffer, NewClassInfo newInfo)
    {
        byte classNameBytes[] = newInfo.theClass.getName().getBytes();
        newHeadBuffer.putShort((short) classNameBytes.length);
        newHeadBuffer.put(classNameBytes);
        int classOffset = newHeadBuffer.limit() + newBodyBuffer.position();
        newHeadBuffer.putInt(classOffset);
        newHeadBuffer.putInt(newInfo.byteBuffer.remaining());
        newBodyBuffer.put(newInfo.byteBuffer);
    }

    /**
     * 
     * <p>功能描述：在文件按上追加资源信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param newHeadBuffer
     * @param resourceFilesMap
     */
    private void appendResourceInfos(ByteBuffer newHeadBuffer,
            HashMap resourceFilesMap)
    {
        byte resourceFileNameBytes[];
        for (Iterator it = resourceFilesMap.entrySet().iterator(); it.hasNext(); newHeadBuffer
                .put(resourceFileNameBytes))
        {
            java.util.Map.Entry pair = (java.util.Map.Entry) it.next();
            String resourceName = (String) pair.getKey();
            byte resourceNameBytes[] = resourceName.getBytes();
            newHeadBuffer.putShort((short) resourceNameBytes.length);
            newHeadBuffer.put(resourceNameBytes);
            String resourceFileName = (String) pair.getValue();
            resourceFileNameBytes = resourceFileName.getBytes();
            newHeadBuffer.putShort((short) resourceFileNameBytes.length);
        }

    }

    /**
     * 
     * <p>功能描述：关闭文件通道</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-18</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param fileChannel
     */
    private static void closeFileChannel(FileChannel fileChannel)
    {
        if (fileChannel != null)
        {
            try
            {
                fileChannel.close();
            }
            catch (IOException ignore)
            {
            }
        }
    }
    
    

    // 后台线程
    private final class BackRunnable implements Runnable
    {

        /**
         * 
         * 功能描述：
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            HashMap resourceFilesMap = tidyNewResourceURLs();
            HashMap newClassInfosMap = tidyNewClassInfos();
            int headByteCount = calculateHeadByteCount(newClassInfosMap,
                    resourceFilesMap);
            int bodyByteCount = calculateBodyByteCount(newClassInfosMap);
            int totalByteCount = headByteCount + bodyByteCount;
            FileChannel newChannel = null;
            File newFile = (classBinFile != class1File) ? class1File
                    : class2File;

            try
            {
                newFile.getParentFile().mkdirs();
                newChannel = (new RandomAccessFile(newFile, "rw")).getChannel();
                MappedByteBuffer totalBuffer = newChannel.map(
                        java.nio.channels.FileChannel.MapMode.READ_WRITE, 0L,
                        totalByteCount);
                totalBuffer.position(headByteCount);
                ByteBuffer newBodyBuffer = totalBuffer.slice();
                totalBuffer.position(0);
                ByteBuffer newHeadBuffer = totalBuffer.slice();
                newHeadBuffer.limit(headByteCount);
                newHeadBuffer.put((byte) -1);
                newHeadBuffer.put((byte) 1);
                newHeadBuffer.putLong(0L);
                newHeadBuffer.putShort((short) 0);

                if (currentFileChannel != null)
                {
                    merge(newHeadBuffer, newBodyBuffer, newClassInfosMap,
                            resourceFilesMap);
                }

                appendNewJars(newHeadBuffer, newBodyBuffer, newClassInfosMap,
                        resourceFilesMap);
                Set newJarSet = new HashSet(newClassInfosMap.size()
                        + resourceFilesMap.size());
                newJarSet.addAll(newClassInfosMap.keySet());
                newJarSet.addAll(resourceFilesMap.keySet());
                int newJarCount = newJarSet.size();
                int oldJarCount = cachedJarInfoMap.size();
                int totalJarCount = oldJarCount + newJarCount;
                newHeadBuffer.putShort(10, (short) totalJarCount);
                newHeadBuffer.putLong(2, totalByteCount);
            }
            catch (IOException ignore)
            {
                ignore.printStackTrace();
            }
            finally
            {
                LoaderCache.closeFileChannel(currentFileChannel);
                LoaderCache.closeFileChannel(newChannel);
                newFile.setLastModified(System.currentTimeMillis());
            }
        }

        private BackRunnable()
        {
        }

    }

    // 新建类信息
    private static final class NewClassInfo
    {

        /**
         * jar文件
         */
        private File jarFile;

        /**
         * 类
         */
        private Class theClass;

        /**
         * 字节流
         */
        private ByteBuffer byteBuffer;

        /**
         * 构造函数
         * @param jarFile
         * @param theClass
         */
        NewClassInfo(File jarFile, Class theClass)
        {
//            this.jarFile = null;
//            this.theClass = null;
//            byteBuffer = null;
            this.jarFile = jarFile;
            this.theClass = theClass;
        }
    }

    // Jar缓存信息
    private static final class JarCacheInfo
    {

        /**
         * 类字节信息
         */
        private ArrayList classByteInfos;

        /**
         * 资源信息
         */
        private HashMap resourceInfoMap;

        /**
         * jar文件
         */
        private File jarFile;

        /**
         * 类总数
         */
        private int classCount;

        /**
         * 头部长度
         */
        private int headLength;

        /**
         * Jar的字节流
         */
        private int classByteLengthOfJar;

        /**
         * 构造函数
         * @param jarFile
         * @param classCount
         * @param resourceCount
         */
        JarCacheInfo(File jarFile, int classCount, int resourceCount)
        {
            classByteInfos = null;
            resourceInfoMap = null;
            this.jarFile = null;
            this.classCount = 0;
            headLength = 0;
            classByteLengthOfJar = 0;
            this.jarFile = jarFile;
            this.classCount = classCount;
            classByteInfos = new ArrayList(classCount);
            resourceInfoMap = new HashMap(resourceCount);
        }

        /**
         * 
         * <p>功能描述：添加类信息</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-18</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param classInfo
         */
        void addClassInfo(ClassCacheInfo classInfo)
        {
            classByteInfos.add(classInfo);
        }

        /**
         * 
         * <p>功能描述：增加资源信息</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-18</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param resourceKey
         * @param resourcePath
         */
        void putResourceInfo(String resourceKey, String resourcePath)
        {
            resourceInfoMap.put(resourceKey, resourcePath);
        }

    }

}
