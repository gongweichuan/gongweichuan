/*
 * 创建日期 2006-11-6
 */
package com.coolsql.pub.parse.xml;

import org.jdom.Element;

/**
 * @author liu_xlin
 * 该接口用来将java对象转化为xml文档结构
 */
public interface XMLPropertyForWrite extends XMLEditor{


    
    /**
     * 获取属性的xml编码
     * @return
     */
    public abstract Element getXMLElement() throws XMLException;
    
    /**
     * 获取bean对象
     * @return
     */
    public abstract Object getBean();
}
