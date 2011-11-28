/*
 * 创建日期 2006-6-4
 *
 */
package com.coolsql.sql;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.DriverInfo;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.Schema;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 管理数据库的连接，释放,以及sql语句的执行，如果是查询sql，获取结果集并解析，然后生成bean
 */
public class ConnectionUtil {

    /**
     * key:current execute thread value:current statement
     */
    private static Map longTimeStatement = null;

    /**
     * 保存查询结果集,以及对应的Statement对象
     */
    private static Map st = null;
    static {
        st = Collections.synchronizedMap(new HashMap());
        longTimeStatement = Collections.synchronizedMap(new HashMap());
    }

    private ConnectionUtil() {
    }

    /**
     * 获取数据库连接，并设置该连接的相关属性
     */
    public static void getConnection(Bookmark bookmark)
            throws ConnectionException, ClassNotFoundException, SQLException, UnifyException {
        Connection con = connect(bookmark);
        bookmark.setConnection(con);
        bookmark.setAutoCommit(bookmark.isAutoCommit());
        try {
            bookmark.setDatabase(new Database(bookmark));
        } catch (Exception e) {
            bookmark.disconnect();   //如果初始化数据库对象（Database）出错，将断开数据库连接
        }
    }

    /**
     * Connect to database server, and get a connection.
     * 
     */
    public static Connection connect(Bookmark bookmark) throws SQLException,
            ClassNotFoundException, ConnectionException {
        LogProxy proxy = LogProxy.getProxy();
        proxy.info("Connecting to database : " + bookmark.getAliasName());
        try {
            DriverInfo jdbcDriver = bookmark.getDriver();
            String driverClass = jdbcDriver.getClassName();
            Driver driver;
            //实例化驱动程序类
            driver = (Driver) (LoadJar.getInstance()
                    .getDriverClass(driverClass).newInstance());

            if (driverClass != null) {
            	//Set login timeout
            	DriverManager.setLoginTimeout(Setting.getInstance().
                		getIntProperty(PropertyConstant.PROPERTY_VIEW_BOOKMARK_CONNECT_TIMEOUT, 60));
                //设置数据库连接所需要的信息
                Properties props = new Properties();
                props.put("user", bookmark.getUserName());
                props.put("password", bookmark.getPwd());
                
                if (jdbcDriver.getType().equals("ORACLE")) { //对于oracle数据库需要设置额外的两个属性
                    props.put("remarksReporting", "true");
                    props.put("includeSynonyms", "true");
                    props.put("resultSetMetaDataOptions ", 1);
                }

                /** -----------------------------------------*/
    			// identify the program name when connecting
    			// this is different for each DBMS.
    			String propName = null;
    			String url=bookmark.getConnectUrl();
    			if (url.startsWith("jdbc:oracle"))
    			{
    				propName = "v$session.program";
    				
    				// it seems that the Oracle 10 driver does not 
    				// add this to the properties automatically
    				// (as the drivers for 8 and 9 did)
    				String user = System.getProperty("user.name",null);
    				if (user != null) props.put("v$session.osuser", user);
    			}
    			else if (url.startsWith("jdbc:inetdae"))
    			{
    				propName = "appname";
    			}
    			else if (url.startsWith("jdbc:jtds"))
    			{
    				propName = "APPNAME";
    			}
    			else if (url.startsWith("jdbc:microsoft:sqlserver"))
    			{
    				// Old MS SQL Server driver
    				propName = "ProgramName";
    			}
    			else if (url.startsWith("jdbc:sqlserver:"))
    			{
    				// New SQL Server 2005 JDBC driver
    				propName = "applicationName";
    				if (!props.containsKey("workstationID"))
    				{
    					InetAddress localhost = InetAddress.getLocalHost();
    					String localName = (localhost != null ? localhost.getHostName() : null);
    					if (localName != null)
    					{
    						props.put("workstationID", localName);
    					}
    				}
    			}
    			if (propName != null && !props.containsKey(propName))
    			{
    				String appName ="CoolSQL";
    				props.put(propName, appName);
    			}
                /** -----------------------------------------*/
                
                Connection connection = driver.connect(
                		url, props); //连接数据库
                if (connection == null) //获取连接失败
                {
                    throw new ConnectionException(
                            "Error: Driver returned a null connection: "
                                    + bookmark.toString());
                } else //获取连接成功，获取数据库相关信息
                {

                    DatabaseMetaData metaData = connection.getMetaData();
                    jdbcDriver.setDriverName(metaData.getDriverName()); //驱动程序名称
                    jdbcDriver.setDriverVersion(metaData.getDriverVersion());
                    jdbcDriver.setDbProductName(metaData
                            .getDatabaseProductName());
                    jdbcDriver.setDbPruductVersion(metaData
                            .getDatabaseProductVersion());
                    proxy.info("Connected to: " + bookmark.getAliasName());
                    return connection;
                }
            } else { //书签没有对应的数据库驱动类
                throw new ConnectionException(PublicResource
                        .getSQLString("database.connect.nodriver"));
            }
        } catch (SQLException e) {
            throw e;
        } catch(Throwable e)
        {
        	String msg="Error connecting to database. (" + e.getClass().getName() + " - " + e.getMessage() + ")";
        	LogProxy.errorLog(msg,e);
        	throw new SQLException(msg);
        }
    }

