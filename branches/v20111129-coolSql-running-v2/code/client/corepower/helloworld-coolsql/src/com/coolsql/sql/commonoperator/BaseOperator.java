/**
 * 
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.coolsql.pub.exception.UnifyException;

/**
 * 所有操作类的基类
 * @author 刘孝林(kenny liu)
 *
 * 2008-1-17 create
 */
public abstract class BaseOperator implements Operatable {

	/* (non-Javadoc)
	 * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
	 */
	public void operate(Object arg0, Object arg1) throws UnifyException,
			SQLException {

	}

}
