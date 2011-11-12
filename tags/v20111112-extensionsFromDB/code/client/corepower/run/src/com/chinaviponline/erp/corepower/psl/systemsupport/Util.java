/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * <p>文件名称：Util.java</p>
 * <p>文件描述：工具类</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-12</p>
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
public class Util
{
    /**
     * 日志
     */
    private static Logger log;

    /**
     * 启动模式
     */
    private static String startMode = null;

    /**
     * 客户端模式
     */
    public static final String CLIENT_MODE = "client";

    /**
     * packagename
     */
    private static final String PRE_KEY_CATEGORY_NAME = "category.packagename.";

    /**
     * priority
     */
    private static final String PRE_KEY_CATEGORY_PRIORITY = "category.priority.";

    /**
     * appender
     */
    private static final String PRE_KEY_CATEGORY_APPENDER = "category.appender.";

    // 启动日志类
    static
    {
        log = Logger.getLogger(Util.class);
    }

    /**
     * 构造函数 仿单例
     *
     */
    private Util()
    {
    }

    /***
     * 
     * <p>功能描述：格式化字符串</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param inputStr
     * @param srcDelimiter
     * @param dstDelimiter
     * @return
     */
    public static String formattedStr(String inputStr, String srcDelimiter,
            String dstDelimiter)
    {
        StringTokenizer st = new StringTokenizer(inputStr, srcDelimiter);
        StringBuffer sb = new StringBuffer();
        if (st.hasMoreTokens())
        {
            sb.append(st.nextToken());
        }
        for (; st.hasMoreTokens(); sb.append(st.nextToken()))
        {
            sb.append(dstDelimiter);
        }

        return sb.toString();
    }

