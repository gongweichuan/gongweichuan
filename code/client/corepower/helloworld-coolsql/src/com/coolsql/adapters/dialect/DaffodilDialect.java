package com.coolsql.adapters.dialect;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.hibernate.dialect.Dialect;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.ISQLDatabaseMetaData;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Table;

/**
 * An extension to the standard Hibernate Daffodil dialect
 */

public class DaffodilDialect extends Dialect 
                             implements HibernateDialect {
    
    public DaffodilDialect() {
        super();
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.BINARY, 4192, "binary($l)");
        registerColumnType(Types.BINARY, 1073741823, "long varbinary($l)");
        registerColumnType(Types.BINARY, "long varbinary(1073741823)");
        registerColumnType(Types.BIT, "bit");
        registerColumnType(Types.BLOB, 1073741823, "blob($l)");
        registerColumnType(Types.BLOB, "blob(1073741823)");
        registerColumnType(Types.BOOLEAN, "boolean");
        registerColumnType(Types.CHAR, 4192, "char($l)");
        registerColumnType(Types.CHAR, 1073741823, "clob($l)");
        registerColumnType(Types.CHAR, "clob(1073741823)");
        registerColumnType(Types.CLOB, 1073741823, "clob($l)");
        registerColumnType(Types.CLOB, "clob(1073741823)");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.DECIMAL, "decimal($p,$s)");
        registerColumnType(Types.DOUBLE, "double precision");
        registerColumnType(Types.FLOAT, "float($p)");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.LONGVARBINARY, 1073741823, "long varbinary($l)");
        registerColumnType(Types.LONGVARBINARY, "long varbinary(1073741823)");
        registerColumnType(Types.LONGVARCHAR, 1073741823, "long varchar($l)");
        registerColumnType(Types.LONGVARCHAR, "long varchar(1073741823)");
        registerColumnType(Types.NUMERIC, "numeric($p,$s)");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.VARBINARY, 4192, "varbinary($l)");
        registerColumnType(Types.VARBINARY, 1073741823, "long varbinary($l)");
        registerColumnType(Types.VARBINARY, "long varbinary(1073741823)");
        registerColumnType(Types.VARCHAR, 4192, "varchar($l)");
        registerColumnType(Types.VARCHAR, 1073741823, "clob($l)");
        registerColumnType(Types.VARCHAR, "clob(1073741823)");
        
    }  
    
    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#supportsSchemasInTableDefinition()
     */
    public boolean supportsSchemasInTableDefinition() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getLengthFunction()
     */
    public String getLengthFunction(int dataType) {
        return "length";
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getMaxFunction()
     */
    public String getMaxFunction() {
        return "max";
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getMaxPrecision(int)
     */
    public int getMaxPrecision(int dataType) {
        if (dataType == Types.FLOAT)
        {
            return 15;
        }
        if (dataType == Types.NUMERIC
                || dataType ==  Types.DECIMAL) {
            return 38;
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getMaxScale(int)
     */
    public int getMaxScale(int dataType) {
        return getMaxPrecision(dataType);
    }
    
    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getPrecisionDigits(int, int)
     */
    public int getPrecisionDigits(int columnSize, int dataType) {
        return columnSize;
    }
    
    /* (non-Javadoc)
     * @see com.coolsql.adapters.dialect.HibernateDialect#getColumnLength(int, int)
     */
    public int getColumnLength(int columnSize, int dataType) {
        return columnSize;
    }

    /**
     * The string which identifies this dialect in the dialect chooser.
     * 
     * @return a descriptive name that tells the user what database this dialect
     *         is design to work with.
     */
    public String getDisplayName() {
        return "Daffodil";
    }

    /**
     * Returns boolean value indicating whether or not this dialect supports the
     * specified database product/version.
     * 
     * @param databaseProductName the name of the database as reported by 
     * 							  DatabaseMetaData.getDatabaseProductName()
     * @param databaseProductVersion the version of the database as reported by
     *                              DatabaseMetaData.getDatabaseProductVersion()
     * @return true if this dialect can be used for the specified product name
     *              and version; false otherwise.
     */
    public boolean supportsProduct(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().startsWith("Daffodil")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}    

    public String getAddColumnString() {
        return "ADD COLUMN";
    }
    
    /**
     * Returns the SQL statement to use to add a column to the specified table
     * using the information about the new column specified by info.
     * @param info information about the new column such as type, name, etc.
     * 
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         adding columns after a table has already been created.
     */
    public String[] getColumnAddSQL(Column info) throws UnsupportedOperationException {
        return new String[] {
            DialectUtils.getColumnAddSQL(info, this, true, false, true)
        };
    }

    /**
     * Returns a boolean value indicating whether or not this dialect supports
     * adding comments to columns.
     * 
     * @return true if column comments are supported; false otherwise.
     */
    public boolean supportsColumnComment() {
        return false;
    }    
    
    /**
     * Returns the SQL statement to use to add a comment to the specified 
     * column of the specified table.
     * 
     * @param tableName the name of the table to create the SQL for.
     * @param columnName the name of the column to create the SQL for.
     * @param comment the comment to add.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    @SuppressWarnings("unused")
    public String getColumnCommentAlterSQL(String tableName, 
                                           String columnName, 
                                           String comment) 
        throws UnsupportedOperationException 
    {
        int featureId = DialectUtils.COLUMN_COMMENT_ALTER_TYPE;
        String msg = DialectUtils.getUnsupportedMessage(this, featureId);
        throw new UnsupportedOperationException(msg);
    }
    
    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports dropping columns from tables.
     * 
     * @return true if the database supports dropping columns; false otherwise.
     */
    public boolean supportsDropColumn() {
        return true;
    }

    /**
     * Returns the SQL that forms the command to drop the specified colum in the
     * specified table.
     * 
     * @param tableName the name of the table that has the column
     * @param columnName the name of the column to drop.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         dropping columns. 
     */
    public String getColumnDropSQL(String tableName, String columnName) 
        throws UnsupportedOperationException {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(tableName);
        result.append(" DROP COLUMN ");
        result.append(columnName);
        result.append(" CASCADE");
        return result.toString();        
    }
    
    /**
     * Returns the SQL that forms the command to drop the specified table.  If
     * cascade contraints is supported by the dialect and cascadeConstraints is
     * true, then a drop statement with cascade constraints clause will be 
     * formed.
     * 
     * @param Table the table to drop
     * @param cascadeConstraints whether or not to drop any FKs that may 
     * reference the specified table.
     * @return the drop SQL command.
     */
    public List<String> getTableDropSQL(Table Table, boolean cascadeConstraints, boolean isMaterializedView){
        return DialectUtils.getTableDropSQL(Table, true, cascadeConstraints, false, DialectUtils.CASCADE_CLAUSE, false);
    }

    /**
     * Returns the SQL that forms the command to add a primary key to the 
     * specified table composed of the given column names.
     * 
     * alter table test add constraint pkName primary key (<PK COLS>)
     * 
     * @param pkName the name of the constraint
     * @param columnNames the columns that form the key
     * @return
     */
    public String[] getAddPrimaryKeySQL(String pkName, 
                                        Column[] columns, 
                                        Table ti) 
    {
        return new String[] {
            DialectUtils.getAddPrimaryKeySQL(ti, pkName, columns, false)
        };
    }
    
    /**
     * Returns the SQL statement to use to add a comment to the specified 
     * column of the specified table.
     * @param info information about the column such as type, name, etc.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    public String getColumnCommentAlterSQL(Column info) 
        throws UnsupportedOperationException
    {
        int featureId = DialectUtils.COLUMN_COMMENT_ALTER_TYPE;
        String msg = DialectUtils.getUnsupportedMessage(this, featureId);
        throw new UnsupportedOperationException(msg);
    }

    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports changing a column from null to not-null and vice versa.
     * 
     * @return true if the database supports dropping columns; false otherwise.
     */    
    public boolean supportsAlterColumnNull() {
        return false;
    }
    
    /**
     * Returns the SQL used to alter the specified column to not allow null 
     * values
     * 
     * @param info the column to modify
     * @return the SQL to execute
     */
    public String getColumnNullableAlterSQL(Column info) {
        int featureId = DialectUtils.COLUMN_NULL_ALTER_TYPE;
        String msg = DialectUtils.getUnsupportedMessage(this, featureId);
        throw new UnsupportedOperationException(msg);
    }
    
    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports renaming columns.
     * 
     * @return true if the database supports changing the name of columns;  
     *         false otherwise.
     */
    public boolean supportsRenameColumn() {
        return false;
    }    
    
    /**
     * Returns the SQL that is used to change the column name.
     * 
     * 
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     */
    public String getColumnNameAlterSQL(Column from, Column to) {
        int featureId = DialectUtils.COLUMN_NAME_ALTER_TYPE;
        String msg = DialectUtils.getUnsupportedMessage(this, featureId);
        throw new UnsupportedOperationException(msg);
    }
    
    /**
     * Returns a boolean value indicating whether or not this dialect supports 
     * modifying a columns type.
     * 
     * @return true if supported; false otherwise
     */
    public boolean supportsAlterColumnType() {
        return false;
    }
    
    /**
     * Returns the SQL that is used to change the column type.
     * 
     * @param from the Column as it is
     * @param to the Column as it wants to be
     * 
     * @return the SQL to make the change
     * @throw UnsupportedOperationException if the database doesn't support 
     *         modifying column types. 
     */
    public List<String> getColumnTypeAlterSQL(Column from, 
                                        Column to)
        throws UnsupportedOperationException
    {
        int featureId = DialectUtils.COLUMN_TYPE_ALTER_TYPE;
        String msg = DialectUtils.getUnsupportedMessage(this, featureId);
        throw new UnsupportedOperationException(msg);
    }

    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports changing a column's default value.
     * 
     * @return true if the database supports modifying column defaults; false 
     *         otherwise
     */
    public boolean supportsAlterColumnDefault() {
        return true;
    }
    
    /**
     * Returns the SQL command to change the specified column's default value
     *   
     * @param info the column to modify and it's default value.
     * @return SQL to make the change
     */
    public String getColumnDefaultAlterSQL(Column info) {
        String alterClause = DialectUtils.ALTER_COLUMN_CLAUSE;
        String defaultClause = DialectUtils.SET_DEFAULT_CLAUSE;
        return DialectUtils.getColumnDefaultAlterSQL(this, 
                                                     info, 
                                                     alterClause, 
                                                     false, 
                                                     defaultClause);
    }
    
    /**
     * Returns the SQL command to drop the specified table's primary key.
     * 
     * alter table test drop constraint pk_test cascade
     * 
     * @param pkName the name of the primary key that should be dropped
     * @param tableName the name of the table whose primary key should be 
     *                  dropped
     * @return
     */
    public String getDropPrimaryKeySQL(String pkName, String tableName) {
        return DialectUtils.getDropPrimaryKeySQL(pkName, tableName, true, true);
    }
    
    /**
     * Returns the SQL command to drop the specified table's foreign key 
     * constraint.
     * 
     * @param fkName the name of the foreign key that should be dropped
     * @param tableName the name of the table whose foreign key should be 
     *                  dropped
     * @return
     */
    public String getDropForeignKeySQL(String fkName, String tableName) {
        return DialectUtils.getDropForeignKeySQL(fkName, tableName);
    }
    
    /**
     * Returns the SQL command to create the specified table.
     * 
     * @param tables the tables to get create statements for
     * @param md the metadata from the ISession
     * @param prefs preferences about how the resultant SQL commands should be 
     *              formed.
     * @param isJdbcOdbc whether or not the connection is via JDBC-ODBC bridge.
     *  
     * @return the SQL that is used to create the specified table
     * @throws UnifyException 
     */
    public List<String> getCreateTableSQL(List<Table> tables, 
                                          ISQLDatabaseMetaData md,
                                          CreateScriptPreferences prefs,
                                          boolean isJdbcOdbc)
        throws SQLException, UnifyException
    {
        return DialectUtils.getCreateTableSQL(tables, md, this, prefs, isJdbcOdbc);
    }
    
}
