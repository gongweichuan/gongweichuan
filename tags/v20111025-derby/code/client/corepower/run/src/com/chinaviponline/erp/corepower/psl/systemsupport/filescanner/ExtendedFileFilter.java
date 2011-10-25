/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.filescanner;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>文件名称：ExtendedFileFilter.java</p>
 * <p>文件描述：判断是否是文件名</p>
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
public class ExtendedFileFilter implements FileFilter
{
    /**
     * 正则表达式
     */
    private Pattern stringPattern;

    /**
     * 用正则表达式解析出文件路径
     */
    public static String regexStr = "^([a-zA-Z]\\:|\\)\\([^\\]+\\)*[^\\/:*?\"<>|]+.*$"; // 表达文件名

    /**
     * 构造函数
     * @param pattern
     * @param ignoreCase
     */
    public ExtendedFileFilter(String pattern, boolean ignoreCase)
    {
        stringPattern = null;
        
        // 修改过的
//        pattern=pattern.replaceAll(".","\\\\.");
//        pattern=pattern.replaceAll("*","\\\\.\\\\*");     
        
        pattern=pattern.replaceAll("\\.","~");        
//        System.out.println(pattern);
        
        pattern=pattern.replaceAll("~","\\\\.");        
//        System.out.println(pattern);
        
        pattern=pattern.replaceAll("\\*","~"); 
//        System.out.println(pattern);
        
        pattern=pattern.replaceAll("~","\\.\\*"); 
//        System.out.println(pattern);
        
        pattern=pattern.replaceAll("\\-","\\\\-");         
//        System.out.println(pattern);       
        
        stringPattern = createStringPattern(pattern, ignoreCase);
    }

    /**
     * 
     * 功能描述：测试指定抽象路径名是否应该包含在某个路径名列表中 
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept(File filePath)
    {
        //TODO 改写可能有错误
        boolean bDir = filePath.isDirectory();

        boolean bFile = false;
        Matcher matches = stringPattern.matcher(filePath.getName());
        bFile = matches.matches();

        return bDir || bFile;
    }

    /**
     * 
     * <p>功能描述：是否文件路径名 </p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param filePath
     * @param dir
     * @return
     */
    public boolean accept(File filePath, boolean dir)
    {
        boolean bFile = false;
        Matcher matches = stringPattern.matcher(filePath.getName());
        bFile = matches.matches();
        return bFile || dir;
    }

    /**
     * 
     * <p>功能描述：构造一个正则表达式的编译表示形式</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param pattern
     * @param ignoreCase
     * @return
     */
    protected Pattern createStringPattern(String pattern, boolean ignoreCase)
    {
        int flags=0;
        if(ignoreCase )
        {
            flags+= Pattern.CASE_INSENSITIVE;   //忽略大小写
        }
        flags+= Pattern.MULTILINE ;     // 多行模式
        flags+= Pattern.COMMENTS  ;     // 允许空白和注释
        
        Pattern strPattern= Pattern.compile(pattern, flags);
        return strPattern;
    }
}
