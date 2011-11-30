/*
 * 创建日期 2006-11-8
 */
package com.coolsql.pub.parse.xml;

import java.beans.PropertyDescriptor;

/**
 * @author liu_xlin
 *
 */
public interface XMLEditor {
    /**
     * 属性名称
     * @return
     */
    public abstract String getName();
    
    /**
     * 对应的java类型
     * @return
     */
    public abstract Class getJavaClass();
    
    /**
     * 获取属性的值
     * @return
     */
    public abstract Object getValue();
    /**
     * 是否是数组
     * @return
     */
    public abstract boolean isArray();
    
    /**
     * 获取方法的描述符
     * @return
     */
    public abstract PropertyDescriptor getPropertyDescriptor();
    
    /**
     * 当前的操作类型：
     * true: 从bean向xml的转化
     * false: 从xml向bean的转化
     * @return
     */
    public abstract boolean editorType();
    
    /**
     * 解析器需要解析的元素对应的变量值是否为null。
     * 该方法在xml->bean和bean->xml两种模式中都会使用到。
     * @return
     */
    public boolean isNull();
}
