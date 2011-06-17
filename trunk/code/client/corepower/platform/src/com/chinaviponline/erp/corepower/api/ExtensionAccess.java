package com.chinaviponline.erp.corepower.api;

//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.Reader;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import com.chinaviponline.erp.corepower.psl.ErpClassLoader;
//
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.input.SAXBuilder;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

/**
 * 
 * <p>文件名称：ExtensionAccess.java</p>
 * <p>文件描述：扩展点实现文件</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2007-7-18</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：
 *  版本号：
 *  修改人：
 *  修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email gongweichuan(AT)gmail.com
 */
public class ExtensionAccess
{

    static class DynamicSubject implements InvocationHandler
    {

        private String implClassNames[];

        private Object chains[];

        public Object invoke(Object proxy, Method method, Object args[])
                throws Throwable
        {
            if (chains == null)
            {
                chains = new Object[implClassNames.length];
                
                for (int i = 0; i < implClassNames.length; i++)
                {
                    try
                    {
                        Class theImplClass = Class.forName(implClassNames[i]);
                        chains[i] = theImplClass.newInstance();
                    }
                    catch (Exception ignore)
                    {
                        throw new Error("Can not resolve the class '"
                                + implClassNames[i] + "'", ignore);
                    }                    
                }

            }
            
            for (int i = 0; i < chains.length; i++)
            {
                method.invoke(chains[i], args);                
            }

            return null;
        }

        DynamicSubject(String implClassNames[])
        {
            this.implClassNames = null;
            chains = null;
            this.implClassNames = implClassNames;
        }
    }

    public static class ExtensionContainter
    {

        ExtensionDesc desc;

        List extensions;

        ClassLoader implClassLoader;

        void writeFields(ObjectOutput out) throws IOException
        {
            desc.writeFields(out);
            out.writeShort(extensions.size());
            ExtensionImpl next;
            
            for (Iterator it = extensions.iterator(); it.hasNext(); next
                    .writeFields(out))
            {
                next = (ExtensionImpl) it.next();                
            }

        }

        void readFields(ObjectInput in) throws IOException
        {
            desc = new ExtensionDesc();
            desc.readFields(in);
            int extensionCount = in.readShort();
            
            
            for (int i = 0; i < extensionCount; i++)
            {
                ExtensionImpl next = new ExtensionImpl();
                next.readFields(in);
                extensions.add(next);
            }

        }

        public void addImpl(ExtensionImpl impl)
        {
            extensions.add(impl);
        }

        void ensure(ExtensionDesc desc)
        {
            if (desc.extentionInterfaceClass == null)
                try
                {
                    if (implClassLoader == null)
                    {
                        desc.extentionInterfaceClass = Class
                                .forName(desc.extentionInterfaceClassName);                        
                    }
                    else
                    {
                        desc.extentionInterfaceClass = implClassLoader
                                .loadClass(desc.extentionInterfaceClassName);                        
                    }
                }
                catch (ClassNotFoundException e)
                {
                    throw new Error("The extension interface class '"
                            + desc.extentionInterfaceClassName
                            + "' does not exist");
                }
        }

        public Object getOne(String type)
        {
            ExtensionImpl fuctionExtension;
            ExtensionImpl extension;
            
            label0:
            {
                fuctionExtension = null;
                extension = null;
                ExtensionImpl next;
                
                label1: do
                {
                    for (Iterator it = extensions.iterator(); it.hasNext();)
                    {
                        next = (ExtensionImpl) it.next();
                        
                        if (next.preciseMatch)
                        {
                            continue label1;                            
                        }
                        
                        if (type.startsWith(next.filterType)
                                || ExtensionAccess.isMatched(type,
                                        next.filterType))
                        {
                            if (extension == null)
                            {
                                extension = next;                                
                            }
                            else if (extension.filterType.length() < next.filterType
                                    .length())
                            {
                                extension = next;                                
                            }
                        }
                        else if (isFunctionCheck(next, type))
                        {
                            fuctionExtension = next;                            
                        }
                        
                    }

                    break label0;
                }
                while (!type.equals(next.filterType));
                
                extension = next;
            }
            if (extension == null)
            {
                extension = fuctionExtension;                
            }
            
            if (extension == null)
            {
                return null;
            }
            else
            {
                ensure(desc);
                return extension.getImplObject(desc.extentionInterfaceClass,
                        implClassLoader);
            }
        }

