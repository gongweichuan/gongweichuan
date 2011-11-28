/*
 * 创建日期 2006-11-6
 */
package com.coolsql.pub.parse.xml;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.xml.token.ObjectToXML;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 将java bean解析为xml元素
 */
public class XMLBeanUtil {

	private String encoding;
	private ClassLoader loader;
    public XMLBeanUtil() {
    	this(null,XMLBeanUtil.class.getClassLoader());
    }
    public XMLBeanUtil(String encoding)
    {
    	this(encoding,XMLBeanUtil.class.getClassLoader());
    }
    public XMLBeanUtil(ClassLoader loader)
    {
    	this(null,loader);
    }
    public XMLBeanUtil(String encoding,ClassLoader loader) {
    	if(encoding==null)
    		this.encoding=System.getProperty("file.encoding");
    	else
    		this.encoding=encoding;
    	
    	if(loader==null)
    		this.loader=XMLBeanUtil.class.getClassLoader();
    	else
    		this.loader=loader;
    }
    /**
     * 获取javabean的属性集合
     * 
     * @param bean
     * @return
     */
    private PropertyDescriptor[] getBeanPropertys(Object bean) {
        BeanInfo info = null;
        try {
            if (bean != null)
                info = Introspector.getBeanInfo(bean.getClass(),
                        java.lang.Object.class);
        } catch (IntrospectionException ex) {
            LogProxy.internalError(ex);
        }
        if (info != null)
            return info.getPropertyDescriptors();
        else
            return null;
    }

    public Element createRootElement(String name) {
        if (StringUtil.trim(name).equals(""))
            name = "bean";
        return new Element(name);
    }

    /**
     * 解析javabean为xml对应的文档结构
     * 
     * @param bean
     * @param name  --标签名称
     * @return
     * @throws XMLException
     */
    public Element parseBean(Object bean, String name) throws XMLException {
        /**
         * 首先检测是否为直接可解析对象
         */
        Element root =checkRootBean(bean);
        if(root!=null)
            return root;
        
        /**
         * 如果为非直接解析对象，对bean的属性进行解析
         */
        root=createRootElement(name);

        if (bean == null) {
//            root.addContent(XMLConstant.NULL);
            root.setAttribute(XMLConstant.TAG_ARRTIBUTE_ISNULL,"true");
            return root;
        }

        root.setAttribute(XMLConstant.TAG_ARRTIBUTE_DATATYPE, bean.getClass()
                .getName());
        Vector factors = beanToXMLParser(bean);

        Iterator it = factors.iterator();
        while (it.hasNext()) {
            BeanAndXMLParse parser = (BeanAndXMLParse) it.next();

            Element element = parser.getXMLElement();
            if (element != null)
                root.addContent(element);
        }

        return root;
    }
    /**
     * 检测给定对象是否是解析元素对象，该方法检测对象是否可以直接进行解析，
     * @param bean
     * @return  --如果bean是解析元素对象，返回相应的文档元素对象，否则返回null值
     * @throws XMLException
     */
    private Element checkRootBean(Object bean) throws XMLException
    {
        if(bean==null)
            return null;
        BeanAndXMLParse parse=checkIsParseFactor(bean);
        if(parse!=null)
        {
            return parse.getXMLElement();
        }
        return null;
    }
    /**
     * 将java对象转化为对应的xml文档对象
     * 
     * @param bean
     * @param name
     * @return
     * @throws XMLException
     */
    public Document beanToXMLDoc(Object bean, String name) throws XMLException {
        Document doc = new Document(parseBean(bean, name));
        return doc;
    }

