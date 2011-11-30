/*
 * 创建日期 2006-11-7
 */
package com.coolsql.pub.parse.xml;

/**
 * @author liu_xlin
 *XML数据常量
 */
public class XMLConstant {
   
    /**
     * 标签名
     */
    public static final String TAG_PROPERTY="Property";
    public static final String TAG_PROPERTY_BEAN="BeanProperty";
    
    //对于map类型属性，定义了键值对的标签名称
    public static final String TAG_PROPERTY_KEY="Key";
    public static final String TAG_PROPERTY_VALUE="Value";
    public static final String TAG_PROPERTY_KEYVALUE="key-value";
    
    /**
     * 属性名
     */
    public static final String TAG_ARRTIBUTE_NAME="name"; //bean属性名称
    
    public static final String TAG_ARRTIBUTE_DATATYPE="datatype";  //java类型
    
    public static final String TAG_ARRTIBUTE_ISNULL="isNull";  //变量对应值是否为null
    
    /**
     * 对于未定义的或者其他情况的属性名称的定义
     */
    public static final String NODEFINED="";
    
    public static final String NULL="NULL";
    
    public static final String NO_NAME="NONE";
}
