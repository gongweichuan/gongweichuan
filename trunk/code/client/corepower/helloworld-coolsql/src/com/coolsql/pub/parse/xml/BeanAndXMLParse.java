/*
 * 创建日期 2006-11-7
 */
package com.coolsql.pub.parse.xml;

import java.beans.PropertyDescriptor;

import org.jdom.Element;


/**
 * @author liu_xlin
 * 类型解析的基类
 */
public abstract class BeanAndXMLParse implements XMLPropertyForWrite,XMLPropertyForRead {

    private String propertyName=null;   //属性名称
    private Object propertyValue=null;  //属性值
    private Class dataType=null;  //属性类型
    
    private Object bean=null;  //当前属性所述对象
    
    private Element element=null;//如果为xml to bean，那么需要初始化该值
    private String elementName=null;
    
    private boolean editorType;//编辑类型
    private PropertyDescriptor propertyDes=null;  //属性的描述符
    
    private boolean isNull=false;//变量值是否为null
    
    public BeanAndXMLParse(Class dataType,String propertyName,Object propertyValue)
    {
        this(dataType,propertyName,propertyValue,XMLConstant.TAG_PROPERTY,false);
    }   
    public BeanAndXMLParse(Class dataType,String propertyName,Object propertyValue,String elementName,boolean isNull)
    {
        this.dataType=dataType;
        this.propertyName=propertyName;
        this.propertyValue=propertyValue;
        this.elementName=elementName;
        this.isNull=isNull;
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getName()
     */
    public String getName() {
       return propertyName;
    }

    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getJavaClass()
     */
    public Class getJavaClass() {
        return dataType;
    }

    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForWrite#getValue()
     */
    public Object getValue() {
        return propertyValue;
    }
    public Object getBean()
    {
        return bean;
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getParentElement()
     */
    public Element getElement() {
        return element;
    }
    public boolean editorType()
    {
        return editorType;
    }
    public PropertyDescriptor getPropertyDescriptor()
    {
      return propertyDes;   
    }
    public boolean isNull()
    {
        return isNull;
    }
    /**
     * 默认为不是数组类型
     */
    public boolean isArray()
    {
        return false;
    }

    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }
    public void setElement(Element element) {
        this.element = element;
    }
    public void setNull(boolean isNull)
    {
      this.isNull=isNull;   
    }

    public boolean checkInfoIntegrality() throws XMLException
    {
//        if(propertyName!=null&&propertyValue!=null&&(isArray()||dataType!=null))
//            return true;
//        else
//            throw new XMLException("data is not integrated!");
        return true;
    }
    public void setBean(Object bean) {
        this.bean = bean;
    }
    public void setEditorType(boolean editorType) {
        this.editorType = editorType;
    }
    public void setPropertyDes(PropertyDescriptor propertyDes) {
        this.propertyDes = propertyDes;
    }
    /**
     * 将元素初始化
     * @return
     * @throws XMLException
     */
    public Element getInitedEelement() throws XMLException
    {
        Element element=new Element(getElementName());
        if(propertyName!=null)
            element.setAttribute(XMLConstant.TAG_ARRTIBUTE_NAME,propertyName);
        if(dataType!=null)
        {
            element.setAttribute(XMLConstant.TAG_ARRTIBUTE_DATATYPE,dataType.getName());
        }else
        {
            throw new XMLException("parse token has no data type information! ");
        }
        
        if(getValue()==null)
            element.setAttribute(XMLConstant.TAG_ARRTIBUTE_ISNULL,"true");
        
        return element;
    }
    public String getElementName() {
        return elementName;
    }
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
