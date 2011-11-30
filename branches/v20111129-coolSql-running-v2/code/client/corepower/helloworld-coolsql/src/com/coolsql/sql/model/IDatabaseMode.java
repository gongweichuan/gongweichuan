/**
 * 
 */
package com.coolsql.sql.model;

/**
 * @author 刘孝林
 *
 * 2008-1-1 create
 */
public interface IDatabaseMode {

	/**
	 * Return the name of object in database.  
	 * Such as table name, catalog name, shema name...
	 */
	public String getName();
	/**
	 * Return the object type in database. 
	 * The range of returned value refers to BookmarkPubInfo.
	 */
	public int getType();
}
