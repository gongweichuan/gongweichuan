/**
 * 
 */
package com.coolsql.gui.property.database;

import java.sql.SQLException;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.ForeignKey;
import com.coolsql.view.log.LogProxy;

/**
 * the property pane that dislays exportedkey information for entity
 * @author 刘孝林
 *
 * 2008-1-6 create
 */
public class ExportedKeyPropertyPane extends ForeignKeyPropertyPane {

	private static final long serialVersionUID = 3675737523339104406L;

	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.database.ForeignKeyPropertyPane#getForeignKeys(com.coolsql.sql.model.Entity)
	 */
	@Override
	protected ForeignKey[] getForeignKeys(Entity entity) {
		ForeignKey[] fks = null;
		try {
			fks = entity.getExportedKeys();
		} catch (UnifyException e) {
			LogProxy.errorReport(this, e);
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(this, e);
		}
		return fks;
	}

	@Override
	protected int[] getForeignEntityInfo() {
		
		return new int[]{4,5,6};
	}
}