    /**
     * 获取javabean的属性，然后将其初始化为XML元素解析因子
     * 
     * @param bean
     * @return
     */
    private Vector beanToXMLParser(Object bean) {
        Vector factors=null;
        PropertyDescriptor[] pros = getBeanPropertys(bean);
        if (pros != null) {
            factors = new Vector();

            for (int i = 0; i < pros.length; i++) {
                String propName = pros[i].getName();
                Method getter = pros[i].getReadMethod();
                Method setter=pros[i].getWriteMethod();
                if(getter==null||setter==null)  //如果没有get方法，不进行保存
                {
                    continue;
                }
                Class returnType = getter.getReturnType();

                BeanAndXMLParse parse = XMLFactory.getBeanToXMLParser(
                        returnType, true);
                if (parse == null) //如果找不到相应的解析类型，不做任何处理，直接进入下一属性的解析
                    continue;

                parse.setPropertyDes(pros[i]);
                parse.setPropertyName(propName);
                parse.setBean(bean);
                try {
                    parse.setPropertyValue(getter.invoke(bean, null));
                    if (parse.getValue() == bean) //防止死循环
                        continue;
                } catch (Exception e) {
                    parse.setPropertyValue(null);
                }

                factors.add(parse);
            }

            return factors;
        } else {
            return new Vector();
        }
    }
    /**
     * 校验给定对象类型是否为非对象解析因子，然后返回解析对象
     * @param bean
     * @return  --BeanAndXMLParse对象
     */
    private BeanAndXMLParse checkIsParseFactor(Object bean)
    {
        BeanAndXMLParse parse = XMLFactory.getBeanToXMLParser(
                bean.getClass(), true);
        if(parse instanceof ObjectToXML)
        {
            return null;
        }else
        {
            parse.setPropertyDes(null);
            parse.setPropertyName(null);
            parse.setBean(null);
            parse.setPropertyValue(bean);
            return parse;
        }
    }
    /**
     * 将java bean导出为xml文档形式
     * 
     * @param file
     * @param bean
     * @param beanName
     * @throws XMLException
     */
    public void exportBeanToXML(File file, Object bean, String beanName)
            throws XMLException {
        if (file == null || bean == null)
            return;
        Document doc = null;
        doc = beanToXMLDoc(bean, beanName);
        saveDocumentToFile(doc,file);
    }
    public void saveDocumentToFile(Document doc,File file) throws XMLException
    {
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(file.getAbsolutePath(),false, false);
            out = new FileOutputStream(file);
            Format userFormat = Format.getPrettyFormat();
            userFormat.setEncoding(encoding);
            XMLOutputter xmlOut = new XMLOutputter(userFormat);
            xmlOut.output(doc, out);
        } catch (FileNotFoundException e) {
            throw new XMLException(e);
        } catch (IOException e) {
            throw new XMLException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
    /**
     * 从文件中解析出文档对象模型
     * 
     * @param file
     * @return
     * @throws XMLException
     * @throws IOException
     * @throws MalformedURLException
     * @throws JDOMException
     */
    public Document importDocumentFromXML(File file) throws MalformedURLException, XMLException, IOException {
        if (file == null)
            return null;
        if (!file.exists())
            return null;

        return importDocumentFromXML(file.toURL().openStream());

    }
    public Document importDocumentFromXML(InputStream input) throws XMLException {
        if (input == null)
            return null;

        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            doc = builder.build(input);
        } catch (JDOMException e) {
            throw new XMLException(e);
        } catch (IOException e) {
        	throw new XMLException(e);
		}
        return doc;

    }

    public  Class<?> getClass(String className) throws XMLException {
        try {
            return LoadJar.getInstance().getClassByName(className);
        } catch (ClassNotFoundException e) {
            throw new XMLException("can't find class:" + className + ", ClassLoader:" + loader.getClass());
        }
    }

