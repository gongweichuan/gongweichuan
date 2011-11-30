/*
 * Created on 2007-3-2
 */
package com.coolsql.modifydatabase.insert;

/**
 * @author liu_xlin
 *编辑表控件的表格元素类型定义
 */
public interface EditeTableCell {

    /**
     * 表格对象是否为空
     * @return
     */
    public boolean isNull();
    
    /**
     * 表格元素在界面上的的显示内容
     * @return
     */
    public String getDisplayLabel();
    
    /**
     * 获取表格元素对象值
     * @return
     */
    public Object getValue();
    
    /**
     * 设置表格元素的对象值
     */
    public void setValue(Object value);
    /**
     * 将表格元素值设为空（并非null）
     *
     */
    public void setEmpty();
}