        public Object[] getMany(String type)
        {
            List implInstances = new LinkedList();
            Collections.sort(extensions);
            Iterator it = extensions.iterator();
            
            do
            {
                if (!it.hasNext())
                {
                    break;                    
                }
                
                ExtensionImpl next = (ExtensionImpl) it.next();
                
                if (!next.preciseMatch)
                {
                    if (ExtensionAccess.isMatched(type, next.filterType)
                            || isFunctionCheck(next, type))
                    {
                        ensure(desc);
                        implInstances.add(next.getImplObject(
                                desc.extentionInterfaceClass, implClassLoader));
                    }
                }
                else if (type.equals(next.filterType))
                {
                    ensure(desc);
                    implInstances.add(next.getImplObject(
                            desc.extentionInterfaceClass, implClassLoader));
                }
            }
            while (true);
            return implInstances.toArray();
        }

        public Object[] getAll()
        {
            Object returnedValues[] = new Object[extensions.size()];
            int index = 0;
            Collections.sort(extensions);
            
            for (Iterator it = extensions.iterator(); it.hasNext();)
            {
                ExtensionImpl next = (ExtensionImpl) it.next();
                ensure(desc);
                returnedValues[index] = next.getImplObject(
                        desc.extentionInterfaceClass, implClassLoader);
                index++;
            }

            return returnedValues;
        }

        private boolean isFunctionCheck(ExtensionImpl next, String inputType)
    {
        String lowText;
        String highText;
        
        if(!next.filterType.startsWith("check(bound("))
        {
//            break MISSING_BLOCK_LABEL_105; 
            return false;
        }
        
        String valueText = next.filterType.substring(12);
        int firstCommaIndex = valueText.indexOf(",");
        int firstQuataIndex = valueText.indexOf(")");
        lowText = valueText.substring(0, firstCommaIndex).trim();
        highText = valueText.substring(firstCommaIndex + 1, firstQuataIndex);
        int low;
        int high;
        int inputValue;
        low = Integer.parseInt(lowText);
        high = Integer.parseInt(highText);
        inputValue = Integer.parseInt(inputType);
        
        if(low <= inputValue && inputValue <= high)
        {
            return true;            
        }
                
//        break MISSING_BLOCK_LABEL_105;
//        Exception ex;
//        ex;
//        return false;
        return false;
    }

        ExtensionContainter()
        {
            extensions = new LinkedList();
            implClassLoader = null;
        }

        ExtensionContainter(ExtensionDesc desc)
        {
            extensions = new LinkedList();
            implClassLoader = null;
            this.desc = desc;
        }

        public ExtensionContainter(ExtensionDesc desc,
                ClassLoader implClassLoader)
        {
            extensions = new LinkedList();
            this.implClassLoader = null;
            this.desc = desc;
            this.implClassLoader = implClassLoader;
        }
    }

    public static class ExtensionImpl implements Comparable
    {

        String filterType;

        String implClassName;

        Object implObject;

        String setMethodNames[];

        String attributeValues[];

        String chainImplClassNames[];

        boolean preciseMatch;

        int serialNumber;

        boolean singleton;

        void writeFields(ObjectOutput out) throws IOException
        {
            out.writeUTF(filterType);
            out.writeBoolean(preciseMatch);
            out.writeInt(serialNumber);
            out.writeBoolean(singleton);
            if (chainImplClassNames == null)
            {
                out.writeBoolean(false);
                out.writeUTF(implClassName);
                int methodCount = setMethodNames != null ? setMethodNames.length
                        : 0;
                out.writeByte((byte) methodCount);
                for (int i = 0; i < methodCount; i++)
                {
                    out.writeUTF(setMethodNames[i]);
                    out.writeUTF(attributeValues[i]);
                }

            }
            else
            {
                out.writeBoolean(true);
                out.writeByte(chainImplClassNames.length);
                
                for (int i = 0; i < chainImplClassNames.length; i++)
                {
                    out.writeUTF(chainImplClassNames[i]);                    
                }
            }
        }