    /**
     * 根据给定的xml元素对象，获取对应的java bean对象
     * 
     * @param root
     * @return
     * @throws XMLException
     * @throws XMLException
     */
    public Object getBean(Element root) throws XMLException{
        return getBean(root,null);
    }
    /**
     * 根据给定的xml元素对象，获取对应的java bean对象,该方法指定了已经被实例化的对象，该方法的处理过程负责设置给定对象（bean）的属性值。
     * 该方法适用于构造方法修饰符为非public的类型
     * @param root  --文档元素
     * @param bean  --Object 对象，该对象已经将需要返回的对象进行了初步的实例化。
     * @return
     * @throws XMLException
     */
    protected Object getBean(Element root,Object bean) throws XMLException{
        BeanInfo info = null;
        try {
            if (bean == null) {
                /**
                 * 检测是否为非对象解析元素
                 */
                bean = checkRootElement(root);
                if (bean != null)
                    return bean;

                /**
                 * 如果root对应的解析因子为对象
                 */
                String dataType = StringUtil.trim(root.getAttribute(
                        XMLConstant.TAG_ARRTIBUTE_DATATYPE).getValue());
                Class beanClass = getClass(dataType);
                
                /**
                 * 如果为接口，不作处理
                 */
                if(beanClass.isInterface())
                    return null;
                
                bean = beanClass.newInstance();  
            }

            info = Introspector.getBeanInfo(bean.getClass(),
                    Introspector.USE_ALL_BEANINFO);
        }catch (InstantiationException e) {
            throw new XMLException(PublicResource.getSQLString("system.xml.instantiateerror")+e.getMessage(),e);
        }catch (Exception e) {
            throw new XMLException(PublicResource
                    .getSQLString("system.xml.loaderror")
                    + e.getMessage(), e);
        } 
        
        /**
         * 将属性描述放入map对象中，便于后面操作的查找
         */
        PropertyDescriptor propDesc[] = info.getPropertyDescriptors();
        Map props = new HashMap();
        for (int i = 0; i < propDesc.length; i++)
            props.put(propDesc[i].getName(), propDesc[i]);

        //获取元素解析类
        Vector beanParsers = xmlToBeanParser(root, bean);

        Iterator it = beanParsers.iterator();
        while (it.hasNext()) {
            BeanAndXMLParse parser = (BeanAndXMLParse) it.next();
            PropertyDescriptor curProp = (PropertyDescriptor) props.get(parser
                    .getName());
            Object tmpValue = parser.isNull()?null:parser.getObjectInXML();  //如果xml中定义该变量为null,则直接将变量值赋为null
            evaluateProperty(bean, curProp, tmpValue);
        }

        return bean;
    }
    /**
     * 检验给定的文档元素对象是否是非对象因子。如果是非对象元素，直接解析，并返回相应的值，否则直接返回null值
     * @param root
     * @return  --如果是非对象元素，并且解析过程不出错，返回非空对象；如果是对象元素，返回null值
     * @throws XMLException
     */
    private Object checkRootElement(Element root) throws XMLException
    {
        String dataType = StringUtil.trim(root.getAttribute(
                XMLConstant.TAG_ARRTIBUTE_DATATYPE).getValue());
        Class beanClass = getClass(dataType);

        BeanAndXMLParse parser=XMLFactory.getBeanToXMLParser(beanClass,false);
        if(parser instanceof ObjectToXML)
        {
            return null;
        }else
        {
            parser.setElement(root);
            
            return parser.getObjectInXML();
            
        }
    }
    /**
     * 对属性进行赋值
     * 
     * @param bean
     *            --被赋值的对象
     * @param propDescr
     *            --属性描述
     * @param value --
     *            值
     * @throws XMLException
     */
    public static void evaluateProperty(Object bean,
            PropertyDescriptor propDescr, Object value) throws XMLException {
        Method setter = propDescr.getWriteMethod();
        try {
            setter.invoke(bean, new Object[] { value });
        } catch (Exception e) {
            throw new XMLException(e);
        }
    }

    /**
     * 将xml文档解析为bean的解析类
     * 
     * @param doc
     * @return
     * @throws XMLException
     */
    private Vector xmlToBeanParser(Element root, Object bean)
            throws XMLException {
        Vector v = new Vector();

        /**
         * 将属性描述放入map对象中，便于后面操作的查找
         */
        PropertyDescriptor propDesc[] = this.getBeanPropertys(bean);
        Map props = new HashMap();
        for (int i = 0; i < propDesc.length; i++)
            props.put(propDesc[i].getName(), propDesc[i]);

        Iterator it = root.getChildren().iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String name = e.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME); //属性名
            String dataType = StringUtil.trim(e.getAttribute(
                    XMLConstant.TAG_ARRTIBUTE_DATATYPE).getValue());//数据类型

            PropertyDescriptor pd = (PropertyDescriptor) props.get(name);
            if (pd == null)//如果能够找到相应的属性，那么进行处理，否则进入下一次解析
                continue;

            Method setter = pd.getWriteMethod();
            if(setter==null)  //对于方法isProperty类型的方法，没有对应的设置方法，那么setter为null
                continue;
            
            if(!name.equals(pd.getName()))  //如果属性名不匹配
            {
                throw new XMLException("property name("+name+") in xml don't match with name("+pd.getName()+") in java class");
            }
            
            Class paramType =setter.getParameterTypes()[0];

