/*
 * 创建日期 2006-11-27
 */
package com.coolsql.pub.parse.xml.token;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.coolsql.pub.parse.xml.XMLConstant;
import com.coolsql.pub.parse.xml.XMLException;
import com.coolsql.pub.parse.xml.XMLFactory;

/**
 * @author liu_xlin
 *集合对象解析对象
 */
public class CollectionXMLToken extends BeanAndXMLParse {
    public CollectionXMLToken(Class cl)
    {
        this(cl,null,null);
    }
    public CollectionXMLToken(Class cl,String propertyName,Object propertyValue)
    {
        super(cl,propertyName,propertyValue);   
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
    /* <collection><value></value></collection>
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getXMLElement()
     */
    public Element getXMLElement() throws XMLException {
        Element element=getInitedEelement();
        if(getValue()==null)
        {
//            element.addContent(XMLConstant.NULL);
            return element;
        }
        
        BeanAndXMLParse parser=null;
        Collection tmpValue=(Collection)getValue();
        Iterator it=tmpValue.iterator();
        while(it.hasNext())
        {
            Object elementObject=it.next();
            parser=XMLFactory.getBeanToXMLParser(elementObject.getClass(),true);
            if(parser==null) //如果找不到相应的解析类型，不做任何处理，直接进入下一属性的解析
                continue;
            parser.setBean(tmpValue);
            if(elementObject instanceof String)
            parser.setPropertyName(null);
            parser.setPropertyValue(elementObject);
            parser.setElementName(XMLConstant.TAG_PROPERTY_VALUE);
  
            element.addContent(parser.getXMLElement());
        }
        return element;
    }

    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        Element element = getElement();
        List children = element.getChildren();
        Iterator it = children.iterator();
        
        Collection collection=null;
        try {
            collection=(Collection)getJavaClass().newInstance();
        } catch (Exception e1) {
            throw new XMLException(e1);
        }
        
        while(it.hasNext())
        {
            Element child = (Element) it.next();  //元素对象
   
            BeanAndXMLParse parser = XMLFactory.getBeanToXMLParser(child
                    .getAttributeValue(XMLConstant.TAG_ARRTIBUTE_DATATYPE), false); //属性类型
            parser.setElement(child);
            parser.setPropertyName(child.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME));
            Object ob = parser.getObjectInXML();
            collection.add(ob);
        }
        return collection;
    }

}
