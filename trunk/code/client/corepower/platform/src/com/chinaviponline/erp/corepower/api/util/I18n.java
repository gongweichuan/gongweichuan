/**
 * 
 */
package com.chinaviponline.erp.corepower.api.util;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemSupportService;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

/**
 * <p>文件名称：I18n.java</p>
 * <p>文件描述：错误码的国际化</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-2</p>
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
 * @email  gongweichuan(AT)gmail.com
 */

public class I18n
{

    //    static DebugPrn dMsg;

    private static final int INIT_SIZE = 50;

    private static Map instanceCache = Collections.synchronizedMap(new HashMap(
            50));

    private static HashMap i18nFileCache = new HashMap(50);

    private static SerializationContainer registryInfo = null;

    private File xmlFile;

    private Locale locale;

    private Map itemMap;

    static
    {
       
    }

    public static I18n getInstance(String xmlFileName)
    {
        return getInstance(xmlFileName, Locale.getDefault());
    }

    public static I18n getInstance(String xmlFileName, Locale theLocale)
    {
        if (registryInfo == null)
        {
            registryInfo = (SerializationContainer) ServiceAccess
                    .getSystemSupportService().getFromRegistry(
                            "corepower.api.i18n");            
        }
        
        if (!xmlFileName.endsWith("-i18n.xml"))
        {
            throw new IllegalArgumentException("The xml file '" + xmlFileName
                    + "' does not end with -18n.xml");            
        }
        
        I18n instance = (I18n) instanceCache.get(xmlFileName);
        
        
        if (instance == null || !instance.locale.equals(theLocale))
        {
            try
            {
                instance = new I18n(xmlFileName, theLocale);
            }
            catch (JDOMException e)
            {
                e.printStackTrace();
                throw new IllegalArgumentException("The locale file '"
                        + xmlFileName + "' is not valid");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new IllegalArgumentException(
                        "IOException occured when reading '" + xmlFileName
                                + "'");
            }
            instanceCache.put(xmlFileName, instance);
        }
        return instance;
    }

    public static I18n getInstance(File xmlFile)
    {
        return getInstance(xmlFile, Locale.getDefault());
    }

    public static I18n getInstance(File xmlFile, Locale theLocale)
    {
        try
        {
            return new I18n(xmlFile, theLocale);
        }
        catch (JDOMException e)
        {
            throw new IllegalArgumentException(
                    "JDOMException occured when reading '" + xmlFile + "'");
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(
                    "JDOMException occured when reading '" + xmlFile + "'");
        }

    }

    public static void refreshI18ns()
    {
        instanceCache.clear();
        initFilesNames();
    }

    public String getLabelValue(String labelKey)
    {
        return getLabelValue2(labelKey);
    }

    /**
     * @deprecated Method getLabelValue2 is deprecated
     */

    public String getLabelValue2(String labelKey)
    {
        Item item = (Item) itemMap.get(labelKey);
        if (item == null)
        {
            //            dMsg.warn("The xml file of '" + xmlFile
            //                    + "' contains no label whose key is '" + labelKey + "'");
            return labelKey;
        }
        else
        {
            return item.getLabelValue();
        }
    }

    public String getLabelValue(String labelKey, String arguments[])
    {
        String labelValue = getLabelValue(labelKey);
        return MessageFormat.format(labelValue, arguments);
    }

    public String[] getLabelValues(String patternKey)
    {
        Pattern pattern = createPattern(patternKey);
        LinkedList labelKeys = new LinkedList();
        Iterator it = itemMap.values().iterator();
        
        do
        {
            if (!it.hasNext())
            {
                break;
            }
            
            Item next = (Item) it.next();
            Matcher matcher = pattern.matcher(next.getLabelKey());
            
            if (matcher.find())
            {
                labelKeys.add(next.getLabelKey());                
            }            
        }
        while (true);
        
        Collections.sort(labelKeys);
        LinkedList labelValues = new LinkedList();
        it = labelKeys.iterator();
        
        for (; it.hasNext(); labelValues.add(((Item) itemMap.get(it.next()))
                .getLabelValue()))
        {
            ;
        }

        return (String[]) labelValues.toArray(new String[labelValues.size()]);
    }

    private I18n(String xmlFileName, Locale theLocale) throws JDOMException,
            IOException
    {
        xmlFile = null;
        locale = null;
        itemMap = null;
        File theXmlFile = null;
        if (xmlFileName.indexOf("/") < 0
                && xmlFileName.indexOf(File.separator) < 0)
        {
            if (i18nFileCache.size() == 0)
                initFilesNames();
            theXmlFile = (File) i18nFileCache.get(xmlFileName);
            
            if (theXmlFile == null)
            {
                throw new IllegalArgumentException("The i18n XML file '"
                        + xmlFileName + "' does not exist");                
            }
        }
        else
        {
            theXmlFile = new File(xmlFileName);
        }
        readLabels(theXmlFile, theLocale, new HashSet());
    }

