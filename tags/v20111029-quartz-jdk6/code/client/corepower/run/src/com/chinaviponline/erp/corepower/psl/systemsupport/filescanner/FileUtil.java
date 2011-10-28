/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.filescanner;

import java.io.File;
import java.util.LinkedList;
/**
 * <p>文件名称：FileUtil.java</p>
 * <p>文件描述：文件工具类</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-11</p>
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
public class FileUtil
{
    /**
     * 私有的构造函数
     *
     */
    private FileUtil()
    {
    }

    /**
     * 
     * <p>功能描述：查找所有以suffixFileName结尾的文件</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
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
    public static File[] getFiles(String filepaths[], String suffixFileName, boolean ignoreCase)
    {
        ExtendedFileFilter filter = new ExtendedFileFilter(suffixFileName, ignoreCase);
        File dir = null;
        LinkedList list = new LinkedList();
        for(int i = 0; i < filepaths.length; i++)
        {
            dir = new File(filepaths[i]);
            if(dir.exists())
            {
                getFiles(dir, filter, list);
            }
                
        }
        File files[] = new File[list.size()];
        list.toArray(files);
        return files;
    }

    /**
     * 
     * <p>功能描述：递归,如果不是目录,则保存所需</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-11</p>
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
    private static void getFiles(File dir, ExtendedFileFilter filter, LinkedList foundedFiles)
    {
        File fs[] = dir.listFiles(filter);
        if(fs == null)
        {
            return;
        }
           
        for(int i = 0; i < fs.length; i++)
        {
            if(fs[i].isFile())
            {
                foundedFiles.add(fs[i]);
            }               
        }

        for(int i = 0; i < fs.length; i++)
        {
            if(fs[i].isDirectory())
            {
                getFiles(fs[i], filter, foundedFiles);
            }                
        }
    }
}
