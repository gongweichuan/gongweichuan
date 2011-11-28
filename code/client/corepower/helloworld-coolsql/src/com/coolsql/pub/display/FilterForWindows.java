/*
 * 创建日期 2006-10-20
 */
package com.coolsql.pub.display;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author liu_xlin
 *该过滤器用于调用本地文件选择对话框,只允许选择jar和zip后缀的文件
 */
public class FilterForWindows implements FilenameFilter {
    private FileSelectFilter filter=null;
    public FilterForWindows()
    {
        this(null,null);
    }
    public FilterForWindows(String type)
    {
        this(type,null);
    }
    public FilterForWindows(String type,String des)
    {
        filter=new FileSelectFilter(type,des);
    }
    /* （非 Javadoc）
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    public boolean accept(File dir, String name) {
        return filter.accept(new File(dir,name));
    }

}
