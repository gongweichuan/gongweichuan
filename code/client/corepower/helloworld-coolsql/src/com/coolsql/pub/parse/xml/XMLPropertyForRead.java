/*
 * 创建日期 2006-11-8
 */
package com.coolsql.pub.parse.xml;

import org.jdom.Element;

/**
 * @author liu_xlin
 *该接口用来解析xml文档中的对象信息
 */
public interface XMLPropertyForRead extends XMLEditor{

    /**
     * 获取从xml文件中解析后的对象
     * @return
     */
    public abstract Object getObjectInXML() throws XMLException;
    
    /**
     * 获取上层文档元素
     * @return
     */
    public abstract Element getElement();
}
