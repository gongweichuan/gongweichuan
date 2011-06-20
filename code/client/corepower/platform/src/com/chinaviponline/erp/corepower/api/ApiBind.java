/**
 * 
 */
package com.chinaviponline.erp.corepower.api;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemSupportService;

//import javax.management.MBeanServer;
//import javax.management.ObjectName;

/**
 * <p>文件名称：ApiBind.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-7-9</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public class ApiBind extends BaseMBeanSupport implements ApiBindMBean
{

    public static class InterfaceContainer implements Externalizable
    {

        private static final long serialVersionUID = 0xffffffffff67ec27L;

        ArrayList localItems;

        ArrayList ejbItems;

        ArrayList extensionContainers;

        public void writeExternal(ObjectOutput out) throws IOException
        {
            writeServices(out, localItems);
            writeServices(out, ejbItems);
            writeExtensions(out);
        }

        public void readExternal(ObjectInput in) throws IOException,
                ClassNotFoundException
        {
            readServices(in, localItems, true);
            readServices(in, ejbItems, false);
            readExtensions(in);
        }

        private void writeServices(ObjectOutput out, ArrayList items)
                throws IOException
        {
            int itemSize = items.size();
            out.writeShort((short) itemSize);
            for (int i = 0; i < itemSize; i++)
            {
                ServiceAccess.ServiceItem next = (ServiceAccess.ServiceItem) items
                        .get(i);
                next.writeFields(out);
            }

        }

        private void writeExtensions(ObjectOutput out) throws IOException
        {
            int extensionCount = extensionContainers.size();
            out.writeShort(extensionCount);
            for (int i = 0; i < extensionCount; i++)
            {
                ExtensionAccess.ExtensionContainter next = (ExtensionAccess.ExtensionContainter) extensionContainers
                        .get(i);
                next.writeFields(out);
            }

        }

        private void readServices(ObjectInput in, ArrayList items,
                boolean isLocal) throws IOException
        {
            int itemSize = in.readShort();
            
            for (int i = 0; i < itemSize; i++)
            {
                ServiceAccess.ServiceItem item = null;
                
                if (isLocal)
                {
                    item = new ServiceAccess.LocalItem();                    
                }
                else
                {
                    item = new ServiceAccess.EJBItem();                    
                }
                
                item.readFields(in);
                items.add(item);
            }

        }

        private void readExtensions(ObjectInput in) throws IOException
        {
            int extensionCount = in.readShort();
            
            for (int i = 0; i < extensionCount; i++)
            {
                ExtensionAccess.ExtensionContainter next = new ExtensionAccess.ExtensionContainter();
                next.readFields(in);
                extensionContainers.add(next);
            }

        }

        private void clear()
        {
            localItems.clear();
            ejbItems.clear();
            extensionContainers.clear();
            localItems = null;
            ejbItems = null;
            extensionContainers = null;
        }

        public InterfaceContainer()
        {
            localItems = new ArrayList(50);
            ejbItems = new ArrayList(5);
            extensionContainers = new ArrayList(100);
        }
    }

    public ApiBind()
    {
    }

    public void createService()
    {
        String BOOST_SERVICE = (String) System.getProperties().remove(
                "corepower.psl.systemsupport.serviceimpl");
        
        if (BOOST_SERVICE == null)
        {
            throw new Error(
                    "Can not found systemsupport service implementation in system properties");            
        }
        
        try
        {
            Class boostServiceClass = Class.forName(BOOST_SERVICE);
            Object impl = boostServiceClass.newInstance();
            ServiceAccess
                    .injectServiceBinding(
                            "system-support-service",
                            SystemSupportService.class,
                            impl);
        }
        catch (Exception ex)
        {
            throw new Error("init systemsupport service implementation error",
                    ex);
        }
        
        InterfaceContainer serialized = (InterfaceContainer) ServiceAccess
                .getSystemSupportService()
                .getFromRegistry("corepower.api.interfaces");
        
        
        if (serialized == null)
        {
            serialized = new InterfaceContainer();
            readOtherServiceFiles(serialized);
            readExtensionDescFiles();
            readExtensionImplFiles();
            serialized.extensionContainers.addAll(ExtensionAccess.containerMap
                    .values());
            try
            {
                ServiceAccess.getSystemSupportService().setIntoRegistry(
                        "corepower.api.interfaces", serialized);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            int i = 0;
            for (int size = serialized.localItems.size(); i < size; i++)
            {
                ServiceAccess.ServiceItem item = (ServiceAccess.ServiceItem) serialized.localItems
                        .get(i);
                ServiceAccess.serviceMap.put(item.serviceID, item);
            }

            i = 0;
            for (int size = serialized.ejbItems.size(); i < size; i++)
            {
                ServiceAccess.ServiceItem item = (ServiceAccess.ServiceItem) serialized.ejbItems
                        .get(i);
                ServiceAccess.serviceMap.put(item.serviceID, item);
            }

            i = 0;
            for (int size = serialized.extensionContainers.size(); i < size; i++)
            {
                ExtensionAccess.ExtensionContainter next = (ExtensionAccess.ExtensionContainter) serialized.extensionContainers
                        .get(i);
                ExtensionAccess.containerMap.put(next.desc.extensionID, next);
            }

        }
        serialized.clear();
        String locale = ServiceAccess.getSystemSupportService().getERPProperty(
                "erp.locale");
        StringTokenizer st = new StringTokenizer(locale, "_");
        String lan = st.nextToken();
        String con = st.nextToken();
        Locale.setDefault(new Locale(lan, con));
    }

    public void startService()
    {
    }

    public void stopService()
    {
    }

    public void destroyService()
    {
    }

    private static void readServiceFile(File bindingFile,
            InterfaceContainer serialized)
    {
        try
        {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(bindingFile);
            Element rootElement = document.getRootElement();
            ServiceAccess.ServiceItem item;
            for (Iterator it = rootElement.getChildren().iterator(); it
                    .hasNext(); ServiceAccess.serviceMap.put(item.serviceID,
                    item))
            {
                Element serviceElement = (Element) it.next();
                item = null;
                if (serviceElement.getChild("local-impl") != null)
                {
                    item = new ServiceAccess.LocalItem(serviceElement);
                    serialized.localItems.add(item);
                }
                else
                {
                    item = new ServiceAccess.EJBItem(serviceElement);
                    serialized.ejbItems.add(item);
                }
            }

        }
        catch (JDOMException e)
        {
            e.printStackTrace();
            throw new Error("The xml file '" + bindingFile + "' is not valid",
                    e);
        }
        catch (IOException e)
        {
            throw new Error("IOException occured when reading '" + bindingFile
                    + "'", e);
        }
    }

    private static void readOtherServiceFiles(InterfaceContainer serialized)
    {
        File allBindingFiles[] = ServiceAccess.getSystemSupportService()
                .getFiles("-servicebind.xml");
        
        for (int i = 0; i < allBindingFiles.length; i++)
        {
            readServiceFile(allBindingFiles[i], serialized);            
        }

    }

    private static void readExtensionDescFiles()
    {
        File descFiles[] = ServiceAccess.getSystemSupportService().getFiles(
                "-extensiondesc.xml");
        
        for (int i = 0; i < descFiles.length; i++)
        {
            try
            {
                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(descFiles[i]);
                Element rootElement = document.getRootElement();
                ExtensionAccess.ExtensionDesc desc;
                ExtensionAccess.ExtensionContainter container;
                for (Iterator it = rootElement.getChildren().iterator(); it
                        .hasNext(); ExtensionAccess.containerMap.put(
                        desc.extensionID, container))
                {
                    Element extensionDescElement = (Element) it.next();
                    desc = new ExtensionAccess.ExtensionDesc(
                            extensionDescElement);
                    container = new ExtensionAccess.ExtensionContainter(desc);
                }

            }
            catch (JDOMException e)
            {
                throw new Error("The extension desc file '" + descFiles[i]
                        + "' is not valid", e);
            }
            catch (IOException e)
            {
                throw new Error("IOException occured when reading '"
                        + descFiles[i] + "'", e);
            }            
        }

    }

    private static void readExtensionImplFiles()
    {
        File extensionFiles[] = ServiceAccess.getSystemSupportService()
                .getFiles("-extensionimpl.xml");
        
        for (int i = 0; i < extensionFiles.length; i++)
        {
            try
            {
                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(extensionFiles[i]);
                Element rootElement = document.getRootElement();
                for (Iterator it = rootElement.getChildren().iterator(); it
                        .hasNext();)
                {
                    Element extensionElement = (Element) it.next();
                    String extensionID = extensionElement.getAttribute("id")
                            .getValue().trim();
                    ExtensionAccess.ExtensionContainter container = (ExtensionAccess.ExtensionContainter) ExtensionAccess.containerMap
                            .get(extensionID);

                    if (container == null)
                    {
                        throw new Error("In the extension file '"
                                + extensionFiles[i]
                                + "' there is a invalid extention point '"
                                + extensionID
                                + "' which does not exist in one desc file");
                    }

                    if (extensionElement.getName().equals("extension-point"))
                    {
                        Iterator itor = extensionElement.getChildren()
                                .iterator();
                        while (itor.hasNext())
                        {
                            Element implElement = (Element) itor.next();
                            ExtensionAccess.ExtensionImpl extension = new ExtensionAccess.ExtensionImpl(
                                    implElement);
                            container.addImpl(extension);
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
                        ExtensionAccess.ExtensionImpl extension = new ExtensionAccess.ExtensionImpl(
                                filterType, chainElements);
                        container.addImpl(extension);
                    }
                }

            }
            catch (JDOMException e)
            {
                throw new Error("The extension impl file '" + extensionFiles[i]
                        + "' is not valid", e);
            }
            catch (IOException e)
            {
                throw new Error("IOException occured when reading '"
                        + extensionFiles[i] + "'", e);
            }            
        }

    }

}
