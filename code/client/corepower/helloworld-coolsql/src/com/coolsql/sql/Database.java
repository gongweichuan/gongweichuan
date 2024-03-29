/*
 * 创建日期 2006-9-9
 */
package com.coolsql.sql;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.coolsql.adapters.DatabaseAdapter;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.util.SqlUtil;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.ColumnImpl;
import com.coolsql.sql.model.DataType;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.EntityFactory;
import com.coolsql.sql.model.EntityImpl;
import com.coolsql.sql.model.ForeignKey;
import com.coolsql.sql.model.ForeignKeyImpl;
import com.coolsql.sql.model.Schema;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 数据库信息bean
 */
public class Database {
	/**
	 * 数据库元数据信息通过该对象获取。
	 */
	private ISQLDatabaseMetaData dbMetaData=null;
    /**
     * 数据库适配器
     */
    private DatabaseAdapter databaseAdapter;

    /**
     * 数据库所对应的书签对象
     */
    private Bookmark bookmark;

    /**
     * 数据库所具有的实体类型
     */
    private List<String> entityTypes;

    /**
     * 数据类型的数据映射
     * key:java类型（String）  value：DataType对象
     */
    private Map<String,DataType> dataTypeMap;
    
    //max number of rows displayed in table.
    private int numberOfRowsPerPage=Setting.getInstance()
    	.getIntProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE, 200);//
    private static final String ALL_TABLE_TYPES[] = { SqlUtil.TABLE, SqlUtil.VIEW,
    	SqlUtil.SEQUENCE };

    private static final List<String> STANDARD_TABLE_TYPES;
 
    /**
     * 初始化标准表类型
     */
    static {
        STANDARD_TABLE_TYPES = Collections.synchronizedList(new ArrayList<String>());
        int i = 0;
        for (int length = ALL_TABLE_TYPES != null ? ALL_TABLE_TYPES.length : 0; i < length; i++)
            STANDARD_TABLE_TYPES.add(ALL_TABLE_TYPES[i]);

    }
    public ISQLDatabaseMetaData getDatabaseMetaData()
    {
    	return dbMetaData;
    }