        void readFields(ObjectInput in) throws IOException
        {
            filterType = in.readUTF();
            preciseMatch = in.readBoolean();
            serialNumber = in.readInt();
            singleton = in.readBoolean();
            boolean isChain = in.readBoolean();
            
            if (!isChain)
            {
                implClassName = in.readUTF();
                int methodCount = in.readByte();
                
                if (methodCount > 0)
                {
                    setMethodNames = new String[methodCount];
                    attributeValues = new String[methodCount];
                    for (int i = 0; i < methodCount; i++)
                    {
                        setMethodNames[i] = in.readUTF();
                        attributeValues[i] = in.readUTF();
                    }

                }
            }
            else
            {
                int chainCount = in.readByte();
                chainImplClassNames = new String[chainCount];
                
                for (int i = 0; i < chainCount; i++)
                {
                    chainImplClassNames[i] = in.readUTF();                    
                }

                implObject = new DynamicSubject(chainImplClassNames);
            }
        }

        public int compareTo(Object o)
        {
            ExtensionImpl other = (ExtensionImpl) o;
            return serialNumber - other.serialNumber;
        }

        Object getImplObject(Class extensionInterfaceClass,
                ClassLoader implClassLoader)
        {
            if (implObject == null || !singleton)
            {
                Class implClass = null;
                try
                {
                    if (implClassLoader == null)
                    {
                        implClass = Class.forName(implClassName);                        
                    }
                    else
                    {
                        implClass = implClassLoader.loadClass(implClassName);                        
                    }
                    
                    implObject = implClass.newInstance();
                    
                    if (setMethodNames != null)
                    {
                        for (int i = 0; i < setMethodNames.length; i++)
                        {
                            Method setMethod = null;
                            try
                            {
                                setMethod = implClass
                                        .getMethod(
                                                setMethodNames[i],
                                                new Class[] {java.lang.String.class});
                                setMethod.invoke(implObject,
                                        new Object[] {attributeValues[i]});
                            }
                            catch (NoSuchMethodException e)
                            {
                                throw new Error("The class '" + implClass
                                        + "' has no public method of '"
                                        + setMethodNames[i] + "'", e);
                            }
                            catch (InvocationTargetException e)
                            {
                                throw new Error("The method '" + setMethod
                                        + "' can not be invoked", e);
                            }
                            catch (IllegalAccessException e)
                            {
                                throw new Error("Can not access the method '"
                                        + setMethod + "'", e);
                            }
                        }

                    }
                }
                catch (ClassNotFoundException e)
                {
                    throw new Error("Can not found the class '" + implClassName
                            + "'", e);
                }
                catch (InstantiationException e)
                {
                    throw new Error(
                            "Can not construct an instance with the class '"
                                    + implClass + "'");
                }
                catch (IllegalAccessException e)
                {
                    throw new Error("Can not access the class '" + implClass
                            + "'");
                }
            }
            else if (implObject.getClass().equals(ExtensionAccess.DynamicSubject.class))
            {
                implObject = Proxy.newProxyInstance(extensionInterfaceClass
                        .getClassLoader(),
                        new Class[] {extensionInterfaceClass},
                        (InvocationHandler) implObject);                
            }
            
            return implObject;
        }

        ExtensionImpl()
        {
            setMethodNames = null;
            attributeValues = null;
            chainImplClassNames = null;
            preciseMatch = false;
            serialNumber = 0;
            singleton = true;
        }

