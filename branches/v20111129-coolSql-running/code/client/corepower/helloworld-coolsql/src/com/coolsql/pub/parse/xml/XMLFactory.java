/*
 * 创建日期 2006-11-7
 */
package com.coolsql.pub.parse.xml;

import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.xml.token.ArrayToXML;
import com.coolsql.pub.parse.xml.token.BooleanToXML;
import com.coolsql.pub.parse.xml.token.CharacterToXML;
import com.coolsql.pub.parse.xml.token.CollectionXMLToken;
import com.coolsql.pub.parse.xml.token.DoubleToXML;
import com.coolsql.pub.parse.xml.token.FloatToXML;
import com.coolsql.pub.parse.xml.token.IntegerToXML;
import com.coolsql.pub.parse.xml.token.LongToXML;
import com.coolsql.pub.parse.xml.token.MapXMLToken;
import com.coolsql.pub.parse.xml.token.ObjectToXML;
import com.coolsql.pub.parse.xml.token.ShortToXML;
import com.coolsql.pub.parse.xml.token.StringToXML;
import com.coolsql.view.log.LogProxy;

/**
 * The factory for constructing XML.
 * @author liu_xlin
 */
public class XMLFactory {
    public static BeanAndXMLParse getBeanToXMLParser(String javaClass)
    {
        Class<?> type = null;
        try {
            type = LoadJar.getInstance().getClassLoader().loadClass(javaClass);
        } catch (ClassNotFoundException e) {
            LogProxy.internalError(e);
        }
        return getBeanToXMLParser(type);
    }
    /**
     * Retrieve the object parser.
     * @param javaClass A qualified class name
     * @param editorType  --true:bean to XML ,false:xml to bean
     */
    public static BeanAndXMLParse getBeanToXMLParser(String javaClass, boolean editorType)
    {
        Class<?> type = null;
        try {
            type = LoadJar.getInstance().getClassLoader().loadClass(javaClass);
        } catch (ClassNotFoundException e) {
            LogProxy.internalError(e);
        }
        return getBeanToXMLParser(type,editorType);
    }
    /**
     * 获取对象解析器
     * @param javaClass
     * @param editorType  --true:bean to xml ,false:xml to bean
     * @return
     */
    public static BeanAndXMLParse getBeanToXMLParser(Class<?> javaClass,boolean editorType)
    {
        BeanAndXMLParse tmp=getBeanToXMLParser(javaClass);
        if(tmp!=null)
            tmp.setEditorType(editorType);
        
        return tmp;
    }
    /**
     * 获取对象解析器
     */
    private static BeanAndXMLParse getBeanToXMLParser(Class<?> javaClass)
    {
        
        if(javaClass.isArray())   //数组
        {
            return new ArrayToXML(javaClass);
        }else if(javaClass==Boolean.TYPE)  //原始类型判断 ------start----
        {
            return new BooleanToXML();
        }else if(javaClass==Integer.TYPE)
        {
            return new IntegerToXML();
        }else if(javaClass==Short.TYPE)
        {
            return new ShortToXML();
        }else if(javaClass==Long.TYPE)
        {
            return new LongToXML();
        }else if(javaClass==Float.TYPE)
        {
            return new FloatToXML();
        }else if(javaClass==Double.TYPE)
        {
            return new DoubleToXML();
        }else if(javaClass==Character.TYPE)  //原始类型判断  -------end-----
        {
            return new CharacterToXML();
        }else if(javaClass==java.lang.String.class)
        {
            return new StringToXML();
        }else if(java.util.Map.class.isAssignableFrom(javaClass))   //map类型
        {
            return new MapXMLToken(javaClass);
        }
        else if(java.util.Collection.class.isAssignableFrom(javaClass))  //集合类型
        {
           return new CollectionXMLToken(javaClass);   
        }else                           //其他对象类型
        {
           return new ObjectToXML(javaClass);
        }
    }
    /**
     * 是否是解析因子
     * @param type --class类型
     * @return  --如果是返回true，否返回falses
     */
    public static boolean isParseFactor(Class<?> type)
    {
        if(getBeanToXMLParser(type) instanceof ObjectToXML)
        {
            return false;
        }else
            return true;
    }
}
