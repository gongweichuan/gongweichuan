/*
 * 创建日期 2006-11-7
 */
package com.coolsql.pub.parse.xml.token;

import org.jdom.Element;

import com.coolsql.pub.parse.xml.BeanAndXMLParse;
import com.coolsql.pub.parse.xml.XMLException;

/**
 * @author liu_xlin
 *float类型向XML的转化解析类
 */
public class FloatToXML extends BeanAndXMLParse {
    public FloatToXML()
    {
        this(null,null);
    }
    public FloatToXML(String propertyName,Object propertyValue)
    {
        super(Float.TYPE,propertyName,propertyValue);   
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
        
        element.addContent(getValue().toString());
        return element;
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.parse.xml.XMLPropertyForRead#getObjectInXML()
     */
    public Object getObjectInXML() throws XMLException {
        String txt=getElement().getTextTrim();
        if(!isNull())
        {
            Object value=null;
            try
            {
                value=new Float(txt);
            }catch(NumberFormatException e)
            {
                value=new Float(0);
            }
            return value;
        }else
            return new Float(0);
    }

}
