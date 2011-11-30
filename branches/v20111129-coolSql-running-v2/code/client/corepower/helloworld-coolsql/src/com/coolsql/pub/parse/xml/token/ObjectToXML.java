/*
 * 创建日期 2006-11-7
 */
package com.coolsql.pub.parse.xml.token;

import org.jdom.Element;

import com.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.coolsql.pub.parse.xml.XMLBeanUtil;
import com.coolsql.pub.parse.xml.XMLConstant;
import com.coolsql.pub.parse.xml.XMLException;

/**
 * @author liu_xlin 对象类型向XML的转化解析类
 */
public class ObjectToXML extends BeanAndXMLParse {
    public ObjectToXML(Class cl) {
        super(cl, null, null);
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        this.checkInfoIntegrality();

        XMLBeanUtil xml = new XMLBeanUtil();
        Element element = xml.parseBean(this.getValue(),
                XMLConstant.TAG_PROPERTY_BEAN);
        if(element==null)
            throw new XMLException("can't get a java bean");
        
        if (getName() != null)
            element
                    .setAttribute(XMLConstant.TAG_ARRTIBUTE_NAME,getName());
        element.setAttribute(XMLConstant.TAG_ARRTIBUTE_DATATYPE,getJavaClass().getName());

        return element;
    }

    /**
     * 设置属性值，覆盖该方法，以确定非原始数据类型的真正类型实例
     */
    public void setPropertyValue(Object value) {
        super.setPropertyValue(value);
        if (this.getValue() != null)
            setDataType(getValue().getClass());
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        XMLBeanUtil xml = new XMLBeanUtil();
        return xml.getBean(getElement());

    }

}