    /**
     * 断开数据库的连接
     * 
     * @param con
     * @throws SQLException
     */
    public static void disconnect(Connection con) throws SQLException {
        if (con == null)
            return;
        if(!con.getAutoCommit())
        {
        	String setting=Setting.getInstance().getProperty(
    				PropertyConstant.PROPERTY_VIEW_BOOKMARK_BEFORE_DISCONNECT ,"commit");
        	if(setting.equals("commit"))
        	{
        		con.commit();
        	}else
        	{
        		con.rollback();
        	}
        }
        con.close();
        System.out.println("disconnect!");
    }

    /**
     * 提交
     * 
     * @param con
     * @throws SQLException
     */
    public static void commit(Connection con) throws SQLException {
        con.commit();
    }

    /**
     * 回滚
     * 
     * @param con
     * @throws SQLException
     */
    public static void rollback(Connection con) throws SQLException {

        con.rollback();
    }

    /**
     * 获取当前数据库的所有模式名
     * @deprecated
     * @param con
     * @return
     * @throws SQLException
     * 
     */
    public static List getSchemas(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        if (!metaData.supportsSchemasInTableDefinitions()
				|| !metaData.supportsSchemasInDataManipulation()
				|| !metaData.supportsSchemasInIndexDefinitions())
			return new ArrayList();
        ResultSet set = metaData.getSchemas();
        List schemas = new ArrayList();
        try {
            while (set.next()) {
                schemas.add(StringUtil.trim(set.getString(1)));
            }
        } finally {
            if (set != null)
                set.close();
        }
        return schemas;
    }
    /**
     * 根据分类获取，分类下的模式对象
     * @param bookmark
     * @param catalog
     * @return
     * @throws UnifyException
     * @throws SQLException
     */
    public static Schema[] getSchemas(Bookmark bookmark,String catalog) throws UnifyException, SQLException
    {
    	return bookmark.getDbInfoProvider().getSchemas(catalog);
    }
    public static SQLResults execute(Bookmark bookmark, Connection con,
            Entity entity[], String sql, int numberOfRowsPerPage)
            throws SQLException {
        long startTime = System.currentTimeMillis();

        LogProxy log = LogProxy.getProxy();
        log.debug("SQL (" + bookmark.getAliasName() + ") [" + sql + "]");
        Statement statement = con.createStatement();
        //保存当前执行的线程所对应的Statement对象
        longTimeStatement.put(Thread.currentThread(), statement);
        try {
        	long costTime;
            SQLResults results;
            if(numberOfRowsPerPage>0&&numberOfRowsPerPage!=Integer.MAX_VALUE)
            	statement.setMaxRows(numberOfRowsPerPage+1); //max rows was setted as the value of numberOfRowsPerPage first
            if (statement.execute(sql)) {
            	costTime=System.currentTimeMillis() - startTime;
                //执行完毕后，将当前线程对应的键值对删除

                ResultSet set = statement.getResultSet();
                try {
                    results = SQLStandardResultSetResults.create(set, bookmark,
                            sql, entity, numberOfRowsPerPage);
                } finally {
                    set.close();
                }
            } else {
            	costTime=System.currentTimeMillis() - startTime;
                //执行完毕后，将当前线程对应的键值对删除
                longTimeStatement.remove(Thread.currentThread());

                int updates = statement.getUpdateCount();
                results = new SQLUpdateResults(updates);
                results.setSql(sql);
            }
            log.debug("Success: result set displayed");
            if (results != null) {
                results.setTime(startTime);//设置执行时间
                results.setCostTime(costTime); //设置执行所花费时间
            }
            return results;
        } finally {
            longTimeStatement.remove(Thread.currentThread());
            statement.close();           
        }
    }
    public static ResultSet executeQuery(Bookmark bookmark, String sql) throws UnifyException, SQLException
    {
    	return executeQuery(bookmark,sql,false);
    }
    /**
     * Execute specified SQL statement. User can specify the flag whether getting all row data.
     * 
     * @param bookmark --Bookmark object.
     * @param sql --SQL statement
     * @param isQueryAll --It indicates whether querying all row data.
     *  The true value means that the statement used for executing will not invoke setMaxRows() method. 
     * @return ResultSet 
     */
    public static ResultSet executeQuery(Bookmark bookmark, String sql, boolean isQueryAll)
            throws UnifyException, SQLException {
        LogProxy log = LogProxy.getProxy();
        log.debug("SQL (" + bookmark.getAliasName() + ") [" + sql + "]");

        Connection con = bookmark.getConnection();
        Statement statement = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        longTimeStatement.put(Thread.currentThread(), statement);
        int numberOfRowsPerPage=bookmark.getDbInfoProvider().getNumberOfRowsPerPage();
        if(!isQueryAll)
        {
	        if(numberOfRowsPerPage>0&&numberOfRowsPerPage!=Integer.MAX_VALUE)
	        	statement.setMaxRows(numberOfRowsPerPage+1);
        }
        if (statement.execute(sql)) { //sql为查询动作
            longTimeStatement.remove(Thread.currentThread());

            ResultSet set = statement.getResultSet();
            log.debug("execute successfully!");
            regStatement(set, statement);
            return set;
        } else { //sql为修改更新类型
            longTimeStatement.remove(Thread.currentThread());

            statement.close();
            throw new UnifyException(PublicResource
                    .getSQLString("sql.execute.sqltype.queryonly"));
        }
    }
    