//    public Database() throws UnifyException, SQLException {
//        this(null);
//    }

    public Database(Bookmark bookmark) throws UnifyException, SQLException {
        this.bookmark = bookmark;
        if (bookmark != null)
        {
            databaseAdapter = bookmark.getAdapter();
        }
        else
            databaseAdapter = null;
        
        if (bookmark != null)
        {
        	dbMetaData=new SQLDatabaseMetaData(bookmark);
            dataTypeMap=new HashMap<String,DataType>();
            DataType[] dataType=getTypes();
            for(int i=0;i<dataType.length;i++)
            {
                dataTypeMap.put(dataType[i].getDatabaseTypeName().toUpperCase(),dataType[i]);
            }
        }
        //when the value of property changed,listener will be performed.
        Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
        {

			public void propertyChange(PropertyChangeEvent evt) {
				numberOfRowsPerPage=new Integer(evt.getNewValue().toString()).intValue();
			}
        	
        }
        , PropertyConstant.PROPERTY_VIEW_RESULTSET_NUMBEROFROWSPERPAGE);
    }

    public void dispose() {
        if (entityTypes != null)
        {
            entityTypes.clear();
            entityTypes=null;
        }
        if(dataTypeMap!=null)
        {
            dataTypeMap.clear();
            dataTypeMap=null;
        }
        bookmark = null;
        databaseAdapter = null;
    }
    public String adjustObjectnameCase(String name)
    {
    	name=StringUtil.trim(name);
    	if(name.equals(""))
    		return name;
		try
		{
			if (getDatabaseMetaData().storesUpperCaseIdentifiers())
			{
				return name.toUpperCase();
			}
			else if (getDatabaseMetaData().storesLowerCaseIdentifiers())
			{
				return name.toLowerCase();
			}
		}
		catch (Exception e)
		{
			LogProxy.errorLog("adjustObjectnameCase failed",e);
		}
		return name;
    }
    /**
     * 根据给定的java类型，获取对应当前数据库支持的数据类型
     * @param javaType  --java类型
     * @return   --数据类型对象。
     */
    public DataType getDataType(String typeName)
    {
        if(dataTypeMap==null||typeName==null)
            return null;
        
        return (DataType)dataTypeMap.get(typeName.toUpperCase());
    }
    /**
     * 获取数据库的用户名
     * 
     * @return
     * @throws NotConnectedException
     * @throws SQLException
     */
    public String getUsername() throws UnifyException, SQLException {
    	return dbMetaData.getUserName();
    }

    /**
     * 获取数据库可能包含的所有实体类型：获取所连数据库中所有的实体类型，具体从数据库适配器中获取信息
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unused")
    private Set<String> initializeEntityTypes(Connection connection)
            throws SQLException {
        Set<String> set = new HashSet<String>(); //避免出现重复的类型
        if (databaseAdapter.getShowTableQuery(null,bookmark.getUserName()) != null) //如果适配器中无表的查询定义，则意味着无table类型。下同
            set.add(SqlUtil.TABLE);
        else if (databaseAdapter.getShowViewQuery(null,bookmark.getUserName()) != null)
            set.add(SqlUtil.VIEW);
//        else if (databaseAdapter.getShowSequenceQuery(null,bookmark.getUserName()) != null)
//            set.add(SqlUtil.SEQUENCE);
        else if(databaseAdapter.getShowSynonymsQuery(null,bookmark.getUserName())!=null)
        	set.add(SqlUtil.SYNONYM);
        DatabaseMetaData metaData = connection.getMetaData();
        
        ResultSet resultSet=null;
        try
        {
        	
	        for (resultSet = metaData.getTableTypes(); resultSet.next();) {
	            String type = resultSet.getString(com.coolsql.pub.util.SqlUtil.TABLE_METADATA_TABLE_TYPE);
	            if (type != null)
	                type = type.trim();
	            
	            set.add(type);
	        }
        }finally
        {
        	if(resultSet!=null)
        		resultSet.close();
        }
        return set;
    }

    /**
     * 获取数据库的基本信息（产品名称 数据库版本）
     * 
     * @return
     * @throws SQLException
     */
    public String getInformation() throws SQLException {
//        try {
//            DatabaseMetaData metaData = getMetaData();
//            return metaData != null ? metaData.getDatabaseProductName() + " "
//                    + metaData.getDatabaseProductVersion() : null;
//        } catch (UnifyException e) {
//            return "";
//        }
        return dbMetaData.getDatabaseProductVersion();
    }

    /**
     * 获取数据库的指定模式名下，type类型的实体数组
     */
    public Entity[] getEntities(Bookmark bookmark, Schema schema, String[] type)
            throws SQLException, UnifyException {
        Connection connection = bookmark.getConnection();
        Entity result[] = getEntities(bookmark, connection, schema, type);
        return result != null ? result : new Entity[0];
    }
    public Entity[] getEntities(Bookmark bookmark, String catalog, String schema, String[] type)
    	throws SQLException, UnifyException {
		Connection connection = bookmark.getConnection();
		Schema sch = new Schema(catalog, schema);
		Entity result[] = getEntities(bookmark, connection, sch, type);
		return result != null ? result : new Entity[0];
	}

    protected Entity[] getEntities(Bookmark bookmark, Connection connection,
            Schema schema, String[] type) throws SQLException {
        List<Entity> list = new ArrayList<Entity>();
        String types[] = type != null ? type : dbMetaData.getTableTypes();

        list.addAll(getEntitiesList(bookmark, connection, types, schema));
        for (int i = 0; i < types.length; i++) {
            list
                    .addAll(getSynonymsList(bookmark, connection, types[i],
                            schema));
        }

        return (Entity[]) list.toArray(new Entity[list.size()]);
    }

    /**
     * 同上
     */
    protected List<Entity> getEntitiesList(Bookmark bookmark, Connection connection,
            String[] type, Schema schema) throws SQLException {
        List<Entity> list = new ArrayList<Entity>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet set= metaData.getTables(schema==null?null:schema.getCatalog(), schema == null ? null : schema
                    .getName(), "%", type);
        while (set.next()) {
            String tempSchema = StringUtil.trim(set.getString(2));

            String tableName = StringUtil.trim(set.getString(3));

            String entityType = StringUtil.trim(set.getString(4));
            
            String remark = StringUtil.trim(set.getString(5));
            if (tableName != null && tableName.length() > 0) {
                Entity entity = EntityFactory.getInstance().create(bookmark,schema.getCatalog(),
                        tempSchema, tableName, entityType, remark, false);
                if (entity != null)
                    list.add(entity);
            }
        }
        set.close();
        if (list.size() > 0)
            return list;
        SQLResultSetResults results = null;

        if (type == null)
            return list;
        for (int n = 0; n < type.length; n++) {
            String sql = getSQL(bookmark, type[n], schema);
            if (sql != null) {
                results = (SQLResultSetResults) execute(connection, sql,
                        0);
                int i = 1;
                for (int size = results != null ? results.getRowCount() : 0; i <= size; i++) {
                    String schemaName = results.getColumnCount() != 1 ? results
                            .getElement(1, i).toString() : schema.getName();
                    if (schemaName != null)
                        schemaName = schemaName.trim();
                    String tableName = results.getColumnCount() != 1 ? results
                            .getElement(2, i).toString() : results.getElement(
                            1, i).toString();
                    if (tableName != null && tableName.length() > 0) {
                        Entity entity = EntityFactory.getInstance()
                                .create(bookmark,schema.getCatalog(), schemaName, tableName,
                                        type[n], "", false);
                        if (entity != null)
                            list.add(entity);
                    }
                }

            }
        }
        return list;
    }

    /**
     * 查询实体信息
     */
    public List<Entity> queryEntities(Bookmark bookmark, Connection connection,String catalog,
            String schema, String entityName, String[] type)
            throws UnifyException, SQLException {
        List<Entity> list = new ArrayList<Entity>();
        DatabaseMetaData metaData = connection.getMetaData();
        String[] types = type != null ? type : getDatabaseMetaData().getTableTypes();

//        if (metaData.supportsSchemasInTableDefinitions()) //如果支持模式定义
        ResultSet set  = metaData.getTables(catalog, schema,
                    entityName, types);
//        else
//            //不支持模式定义
//            set = metaData.getTables(null, null, entityName, types);
        while (set.next()) {
        	String tempCatalog=StringUtil.trim(set.getString(com.coolsql.pub.util.SqlUtil.TABLE_METADATA_TABLE_CATALOG));
            String tempSchema = set.getString(com.coolsql.pub.util.SqlUtil.TABLE_METADATA_TABLE_SCHEM);
            tempSchema = tempSchema != null ? tempSchema.trim() : "";

            String tableName = set.getString(com.coolsql.pub.util.SqlUtil.TABLE_METADATA_TABLE_NAME);
            tableName = tableName != null ? tableName.trim() : "";

            String typeName = set.getString(com.coolsql.pub.util.SqlUtil.TABLE_METADATA_TYPE_NAME);
            
            String remark =set.getString(com.coolsql.pub.util.SqlUtil.TABLE_METADATA_REMARK);
            if (tableName != null && tableName.length() > 0) {
                Entity entity = EntityFactory.getInstance().create(bookmark,tempCatalog,
                        tempSchema, tableName, typeName, remark, false);
                if (entity != null)
                    list.add(entity);
            }
        }
        set.close();
        if (list.size() > 0) //如果能够查到，则直接返回，如果未正常查到，通过数据库特定信息表获取相关信息
            return list;
        types = getDatabaseMetaData().getTableTypes();
        for (int i = 0; i < types.length; i++) {
            list.addAll(getLocalDBEntity(bookmark, connection,catalog, schema,
                    entityName, types[i]));
        }
        return list;
    }

    /**
     * Get entity list by database adapter.
     */
    public List<Entity> getLocalDBEntity(Bookmark bookmark, Connection connection,String catalog,
            String schema, String type) throws SQLException {
        List<Entity> list = new ArrayList<Entity>();
        SQLResultSetResults results = null;
        String sql = getSQL(bookmark,catalog, type, schema);
        if (sql != null) {
            results = (SQLResultSetResults) execute(connection, sql,
                    0);
            int i = 1;
            for (int size = results != null ? results.getRowCount() : 0; i <= size; i++) {
                String schemaName = results.getColumnCount() != 1 ? results
                        .getElement(1, i).toString() : schema;
                if (schemaName != null)
                    schemaName = schemaName.trim();
                String tableName = results.getColumnCount() != 1 ? results
                        .getElement(2, i).toString() : results.getElement(1, i)
                        .toString();
                if (tableName != null && tableName.length() > 0) {
                    Entity entity = EntityFactory.getInstance().create(
                            bookmark, catalog,schemaName, tableName, type, "", false);
                    if (entity != null)
                        list.add(entity);
                }
            }

        }
        return list;
    }

    public List<Entity> getLocalDBEntity(Bookmark bookmark, Connection connection,String catalog,
            String schema, String entityName, String type) throws SQLException {
        List<Entity> list = new ArrayList<Entity>();
        SQLResultSetResults results = null;
        String sql = getSQL(bookmark, type, schema, entityName);
        if (sql != null) {
            results = (SQLResultSetResults) execute(connection, sql,
                    0);
            int i = 1;
            for (int size = results != null ? results.getRowCount() : 0; i <= size; i++) {
                String schemaName = results.getColumnCount() != 1 ? results
                        .getElement(1, i).toString() : schema;
                if (schemaName != null)
                    schemaName = schemaName.trim();
                String tableName = results.getColumnCount() != 1 ? results
                        .getElement(2, i).toString() : results.getElement(1, i)
                        .toString();
                if (tableName != null && tableName.length() > 0) {
                    Entity entity = EntityFactory.getInstance().create(
                            bookmark, catalog,schemaName, tableName, type, "", false);
                    if (entity != null)
                        list.add(entity);
                }
            }

        }
        return list;
    }

    /**
     * 查询所有满足查询条件的列信息,返回结果只包含了列名、所属实体，所属模式、数据类型，长度、scale
     * @throws SQLException
     */
    public List<Column> queryColumns(Bookmark bookmark, Connection connection,String catalog,
            String schema, String entity, String columnName)
            throws SQLException {
        List<Column> list = new ArrayList<Column>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = null;

        schema = getValidateSQlWord(schema);
        entity = getValidateSQlWord(entity);
        resultSet = metaData.getColumns(catalog, schema, entity, columnName);
        try {
            while (resultSet.next()) {

                String schemaName = resultSet
                        .getString(EntityImpl.COLUMN_METADATA_SCHEMA);
                String entityName = resultSet
                        .getString(EntityImpl.COLUMN_METADATA_TABLE);

                //创建列所属实体
                Entity entityData = EntityFactory.getInstance().create(
                        bookmark, catalog,schemaName, entityName, SqlUtil.TABLE, "", false);
                ColumnImpl column = new ColumnImpl(
                        entityData,
                        resultSet
                                .getString(EntityImpl.COLUMN_METADATA_COLUMN_NAME),
                        resultSet
                                .getString(EntityImpl.COLUMN_METATDATA_TYPE_NAME),
                        resultSet.getInt(EntityImpl.COLUMN_METATDATA_DATA_TYPE),
                        resultSet
                                .getInt(EntityImpl.COLUMN_METADATA_COLUMN_SIZE),
                        resultSet
                                .getInt(EntityImpl.COLUMN_METADATA_DECIMAL_DIGITS),
                        "YES"
                                .equalsIgnoreCase(resultSet
                                        .getString(EntityImpl.COLUMN_METADATA_IS_NULLABLE)),
                        resultSet
                                .getInt(EntityImpl.COLUMN_METADATA_ORDINAL_POSITION),
                        resultSet.getString(EntityImpl.COLUMN_METADATA_REMARKS),
                        resultSet.getInt(EntityImpl.COLUMN_METADATA_NUM_PREC_RADIX),
                        resultSet.getInt(EntityImpl.COLUMN_METADATA_CHAR_OCTET_LENGTH),
                        resultSet.getString(EntityImpl.COLUMN_METADATA_COLUMN_DEF),
                        resultSet.getInt(EntityImpl.COLUMN_METADATA_NULLABLE));
                list.add(column);
            }
        } finally {
            resultSet.close();
        }
        return list;
    }

    /**
     * 查询指定目录的列的具体信息，查询结果只允许得到一条结果，
     * 
     * @param bookmark
     * @param connection
     * @param schema
     * @param entity
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Column queryColumnDetail(Bookmark bookmark, Connection connection,String catalog,
            String schema, String entity, String columnName)
            throws SQLException {
        List<Column> list = queryColumns(bookmark, connection, catalog,schema, entity,
                columnName);
        if (list.size() != 1) //如果结果不是唯一，那么返回null值
            return null;

        ColumnImpl columnData = (ColumnImpl) list.get(0);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet set = metaData.getPrimaryKeys(catalog, schema, entity); //获取实体的主键与索引信息
        while (set.next()) {
            String name = set.getString(4); //列名
            short keySequence = set.getShort(5); //主键（索引）的序号
            if (name.equals(columnData.getName())) //如果和所查列名相同，视为主键
            {
                columnData.setPrimaryKeyOrder(keySequence);
                break;
            }
        }

        return columnData;
    }

    /**
     * 通过数据库元数据类查询列信息时，对所需要的参数进行处理
     * 
     * @param name
     * @return
     */
    public String getValidateSQlWord(String name) {
        name = StringUtil.trim(name);
        if (name.equals(""))
            name = "%";
        return name;
    }

    /**
     * 获取的同义字,很少有数据库支持该概念（db2不支持,只有oracle）
     * 
     * @param bookmark
     * @param connection
     * @param type
     * @param schema
     * @return
     * @throws SQLException
     */
    protected List<Entity> getSynonymsList(Bookmark bookmark, Connection connection,
            String type, Schema schema) throws SQLException {
        List<Entity> list = new ArrayList<Entity>();
        SQLResultSetResults results = null;
        String sql = databaseAdapter.getShowSynonymsQuery(schema.getName(),
                type);
        if (sql != null) {
            results = (SQLResultSetResults) execute(connection, sql,
                    0);
            int i = 1;
            for (int size = results != null ? results.getRowCount() : 0; i <= size; i++) {
                String schemaName = results.getColumnCount() != 1 ? results
                        .getElement(1, i).toString() : schema.getName();
                if (schemaName != null)
                    schemaName = schemaName.trim();
                String tableName = results.getColumnCount() != 1 ? results
                        .getElement(2, i).toString() : results.getElement(1, i)
                        .toString();
                if (tableName != null && tableName.length() > 0) {
                    Entity entity = EntityFactory.getInstance().create(
                            bookmark,schema.getCatalog(), schemaName, tableName, type, "", true);
                    if (entity != null)
                        list.add(entity);
                }
            }

        }
        return list;
    }

    /**
     * 获取数据类型
     * 
     * @return
     * @throws NotConnectedException
     * @throws SQLException
     */
    public DataType[] getTypes() throws UnifyException, SQLException {
        DatabaseMetaData metaData = getMetaData();
        List<DataType> list = new ArrayList<DataType>();
        ResultSet results = metaData.getTypeInfo();
        try {
            while (results.next())
                list.add(new DataType(results.getInt(2), results.getString(1),
                        results.getLong(3), results.getString(4), results
                                .getString(5), results.getString(6)));
        } finally {
            results.close();
        }
        return (DataType[]) list.toArray(new DataType[list.size()]);
    }

    /**
     * 获取连接的元数据信息类
     * 
     * @return
     * @throws UnifyException
     * @throws SQLException
     */
    private DatabaseMetaData getMetaData() throws UnifyException, SQLException {
        Connection connection = bookmark.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        return metaData;
    }

    /**
     * 获取特定数据库中指定模式名下的所有表信息的获取，根据数据类型，从特定的数据库适配器中获取相应的sql进行查询
     * 
     * @param bookmark
     * @param type
     * @param schema
     * @return
     */
    private String getSQL(Bookmark bookmark, String type, Schema schema) {
        return getSQL(bookmark, type, schema.getCatalog(),schema.getName());
    }

    private String getSQL(Bookmark bookmark,String catalog, String type, String schema) {
        if (SqlUtil.TABLE.equals(type))
            return databaseAdapter.getShowTableQuery(catalog,schema);
        else if (SqlUtil.VIEW.equals(type))
            return databaseAdapter.getShowViewQuery(catalog,schema);
        else if (SqlUtil.SEQUENCE.equals(type))
            return databaseAdapter.getShowSequenceQuery(catalog,schema);
        else if (SqlUtil.SYNONYM.equals(type))
        {
        	return databaseAdapter.getShowSynonymsQuery(schema, type);
        }
        else
            return null;
    }
    @SuppressWarnings("unused")
    private String getSQL(Bookmark bookmark,String catalog, String type, String schema,
            String entityName) {
        if (SqlUtil.TABLE.equals(type)||SqlUtil.SYNONYM.equals(type))
            return databaseAdapter.getTableQuery(catalog,schema, entityName);
        else if (SqlUtil.VIEW.equals(type))
            return databaseAdapter.getViewQuery(catalog,schema, entityName);
        else if (SqlUtil.SEQUENCE.equals(type))
            return databaseAdapter.getSequenceQuery(catalog,schema, entityName);
        else
            return null;
    }

    /**
     * 获取指定表的主键被作为外键使用的信息
     * 
     * @param schema
     * @param entityName
     *            该表的主键被作为外键
     * @return
     * @throws NotConnectedException
     * @throws SQLException
     */
    public ForeignKey[] getExportedKeys(String catalog,String schema, String entityName)
            throws UnifyException, SQLException {
        DatabaseMetaData metaData = getMetaData();
        List<ForeignKey> list = new ArrayList<ForeignKey>();
        return getForeignKeys(list, metaData.getExportedKeys(catalog, schema,
                entityName));
    }

    /**
     * 获取指定表的外键信息
     * 
     * @param schema
     * @param entityName
     *            该表的使用其他表的主键作为外键
     * @return
     * @throws NotConnectedException
     * @throws SQLException
     */
    public ForeignKey[] getImportedKeys(String catalog,String schema, String entityName)
            throws UnifyException, SQLException {
        DatabaseMetaData metaData = getMetaData();
        List<ForeignKey> list = new ArrayList<ForeignKey>();
        return getForeignKeys(list, metaData.getImportedKeys(catalog, schema,
                entityName));
    }

    /**
     * 如果一次性获取所有的表的外键信息，那么可能出现重复的信息,如果只是针对单个表进行查询，能够保证准确性
     * 
     * @param list --used to save foreignkeys
     * @param resultSet --foreignkeys result information for database
     * @return
     * @throws SQLException
     */
    private ForeignKey[] getForeignKeys(List<ForeignKey> list, ResultSet resultSet)
            throws SQLException {
        ForeignKeyImpl foreignKey = null;
        int lowestKeySequence = Integer.MAX_VALUE;
        try {
            for (; resultSet.next(); foreignKey.addColumns(resultSet
                    .getString(4), resultSet.getString(8))) {
                int keySequence = resultSet.getInt(9);
                lowestKeySequence = Math.min(lowestKeySequence, keySequence);
                if (keySequence == lowestKeySequence || foreignKey == null) {
                    foreignKey = new ForeignKeyImpl();
                    list.add(foreignKey);
                    foreignKey.setPkName(resultSet.getString(13));
                    foreignKey.setFkName(resultSet.getString(12));
                    foreignKey.setDeleteRule(resultSet.getShort(11));
                    foreignKey.setUpdateRule(resultSet.getShort(11));
                    foreignKey.setForeignEntityCatalog(resultSet.getString(5));
                    foreignKey.setForeignEntitySchema(resultSet.getString(6));
                    foreignKey.setForeignEntityName(resultSet.getString(7));
                    
                    foreignKey.setLocalEntitySchema(resultSet.getString(2));
                    foreignKey.setLocalEntityCatalog(resultSet.getString(1));
                    foreignKey.setLocalEntityName(resultSet.getString(3));
                }
            }

            ForeignKey aforeignkey[] = (ForeignKey[]) list
                    .toArray(new ForeignKey[list.size()]);
            return aforeignkey;
        } finally {
            resultSet.close();
        }
    }

    /**
     * 获取所有的模式名,如果catalog为null，返回所有的schema
     * 
     * @return
     * @throws NotConnectedException
     * @throws SQLException
     */
    public Schema[] getSchemas(String catalog) throws UnifyException, SQLException {
//        if(!dbMetaData.supportsSchemas())
//        	return new Schema[0];
        Schema[] allSchemas=dbMetaData.getSchemas();
        if(catalog==null)
        	return allSchemas;
        List<Schema> list = new ArrayList<Schema>();
        for(int i=0;i<allSchemas.length;i++)
        {
        	if(allSchemas[i].getCatalog()==null)
        	{
        		Schema tmp=new Schema(catalog,allSchemas[i].getName(),allSchemas[i].getDisplayName(),allSchemas[i].isDefault());
        		list.add(tmp);
        	}else if(allSchemas[i].getCatalog().equals(catalog))
		        list.add(allSchemas[i]);
        }
        return (Schema[])list.toArray(new Schema[list.size()]);
    }

    /**
     * Get data count of specified table according to database adapter.
     * @throws SQLException
     */
    public int getSize(Bookmark bookmark, Connection connection,
            String tableName, DatabaseAdapter adapter) throws SQLException {
        SQLResultSetResults results = (SQLResultSetResults) execute(connection,
                adapter.getCountQuery(tableName));
        if (results.getRowCount() > 0 && results.getColumnCount() > 0)
            return Integer.parseInt(results.getElement(1, 1).toString());
        else
            return -1;
    }

    /**
     * 用于viewtable：查看表的所有行
     * @throws SQLException
     */
    public SQLResults execute(Connection con, Entity entity[], String s)
            throws SQLException {
        return ConnectionUtil.execute(bookmark, con, entity, s, numberOfRowsPerPage);
    }

    public SQLResults execute(Connection con, String sql) throws SQLException {
        return ConnectionUtil.execute(bookmark, con, null, sql, numberOfRowsPerPage);
    }

    public SQLResultSetResults getMetaData(Entity entity, Connection connection)
            throws SQLException {
        String query = SqlUtil.buildSelectAllColumnsNoRowsSql(entity);
        SQLResultSetResults results = null;
        if (connection != null) {
            Statement statement = connection.createStatement();
            try {
                ResultSet set = statement.executeQuery(query);

                try {
                    results = SQLMetaDataResults.create(entity.getBookmark(),
                            set, query, new Entity[] { entity });
                } finally {
                    set.close();
                }
            } finally {
                statement.close();
            }
        }
        return results;
    }

    public SQLResults execute(Connection con, String sql,
            int numberOfRowsPerPage) throws SQLException {
        return ConnectionUtil.execute(bookmark, con, null, sql,
                numberOfRowsPerPage);
    }

    /**
     * @return 返回 bookmark。
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /**
     * @param bookmark
     *            要设置的 bookmark。
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

	/**
	 * @return the numberOfRowsPerPage
	 */
	public int getNumberOfRowsPerPage() {
		return this.numberOfRowsPerPage;
	}
	public String getIdentifierQuoteString()
	{
		if(dbMetaData!=null)
		{
			try {
				return dbMetaData.getIdentifierQuoteString();
			} catch (SQLException e) {
				return "";
			}
		}
		else
			return "";
	}
}
