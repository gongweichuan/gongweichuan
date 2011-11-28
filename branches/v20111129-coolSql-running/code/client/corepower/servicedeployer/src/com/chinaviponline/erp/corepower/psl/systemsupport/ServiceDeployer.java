/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBean;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.chinaviponline.erp.corepower.api.psl.systemsupport.Par;
import com.chinaviponline.erp.corepower.psl.systemsupport.classloader.SelfAdaptClassLoader;
import com.chinaviponline.erp.corepower.psl.systemsupport.filescanner.FileScanner;

/**
 * <p>文件名称：ServiceDeployer.java</p>
 * <p>文件描述：部署各种服务,总的服务调度类</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-19</p>
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
public class ServiceDeployer implements ServiceDeployerInterface
{

    /**
     * 日志
     */
    private static Logger log;

    /**
     * 管理bean列表
     */
    private static List mbeans = new LinkedList();

    // 初始化
    static
    {
        log = Logger.getLogger(ServiceDeployer.class);
    }

    /**
     * 构造函数
     *
     */
    public ServiceDeployer()
    {
    }

    /**
     * 
     * 功能描述：部署服务
     * @see com.chinaviponline.erp.corepower.psl.systemsupport.ServiceDeployerInterface#deployServices()
     */
    public void deployServices()
    {
        Par pars[] = ParCollector.getSequenceDeployedPars();
        int size = pars.length;
        String deployPaths[] = new String[size];
        
        // 插件目录集合
        for (int i = 0; i < size; i++)
        {
            deployPaths[i] = pars[i].getBaseDir();            
        }

        File parJarFiles[] = FileScanner.getFiles(deployPaths, "*.jar", true);
        URL urls[] = new URL[parJarFiles.length];
        
        // jar文件集合
        for (int i = 0; i < parJarFiles.length; i++)
        {
            try
            {
                urls[i] = parJarFiles[i].toURL();
                log.debug("par jar:" + urls[i].toString());
            }
            catch (MalformedURLException ignore)
            {
                log.error("load jar " + urls[i].toString() + " fail!", ignore);
            }            
        }

        // 类加载器
        SelfAdaptClassLoader thisClassLoader = (SelfAdaptClassLoader) Thread
                .currentThread().getContextClassLoader();
        thisClassLoader.addURLs(urls);
        RegistryInfo registryInfo = (RegistryInfo) SystemSupportServiceClientImpl
                .getRegistry("corepower.psl.systemsurport.mbeans");
        
        if (registryInfo == null)
        {
            SAXBuilder builder = new SAXBuilder();
            File serviceFiles[] = FileScanner.getFiles(deployPaths,
                    "*-service.xml", true);
            Map mbeanMethodMap = new HashMap();
            for (int i = 0; i < serviceFiles.length; i++)
                try
                {
                    deployService(thisClassLoader, builder, serviceFiles[i],
                            mbeanMethodMap);
                }
                catch (Exception ignore)
                {
                    log.error("deploy " + serviceFiles[i] + " fail!", ignore);
                }

            try
            {
                SystemSupportServiceClientImpl.setRegistry(
                        "corepower.psl.systemsurport.mbeans", new RegistryInfo(
                                mbeanMethodMap));
            }
            catch (Exception ignore)
            {
                log.info(ignore);
            }
        }
        else
        {
            registryInfo.deployAfterUnserialization();
        }
    }

    /**
     * 
     * 功能描述：反部署
     * @see com.chinaviponline.erp.corepower.psl.systemsupport.ServiceDeployerInterface#undeployServices()
     */
    public void undeployServices()
    {
        for (ListIterator it = mbeans.listIterator(); it.hasPrevious();)
        {
            ServiceMBean previous = (ServiceMBean) it.previous();
            try
            {
                previous.stop();
                previous.destroy();
            }
            catch (Exception ignore)
            {
                log.info(ignore);
            }
        }

    }

    /**
     * 
     * <p>功能描述：部署服务</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-20</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param classLoader
     * @param builder
     * @param serviceXMLFile
     * @param aMbeanMethodMap
     * @throws Exception
     */
    private void deployService(ClassLoader classLoader, SAXBuilder builder,
            File serviceXMLFile, Map aMbeanMethodMap) throws Exception
    {
        Document doc = builder.build(serviceXMLFile);
        log.debug("service file:" + serviceXMLFile);
        Element rootElement = doc.getRootElement();
        String mbeanClassName = null;
        for (Iterator it = rootElement.getChildren("mbean").iterator(); it
                .hasNext();)
        {
            Element mbeanElement = (Element) it.next();
            try
            {
                mbeanClassName = mbeanElement.getAttributeValue("code");
                log.debug("mbean class:" + mbeanClassName);
                int attrNum = 0;
                Map methodMap = new HashMap();
                for (Iterator it1 = mbeanElement.getChildren("attribute")
                        .iterator(); it1.hasNext();)
                {
                    Element attrElement = (Element) it1.next();
                    String attrName = attrElement.getAttributeValue("name");
                    String attrValue = attrElement.getText();
                    log.debug("attribute name: " + attrName
                            + ", attribute value:" + attrValue);
                    methodMap.put("set" + attrName, attrValue);
                    attrNum++;
                }

                Class mbeanClass = Class.forName(mbeanClassName, true,
                        classLoader);
                ServiceMBean mbean = (ServiceMBean) mbeanClass.newInstance();
                
                if (attrNum > 0)
                {
                    Method methods[] = mbeanClass.getMethods();
                    invokeMethod(mbean, methods, methodMap);
                }
                
                mbean.create();
                mbean.start();
                mbeans.add(mbean);
                aMbeanMethodMap.put(mbean, methodMap);
            }
            catch (Exception ignore)
            {
                log.error("deploy " + mbeanClassName + " mbean fail!", ignore);
            }
        }

    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-19</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param mbean
     * @param methods
     * @param methodMap
     * @throws Exception
     */
    private static void invokeMethod(Object mbean, Method methods[],
            Map methodMap) throws Exception
    {
        for (int i = 0; i < methods.length; i++)
        {
            String methodName = methods[i].getName();
            String methodValue = (String) methodMap.get(methodName);
            if (methodValue == null)
                continue;
            Class methodParaType[] = methods[i].getParameterTypes();
            if (methodParaType.length != 1)
                continue;
            
            String methodParaTypeName = methodParaType[0].getName();            
            Object paraObj;
            
            if (methodParaTypeName.indexOf("byte") != -1)
            {
                paraObj = Byte.valueOf(methodValue);                
            }
            else if (methodParaTypeName.indexOf("short") != -1)
            {
                paraObj = Short.valueOf(methodValue);                
            }
            else if (methodParaTypeName.indexOf("int") != -1)
            {
                paraObj = Integer.valueOf(methodValue);                
            }
            else if (methodParaTypeName.indexOf("long") != -1)
            {
                paraObj = Long.valueOf(methodValue);                
            }
            else if (methodParaTypeName.indexOf("float") != -1)
            {
                paraObj = Float.valueOf(methodValue);                
            }
            else if (methodParaTypeName.indexOf("double") != -1)
            {
                paraObj = Double.valueOf(methodValue);                
            }
            else if (methodParaTypeName.indexOf("boolean") != -1)
            {
                paraObj = Boolean.valueOf(methodValue);                
            }
            else
            {
                paraObj = methodValue;                
            }
                        
            Object paras[] = {paraObj};
            methods[i].invoke(mbean, paras);
        }

    }

    // RegistryInfo
    public static final class RegistryInfo implements Externalizable
    {

        /**
         * 序列化ID
         */
        private static final long serialVersionUID = 0x9838473894823L;

        /**
         * 方法集合
         */
        private Map mbeanMethodMap;

        /**
         * 类名
         */
        private List registryMBeanClassNames;

        /**
         * 注册map
         */
        private Map registryMap;

        /**
         * 构造函数
         *
         */
        public RegistryInfo()
        {
            mbeanMethodMap = null;
            registryMBeanClassNames = null;
            registryMap = null;
        }

        /**
         * 
         * @param mbeanMethodMap
         */
        RegistryInfo(Map mbeanMethodMap)
        {
            this.mbeanMethodMap = null;
            registryMBeanClassNames = null;
            registryMap = null;
            this.mbeanMethodMap = mbeanMethodMap;
        }
        
        /**
         * 
         * 
         * 功能描述：
         * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
         */
        public void writeExternal(ObjectOutput out) throws IOException
        {
            out.writeShort(ServiceDeployer.mbeans.size());
            for (Iterator it = ServiceDeployer.mbeans.iterator(); it.hasNext();)
            {
                Object nextMBean = it.next();
                HashMap methodMap = (HashMap) mbeanMethodMap.get(nextMBean);
                out.writeUTF(nextMBean.getClass().getName());
                out.write(methodMap.size());
                Iterator itr = methodMap.entrySet().iterator();
                while (itr.hasNext())
                {
                    java.util.Map.Entry nextEntry = (java.util.Map.Entry) itr
                            .next();
                    String methodName = (String) nextEntry.getKey();
                    String methodValue = (String) nextEntry.getValue();
                    out.writeUTF(methodName);
                    out.writeUTF(methodValue);
                }
            }

        }

        /**
         * 
         * 功能描述：
         * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
         */
        public void readExternal(ObjectInput in) throws IOException,
                ClassNotFoundException
        {
            int mbeanCount = in.readShort();
            registryMBeanClassNames = new ArrayList(mbeanCount);
            registryMap = new HashMap(mbeanCount);
            for (int i = 0; i < mbeanCount; i++)
            {
                String mbeanClassName = in.readUTF();
                int attrCount = in.read();
                HashMap methodMap = new HashMap(attrCount);
                for (int j = 0; j < attrCount; j++)
                {
                    String methodName = in.readUTF();
                    String methodValue = in.readUTF();
                    methodMap.put(methodName, methodValue);
                }

                registryMBeanClassNames.add(mbeanClassName);
                registryMap.put(mbeanClassName, methodMap);
            }

        }

        /**
         * 
         * <p>功能描述：</p>
         * <p>创建人：龚为川</p>
         * <p>创建日期：2008-5-20</p>
         * <p>修改记录1：</p>
         * <pre>
         *  修改人：    修改日期：    修改内容：
         * </pre>
         * <p>修改记录2：</p>
         *
         */
        private void deployAfterUnserialization()
        {
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            int i = 0;
            for (int mbeanCount = registryMBeanClassNames.size(); i < mbeanCount; i++)
            {
                String mbeanClassName = (String) registryMBeanClassNames.get(i);
                Map methodMap = (Map) registryMap.get(mbeanClassName);
                try
                {
                    Class mbeanClass = classLoader.loadClass(mbeanClassName);
                    ServiceMBean mbean = (ServiceMBean) mbeanClass
                            .newInstance();
                    int attrCount = methodMap.size();
                    if (attrCount > 0)
                        ServiceDeployer.invokeMethod(mbean, mbeanClass
                                .getMethods(), methodMap);
                    mbean.create();
                    mbean.start();
                    ServiceDeployer.mbeans.add(mbean);
                }
                catch (Exception ignore)
                {
                    ServiceDeployer.log.error("deploy " + mbeanClassName
                            + " mbean fail!", ignore);
                }
            }

        }

    }
}