    /**
     * 
     * <p>功能描述：获取Properties文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param absoluteFilePath
     * @param fileName
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static Properties getPropertiesFormFile(String absoluteFilePath,
            String fileName) throws IOException, FileNotFoundException
    {
        FileInputStream fis = null;
        Properties properties = null;
        Properties userProperties = new Properties();
        String file = absoluteFilePath + File.separator + fileName;
        fis = new FileInputStream(file);
        userProperties.load(fis);
        properties = userProperties;
        if (fis != null)
        {
            fis.close();
        }
        return properties;

    }

    /**
     * 
     * <p>功能描述：格式化输入参数</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param args
     * @param deleteTag
     * @param appendString
     * @return
     */
    public static String[] formatArgs(String args[], String deleteTag,
            String appendString)
    {
        ArrayList list = new ArrayList();
        String lDeleteTag = deleteTag.trim();
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals(lDeleteTag))
            {
                i++;
            }
            else
            {
                list.add(args[i]);
            }
        }

        int j = -1;
        if (appendString != null)
        {
            j = appendString.trim().indexOf(" ");
            if (j != -1)
            {
                list.add(appendString.substring(0, j));
                list.add(appendString.substring(j).trim());
            }
            else
            {
                list.add(appendString);
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * 
     * <p>功能描述：获取属性值</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param p
     * @param s
     * @return
     */
    public static String[] getNeededElementsByIndex(Properties p, String s)
    {
        Enumeration en = p.propertyNames();
        String key = null;
        TreeMap tm = new TreeMap();
        do
        {
            if (!en.hasMoreElements())
            {
                break;
            }
            key = (String) en.nextElement();
            if (key.indexOf(s) != -1)
            {
                tm.put(key, p.getProperty(key));
            }
        }
        while (true);

        Object obs[] = tm.values().toArray();
        int size = obs.length;
        String strs[] = new String[size];
        for (int i = 0; i < size; i++)
        {
            strs[i] = (String) obs[i];
        }

        return strs;
    }

    /**
     * 
     * <p>功能描述：获取所有类别信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param p
     * @return
     * @throws IOException
     */
    public static CategoryInfo[] getCategoryInfos(Properties p)
            throws IOException
    {
        Enumeration en = p.propertyNames();
        String key = null;
        TreeMap tMap = new TreeMap();
        do
        {
            if (!en.hasMoreElements())
                break;
            key = (String) en.nextElement();
            if (key.indexOf("category.packagename.") != -1)
            {
                tMap.put(key, p.getProperty(key));
            }
        }
        while (true);

        String tmpKeys[] = (String[]) tMap.keySet().toArray(
                new String[tMap.keySet().size()]);
        if (tmpKeys == null || tmpKeys.length == 0)
        {
            return new CategoryInfo[0];
        }

        CategoryInfo infos[] = new CategoryInfo[tmpKeys.length];
        String index = null;
        String name = null;
        String priority = null;
        String appender = null;
        for (int i = 0; i < tmpKeys.length; i++)
        {
            index = tmpKeys[i].substring("category.packagename.".length());
            name = p.getProperty(tmpKeys[i]);
            if (name == null || name.trim().length() == 0)
            {
                throw new IOException("The configuration item \"" + tmpKeys[i]
                        + "\" is set incorrectly, "
                        + "since its value is invalid" + " in deploy"
                        + "(-default).properties.");
            }
            priority = p.getProperty("category.priority." + index);
            if (priority == null || priority.trim().length() == 0)
            {
                throw new IOException("The configuration item \"" + tmpKeys[i]
                        + "\" is set incorrectly, "
                        + "since its corresponding item \""
                        + "category.priority." + index
                        + "\" is not set or invalid" + " in deploy"
                        + "(-default).properties.");
            }

            appender = p.getProperty("category.appender." + index);
            infos[i] = new CategoryInfo(name, priority, appender);
        }

        return infos;
    }

    /**
     * 
     * <p>功能描述：复制文件,Java默认没有支持</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copyFile(File in, File out) throws IOException
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try
        {
            fis = new FileInputStream(in);
            sourceChannel = fis.getChannel();
            fos = new FileOutputStream(out);
            destinationChannel = fos.getChannel();
            sourceChannel.transferTo(0L, sourceChannel.size(),
                    destinationChannel);
        }
        finally
        {
            if (sourceChannel != null)
            {
                try
                {
                    sourceChannel.close();
                }
                catch (IOException ignore)
                {
                }
            }

            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException ignore)
                {
                }
            }
            if (destinationChannel != null)
            {
                try
                {
                    destinationChannel.close();
                }
                catch (IOException ignore)
                {
                }
            }
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

    /**
     * 
     * <p>功能描述：删除文件 Java默认没有提供</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param dirName
     * @param fileName
     */
    public static void deleteFile(String dirName, String fileName)
    {
        File f = new File(dirName, fileName);
        if (f.exists() && !f.delete())
        {
            log.info("Delete " + f.getAbsolutePath()
                    + " failed. Please delete it by hand....");
        }
    }

    /**
     * 
     * <p>功能描述：删除临时目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    public static void deleteTempDir()
    {
        LinkedList delBuf = new LinkedList();
        int failNum = 0;
        String tempDir = System.getProperty("jboss.server.temp.dir");
        if (tempDir != null)
        {
            log.info(" tempDir = " + tempDir);
            failNum = delTree(tempDir, failNum, delBuf);
            int delNum = delBuf.size();
            if (delNum > 0)
            {
                log.info(delNum + " temporary file(s) have been deleted.");
                log.info(failNum + " temporary file(s) have NOT been deleted.");
            }
        }
        else
        {
            return;
        }
    }

    /**
     * 
     * <p>功能描述：递归删除目录和文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param absolutePathName
     * @param failNum
     * @param delBuf
     * @return
     */
    public static int delTree(String absolutePathName, int failNum,
            LinkedList delBuf)
    {
        File path = new File(absolutePathName);
        if (!path.exists())
        {
            return failNum;
        }

        if (path.isDirectory())
        {
            File files[] = path.listFiles();
            if (files != null)
            {
                for (int i = 0; i < files.length; i++)
                {
                    if (files[i].isFile())
                    {
                        if (files[i].delete())
                        {
                            delBuf.add(files[i].getAbsolutePath());
                        }
                        else
                        {
                            failNum++;
                        }

                    }
                    else
                    {
                        delTree(files[i].getAbsolutePath(), failNum, delBuf);
                    }
                }
            }

            if (path.delete())
            {
                delBuf.add(absolutePathName);
            }
            else
            {
                failNum++;
            }

        }
        else
        {
            if (path.delete())
            {
                delBuf.add(absolutePathName);
            }
            else
            {
                failNum++;
            }
        }
        return failNum;
    }

    /**
     * 
     * <p>功能描述：找到插件的目录</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param absolutePath
     * @return
     */
    public static String findPar(String absolutePath)
    {
        int p = absolutePath.lastIndexOf(".par");       //TODO 需要常量化,因为以后可能修改成一个.zip类型的文件而不是目录
        
        String parId = null;
        if (p > 0)
        {
            String q = absolutePath.substring(0, p);
            parId = q.substring(q.lastIndexOf(File.separator) + 1);
        }
        else
        {
            parId = null;
        }
        
        return parId;
    }

    /**
     * 
     * <p>功能描述：启动模式</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param startMode
     */
    public static void setStartMode(String startMode)
    {
        Util.startMode = startMode;
    }

    /**
     * 
     * <p>功能描述：获取启动模式</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static String getStartMode()
    {
        return startMode;
    }


}
