package com.chinaviponline.erp.corepower.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>文件名称：FileScanner.java</p>
 * <p>文件描述：查找所有 lib 下的 .jar 文件</p>
 * <p>版权所有： 版权所有(C)2000-2010</p>
 * <p>公   司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2007-7-28</p>
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
 * @author 龚为川gongwc
 */
public class FileScanner
{

    /**
     * 文件分隔符
     */
    private static final String separator = System
            .getProperty("file.separator");

    private static final String pathSeparator = System
            .getProperty("path.separator");

    /**
     * 当前工作目录
     */
    private static final String usrDir = System.getProperty("user.dir");

    private List fileList = new ArrayList();

    private static FileScanner singleton = null;

    /**
     * 单例
     *
     */
    private FileScanner()
    {

    }

    public static FileScanner getSingleton()
    {
        if (singleton == null)
        {
            singleton = new FileScanner();

        }
        return singleton;
    }

    /**
     * 
     * <p>功能描述：寻找 pathName 目录下所有以 extName 结尾的文件路径</p>
     * <p>创建人：龚为川gongwc</p>
     * <p>创建日期：2007-7-28</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param path 待搜索的文件
     * @param extName 后缀名
     * @return
     */
    private void findExtFiles(File path, String extName)
    {
        if (!path.exists())
        {
            return;
        }

        if (path.isDirectory())
        {
            // 是目录
            File allFiles[] = path.listFiles();

            for (int i = 0, count = allFiles.length; i < count; i++)
            {
                findExtFiles(allFiles[i], extName);
            }
        }
        else if (path.isFile())
        {
            // 是文件
            if (path.getName().endsWith(extName))
            {
                fileList.add(path.getParent() + separator + path.getName());
            }
        }

        return;
    }

    /**
     * 
     * <p>功能描述：搜寻目录为 lib ,后缀为</p>
     * <p>创建人：龚为川gongwc</p>
     * <p>创建日期：2007-7-28</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param lib
     * @param ext
     * @return
     */
    public String findExts(String lib, String ext)
    {
        fileList = new ArrayList();
        File libPath = (new File(usrDir + separator + lib));
        findExtFiles(libPath, ext);

        StringBuffer stringbuffer = new StringBuffer();

        for (int i = 0, count = fileList.size(); i < count; i++)
        {
            stringbuffer.append((String) fileList.get(i));
            stringbuffer.append(pathSeparator);
        }

        return stringbuffer.toString();
    }

    /**
     * <p>功能描述：</p>
     * <p>创建人：龚为川gongwc</p>
     * <p>创建日期：2007-7-28</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param args
     */
    public static void main(String[] args)
    {
        FileScanner fs = FileScanner.getSingleton();
        System.out.println(fs.findExts("lib", ".jar"));

    }

}
