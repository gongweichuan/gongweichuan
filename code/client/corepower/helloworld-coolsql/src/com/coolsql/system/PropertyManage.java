/**
 * 
 */
package com.coolsql.system;

import java.io.File;

import com.coolsql.pub.parse.xml.XMLBeanUtil;
import com.coolsql.pub.parse.xml.XMLException;

/**
 * 属性设置信息的管理类，用于读取系统的设置信息。
 * @author kenny liu
 *
 * 2007-10-30 create
 */
public class PropertyManage {

	private static SystemProperties sp=null;  //系统属性
	
	public final static String FILE_SYSTEMPROPERTY=SystemConstant.userPath+"SystemProperty.xml";
	
	private static LaunchInitProperty launchPros;
	private PropertyManage()
	{
		
	}
	/**
	 * 加载系统属性信息
	 * @throws XMLException 如果解析系统属性配置文件错误，将会抛出此异常类型
	 */
	public static void loadSystemProperty() throws XMLException
	{
		if(sp==null)
		{
			XMLBeanUtil xml = new XMLBeanUtil();
			Object ob=xml.importBeanFromXML(FILE_SYSTEMPROPERTY);
			if(ob==null)  //文件不存在
			{
				sp= new SystemProperties();
				return;
			}
			if(!(ob instanceof SystemProperties))
				throw new XMLException("系统属性文件配置错误，错误原因：配置类类型错误,错误类型："+ob.getClass().getName());
			sp=(SystemProperties)ob;
		}
	}
	/**
	 * 获取系统属性信息。
	 * @return 系统属性对象
	 * 
	 */
	public static SystemProperties getSystemProperty() 
	{
		return sp;
	}
	/**
	 * 保存系统属性信息
	 * @throws XMLException 如果保存系统属性对象出错，将抛出此异常类型
	 */
	public static void saveSystemProperty() throws XMLException
	{
		if(sp==null)
			return;
		XMLBeanUtil xml = new XMLBeanUtil();
		xml.exportBeanToXML(new File(FILE_SYSTEMPROPERTY), sp, "systemproperty");
	}
	public static LaunchInitProperty getLaunchSetting()
	{
		if(launchPros==null)
		{
			launchPros=new LaunchInitProperty();
		}
		return launchPros;
	}
	public static void saveLaunchSetting()
	{
		getLaunchSetting().saveToFile();
	}
}
