/*
 * 创建日期 2006-11-9
 */
package com.coolsql.system;

import com.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin
 *
 */
public interface CloseSystem {

    /**
     * 关闭程序的执行代码
     *
     */
    public abstract void close() throws UnifyException;
    
    /**
     * 保存信息
     */
    public abstract void save() throws UnifyException;
}
