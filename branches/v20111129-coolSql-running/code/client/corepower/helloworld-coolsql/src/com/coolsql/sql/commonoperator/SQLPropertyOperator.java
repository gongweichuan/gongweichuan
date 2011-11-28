/*
 * 创建日期 2006-12-1
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.coolsql.gui.property.database.SQLProperty;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.bookmarkview.RecentSQL;

/**
 * @author liu_xlin
 *查看最近执行sql的属性
 */
public class SQLPropertyOperator implements Operatable {

    /* （非 Javadoc）
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            throw new UnifyException("no argument");
        if(!(arg instanceof RecentSQL))
            throw new UnifyException("class type of argument is incorrect:"+arg.getClass());

        RecentSQL sqlData=(RecentSQL)arg;
        SQLProperty propertyDialog=new SQLProperty(sqlData);
        propertyDialog.setVisible(true);
    }

    /* （非 Javadoc）
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException,
            SQLException {
    }

}
