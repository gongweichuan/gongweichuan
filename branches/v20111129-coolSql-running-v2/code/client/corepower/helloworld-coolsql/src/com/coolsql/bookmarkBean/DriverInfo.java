/*
 * 创建日期 2006-6-2
 *
 */
package com.coolsql.bookmarkBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.coolsql.adapters.AdapterFactory;
import com.coolsql.main.frame.Launcher;
import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.URLBuilder;

/**
 * @author liu_xlin
 * 驱动程序信息类
 */
public class DriverInfo implements Serializable{
	private static final long serialVersionUID = 5584580757674025281L;
	/**
	 * 驱动程序类名
	 */
	private String className = null;
    /**
     * 类型
     */
	private String type = null;
    /**
     * 该驱动的别名
     */
	private String driverName = null;
    /**
     * 驱动程序版本
     */
    private String driverVersion=null;
    /**
     * 该类所处代码源
     */
	private String filePath = null;
	/**
	 * 该驱动所对应的连接方式
	 */
	private String urlPattern=null;
	
	/**
	 * 数据库产品名称
	 */
	private String dbProductName=null;
	/**
	 * 数据库版本
	 */
    private String dbPruductVersion=null;   

    /**
     * 数据库连接url中的参数（datasource或者dbname、address、port）
     */
    private Map<String,String> params=null;
	public DriverInfo() {
		this(null, null, null, null,null,null);
	}
    public DriverInfo(String className)
    {
    	params=new HashMap<String,String>();
    	this.className=className;
    	if(!Launcher.isInitializing)
    	    createInfoByClass(className);
    }
	public DriverInfo(String driverName, String className, String type,
			String filePath,
			String urlPattern,String version) {
		if (driverName != null) {
			this.driverName = driverName;
		}
		if (className != null) {
			this.className = className;
		}
		if (type != null) {
			this.type = type;
		}
		if (filePath != null) {
			this.filePath = filePath;
		}
		params=new HashMap<String,String>();
		if(urlPattern!=null)
		{
			this.urlPattern=urlPattern;
			AdapterFactory instance=AdapterFactory.getInstance();
			Map<String,String> map=instance.getAdapter(type).getDefaultConnectionParameters();
			createParams(URLBuilder.getVariableNames(urlPattern),map);
		}
	}
	/**
	 * 通过类名来创建数据库驱动信息
	 * @param className
	 */
	protected void createInfoByClass(String className)
    {
       if(className==null||className.trim().equals(""))
       {
       	 return ;
       }
       this.className=className;
       AdapterFactory instance=AdapterFactory.getInstance();
       this.setFilePath(LoadJar.getInstance().getLibPath(className));
       this.setType(instance.getAdapterType(className));
       this.setUrlPattern(instance.getURLPattern(className));
       this.setDriverName("");
       this.setDriverVersion("");
       
       params.clear();
       String[] args=URLBuilder.getVariableNames(urlPattern);
       Map<String,String> map=instance.getAdapter(type).getDefaultConnectionParameters();
       this.createParams(args,map);
    }
    /**
     * 初始化数据库连接参数
     * @param args
     */
    protected void createParams(String[] args,Map<String,String> defaultMap)
    {
    	if(args==null||defaultMap==null)
    		return ;
    	for(int i=0;i<args.length;i++)
    	{
    		if(defaultMap.containsKey(args[i]))
    		    params.put(args[i],defaultMap.get(args[i]));
    		else
    			params.put(args[i],"");
    	}
    }
    /**
     * 是否为Mysql数据库
     * @return
     */
    public boolean isMysql()
    {
    	return "MYSQL".equals(type);
    }
    public boolean isJdbcOdbc()
    {
    	return "JDBC_ODBC_BRIDGE".equals(type);
    }
	/**
	 * @return 返回 className。
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            要设置的 className。
	 */
	public void setClassName(String className) {
		this.className = className;
		if(!Launcher.isInitializing)
		    createInfoByClass(className);
	}

	/**
	 * @return 返回 driverName。
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName
	 *            要设置的 driverName。
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 filePath。
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            要设置的 filePath。
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return 返回 type。
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            要设置的 type。
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return 返回 urlPattern。
	 */
	public String getUrlPattern() {
		return urlPattern;
	}
	/**
	 * @param urlPattern 要设置的 urlPattern。
	 */
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	/**
	 * @return 返回 version。
	 */
	/**
	 * @return 返回 params。
	 */
	public Map<String,String> getParams() {
		return params;
	}
	/**
	 * @param params 要设置的 params。
	 */
	public void setParams(Map<String,String> params) {
	    this.params.clear();
		this.params = params;
	}
	/**
	 * 获取原始URL
	 * @return
	 */
	public String getOriginalURL()
	{
	    AdapterFactory instance=AdapterFactory.getInstance();
	    return instance.getURLPattern(className);
	}
    /**
     * @return 返回 dbProductName。
     */
    public String getDbProductName() {
        return dbProductName;
    }
    /**
     * @param dbProductName 要设置的 dbProductName。
     */
    public void setDbProductName(String dbProductName) {
        this.dbProductName = dbProductName;
    }
    /**
     * @return 返回 dbPruductVersion。
     */
    public String getDbPruductVersion() {
        return dbPruductVersion;
    }
    /**
     * @param dbPruductVersion 要设置的 dbPruductVersion。
     */
    public void setDbPruductVersion(String dbPruductVersion) {
        this.dbPruductVersion = dbPruductVersion;
    }
    /**
     * @return 返回 driverVersion。
     */
    public String getDriverVersion() {
        return driverVersion;
    }
    /**
     * @param driverVersion 要设置的 driverVersion。
     */
    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }
}