    /**
     * 根据结果集找到对应的Statement对象，然后将其关闭
     * 
     * @param set
     *            --结果集对象，调用此方法后，该结果集对象将失效
     * @throws SQLException
     */
    public static void closeStatement(ResultSet set) throws SQLException {
        Statement statement = (Statement) st.remove(set);
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * 将每次查询的结果集登记在内存中，便于数据解析完成后，关闭相关的对象
     * 
     * @param set
     * @param statement
     */
    public static void regStatement(ResultSet set, Statement statement) {
        System.out.println(":" + longTimeStatement.size());
        st.put(set, statement);
    }

    /**
     * 检测结果集是否可以向后滚，该方法区别于next
     * 
     * @param set
     * @return
     * @throws SQLException
     */
    public static boolean hasNext(ResultSet set) throws SQLException {
        boolean tmp = set.next();
        if (tmp)
            set.previous();
        return tmp;
    }

    /**
     * 创建Statement对象后，在执行之前将该对象保存
     * 
     * @param th
     *            --执行长时间处理的线程
     * @param st
     *            --Statement对象
     */
    public static void addLongTimeStatement(Thread th, Statement st) {
        if (longTimeStatement.size() > 0) {
            LogProxy.message("出现长时间未关闭的Statement对象！", 1);
        }
        System.out.println("longTimeStatement:" + longTimeStatement.size());
        longTimeStatement.put(th, st);
    }

    /**
     * 执行完成后调用该方法
     * 
     * @param th
     *            --执行长时间处理的线程
     */
    public static void removeLongTimeStatement(Thread th) {
        longTimeStatement.remove(th);
    }

    /**
     * 将执行动作取消
     * 
     * @param th
     *            --执行长时间处理的线程
     * @throws SQLException
     */
    public static void quitLongTimeStatement(Thread th) throws SQLException {
        Statement st = (Statement) longTimeStatement.get(th);
        if (st != null)
        {
        	try
        	{
            	st.cancel();
        	}catch(Throwable t)
        	{
        		LogProxy.errorLog("cancel statement failed", t);
        	}
        	try
        	{
        		st.close();
        	}catch(Throwable t)
        	{
        		LogProxy.errorLog("closing statement is not successful", t);
        	}
        }
        removeLongTimeStatement(th);
    }
}
