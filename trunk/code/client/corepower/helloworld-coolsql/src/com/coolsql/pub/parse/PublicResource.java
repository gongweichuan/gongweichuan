/*
 * 创建日期 2006-6-1
 *
 */
package com.coolsql.pub.parse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.util.StringUtil;

/**
 * @author liu_xlin
 * 读取资源信息
 */
public class PublicResource {
    //图形展示信息
	private static ResourceBundle bundle = null;
	//db处理时所需资源
	private static ResourceBundle sqlBundle = null;
	
	//公共界面显示的资源
	private static ResourceBundle utilBundle = null;
	
	//系统信息资源
	private static ResourceBundle systemBundle=null;
	private static File file=null;
	private static long old=-1;
	private PublicResource() {
	}

	/**
	 * initalize resource
	 *  
	 */
	public static void changeBundle(String respath) {
		bundle = ResourceBundle.getBundle(respath);
		file=new File(transPath(respath));
		old=file.lastModified();
	}
    /**
     * 返回一个资源绑定实例
     * @return
     */
	private static ResourceBundle getBundle() {
		
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("com.coolsql.resource.MyResource", Locale.getDefault());
			file=new File("/com/coolsql/resource/MyResource.properties");
			old=file.lastModified();
		}else
		{
			if(fileChanged())
				changeBundle("com.coolsql.resource.MyResource");
		}
		return bundle;
	}
	private static ResourceBundle getSqlBundle()
	{
	    if(sqlBundle==null)
	    {
	        sqlBundle=ResourceBundle.getBundle("com.coolsql.resource.sqlResource", Locale.getDefault());
	    }
	    return sqlBundle;
	}
	private static ResourceBundle getSystemBundle()
	{
	    if(systemBundle==null)
	    {
	        systemBundle=ResourceBundle.getBundle("com.coolsql.resource.systemResource", Locale.getDefault());
	    }
	    return systemBundle;
	}
	private static ResourceBundle getUtilBundle()
	{
	    if(utilBundle==null)
	    {
	        utilBundle=ResourceBundle.getBundle("com.coolsql.resource.utilResource", Locale.getDefault());
	    }
	    return utilBundle;
	}
	/**
	 * 判断资源文件是否改变
	 * @return
	 */
    protected static boolean fileChanged()
    {
    	if(old!=file.lastModified())
    	{
    		return true;
    	}else
    		return false;
    }
	/**
	 * get value by key in resource file
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		try {
			return getBundle().getString(key);
		} catch (java.util.MissingResourceException e) {
			JOptionPane.showMessageDialog(GUIUtil.getMainFrame(),"can't find resource,key=" + key,
					"error", 0);
			return "";
		}
	}
	/**
	 * Retrieve the localized string for the passed key and format it with the
	 * @param key --Key to retrieve string for.
	 * @param args --Any string arguments that should be used as values to 
     *                  parameters found in the localized string. 
	 * @return
	 */
	public static String getString(String key,Object...args)
	{
		String msg=getString(key);
		if(StringUtil.isEmpty(msg))
			return "";
		return MessageFormat.format(msg, args);
	}
	public static String getSQLString(String key) {
		try {
			return getSqlBundle().getString(key);
		} catch (java.util.MissingResourceException e) {
			JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), "can't find resource,key=" + key,
					"error", 0);
			return "";
		}
	}
	public static String getSQLString(String key,Object...args) {
		String msg=getSQLString(key);
		if(StringUtil.isEmpty(msg))
			return "";
		return MessageFormat.format(msg, args);
	}
	public static String getUtilString(String key) {
		try {
			return getUtilBundle().getString(key);
		} catch (java.util.MissingResourceException e) {
			JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), "can't find resource,key=" + key ,
					"error", 0);
			return "";
		}
	}
	public static String getUtilString(String key,Object...args) {
		String msg=getUtilString(key);
		if(StringUtil.isEmpty(msg))
			return "";
		return MessageFormat.format(msg, args);
	}
	public static String getSystemString(String key) {
		try {
			return getSystemBundle().getString(key);
		} catch (java.util.MissingResourceException e) {
			JOptionPane.showMessageDialog(GUIUtil.getMainFrame(),"can't find resource,key=" + key,
					"error", 0);
			return "";
		}
	}
	public static ImageIcon getIcon(String key)
	{
	    String path=getString(key);
	    URL url=PublicResource.class.getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	public static ImageIcon getSQLIcon(String key)
	{
	    String path=getSQLString(key);
	    URL url=PublicResource.class.getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	public static ImageIcon getUtilIcon(String key)
	{
	    String path=getUtilString(key);
	    URL url=PublicResource.class.getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	public static ImageIcon getSystemIcon(String key)
	{
	    String path=getSystemString(key);
	    URL url=PublicResource.class.getResource(path);
        if(url != null)
            return new ImageIcon(url);
        return null;
	}
	private static String transPath(String str)
	{
		if(str==null||str.equals(""))
			return "";
		String tmp=str.replace('.','/');
		tmp=tmp+".properties";
		return tmp;
	}
	public static String getOkButtonLabel()
	{
		return getString("button.label.ok");
	}
	public static String getCancelButtonLabel()
	{
		return getString("button.label.cancel");
	}
	/**
	 * The label for Close button.
	 */
	public static String getCloseButtonLabel() {
		return getString("button.label.close");
	}
	/**
	 * 将给定路径下的文件以输入流的方式打开。
	 * @param path  --被访问的文件路径
	 * @return  
	 */
	public static InputStream openInputStream(String path)
	{
	    File file=new File(path);
	    if(file.exists())
	    {
            try {
                return file.toURL().openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
	    }
	    return PublicResource.class.getResourceAsStream(path);
	}
	public static Properties getPropertiesAssociateWithLocal(String baseName,Locale l)
	{
    	ResourceBundle rb=ResourceBundle.getBundle(baseName, l);
    	Enumeration<String> enu=rb.getKeys();
    	
    	Properties resourceProperty=new Properties();
    	while(enu.hasMoreElements())
    	{
    		String key=enu.nextElement();
    		resourceProperty.put(key, rb.getString(key));
    	}
    	
    	return resourceProperty;
	}
}