    private I18n(File theXmlFile, Locale theLocale) throws JDOMException,
            IOException
    {
        xmlFile = null;
        locale = null;
        itemMap = null;
        readLabels(theXmlFile, theLocale, new HashSet());
    }

    private I18n(File theXmlFile, Locale theLocale, Set includesFiles)
            throws JDOMException, IOException
    {
        xmlFile = null;
        locale = null;
        itemMap = null;
        readLabels(theXmlFile, theLocale, includesFiles);
    }

    private void readLabels(File theXmlFile, Locale theLocale,
            Set includeFileNames) throws JDOMException, IOException
    {
        xmlFile = theXmlFile;
        locale = theLocale;
        itemMap = new HashMap();
        includeFileNames.add(theXmlFile.getName());
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(xmlFile);
        Element rootElement = document.getRootElement();
        Element includeFilesElement = rootElement.getChild("include-files");
        
        if (includeFilesElement != null)
        {
            Iterator it = includeFilesElement.getChildren().iterator();
            do
            {
                if (!it.hasNext())
                {
                    break;                    
                }
                
                Element nextIncludeFileElement = (Element) it.next();
                String incluldeFileName = nextIncludeFileElement
                        .getAttributeValue("name");
                if (!includeFileNames.contains(incluldeFileName))
                {
                    if (i18nFileCache.size() == 0)
                        initFilesNames();
                    File includeFile = (File) i18nFileCache
                            .get(incluldeFileName);
                    
                    if (includeFile != null)
                    {
                        I18n includeInstance = new I18n(includeFile, theLocale,
                                includeFileNames);
                        itemMap.putAll(includeInstance.itemMap);
                    }
                }
            }
            while (true);
        }
        
        Element currentLocaleElement = getLocaleElement(rootElement, locale);
        
        if (currentLocaleElement != null)
        {
            Iterator it = currentLocaleElement.getChildren().iterator();
            do
            {
                if (!it.hasNext())
                {
                    break;                    
                }
                
                Element xmlPairElement = (Element) it.next();
                String labelKey = getAttributeValue(xmlPairElement, "label-key");
                String labelValue = getAttributeValue(xmlPairElement,
                        "label-value");
                
                if (labelKey != null)
                {
                    itemMap.put(labelKey, new Item(labelKey, labelValue));                    
                }
            }
            while (true);
        }
    }

    static Element getLocaleElement(Element rootElement, Locale theLocale)
    {
        Element currentLocaleElement;
        label0:
        {
            String languageCountry = theLocale.getLanguage() + '_'
                    + theLocale.getCountry();
            currentLocaleElement = null;
            Iterator it = rootElement.getChildren().iterator();
            do
            {
                if (!it.hasNext())
                {
                    break;                    
                }
                
                Element xmlElement = (Element) it.next();
                if (!languageCountry.equals(getAttributeValue(xmlElement,
                        "language-country")))
                {
                    continue;                    
                }
                
                currentLocaleElement = xmlElement;
                break;
            }
            while (true);
            
            if (currentLocaleElement != null)
            {
                break label0;                
            }
            
            String language = theLocale.getLanguage();

            it = rootElement.getChildren().iterator();
            Element xmlElement;
            do
            {
                if (!it.hasNext())
                {
                    break label0;                    
                }
                
                xmlElement = (Element) it.next();
            }
            while (!language.equals(getAttributeValue(xmlElement,
                    "language-country")));
            currentLocaleElement = xmlElement;
        }
        return currentLocaleElement;
    }

    static String getAttributeValue(Element xmlElement, String attributeName)
    {
        Attribute xmlAttribute = xmlElement.getAttribute(attributeName);
        
        if (xmlAttribute != null)
        {
            return xmlAttribute.getValue();            
        }
        else
        {
            return null;            
        }
    }