            Class type = null;
            if (paramType.isPrimitive()) {  
                if (!paramType.getName().equals(dataType))//如果数据类型不匹配
                    throw new XMLException("datatype(" + dataType
                            + ") in xml don't matche with type("
                            + paramType.getName() + ") in java class!");
                type = paramType;
            } else {
                type = getClass(dataType); //装载类
            }

            BeanAndXMLParse parse = XMLFactory.getBeanToXMLParser(type, false); //获取给定类型的解析类
            parse.setElement(e);
            parse.setPropertyName(name);
            
            String isNull = e.getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME); //是否为null属性值
            if(isNull!=null&&isNull.trim().equals("true"))  //除非显示的指定属性为null,才将该变量值视为null
                parse.setNull(true);
            else //其他情况否视为非null
                parse.setNull(false);
            
            parse.setPropertyDes(pd);
            v.add(parse);
        }
        return v;
    }

    public Object importBeanFromXML(File file) throws MalformedURLException, XMLException, IOException {
        Document doc = importDocumentFromXML(file);
        if (doc != null) {
            return getBean(doc.getRootElement());
        } else
            return null;
    }
    public Object importBeanFromXML(String path) throws XMLException
    {
        Document doc = importDocumentFromXML(getInputStream(path));
        if (doc != null) {
            return getBean(doc.getRootElement());
        } else
            return null;
    }
    public Object importBeanFromXML(InputStream inputStream) throws XMLException
    {
        Document doc = importDocumentFromXML(inputStream);
        if (doc != null) {
            return getBean(doc.getRootElement());
        } else
            return null;
    }
    public Object importBeanFromXML(File file,Object bean) throws MalformedURLException, XMLException, IOException {
        Document doc = importDocumentFromXML(file);
        if (doc != null) {
            return getBean(doc.getRootElement(),bean);
        } else
            return null;
    }
    public Object importBeanFromXML(String path,Object bean) throws XMLException
    {
        Document doc = importDocumentFromXML(getInputStream(path));
        if (doc != null) {
            return getBean(doc.getRootElement(),bean);
        } else
            return null;
    }
    public Object importBeanFromXML(InputStream inputStream,Object bean) throws XMLException
    {
        Document doc = importDocumentFromXML(inputStream);
        if (doc != null) {
            return getBean(doc.getRootElement(),bean);
        } else
            return null;
    }
    /**
     * get inputStream with a given path
     * @param path --path of resource file
     * @return 
     * @throws  
     * @throws XMLException 
     */
    public static InputStream getInputStream(String path) throws XMLException
    {
    	File file=new File(path);
        if(StringUtil.trim(path).equals(""))
        {
            return null;
        }else
        {
        	if(file.exists())
        	{
    			try {
    				return file.toURL().openStream();
    			}catch (Exception e) {
    				throw new XMLException("读取文件错误："+e.toString());
    			}
        	}
            return XMLBeanUtil.class.getResourceAsStream(path);
        }
    }
//    public static void main1(String args[]) {
//        XMLBeanUtil xml = new XMLBeanUtil();
//        Bookmark bookmark = new Bookmark();
//        System.out.println(bookmark.getType());
//        Document doc = null;
//        try {
//            doc = xml.beanToXMLDoc(bookmark, "bookmark");
//        } catch (XMLException e) {
//            e.printStackTrace();
//            return;
//        }
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream("test.xml");
//            XMLOutputter xmlOut = new XMLOutputter("", true);
//            xmlOut.setEncoding("GB2312");
//            xmlOut.output(doc, out);
//        } catch (FileNotFoundException e) {
//            // TODO 自动生成 catch 块
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO 自动生成 catch 块
//            e.printStackTrace();
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (Exception e) {
//                }
//            }
//        }
//    }

//    public static void main(String args[]) {
//        XMLBeanUtil xml = new XMLBeanUtil();
//        Bookmark bookmark = new Bookmark();
//        Document doc = null;
//        try {
//            bookmark = (Bookmark) xml.importBeanFromXML(new File("test.xml"));
//        } catch (XMLException e) {
//            e.printStackTrace();
//            return;
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        PropertyDescriptor propDesc[] = xml.getBeanPropertys(bookmark);
//        for (int i = 0; i < propDesc.length; i++) {
//            Method getter = propDesc[i].getReadMethod();
//            try {
//                System.out.println(propDesc[i].getName()+":"+getter.invoke(bookmark, null));
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(propDesc[i].getName() + ": exception occur");
//            }
//        }
//    }
}
