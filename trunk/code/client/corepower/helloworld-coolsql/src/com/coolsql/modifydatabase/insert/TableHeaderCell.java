/*
 * Created on 2007-1-31
 */
package com.coolsql.modifydatabase.insert;

/**
 * @author liu_xlin
 *表头元素对象的接口定义
 */
public interface TableHeaderCell {

    /**
     * 获取表头的状态，此定义适用于表头的是否选中状态
     * @return
     */
    public boolean getState();
    
    /**
     * 获取表头元素的对象值
     * @return
     */
    public Object getHeaderValue();
}