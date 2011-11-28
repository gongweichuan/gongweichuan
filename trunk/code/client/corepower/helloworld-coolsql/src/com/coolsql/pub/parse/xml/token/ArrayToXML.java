/*
 * 创建日期 2006-11-7
 */
package com.coolsql.pub.parse.xml.token;

import java.util.List;

import org.jdom.Element;

import com.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.coolsql.pub.parse.xml.XMLConstant;
import com.coolsql.pub.parse.xml.XMLException;
import com.coolsql.pub.parse.xml.XMLFactory;

/**
 * @author liu_xlin
 *数组类型向XML的转化解析类
 */
public class ArrayToXML extends BeanAndXMLParse {
    public ArrayToXML(Class cl)
    {
        this(cl,null,null);
    }
    public ArrayToXML(Class cl,String propertyName,Object propertyValue)
    {
        super(null,propertyName,propertyValue);   
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        checkInfoIntegrality();
        
        Element element=getInitedEelement();
        
        if(getValue()==null)
        {
//            element.addContent(XMLConstant.NULL);
            return element;
        }
        
        Object[] tmpValue=(Object[])getValue();
        BeanAndXMLParse parser=XMLFactory.getBeanToXMLParser(getJavaClass().getComponentType(),true);
        if(parser==null) //如果找不到相应的解析类型，不做任何处理，直接进入下一属性的解析
            return null;
        
        for(int i=0;i<tmpValue.length;i++)
        {
            parser.setBean(getBean());
            parser.setPropertyName(XMLConstant.NODEFINED);
            parser.setPropertyValue(tmpValue[i]);
  
            element.addContent(parser.getXMLElement());
        }
        return element;
    }

    public boolean isArray()
    {
        return true;
    }
    /**
     * 设置属性值，覆盖该方法，以确定非原始数据类型的真正类型实例
     */
    public void setPropertyValue(Object value)
    {
        super.setPropertyValue(value);
        if(getValue()!=null)
            setDataType(getValue().getClass());
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        Element element=getElement();
        List children=element.getChildren();
        /**
         * 实例化一个指定类型的数组对象
         */
        Object[] data = new Object[children.size()];
        
        BeanAndXMLParse parser=XMLFactory.getBeanToXMLParser(getJavaClass().getComponentType(),false);
        
        for(int i=0;i<children.size();i++)
        {
            Element e=(Element)children.get(i);
            parser.setPropertyName(e.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME));
            parser.setElement(e);
            
            data[i]=parser.getObjectInXML();
        }
        return data;
    }
}
