/*
 * 创建日期 2006-9-8
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin 对数据库操作的公共接口
 */
public interface Operatable {

    /**
     * 操作功能方法，用于公共的调用
     * 
     * @param arg
     */
    public abstract void operate(Object arg) throws UnifyException,SQLException;

    /**
     * 操作功能方法，用于公共的调用,同上，只是允许使用两个参数
     * 
     * @param arg
     */
    public abstract void operate(Object arg0, Object arg1)throws UnifyException,SQLException;
}