    private static Pattern createPattern(String inputText)
    {
        String prefix = "";
        String suffix = "";
        
        if (!inputText.startsWith("*") && !inputText.startsWith("?"))
        {
            prefix = "^";            
        }
        
        if (!inputText.endsWith("*") && !inputText.endsWith("?"))
        {
            suffix = "$";            
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

    private static void initFilesNames()
    {
        try
        {
            File i18nFiles[] = ServiceAccess.getSystemSupportService()
                    .getFiles("-i18n.xml");
            for (int i = 0; i < i18nFiles.length; i++)
            {
                String fileName = i18nFiles[i].getName();
                
                if (i18nFileCache.get(fileName) == null)
                {
                    ;                    
                }
                
                i18nFileCache.put(i18nFiles[i].getName(), i18nFiles[i]);
            }

        }
        catch (Throwable ignore)
        {
        }
    }

    private I18n()
    {
        xmlFile = null;
        locale = null;
        itemMap = null;
    }

    private void writeFields(ObjectOutput out) throws IOException
    {
        String fullPath = xmlFile.getAbsolutePath();
        out.writeShort(fullPath.length());
        out.writeChars(fullPath);
        out.writeUTF(locale.getLanguage());
        out.writeUTF(locale.getCountry());
        out.writeShort(itemMap.size());
        ArrayList items = new ArrayList(itemMap.values());
        Collections.sort(items);
        boolean isEnglish = locale.getLanguage().equals("en");
        int i = 0;
        for (int size = items.size(); i < size; i++)
        {
            Item next = (Item) items.get(i);
            out.writeUTF(next.getLabelKey());
            if (isEnglish)
            {
                out.writeUTF(next.theLabelValue);
            }
            else
            {
                out.writeShort(next.theLabelValue.length());
                out.writeChars(next.theLabelValue);
            }
        }

    }

    private void readFields(ObjectInput in, char reusedChars[])
            throws IOException
    {
        int fullPathLen = in.readShort();
        
        for (int i = 0; i < fullPathLen; i++)
        {
            reusedChars[i] = in.readChar();            
        }

        xmlFile = new File(new String(reusedChars, 0, fullPathLen));
        String language = in.readUTF();
        String country = in.readUTF();
        locale = new Locale(language, country);
        int itemCount = in.readShort();
        itemMap = new HashMap(itemCount);
        boolean isEnglish = language.equals("en");
        
        for (int i = 0; i < itemCount; i++)
        {
            Item next = new Item();
//            Item.access._mth102(next, in.readUTF());
            if (isEnglish)
            {
//                Item.access._mth002(next, in.readUTF());
            }
            else
            {
                int labelValueLen = in.readShort();
                for (int j = 0; j < labelValueLen; j++)
                    reusedChars[j] = in.readChar();

//                Item.access._mth002(next, new String(reusedChars, 0,
//                        labelValueLen));
            }
//            itemMap.put(Item.access._mth100(next), next);
        }

    }

    public static void startedup()
    {
        if (registryInfo == null)
        {
//            registryInfo = new SerializationContainer();
        }
           
        try
        {
            ServiceAccess.getSystemSupportService().setIntoRegistry(
                    "corepower.api.i18n", registryInfo);
        }
        catch (IOException ex)
        {
//            dMsg.warn("", ex);
        }
    }



   static class Item implements Comparable
    {

        public int getSequence()
        {
            return sequence;
        }

        public String getLabelKey()
        {
            return theLabelKey;
        }

        public String getLabelValue()
        {
            return theLabelValue;
        }

        public int compareTo(Object other)
        {
            Item otherItem = (Item) other;
            return sequence - otherItem.sequence;
        }

        private static int maxSequence = 0;

        private int sequence;

        private String theLabelKey;

        private String theLabelValue;

        Item(String labelKey, String labelValue)
        {
            sequence = maxSequence;
            theLabelKey = null;
            theLabelValue = null;
            maxSequence++;
            theLabelKey = labelKey;
            theLabelValue = labelValue != null ? labelValue : "";
        }

        Item()
        {
            sequence = maxSequence;
            theLabelKey = null;
            theLabelValue = null;
            maxSequence++;
        }
    }

    class SerializationContainer implements Externalizable
    {
        SerializationContainer()
        {
            
        }
        
        public void writeExternal(ObjectOutput out) throws IOException
        {
            /*
            out.writeShort(I18n.access$200().size());
            I18n next;
            for (Iterator it = I18n.access$200().values().iterator(); it
                    .hasNext(); I18n.access$300(next, out))
                next = (I18n) it.next();
            */
        }

        public void readExternal(ObjectInput in) throws IOException,
                ClassNotFoundException
        {
            try
            {
                char reusedChars[] = new char[0x493e0];
                int instanceCount = in.readShort();
                for (int i = 0; i < instanceCount; i++)
                {
                    /*
                    I18n next = new I18n(null);
                    I18n.access$500(next, in, reusedChars);
                    I18n.access$200()
                            .put(I18n.access$600(next).getName(), next);
                     */
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        private static final long serialVersionUID = 0xfffffff3927dd65fL;

    }

}
