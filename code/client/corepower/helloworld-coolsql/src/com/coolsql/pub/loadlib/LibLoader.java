/*
 * 创建日期 2006-6-1
 *
 */
package com.coolsql.pub.loadlib;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author liu_xlin
 *
 */
public class LibLoader extends URLClassLoader {
	/**
	 * @param urls
	 */
	public LibLoader() {
		   super(new URL[0]);		
	}
	/**
	 * @param urls
	 */
	public LibLoader(URL[] urls) {
		   super(urls);		
	}
	/**
	 * @param urls
	 * @param parent
	 */
	public LibLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	/**
	 * 根据指定的URL，装载相应的资源库
	 */
    public void addURL(URL url)
    {
    	super.addURL(url);
    }
    /**
     * 装载多个资源库
     * @param url
     */
    public void addURL(URL url[])
    {
    	if(url!=null)
    	{
    		for(int i=0;i<url.length;i++)
    		{
    			super.addURL(url[i]);
    		}
    	}
    }

}