        public ExtensionImpl(Element extensionElement)
        {
            setMethodNames = null;
            attributeValues = null;
            chainImplClassNames = null;
            preciseMatch = false;
            serialNumber = 0;
            singleton = true;
            if (extensionElement.getAttribute("type") != null)
            {
                filterType = extensionElement.getAttribute("type").getValue()
                        .trim().toLowerCase();                
            }
            else
            {
                filterType = "";                
            }
            
            if (extensionElement.getAttribute("precise-match") != null)
            {
                String preciseMatchText = extensionElement.getAttributeValue(
                        "precise-match").trim();
                
                if (preciseMatchText.equals("true"))
                {
                    preciseMatch = true;                    
                }
            }
            if (extensionElement.getAttribute("singleton") != null)
            {
                String singletonText = extensionElement
                        .getAttributeValue("singleton");
                
                if (singletonText.equals("false"))
                {
                    singleton = false;                    
                }
            }
            
            if (extensionElement.getAttribute("serial-number") != null)
            {
                String serialNumberText = extensionElement.getAttributeValue(
                        "serial-number").trim();
                serialNumber = Integer.parseInt(serialNumberText);
            }
            
            implClassName = extensionElement.getAttribute("class").getValue()
                    .trim();
            Element attributesElement = extensionElement.getChild("attributes");
            
            if (attributesElement != null)
            {
                List attributeElements = attributesElement.getChildren();
                setMethodNames = new String[attributeElements.size()];
                attributeValues = new String[setMethodNames.length];
                int index = 0;
                
                for (Iterator it = attributeElements.iterator(); it.hasNext();)
                {
                    Element attributeElement = (Element) it.next();
                    attributeValues[index] = attributeElement.getAttribute(
                            "value").getValue().trim();
                    String attributeName = attributeElement
                            .getAttribute("name").getValue().trim();
                    char nameChars[] = attributeName.toCharArray();
                    StringBuffer methodNameBuffer = new StringBuffer("set");
                    methodNameBuffer
                            .append(Character.toUpperCase(nameChars[0]));
                    
                    if (nameChars.length > 1)
                    {
                        methodNameBuffer.append(nameChars, 1,
                                nameChars.length - 1);                        
                    }
                    
                    setMethodNames[index] = methodNameBuffer.toString();
                    index++;
                }

            }
        }

        public ExtensionImpl(String filterType, Element chainElements[])
        {
            setMethodNames = null;
            attributeValues = null;
            chainImplClassNames = null;
            preciseMatch = false;
            serialNumber = 0;
            singleton = true;
            this.filterType = filterType;
            chainImplClassNames = new String[chainElements.length];
            
            for (int i = 0; i < chainElements.length; i++)
            {
                chainImplClassNames[i] = chainElements[i].getAttribute("class")
                        .getValue().trim();                
            }

            InvocationHandler ds = new DynamicSubject(chainImplClassNames);
            implObject = ds;
        }
    }

    public static class ExtensionDesc
    {

        String extensionID;

        String extentionInterfaceClassName;

        Class extentionInterfaceClass;

        void writeFields(ObjectOutput out) throws IOException
        {
            out.writeUTF(extensionID);
            out.writeUTF(extentionInterfaceClassName);
        }

        void readFields(ObjectInput in) throws IOException
        {
            extensionID = in.readUTF();
            extentionInterfaceClassName = in.readUTF();
        }

        public String getExtensionID()
        {
            return extensionID;
        }

        ExtensionDesc()
        {
            extentionInterfaceClassName = null;
            extentionInterfaceClass = null;
        }

        public ExtensionDesc(Element extensionDescElement)
        {
            extentionInterfaceClassName = null;
            extentionInterfaceClass = null;
            extensionID = extensionDescElement.getAttribute("id").getValue()
                    .trim();
            String interfaceClassName = extensionDescElement.getChild(
                    "interface-class").getText().trim();
            extentionInterfaceClassName = interfaceClassName;
        }

        ExtensionDesc(String extensionID, Class interfaceClass)
        {
            extentionInterfaceClassName = null;
            extentionInterfaceClass = null;
            this.extensionID = extensionID;
            extentionInterfaceClass = interfaceClass;
            extentionInterfaceClassName = interfaceClass.getName();
        }
    }

    private static final int INIT_SIZE = 100;

    static Map containerMap = new HashMap(100);



    public ExtensionAccess()
    {
    }

