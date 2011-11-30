/*
 * 创建日期 2006-6-2
 *
 */
package com.coolsql.pub.display;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author liu_xlin
 *文件类型过滤
 */
public class FileSelectFilter extends FileFilter {

	private String description=null;
	private String[] fileType=null;
	public FileSelectFilter()
	{
        this((String)null,null);
	}
	public FileSelectFilter(String type)
	{
		this(type,null);
	}
	public FileSelectFilter(String type,String des)
	{
		if(des==null||des.trim().equals(""))
		{
			description="";
		}else
		{
			description=des;
		}
		fileType=new String[1];
		if(type==null||type.trim().equals(""))
		{
			fileType[0]="";
		}else
		{
			fileType[0]=type;
		}	
	}
	public FileSelectFilter(String[] type,String des)
	{
		if(des==null||des.trim().equals(""))
		{
			description="";
		}else
		{
			description=des;
		}
		fileType=type;
	}
	/**
	 * eg:".class"  ".exe"
	 */
	public boolean accept(File f) {
		if(fileType==null||fileType.length<1)
		{
			return true;
		}
		if(f==null)
			return false;
		if(isContain(getFileType(f))||getFileType(f).equals("/@\\"))
			return true;
		else
			return false;    
	}
	/**
	 * 给定的文件后缀名是否在允许显示的清单之中
	 * @param suffix  --文件后缀，如：.exe    .class
	 * @return  --true:允许显示   false:不允许显示
	 */
	private boolean isContain(String suffix)
	{
	    for(int i=0;i<fileType.length;i++)
	    {
	        if(fileType[i].equals(suffix))
	            return true;
	    }
	    return false;
	}
	/* （非 Javadoc）
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 获取文件的后缀(如果为目录，返回/@\)
	 * @param f
	 * @return
	 */
    public static String getFileType(File f)
    {
    	if(f.isDirectory())
    		return "/@\\";
    	String name=f.getName();
    	name=name.toLowerCase();
    	if(name.lastIndexOf(".")==-1)
    	  return "";
    	else
    	  return name.substring(name.lastIndexOf("."),name.length()).toLowerCase();
    }
	/**
	 * @param description 要设置的 description。
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param fileType 要设置的 fileType。
	 */
	public void setFileType(String[] fileType) {
		this.fileType = fileType;
	}
}
