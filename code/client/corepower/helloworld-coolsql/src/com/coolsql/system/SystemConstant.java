/**
 * 
 */
package com.coolsql.system;

import java.io.File;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-1-25 create
 */
public class SystemConstant {

	public final static String separator=File.separator; //目录分隔符
	public final static String userPath=System.getProperty("user.home")+separator+".coolsql"+separator;
	@SuppressWarnings("unchecked")
	public final static String lineSeparator=(String) java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
	/**
	 * 各个信息保存的文件
	 */
	//书签信息
	public static String bookmarkInfo =userPath+ "bookmarkinfo.xml";
	//最近执行sql信息
	public static String recentSqlPATH =userPath+ "recentsql"+separator;
	public static String recentSqlInfo =recentSqlPATH+"recentSql";
	
	//驱动信息
	public static String driversInfo = userPath+"driver.classpath";
	
	public static String extraFiles = userPath+"Extra.classpath";
	//sql编辑视图的编辑内容
	public static String sqlEditeInfo = userPath+"sqlEdite.txt";
	//收藏的sql被存放的文件路径
	public static String favoriteSQLFilePath=userPath+"favoriteSQL.dat";

	/**
	 * plugin folder that place files related to plugin
	 */
	public static String PLUGIN_FOLDER=userPath+separator;
	
	public static String SETTING_FILE=userPath+"System.setting";
	
	public static String LOGVIEW_LOGFILE=userPath+"system.log";
	
	public static String LAUNCH_INI=userPath+"launch.ini";
}