    public static Object getExtension(String extensionID, String type)
    {
        ExtensionContainter container = (ExtensionContainter) containerMap
                .get(extensionID);
        
        if (container == null)
        {
            return null;            
        }
        else
        {
            return container.getOne(type.toLowerCase());            
        }
        
    }

    public static Object[] getExtensions(String extensionID, String type)
    {
        ExtensionContainter container = (ExtensionContainter) containerMap
                .get(extensionID);
        
        
        if (container == null)
        {
            return new Object[0];            
        }
        else
        {
            return container.getMany(type.toLowerCase());            
        }
    }

    public static Object[] getExtensions(String extensionID)
    {
        ExtensionContainter container = (ExtensionContainter) containerMap
                .get(extensionID);
        
        if (container == null)
        {
            return new Object[0];            
        }
        else
        {
            return container.getAll();            
        }
    }

    public static void injectExtensionBinding(File descFile,
            File extensionImplFile)
    {
        try
        {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(descFile);
            Element rootElement = document.getRootElement();
            ExtensionDesc desc;
            ExtensionContainter container;
            
            for (Iterator it = rootElement.getChildren().iterator(); it
                    .hasNext(); containerMap.put(desc.getExtensionID(),
                    container))
            {
                Element extensionDescElement = (Element) it.next();
                desc = new ExtensionDesc(extensionDescElement);
                container = new ExtensionContainter(desc);
            }

        }
        catch (JDOMException e)
        {
            throw new Error("The extension desc file '" + descFile
                    + "' is not valid", e);
        }
        catch (IOException e)
        {
            throw new Error("IOException occured when reading '" + descFile
                    + "'", e);
        }
        
        try
        {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(extensionImplFile);
            Element rootElement = document.getRootElement();
            for (Iterator it = rootElement.getChildren().iterator(); it
                    .hasNext();)
            {
                Element extensionElement = (Element) it.next();
                String theExtensionID = extensionElement.getAttribute("id")
                        .getValue().trim();
                ExtensionContainter theContainer = (ExtensionContainter) containerMap
                        .get(theExtensionID);
                
                if (theContainer == null)
                {
                    throw new Error("In the extension file '"
                            + extensionImplFile
                            + "' there is a invalid extention point '"
                            + theExtensionID
                            + "' which does not exist in one desc file");                    
                }
                
                if (extensionElement.getName().equals("extension-point"))
                {
                    Iterator itor = extensionElement.getChildren().iterator();
                    while (itor.hasNext())
                    {
                        Element implElement = (Element) itor.next();
                        ExtensionImpl extension = new ExtensionImpl(implElement);
                        theContainer.addImpl(extension);
                    }
                }
                else
                {
                    String filterType = "";
                    Attribute typeAttribute = extensionElement
                            .getAttribute("type");
                    if (typeAttribute != null)
                    {
                        filterType = typeAttribute.getValue().trim();                        
                    }
                    
                    Element chainElements[] = (Element[]) extensionElement
                            .getChildren().toArray(
                                    new Element[extensionElement.getChildren()
                                            .size()]);
                    
                    ExtensionImpl extension = new ExtensionImpl(filterType,
                            chainElements);
                    
                    theContainer.addImpl(extension);
                }
            }

        }
        catch (JDOMException e)
        {
            throw new Error("The extension impl file '" + extensionImplFile
                    + "' is not valid", e);
        }
        catch (IOException e)
        {
            throw new Error("IOException occured when reading '"
                    + extensionImplFile + "'", e);
        }
    }

