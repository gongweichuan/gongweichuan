/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.filescanner;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.psl.systemsupport.ErpDeploymentException;
import com.chinaviponline.erp.corepower.psl.systemsupport.ParCollector;
import com.chinaviponline.erp.corepower.psl.systemsupport.PropertiesService;
import com.chinaviponline.erp.corepower.psl.systemsupport.Util;

/**
 * <p>文件名称：FileScanner.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-13</p>
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
public class FileScanner
{
    
    /**
     * 搜索目录
     */
    private static String searchPaths[];

    /**
     * 属性服务
     */
    private static final PropertiesService PS;

    /**
     * 已经搜索到的目录
     */
    private static File SEARCHED_DIRS[];

    /**
     * temp目录
     */
    private static String tempPath;

    /**
     * 运行时目录
     */
    private static String runtimePath;

    /**
     * home目录
     */
    private static String homePath;

    /**
     * home目录长度
     */
    private static int homePathLength;

    /**
     * 缓存没有找到?
     */
    private static boolean cacheNotFound;

    /**
     * 日志
     */
    private static Logger log;

    /**
     * files.cache
     */
    public static final String FILE_CACHE = "files.cache";

    /**
     * 缓存包装类
     */
    private static CacheWapper cacheWapper;

    /**
     * 初始化
     */
    static
    {
        outter:
        {
            searchPaths = null;
            PS = PropertiesService.getInstance();
            SEARCHED_DIRS = null;
            tempPath = PS.getPath("erp.path", "home") + File.separator + "temp"
                    + File.separator;
            runtimePath = PS.getPath("erp.path", "home") + File.separator
                    + "deploy" + File.separator + "runtime" + File.separator;
            homePath = PS.getPath("erp.path", "home");
            homePathLength = homePath.length();
            cacheNotFound = false;
            log = Logger .getLogger((FileScanner.class).getName());
            cacheWapper = null;
            log.debug("begin init FileScanner...");
            log.info("erp home path:" + homePath);
            String removeCacheStr = PS.get("erp.scanfiles.removeCache");
            String cacheNotFoundStr = PS.get("erp.scanfiles.cacheNotFound");
            
            
            if (cacheNotFoundStr != null)
            {
                cacheNotFound = Boolean.valueOf(cacheNotFoundStr)
                .booleanValue();                
            }
            
            try
            {
                if (removeCacheStr == null)
                {
                    throw new ErpDeploymentException(
                    "erp.scanfiles.removeCache property does not exist in deploy-default.properties or deploy.properties file");
                }
                
                if (removeCacheStr.equalsIgnoreCase("true"))
                {
                    String runtimedir = PropertiesService.getInstance()
                            .getPath("erp.path", "deploy")
                            + File.separator + "temp";
                    log.info("Now, delete files.cache ...... ");
                    Util.deleteFile(runtimedir, "files.cache");
                }
                searchPaths = getSearchPaths();
            }
            catch (ErpDeploymentException e)
            {
                log.info(e);
            }
            
            
            ArrayList alist = new ArrayList(searchPaths.length);
            File file = null;
            for (int i = 0; i < searchPaths.length; i++)
            {
                file = new File(searchPaths[i]);
                if (file.exists())
                {
                    alist.add(file);                    
                }
                
                SEARCHED_DIRS = (File[]) alist.toArray(new File[alist.size()]);
            }

            ObjectInputStream inOs = null;
            try
            {
                try
                {
                    File cacheObjectFile = new File(tempPath, "files.cache");
                    if (cacheObjectFile.exists())
                    {
                        BufferedInputStream bis = new BufferedInputStream(
                                new FileInputStream(cacheObjectFile));
                        inOs = new ObjectInputStream(bis);
                        String cachedSearhDirs[] = (String[]) inOs.readObject();
                        
                        if (searchPaths.length != cachedSearhDirs.length)
                        {
                            cacheWapper = new CacheWapper();
                        }
                        else
                        {
                            boolean cacheValid = true;
                            int i = 0;
                            do
                            {
                                if (i >= searchPaths.length)
                                {
                                    break;
                                }
                                   
                                if (!searchPaths[i].equals(cachedSearhDirs[i]))
                                {
                                    cacheValid = false;
                                    break;
                                }
                                i++;
                            }
                            while (true);
                            
                            if (cacheValid)
                            {
                                cacheWapper = (CacheWapper) inOs.readObject();                                
                            }
                            else
                            {
                                cacheWapper = new CacheWapper();                                
                            }
                        }
                    }
                    else
                    {
                        cacheWapper = new CacheWapper();
                    }
                }
                catch (Throwable ignore)
                {
                    cacheWapper = new CacheWapper();
                }
                break outter;
            }
            finally
            {
                inner:
                {
                    try
                    {
                        if (inOs != null)
                        {
                            inOs.close();                            
                        }
                    }
                    catch (IOException ignore)
                    {
                        inOs = null;
                        break inner;
                    }
                    finally
                    {
                        inOs = null;
                        
                    }
                    inOs = null;
                    break inner;
                }
            }
        }
    
        for (int i = 0; i < SEARCHED_DIRS.length; i++)
        {
            log.debug("searchDir include:" + SEARCHED_DIRS[i]);
        }
    }
    
    /**
     * 构造函数
     *
     */
    public FileScanner()
    {
    }
    
  



    /**
     * 
     * <p>功能描述：初始化搜索目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws ErpDeploymentException
     */
    private static void initSearchPaths() throws ErpDeploymentException
    {
        ArrayList fixedPath = PropertiesService.getInstance()
                .getFixedSearchPaths();
        LinkedList deployedDirs = (LinkedList) ParCollector
                .getSequenceParDirList();
        
        if (deployedDirs == null)
        {
            throw new ErpDeploymentException("deployedDirs == null");
        }
        else
        {
            fixedPath.addAll(deployedDirs);
            searchPaths = new String[fixedPath.size()];
            fixedPath.toArray(searchPaths);
            return;
        }
    }

    /**
     * 
     * <p>功能描述：获取搜索目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws ErpDeploymentException
     */
    public static String[] getSearchPaths() throws ErpDeploymentException
    {
        if (searchPaths == null)
        {
            initSearchPaths();            
        }
        
        return searchPaths;
    }

    /**
     * 
     * <p>功能描述：保存cache</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    public static void saveCacheWhenNoFile()
    {
        File cacheObjectFile = new File(tempPath, "files.cache");
        
        if (cacheObjectFile.exists())
        {
            return;
        }
        else
        {
            saveCache();
            return;
        }
    }

    /**
     * 
     * <p>功能描述：保存缓存</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    public static void saveCache()
    {
        outter:
        {
            if (!cacheWapper.isChanged())
            {
                break outter;                
            }
            ObjectOutputStream outOs = null;
            try
            {
                try
                {
                    File cacheObjectFile = new File(tempPath, "files.cache");
                    if (!cacheObjectFile.exists())
                    {
                        cacheObjectFile.createNewFile();                        
                    }
                    
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(cacheObjectFile));
                    outOs = new ObjectOutputStream(bos);
                    outOs.writeObject(searchPaths);
                    
                    synchronized (cacheWapper)
                    {
                        outOs.writeObject(cacheWapper);
                    }
                    outOs.reset();
                    log.info("save files cache ok!");
                }
                catch (IOException ignore)
                {
                    log.info(ignore);
                }
                break outter;
            }
            finally
            {
                inner:
                {
                    if (outOs == null)
                    {
                        break inner;                        
                    }
                    
                    try
                    {
                        outOs.close();
                    }
                    catch (IOException ignore)
                    {
                        outOs = null;
                        break inner;
                    }
                    finally
                    {
                        outOs = null;                       
                    }
                    outOs = null;
                    break inner;
                }
            }
        }
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param fileName
     * @return
     */
    public static File getFile(String fileName)
    {
        return getFile(fileName, true);
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param fileName
     * @param ignoreCase
     * @return
     */
    public static File getFile(String fileName, boolean ignoreCase)
    {
        long start = System.currentTimeMillis();
        String cacheKeyNotFound = null;
        
        if (cacheNotFound)
        {
            if (ignoreCase)
            {
                cacheKeyNotFound = fileName.toLowerCase() + ignoreCase;                
            }
            else
            {
                cacheKeyNotFound = fileName + ignoreCase;                
            }
            
            if (cacheWapper.notFound(cacheKeyNotFound))
            {
                File result = getFile(new File(runtimePath), fileName,
                        ignoreCase);
                log.debug("GetFile '" + fileName + "' with ignoreCase="
                        + ignoreCase
                        + " from not found cache or runtime dir spend:"
                        + Long.toString(System.currentTimeMillis() - start)
                        + " , result is " + result);
                return result;
            }
        }
        
        File file = null;
        
        synchronized (cacheWapper)
        {
            file = cacheWapper.getFile(fileName, ignoreCase);
        }
        
        if (file != null)
        {
            log.debug("Successfullly getFile '" + fileName
                    + "' with ignoreCase=" + ignoreCase + " from cache spend:"
                    + Long.toString(System.currentTimeMillis() - start));
            return file;
        }
        
        int i = 0;
        
        do
        {
            if (i >= SEARCHED_DIRS.length)
            {
                break;                
            }
            
            File res = getFile(SEARCHED_DIRS[i], fileName, ignoreCase);
            if (res != null)
            {
                file = res;
                break;
            }
            i++;
        }
        while (true);
        
        if (file != null)
        {
            synchronized (cacheWapper)
            {
                cacheWapper.putFile(file);
            }
            log.debug("Successfullly getFile '" + fileName
                    + "' with ignoreCase=" + ignoreCase
                    + "  from fileSystem spend:"
                    + Long.toString(System.currentTimeMillis() - start));
        }
        else
        {
            if (cacheNotFound)
            {
                synchronized (cacheWapper)
                {
                    cacheWapper.putNotFound(cacheKeyNotFound);
                }                
            }
            log.info(fileName + " not found ...");
            log.debug(fileName + " not found, and found spend "
                    + Long.toString(System.currentTimeMillis() - start));
        }
        return file;
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param filepaths
     * @param fileName
     * @return
     */
    public static File getFile(String filepaths[], String fileName)
    {
        return getFile(filepaths, fileName, true);
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param filepaths
     * @param fileName
     * @param ignoreCase
     * @return
     */
    public static File getFile(String filepaths[], String fileName,
            boolean ignoreCase)
    {
        long start = System.currentTimeMillis();
        StringBuffer logInfo = new StringBuffer();
        File file = null;
        String cacheKeyNotFound;
        
        if (ignoreCase)
        {
            cacheKeyNotFound = fileName.toLowerCase() + ignoreCase;            
        }
        else
        {
            cacheKeyNotFound = fileName + ignoreCase;            
        }
        
        
        for (int i = 0; i < filepaths.length; i++)
        {
            if (cacheNotFound
                    && cacheWapper.notFound(filepaths[i], cacheKeyNotFound))
            {
                continue;                
            }
            
            synchronized (cacheWapper)
            {
                file = cacheWapper.getFile(filepaths[i], fileName, ignoreCase);
            }
            
            if (file != null)
            {
                log.debug("Successfullly getFile '" + fileName + "'"
                        + "in dir " + filepaths[i] + " with ignoreCase="
                        + ignoreCase + "  from cache spend:"
                        + Long.toString(System.currentTimeMillis() - start));
                return file;
            }
            
            File dir = new File(filepaths[i]);
            
            if (dir.exists())
            {
                file = getFile(dir, fileName, ignoreCase);                
            }
            
            if (file != null)
            {
                synchronized (cacheWapper)
                {
                    cacheWapper.putFile(filepaths[i], file);
                }
                log.debug("Successfullly getFile '" + fileName + "'"
                        + "in dir " + filepaths[i] + " with ignoreCase="
                        + ignoreCase + "  from fileSystem spend:"
                        + Long.toString(System.currentTimeMillis() - start));
                return file;
            }
            
            if (cacheNotFound)
            {
                synchronized (cacheWapper)
                {
                    cacheWapper.putNotFound(filepaths[i], cacheKeyNotFound);
                }                
            }
            logInfo.append(filepaths[i]);
            logInfo.append("  ");
        }

        log.info(fileName + " not found in " + logInfo.toString());
        log.debug(fileName + " not found in " + logInfo.toString()
                + ", and found spend "
                + Long.toString(System.currentTimeMillis() - start));
        return null;
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param dir
     * @param fileName
     * @param ignoreCase
     * @return
     */
    private static File getFile(File dir, String fileName, boolean ignoreCase)
    {
        File files[] = dir.listFiles();
        if (files == null)
        {
            log.info("[FileScaner] can not list " + dir + ", you del it ?");
            return null;
        }
        
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory())
            {
                File tempFile = getFile(files[i], fileName, ignoreCase);
                if (tempFile != null)
                {
                    return tempFile;                    
                }
                continue;
            }
            
            if (ignoreCase)
            {
                if (files[i].getName().equalsIgnoreCase(fileName))
                {
                    return files[i];                    
                }
                continue;
            }
            
            if (files[i].getName().equals(fileName))
            {
                return files[i];                
            }
        }

        return null;
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param suffixFileName
     * @return
     */
    public static File[] getFiles(String suffixFileName)
    {
        return getFiles(suffixFileName, true);
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param suffixFileName
     * @param ignoreCase
     * @return
     */
    public static File[] getFiles(String suffixFileName, boolean ignoreCase)
    {
        long start = System.currentTimeMillis();
        String lSuffixFileName = null;
        if (suffixFileName.indexOf('*') >= 0
                || suffixFileName.indexOf('?') >= 0)
        {
            lSuffixFileName = suffixFileName;            
        }
        else
        {
            lSuffixFileName = '*' + suffixFileName;            
        }
        
        String cacheKey;
        
        if (ignoreCase)
        {
            cacheKey = lSuffixFileName.toLowerCase() + ignoreCase;            
        }
        else
        {
            cacheKey = lSuffixFileName + ignoreCase;            
        }
        
        ExtendedFileFilter filter;
        
        if (cacheNotFound && cacheWapper.notFound(cacheKey))
        {
            LinkedList list = new LinkedList();
            filter = new ExtendedFileFilter(lSuffixFileName, ignoreCase);
            getFiles(new File(runtimePath), filter, list);
            File files[] = new File[list.size()];
            list.toArray(files);
            log.debug("GetSuffixFiles '" + suffixFileName + "'"
                    + " with ignoreCase=" + ignoreCase
                    + "  from not found cache or runtime dir spend:"
                    + Long.toString(System.currentTimeMillis() - start));
            return files;
        }
        
        File cachedFiles[] = null;
        
        synchronized (cacheWapper)
        {
            cachedFiles = cacheWapper.getFiles(cacheKey);
        }
        
        if (cachedFiles != null)
        {
            log.debug("Successfullly getSuffixFiles '" + suffixFileName + "'"
                    + " with ignoreCase=" + ignoreCase + "  from cache spend:"
                    + Long.toString(System.currentTimeMillis() - start));
            return cachedFiles;
        }
        
        ExtendedFileFilter cachewFileFilter = new ExtendedFileFilter(lSuffixFileName, ignoreCase);
        LinkedList list = new LinkedList();
        for (int i = 0; i < SEARCHED_DIRS.length; i++)
        {
            getFiles(SEARCHED_DIRS[i], ((ExtendedFileFilter) (cachewFileFilter)),
                    list);            
        }

        File files[] = new File[list.size()];
        list.toArray(files);
        if (files.length > 0)
        {
            synchronized (cacheWapper)
            {
                cacheWapper.putFiles(cacheKey, files);
            }
            log.debug("Successfullly getSuffixFiles '" + suffixFileName
                    + "' with ignoreCase=" + ignoreCase
                    + "  from fileSystem spend:"
                    + Long.toString(System.currentTimeMillis() - start));
        }
        else
        {
            if (cacheNotFound)
            {
                synchronized (cacheWapper)
                {
                    cacheWapper.putNotFound(cacheKey);
                }                
            }
            log.debug("suffixFileName '" + suffixFileName
                    + "' with ignoreCase=" + ignoreCase
                    + " not found,  and found spend "
                    + Long.toString(System.currentTimeMillis() - start));
        }
        return files;
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param filepaths
     * @param suffixFileName
     * @return
     */
    public static File[] getFiles(String filepaths[], String suffixFileName)
    {
        return getFiles(filepaths, suffixFileName, true);
    }

    /**
     * 
     * <p>功能描述：获取文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param filepaths
     * @param suffixFileName
     * @param ignoreCase
     * @return
     */
    public static File[] getFiles(String filepaths[], String suffixFileName,
            boolean ignoreCase)
    {
        long start = System.currentTimeMillis();
        String ISuffixFileName = null;
        if (suffixFileName.indexOf('*') >= 0
                || suffixFileName.indexOf('?') >= 0)
        {
            ISuffixFileName = suffixFileName;            
        }
        else
        {
            ISuffixFileName = '*' + suffixFileName;            
        }
        
        String cacheKey;
        if (ignoreCase)
        {
            cacheKey = ISuffixFileName.toLowerCase() + ignoreCase;            
        }
        else
        {
            cacheKey = ISuffixFileName + ignoreCase;            
        }
        
        ExtendedFileFilter filter = new ExtendedFileFilter(ISuffixFileName,
                ignoreCase);
        
        File dir = null;
        ArrayList al = new ArrayList();
        for (int i = 0; i < filepaths.length; i++)
        {
            if (cacheNotFound && cacheWapper.notFound(filepaths[i], cacheKey))
            {
                continue;                
            }
            
            File cfiles[] = null;
            synchronized (cacheWapper)
            {
                cfiles = cacheWapper.getFiles(filepaths[i], cacheKey);
            }
            if (cfiles != null)
            {
                log.debug("Successfullly getSuffixFiles '" + suffixFileName
                        + "'" + "in dir " + filepaths[i] + " with ignoreCase="
                        + ignoreCase + "  from cache spend:"
                        + Long.toString(System.currentTimeMillis() - start));                
            }
            
            if (cfiles == null)
            {
                dir = new File(filepaths[i]);
                if (dir.exists())
                {
                    LinkedList list = new LinkedList();
                    getFiles(dir, filter, list);
                    if (list.size() > 0)
                    {
                        cfiles = (File[]) list.toArray(new File[0]);
                        synchronized (cacheWapper)
                        {
                            cacheWapper
                                    .putFiles(filepaths[i], cacheKey, cfiles);
                        }
                        log.debug("Successfullly getSuffixFiles '"
                                + suffixFileName
                                + "'"
                                + "in dir "
                                + filepaths[i]
                                + " with ignoreCase="
                                + ignoreCase
                                + "  from fileSystem spend:"
                                + Long.toString(System.currentTimeMillis()
                                        - start));
                    }
                }
            }
            
            if (cfiles != null)
            {
                al.addAll(Arrays.asList(cfiles));
                continue;
            }
            if (!cacheNotFound)
            {
                continue;                
            }
            
            synchronized (cacheWapper)
            {
                cacheWapper.putNotFound(filepaths[i], cacheKey);
            }
        }

        if (al.size() == 0)
        {
            log.debug("suffixFileName '" + suffixFileName
                    + "' not found,  and found spend "
                    + Long.toString(System.currentTimeMillis() - start));            
        }
        return (File[]) al.toArray(new File[0]);
    }

    /**
     * 
     * <p>功能描述：递归获得文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param dir
     * @param filter
     * @param foundedFiles
     */
    private static void getFiles(File dir, ExtendedFileFilter filter,
            LinkedList foundedFiles)
    {
        File fs[] = dir.listFiles(filter);
        if (fs == null)
        {
            log.info("[FileScaner] can not list " + dir + ", you del it ?");
            return;
        }
        for (int i = 0; i < fs.length; i++)
        {
            if (fs[i].isFile())
            {
                foundedFiles.add(fs[i]); 
            }
        }

        for (int i = 0; i < fs.length; i++)
        {
            if (fs[i].isDirectory())
            {
                getFiles(fs[i], filter, foundedFiles);   
            }
        }

    }

    /**
     * 
     * <p>功能描述：获取文件并刷新缓存</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param suffixFileName
     * @return
     */
    public static File[] getFilesRefreshCache(String suffixFileName)
    {
        return getFilesRefreshCache(suffixFileName, true);
    }

    /**
     * 
     * <p>功能描述：获取文件并刷新缓存</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param suffixFileName
     * @param ignoreCase
     * @return
     */
    public static File[] getFilesRefreshCache(String suffixFileName,
            boolean ignoreCase)
    {
        String lSuffixFileName = null;
        if (suffixFileName.indexOf('*') >= 0
                || suffixFileName.indexOf('?') >= 0)
        {
            lSuffixFileName = suffixFileName;            
        }
        else
        {
            lSuffixFileName = '*' + suffixFileName;            
        }
        
        String cacheKey;
        
        if (ignoreCase)
        {
            cacheKey = lSuffixFileName.toLowerCase() + ignoreCase;            
        }
        else
        {
            cacheKey = lSuffixFileName + ignoreCase;            
        }
        
        ExtendedFileFilter filter = new ExtendedFileFilter(lSuffixFileName,
                ignoreCase);
        LinkedList list = new LinkedList();
        
        for (int i = 0; i < SEARCHED_DIRS.length; i++)
        {
            getFiles(SEARCHED_DIRS[i], filter, list);            
        }

        File files[] = new File[list.size()];
        list.toArray(files);
        
        if (files.length > 0)
        {
            synchronized (cacheWapper)
            {
                cacheWapper.putFiles(cacheKey, files);
            }            
        }
        return files;
    }

    /**
     * 
     * <p>功能描述：获取所有目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param dirName
     * @return
     */
    public static File[] getDirs(String dirName)
    {
        return getDirs(SEARCHED_DIRS, dirName, true);
    }

    /**
     * 
     * <p>功能描述：获取所有目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param basedirs
     * @param dirName
     * @return
     */
    public static File[] getDirs(File basedirs[], String dirName)
    {
        return getDirs(basedirs, dirName, true);
    }

    /**
     * 
     * <p>功能描述：获取所有目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param basedirs
     * @param dirName
     * @param ignoreCase
     * @return
     */
    public static File[] getDirs(File basedirs[], String dirName,
            boolean ignoreCase)
    {
        ExtendedFileFilter filter = new ExtendedFileFilter(dirName, ignoreCase);
        LinkedList list = new LinkedList();
        for (int i = 0; i < basedirs.length; i++)
        {
            if (!basedirs[i].exists())
            {
                log.info("dir " + basedirs[i].getAbsolutePath()
                        + " does not exist.");                
            }
            else
            {
                getDirs(basedirs[i], filter, list);  
            }
        }

        File fResults[] = new File[list.size()];
        list.toArray(fResults);
        return fResults;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param dir
     * @param filter
     * @param foundedDirs
     */
    private static void getDirs(File dir, ExtendedFileFilter filter,
            LinkedList foundedDirs)
    {
        String fileNames[] = dir.list();
        if (fileNames == null)
        {
            return;            
        }
        
        LinkedList unMatchedList = new LinkedList();
        File f = null;
        for (int i = 0; i < fileNames.length; i++)
        {
            f = new File(dir, fileNames[i]);
            
            if (!f.isDirectory())
            {
                continue;                
            }
            
            if (filter == null || filter.accept(f, true))
            {
                foundedDirs.add(f);                
            }
            else
            {
                unMatchedList.add(f);                
            }
        }

        Iterator iter = unMatchedList.iterator();
        File temp = null;
        for (; iter.hasNext(); getDirs(temp, filter, foundedDirs))
        {
            temp = (File) iter.next();            
        }
    }

    //内部类
    // 缓存Wapper
    private static final class CacheWapper implements Serializable
    {

        /**
         * 序列化ID 自动生成
         */
        private static final long serialVersionUID = 0x1864cf871b0f4154L;

        /**
         * 缓存大小1
         */
        private static final int INIT_CASHE = 5000;

        /**
         * 缓存大小2
         */
        private static final int INIT_CASHE2 = 50;

        /**
         * 是否改变
         */
        private transient boolean changed;

        /**
         * 文件缓存
         */
        private Map fileCache;

        /**
         * 目录缓存
         */
        private Map dir2PatternFileCache;

        /**
         * 未缓存的文件
         */
        private Set notFoundCache;

        /**
         * 未缓存的目录
         */
        private Map dir2PatternNotFoundCache;

        /**
         * 构造函数
         *
         */
        private CacheWapper()
        {
            changed = false;
            
            fileCache = Collections.synchronizedMap(new HashMap(5000));
            dir2PatternFileCache = Collections.synchronizedMap(new HashMap(50));
            notFoundCache = Collections.synchronizedSet(new HashSet(5000));
            dir2PatternNotFoundCache = Collections.synchronizedMap(new HashMap(
                    50));
            
            changed = true;
        }
        
        /**
         * 
         * <p>功能描述：是否未缓存</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param aFullFileNameWithoutPath
         * @return
         */
        private boolean notFound(String aFullFileNameWithoutPath)
        {
            return notFoundCache.contains(aFullFileNameWithoutPath);
        }

        /**
         * 
         * <p>功能描述：增加到未缓存</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param aFullFileNameWithoutPath
         */
        private void putNotFound(String aFullFileNameWithoutPath)
        {
            notFoundCache.add(aFullFileNameWithoutPath);
        }
        
        /**
         * 
         * <p>功能描述：是否未缓存</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param dirName
         * @param aFullFileNameWithoutPath
         * @return
         */
        private boolean notFound(String dirName, String aFullFileNameWithoutPath)
        {
            Set notFoundSet = (Set) dir2PatternNotFoundCache.get(dirName);
            
            if (notFoundSet != null)
            {
                return notFoundSet.contains(aFullFileNameWithoutPath);                
            }
            else
            {
                return false;                
            }
        }

        /**
         * 
         * <p>功能描述：增加到未缓存</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param dirName
         * @param aFullFileNameWithoutPath
         */
        private void putNotFound(String dirName, String aFullFileNameWithoutPath)
        {
            Set notFoundSet = (Set) dir2PatternNotFoundCache.get(dirName);
            
            if (notFoundSet == null)
            {
                HashSet hs = new HashSet();
                hs.add(aFullFileNameWithoutPath);
                dir2PatternNotFoundCache.put(dirName, hs);
            }
            else
            {
                notFoundSet.add(aFullFileNameWithoutPath);
            }
        }

        /**
         * 
         * <p>功能描述：获得文件</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param aFullFileNameWithoutPath
         * @param ignoreCase
         * @return
         */
        private File getFile(String aFullFileNameWithoutPath, boolean ignoreCase)
        {
            String fullFileNameWithoutPath = aFullFileNameWithoutPath
                    .toLowerCase();
            ArrayList cachedFileNames = (ArrayList) fileCache
                    .get(fullFileNameWithoutPath);
            
            File file = null;
            if (cachedFileNames != null && cachedFileNames.size() != 0)
            {
                if (ignoreCase)
                {
                    file = new File(FileScanner.homePath
                            + (String) cachedFileNames.get(0));
                }
                else
                {
                    int i = 0;
                    do
                    {
                        if (i >= cachedFileNames.size())
                        {
                            break;                            
                        }
                        
                        File tmpfile = new File(FileScanner.homePath
                                + (String) cachedFileNames.get(i));
                        if (tmpfile.getName().equals(aFullFileNameWithoutPath))
                        {
                            file = tmpfile;
                            break;
                        }
                        i++;
                    }
                    while (true);
                }
                if (file != null)
                {
                    if (file.exists())
                    {
                        return file;
                    }
                    else
                    {
                        FileScanner.log
                                .debug("cache file: "
                                        + file.getAbsolutePath()
                                        + " is not exist,clear this item in fileCache!");
                        fileCache.put(fullFileNameWithoutPath, new ArrayList());
                        changed = true;
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        /**
         * 
         * <p>功能描述：递归获得文件</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param dirName
         * @param aFullFileNameWithoutPath
         * @param ignoreCase
         * @return
         */
        private File getFile(String dirName, String aFullFileNameWithoutPath,
                boolean ignoreCase)
        {
            String fullFileNameWithoutPath = aFullFileNameWithoutPath
                    .toLowerCase();
            File file = null;
            Map pattern2FileCache = (Map) dir2PatternFileCache.get(dirName);
            
            if (pattern2FileCache != null)
            {
                ArrayList fileNames = (ArrayList) pattern2FileCache
                        .get(fullFileNameWithoutPath);
                if (fileNames != null && fileNames.size() != 0)
                {
                    if (ignoreCase)
                    {
                        file = new File(FileScanner.homePath + fileNames.get(0));
                    }
                    else
                    {
                        int i = 0;
                        do
                        {
                            if (i >= fileNames.size())
                            {
                                break;                                
                            }
                            
                            File tmpfile = new File(FileScanner.homePath
                                    + (String) fileNames.get(i));
                            if (tmpfile.getName().equals(
                                    aFullFileNameWithoutPath))
                            {
                                file = tmpfile;
                                break;
                            }
                            i++;
                        }
                        while (true);
                    }
                    if (file != null)
                    {
                        if (file.exists())
                        {
                            return file;
                        }
                        else
                        {
                            FileScanner.log
                                    .debug("cache file: "
                                            + file.getAbsolutePath()
                                            + " is not exist,clear this item in fileCache!");
                            pattern2FileCache.put(fullFileNameWithoutPath,
                                    new ArrayList());
                            changed = true;
                            return null;
                        }
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        /**
         * 
         * <p>功能描述：加入到未缓存集合</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param foundFile
         */
        private void putFile(File foundFile)
        {
            String lowerCaseName = foundFile.getName().toLowerCase();
            
            if (!fileCache.containsKey(lowerCaseName))
            {
                ArrayList a = new ArrayList();
                String path = foundFile.getAbsolutePath();
                a
                        .add(path.substring(FileScanner.homePathLength, path
                                .length()));
                fileCache.put(lowerCaseName, a);
            }
            else
            {
                ArrayList a = (ArrayList) fileCache.get(lowerCaseName);
                String path = foundFile.getAbsolutePath();
                a
                        .add(path.substring(FileScanner.homePathLength, path
                                .length()));
            }
            changed = true;
        }

        /**
         * 
         * <p>功能描述：加入到未缓存集合</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param dirName
         * @param foundFile
         */
        private void putFile(String dirName, File foundFile)
        {
            String lowerCaseName = foundFile.getName().toLowerCase();
            
            if (!dir2PatternFileCache.containsKey(dirName))
            {
                Map pattern2FileCache = Collections
                        .synchronizedMap(new HashMap(50));
                dir2PatternFileCache.put(dirName, pattern2FileCache);
                ArrayList a = new ArrayList();
                String path = foundFile.getAbsolutePath();
                a
                        .add(path.substring(FileScanner.homePathLength, path
                                .length()));
                pattern2FileCache.put(lowerCaseName, a);
            }
            else
            {
                Map pattern2FileCache = (Map) dir2PatternFileCache.get(dirName);
                ArrayList a = (ArrayList) pattern2FileCache.get(lowerCaseName);
                if (a == null)
                {
                    ArrayList a1 = new ArrayList();
                    String path = foundFile.getAbsolutePath();
                    a1.add(path.substring(FileScanner.homePathLength, path
                            .length()));
                    pattern2FileCache.put(lowerCaseName, a1);
                }
                else
                {
                    String path = foundFile.getAbsolutePath();
                    a.add(path.substring(FileScanner.homePathLength, path
                            .length()));
                }
            }
            changed = true;
        }

        /**
         * 
         * <p>功能描述：通过正则表达式获取文件</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param pattern
         * @return
         */
        private File[] getFiles(String pattern)
        {
            if (fileCache.get(pattern) != null)
            {
                String cachedFileNames[] = (String[]) fileCache.get(pattern);
                File cachedFiles[] = new File[cachedFileNames.length];
                
                for (int i = 0; i < cachedFiles.length; i++)
                {
                    cachedFiles[i] = new File(FileScanner.homePath
                            + cachedFileNames[i]);
                    if (!cachedFiles[i].exists())
                    {
                        fileCache.put(pattern, null);
                        FileScanner.log
                                .debug("cache file: "
                                        + cachedFiles[i].getAbsolutePath()
                                        + " is not exist,clear this item in fileCache!");
                        changed = true;
                        return null;
                    }
                }

                return cachedFiles;
            }
            else
            {
                return null;
            }
        }

        /**
         * 
         * <p>功能描述：通过正则表达式获取文件</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param dirName
         * @param pattern
         * @return
         */
        private File[] getFiles(String dirName, String pattern)
        {
            if (dir2PatternFileCache.containsKey(dirName))
            {
                Map pattern2FileCache = (Map) dir2PatternFileCache.get(dirName);
                if (pattern2FileCache.containsKey(pattern))
                {
                    String cachedFileNames[] = (String[]) pattern2FileCache
                            .get(pattern);
                    if (cachedFileNames == null)
                    {
                        return null;                        
                    }
                    
                    File cachedFiles[] = new File[cachedFileNames.length];
                    for (int i = 0; i < cachedFiles.length; i++)
                    {
                        cachedFiles[i] = new File(FileScanner.homePath
                                + cachedFileNames[i]);
                        if (!cachedFiles[i].exists())
                        {
                            pattern2FileCache.put(pattern, null);
                            FileScanner.log
                                    .debug("cache file: "
                                            + cachedFiles[i].getAbsolutePath()
                                            + " is not exist,clear this item in fileCache!");
                            changed = true;
                            return null;
                        }
                    }

                    return cachedFiles;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        /**
         * 
         * <p>功能描述：增加到文件缓存</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param pattern
         * @param foundFiles
         */
        private void putFiles(String pattern, File foundFiles[])
        {
            String fileNames[] = new String[foundFiles.length];
            for (int i = 0; i < fileNames.length; i++)
            {
                String path = foundFiles[i].getAbsolutePath();
                fileNames[i] = path.substring(FileScanner.homePathLength, path
                        .length());
            }

            fileCache.put(pattern, fileNames);
            changed = true;
        }

        /**
         * 
         * <p>功能描述：增加到文件缓存</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @param dirName
         * @param pattern
         * @param foundFiles
         */
        private void putFiles(String dirName, String pattern, File foundFiles[])
        {
            String fileNames[] = new String[foundFiles.length];
            
            for (int i = 0; i < fileNames.length; i++)
            {
                String path = foundFiles[i].getAbsolutePath();
                fileNames[i] = path.substring(FileScanner.homePathLength, path
                        .length());
            }

            if (dir2PatternFileCache.containsKey(dirName))
            {
                Map pattern2FileCache = (Map) dir2PatternFileCache.get(dirName);
                if (pattern2FileCache == null)
                {
                    Map pattern2FileCache1 = Collections
                            .synchronizedMap(new HashMap(50));
                    dir2PatternFileCache.put(dirName, pattern2FileCache1);
                    pattern2FileCache1.put(pattern, fileNames);
                }
                else
                {
                    pattern2FileCache.put(pattern, fileNames);
                }
            }
            else
            {
                Map pattern2FileCache = Collections
                        .synchronizedMap(new HashMap(50));
                dir2PatternFileCache.put(dirName, pattern2FileCache);
                pattern2FileCache.put(pattern, fileNames);
            }
            changed = true;
        }

        /**
         * 
         * <p>功能描述：是否改变</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-13</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         * @return
         */
        private boolean isChanged()
        {
            return changed;
        }

    }

}