    public static void tryToInjectExtensionBindings(File descFiles[],
            File extensionImplFiles[], ClassLoader implClassLoader)
    {
        SAXBuilder builder = new SAXBuilder();
        
        for (int i = 0; i < descFiles.length; i++)
        {
            try
            {
                Element rootElement = builder.build(descFiles[i])
                        .getRootElement();
                ExtensionDesc desc;
                ExtensionContainter container;
                
                for (Iterator it = rootElement.getChildren().iterator(); it
                        .hasNext(); containerMap.put(desc.getExtensionID(),
                        container))
                {
                    Element extensionDescElement = (Element) it.next();
                    desc = new ExtensionDesc(extensionDescElement);
                    container = new ExtensionContainter(desc, implClassLoader);
                }

            }
            catch (Exception ignore)
            {
            }            
        }

        label0: for (int i = 0; i < extensionImplFiles.length;)
            try
            {
                Element rootElement = builder.build(extensionImplFiles[i])
                        .getRootElement();
                Iterator it = rootElement.getChildren().iterator();
                
                do
                {
                    Element extensionElement;
                    ExtensionContainter theContainer;
                    do
                    {
                        if (!it.hasNext())
                        {
                            continue label0;                            
                        }
                        
                        extensionElement = (Element) it.next();
                        String theExtensionID = extensionElement.getAttribute(
                                "id").getValue().trim();
                        theContainer = (ExtensionContainter) containerMap
                                .get(theExtensionID);
                    }
                    while (theContainer == null);
                    
                    if (extensionElement.getName().equals("extension-point"))
                    {
                        Iterator itor = extensionElement.getChildren()
                                .iterator();
                        
                        while (itor.hasNext())
                        {
                            Element implElement = (Element) itor.next();
                            ExtensionImpl extension = new ExtensionImpl(
                                    implElement);
                            theContainer.addImpl(extension);
                        }
                    }
                    else
                    {
                        String filterType = "";
                        Attribute typeAttribute = extensionElement
                                .getAttribute("type");
                        if (typeAttribute != null)
                        {
                            filterType = typeAttribute.getValue().trim();                            
                        }
                        
                        Element chainElements[] = (Element[]) extensionElement
                                .getChildren().toArray(
                                        new Element[extensionElement
                                                .getChildren().size()]);
                        ExtensionImpl extension = new ExtensionImpl(filterType,
                                chainElements);
                        theContainer.addImpl(extension);
                    }
                }
                while (true);
            }
            catch (Exception ignore)
            {
                i++;
            }

    }

    private static Pattern createPattern(String inputStr)
    {
        String inputText = inputStr;
        String prefix = "";
        String suffix = "";
        
        if (inputText.indexOf('*') >= 0 || inputText.indexOf('?') >= 0)
        {
            if (!inputText.startsWith("*") && !inputText.startsWith("?"))
            {
                prefix = "^";                
            }
            
            if (!inputText.endsWith("*") && !inputText.endsWith("?"))
            {
                suffix = "$";                
            }
            
        }
        
        StringBuffer noEscapeStr = new StringBuffer(inputText.length());
        int i = 0;
        
        for (int length = inputText.length(); i < length; i++)
        {
            char c = inputText.charAt(i);
            if (c == '\\')
            {
                noEscapeStr.append("\\\\");
                continue;
            }
            if (c == '.')
            {
                noEscapeStr.append("\\.");
                continue;
            }
            if (c == '*')
            {
                noEscapeStr.append(".*");
                continue;
            }
            if (c == '$')
            {
                noEscapeStr.append("\\$");
                continue;
            }
            if (c == '+')
            {
                noEscapeStr.append("\\+");
                continue;
            }
            if (c == '|')
            {
                noEscapeStr.append("\\|");
                continue;
            }
            if (c == '[')
            {
                noEscapeStr.append("\\[");
                continue;
            }
            if (c == ']')
            {
                noEscapeStr.append("\\]");
                continue;
            }
            if (c == '{')
            {
                noEscapeStr.append("\\{");
                continue;
            }
            if (c == '}')
            {
                noEscapeStr.append("\\}");
                continue;
            }
            if (c == '(')
            {
                noEscapeStr.append("\\(");
                continue;
            }
            if (c == ')')
            {
                noEscapeStr.append("\\)");
                continue;
            }
            
            if (c == '?')
            {
                noEscapeStr.append(".?");                
            }
            else
            {
                noEscapeStr.append(c);                
            }
        }

        return Pattern.compile(prefix + noEscapeStr.toString() + suffix);
    }

    private static boolean isMatched(String inputType, String filterType)
    {
        Pattern pattern = createPattern(filterType);
        Matcher matcher = pattern.matcher(inputType);
        return matcher.find();
    }

}
