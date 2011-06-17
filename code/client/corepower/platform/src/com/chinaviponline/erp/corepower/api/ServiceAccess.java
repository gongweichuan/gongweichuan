package com.chinaviponline.erp.corepower.api;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;

import com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService;
import com.chinaviponline.erp.corepower.api.pfl.log.LogClientService;
import com.chinaviponline.erp.corepower.api.pfl.mainframe.MainFrameService;
import com.chinaviponline.erp.corepower.api.pfl.sm.SmClientService;
import com.chinaviponline.erp.corepower.api.pfl.sm.SmEncryptService;
import com.chinaviponline.erp.corepower.api.pfl.sm.SmServerService;
import com.chinaviponline.erp.corepower.api.psl.hierarchy.FindServiceException;
import com.chinaviponline.erp.corepower.api.psl.hierarchy.HierarchyService;
import com.chinaviponline.erp.corepower.api.psl.jmswrapper.MessageService;
import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemSupportService;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 
 * <p>文件名称：ServiceAccess.java</p>
 * <p>文件描述：服务代理类,从JNDI中查找</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公   司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-2</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：
 *  修改人：
 *  修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email gongweichuan(AT)gmail.com
 */
public class ServiceAccess
{

    public static interface ServiceInterceptor
    {

        public abstract Object getServiceImplProxy(String s, Class class1);
    }

    public static class EJBItem extends ServiceItem
    {

        private String jndiName;

        private String homeClassName;

        private Class homeClass;

        private Method createMethod;

        /**
         * 
         * 功能描述：
         * @see com.chinaviponline.erp.corepower.api.ServiceAccess.ServiceItem#lookup()
         */
        public Object lookup() throws ServiceNotFoundException
        {
            String jndiURL;
            jndiURL = ServiceAccess.getSystemSupportService().getERPProperty(
                    jndiName + "-" + serviceID);

            if (jndiURL == null || jndiURL.length() <= 0)
            {
                //                break MISSING_BLOCK_LABEL_94;
                return null;
            }

            Object homeObject;
            Hashtable h = new Hashtable();
            h.put("java.naming.provider.url", jndiURL);

            try
            {
                Context jndiContext = new InitialContext(h);
                homeObject = jndiContext.lookup(jndiName);

            }
            catch (NamingException ignore)
            {
                try
                {
                    homeObject = ServiceAccess.getHierarchyService()
                            .findService(jndiName);
                }
                catch (FindServiceException e)
                {
                    throw new ServiceNotFoundException(
                            "Can not found the service with JNDI '" + jndiName
                                    + "'", e);
                }
            }

            return getServiceObject(homeObject);
            //            Throwable ignore;
            //            ignore;
            //            Object homeObject = ServiceAccess.getHierarchyService().findService(jndiName);
            //            return getServiceObject(homeObject);
            //            FindServiceException e;
            //            e;
            //            throw new ServiceNotFoundException("Can not found the service with JNDI '" + jndiName + "'", e);
        }

        public Object lookup(int nodeID) throws ServiceNotFoundException
        {

            Object homeObject;
            try
            {
                homeObject = ServiceAccess.getHierarchyService().findService(
                        nodeID, jndiName);
            }
            catch (FindServiceException e)
            {
                throw new ServiceNotFoundException(
                        "Can not found the service in node '" + nodeID
                                + "' with JNDI '" + jndiName + "'", e);
            }

            return getServiceObject(homeObject);

            //            FindServiceException e;
            //            e;
            //            throw new ServiceNotFoundException("Can not found the service in node '" + nodeID + "' with JNDI '" + jndiName + "'", e);
        }

        void writeFields(ObjectOutput out) throws IOException
        {
            out.writeUTF(serviceID);
            out.writeUTF(interfaceClassName);
            out.writeUTF(jndiName);
        }

        void readFields(ObjectInput in) throws IOException
        {
            serviceID = in.readUTF();
            interfaceClassName = in.readUTF();
            jndiName = in.readUTF();
        }

        private Object getServiceObject(Object homeObject)
                throws ServiceNotFoundException
        {
            Object realHomeObject;
            interfaceClass = resolvClass(interfaceClassName);
            realHomeObject = getRealHomeObject(homeObject);
            Object serviceRemoteObject;

            try
            {
                serviceRemoteObject = createMethod.invoke(realHomeObject, null);
            }
            catch (IllegalArgumentException e)
            {
                throw new Error("Can not access the method '" + createMethod
                        + "'");
            }
            catch (IllegalAccessException e)
            {
                throw new Error("Can not access the method '" + createMethod
                        + "'");
            }
            catch (InvocationTargetException e)
            {
                if (e.getCause() instanceof RemoteException)
                {
                    throw new ServiceNotFoundException(
                            "Remote exception occured when invoking the home's create() method, the home is '"
                                    + homeClass + "'", (RemoteException) e
                                    .getCause());
                }

                if (e.getCause() instanceof CreateException)
                {
                    throw new ServiceNotFoundException(
                            "Create exception occured when invoking the home's create() method, the home is '"
                                    + homeClass + "'", (CreateException) e
                                    .getCause());
                }
                else
                {
                    return new ServiceNotFoundException(
                            "Some exception occured when invoking the home's create() method, the home is '"
                                    + homeClass + "'", e);
                }
            }

            if (!interfaceClass
                    .isAssignableFrom(serviceRemoteObject.getClass()))
            {
                throw new Error("The remote object '" + serviceRemoteObject
                        + "' is not the instance of the anticipant interface '"
                        + interfaceClass + "'");
            }

            return serviceRemoteObject;

            //            InvocationTargetException e;
            //            e;

            //            if(e.getCause() instanceof RemoteException)
            //                throw new ServiceNotFoundException("Remote exception occured when invoking the home's create() method, the home is '" + homeClass + "'", (RemoteException)e.getCause());
            //            if(e.getCause() instanceof CreateException)
            //                throw new ServiceNotFoundException("Create exception occured when invoking the home's create() method, the home is '" + homeClass + "'", (CreateException)e.getCause());
            //            else
            //                return new ServiceNotFoundException("Some exception occured when invoking the home's create() method, the home is '" + homeClass + "'", e);
            //            e;
            //            throw new Error("Can not access the method '" + createMethod + "'");
        }

        private Object getRealHomeObject(Object homeObject)
        {
            try
            {
                createMethod = homeObject.getClass().getMethod("create",
                        new Class[0]);
            }
            catch (SecurityException e)
            {
                throw new Error("The home class " + homeClass
                        + "' has no create method", e);
            }
            catch (NoSuchMethodException e)
            {
                throw new Error("The home class " + homeClass
                        + "' has no create method", e);
            }
            return homeObject;
            //            Throwable t;
            //            t;

        }

        EJBItem(Element serviceElement)
        {
            super(serviceElement);
            jndiName = null;
            homeClassName = null;
            homeClass = null;
            createMethod = null;
            Element ejbImplElement = serviceElement.getChild("ejb-impl");
            jndiName = ejbImplElement.getChild("jndi-name").getText().trim();

            if (ejbImplElement.getChild("home-class") != null)
            {
                homeClassName = ejbImplElement.getChild("home-class").getText()
                        .trim();
            }
        }

        EJBItem()
        {
            jndiName = null;
            homeClassName = null;
            homeClass = null;
            createMethod = null;
        }
    }

    public static class LocalItem extends ServiceItem
    {

        private String localImplClassName;

        private Class localImplClass;

        private String setMethodNames[];

        private Method setMethods[];

        private String attributeValues[];

        private boolean singletonEnabled;

        private Object singletonInstance;

        public Object lookup() throws ServiceNotFoundException
        {
            if (singletonEnabled)
            {
                synchronized (this)
                {
                    if (singletonInstance == null)
                        singletonInstance = makeInstance();
                }
                return singletonInstance;
            }
            else
            {
                return makeInstance();
            }
        }

        void writeFields(ObjectOutput out) throws IOException
        {
            out.writeUTF(serviceID);
            out.writeUTF(interfaceClassName);
            out.writeUTF(localImplClassName);
            out.writeBoolean(singletonEnabled);
            int setMethodCount = setMethodNames != null ? ((int) ((byte) setMethodNames.length))
                    : 0;
            out.writeByte(setMethodCount);
            for (int i = 0; i < setMethodCount; i++)
            {
                out.writeUTF(setMethodNames[i]);
                out.writeUTF(attributeValues[i]);
            }

        }

        void readFields(ObjectInput in) throws IOException
        {
            serviceID = in.readUTF();
            interfaceClassName = in.readUTF();
            localImplClassName = in.readUTF();
            singletonEnabled = in.readBoolean();
            int setMethodCount = in.readByte();
            setMethodNames = setMethodCount != 0 ? new String[setMethodCount]
                    : null;
            attributeValues = setMethodCount != 0 ? new String[setMethodCount]
                    : null;
            for (int i = 0; i < setMethodCount; i++)
            {
                setMethodNames[i] = in.readUTF();
                attributeValues[i] = in.readUTF();
            }

        }

        private Object makeInstance()
        {
            interfaceClass = resolvClass(interfaceClassName);
            if (localImplClass == null)
            {
                resolveLocalImplClass();
            }

            Object instance = null;

            try
            {
                instance = localImplClass.newInstance();
            }
            catch (InstantiationException e)
            {
                throw new Error(
                        "Can not construct an instance with the class '"
                                + localImplClass + "'", e);
            }
            catch (IllegalAccessException e)
            {
                throw new Error("Can not access the class '" + localImplClass
                        + "'", e);
            }

            if (setMethods != null)
            {
                for (int i = 0; i < setMethods.length; i++)
                {
                    try
                    {
                        setMethods[i].invoke(instance,
                                new Object[] {attributeValues[i]});
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new Error("The method '" + setMethods[i]
                                + "' can not be invoked", e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new Error("Can not access the method '"
                                + setMethods[i] + "'", e);
                    }
                }

            }
            return instance;
            //            InstantiationException e;
            //            e;
            //            throw new Error("Can not construct an instance with the class '" + localImplClass + "'", e);
            //            e;
            //            throw new Error("Can not access the class '" + localImplClass + "'", e);
        }

        private void resolveLocalImplClass()
        {
            Class theLocalImplClass = resolvClass(localImplClassName);
            if (!interfaceClass.isAssignableFrom(theLocalImplClass))
            {
                throw new Error(
                        "The implementation class '"
                                + theLocalImplClass
                                + "' is not valid, because it does not implement the interface class '"
                                + interfaceClass + "'");
            }

            if (setMethodNames != null)
            {
                for (int i = 0; i < setMethodNames.length; i++)
                {
                    try
                    {
                        setMethods[i] = theLocalImplClass.getMethod(
                                setMethodNames[i],
                                new Class[] {java.lang.String.class});
                    }
                    catch (NoSuchMethodException e)
                    {
                        throw new Error("The class '" + theLocalImplClass
                                + "' has no public method of '"
                                + setMethodNames[i] + "'", e);
                    }
                }

            }
            localImplClass = theLocalImplClass;
        }

        LocalItem(Element serviceElement)
        {
            super(serviceElement);
            localImplClassName = null;
            localImplClass = null;
            setMethodNames = null;
            setMethods = null;
            attributeValues = null;
            singletonEnabled = false;
            singletonInstance = null;
            Element localImplElement = serviceElement.getChild("local-impl");
            localImplClassName = localImplElement.getChild("impl-class")
                    .getText().trim();

            if (localImplElement.getChild("singleton") != null)
            {
                singletonEnabled = true;
            }

            Element attributesElement = localImplElement.getChild("attributes");
            if (attributesElement != null)
            {
                List attributeElements = attributesElement.getChildren();
                setMethods = new Method[attributeElements.size()];
                setMethodNames = new String[attributeElements.size()];
                attributeValues = new String[setMethods.length];
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

        public LocalItem(String serviceID, Class interfaceClass,
                Object serviceImplObject)
        {
            super(serviceID, interfaceClass);
            localImplClassName = null;
            localImplClass = null;
            setMethodNames = null;
            setMethods = null;
            attributeValues = null;
            singletonEnabled = false;
            singletonInstance = null;
            localImplClassName = serviceImplObject.getClass().getName();
            localImplClass = serviceImplObject.getClass();
            singletonEnabled = true;
            singletonInstance = serviceImplObject;

            if (!interfaceClass.isAssignableFrom(localImplClass))
            {
                throw new Error(
                        "The implementation class '"
                                + localImplClass
                                + "' is not valid, because it does not implement the interface class '"
                                + interfaceClass + "'");
            }
            else
            {
                return;
            }
        }

        LocalItem()
        {
            localImplClassName = null;
            localImplClass = null;
            setMethodNames = null;
            setMethods = null;
            attributeValues = null;
            singletonEnabled = false;
            singletonInstance = null;
        }
    }

    public static abstract class ServiceItem
    {

        String serviceID;

        Class interfaceClass;

        String interfaceClassName;

        Class resolvClass(String className)
        {
            try
            {
                return Class.forName(className);
            }
            catch (ClassNotFoundException e)
            {
                throw new Error("Can not find the class '" + className + "'");
            }

            //            ClassNotFoundException e;
            //            e;

        }

        public Class getInterfaceClass()
        {
            if (interfaceClass == null)
            {
                interfaceClass = resolvClass(interfaceClassName);
            }

            return interfaceClass;
        }

        public abstract Object lookup() throws ServiceNotFoundException;

        abstract void writeFields(ObjectOutput objectoutput) throws IOException;

        abstract void readFields(ObjectInput objectinput) throws IOException;

        ServiceItem(Element serviceElement)
        {
            serviceID = null;
            interfaceClass = null;
            interfaceClassName = null;
            serviceID = serviceElement.getAttribute("id").getValue().trim();
            interfaceClassName = serviceElement.getChild("interface-class")
                    .getText().trim();
        }

        ServiceItem(String serviceID, Class theInterfaceClass)
        {
            this.serviceID = null;
            interfaceClass = null;
            interfaceClassName = null;
            this.serviceID = serviceID;
            interfaceClass = theInterfaceClass;
            interfaceClassName = interfaceClass.getName();
        }

        ServiceItem()
        {
            serviceID = null;
            interfaceClass = null;
            interfaceClassName = null;
        }
    }

    static Map serviceMap = new HashMap();

    static ServiceInterceptor serviceInterceptor = null;

    //    static Class class$java$lang$String; /* synthetic field */

    public ServiceAccess()
    {
    }

    public static Object lookup(String serviceID)
            throws ServiceNotFoundException
    {
        ServiceItem item = (ServiceItem) serviceMap.get(serviceID);

        if (item == null)
        {
            throw new ServiceNotFoundException("Can not find the service '"
                    + serviceID + "'");
        }

        if (serviceInterceptor != null)
        {
            return serviceInterceptor.getServiceImplProxy(serviceID, item
                    .getInterfaceClass());
        }
        else
        {
            return item.lookup();
        }
    }

    public static Object lookup(int nodeID, String serviceID)
            throws ServiceNotFoundException
    {
        Object item = serviceMap.get(serviceID);

        if (item == null)
        {
            throw new ServiceNotFoundException("Can not find the service '"
                    + serviceID + "'");
        }

        if (!(item instanceof EJBItem))
        {
            throw new ServiceNotFoundException(
                    "This lookup method is used to get an EJB remote interface by hierarchy service");
        }
        else
        {
            return ((EJBItem) item).lookup(nodeID);
        }
    }

    public static HierarchyService getHierarchyService()
    {
        return (HierarchyService) getServiceInterface(HierarchyService.ROLE);
    }

    public static SystemSupportService getSystemSupportService()
    {
        return (SystemSupportService) getServiceInterface("system-support-service");
    }

    //    public static SystemMonitorService getSystemMonitorService()
    //    {
    //        return (SystemMonitorService) getServiceInterface(SystemMonitorService.ROLE);
    //    }

    public static MessageService getMessageService()
    {
        return (MessageService) getServiceInterface(MessageService.ROLE);
    }

    //    public static TimerService getTimerService()
    //    {
    //        return (TimerService) getServiceInterface(TimerService.ROLE);
    //    }

    //    public static VfsService getVfsService()
    //    {
    //        return (VfsService) getServiceInterface(VfsService.ROLE);
    //    }

    //    public static FtpClientService getFtpClientService()
    //    {
    //        return (FtpClientService) getServiceInterface(FtpClientService.ROLE);
    //    }

    //    public static SftpClientService getSftpClientService()
    //    {
    //        return (SftpClientService) getServiceInterface(SftpClientService.ROLE);
    //    }

    //    public static FileTransferClientService getFileTransferClientService()
    //    {
    //        return (FileTransferClientService) getServiceInterface(FileTransferClientService.ROLE);
    //    }

    public static MainFrameService getMainFrameService()
    {
        return (MainFrameService) getServiceInterface("mainframe-service");
    }

    /**
     * @deprecated Method getPrintService is deprecated
     */

    //    public static PrintComponentService getPrintService()
    //    {
    //        return (PrintComponentService) getServiceInterface(PrintComponentService.ROLE);
    //    }
    //    public static PrintService getPrinter()
    //    {
    //        return (PrintService) getServiceInterface(PrintService.ROLE);
    //    }
    //    public static ExportService getExporter()
    //    {
    //        return (ExportService) getServiceInterface(ExportService.ROLE);
    //    }
    //    public static ImportService getImporter()
    //    {
    //        return (ImportService) getServiceInterface(ImportService.ROLE);
    //    }
    public static FClientService getFClientService()
    {
        return (FClientService) getServiceInterface(FClientService.ROLE);
    }

    //    public static FServerService getFServerService()
    //    {
    //        return (FServerService) getServiceInterface(FServerService.ROLE);
    //    }

    public static SmClientService getSmClientService()
    {
        //        return (SmClientService) getServiceInterface("com.chinaviponline.erp.corepower.api.pfl.sm.SmClientService");

        return (SmClientService) getServiceInterface(SmClientService.ROLE);
    }

    public static SmEncryptService getSmEncryptService()
    {
        //        return (SmEncryptService) getServiceInterface("com.chinaviponline.erp.corepower.api.pfl.sm.SmEncryptService");

        return (SmEncryptService) getServiceInterface(SmEncryptService.ROLE);
    }

    public static SmServerService getSmServerService()
    {
        return (SmServerService) getServiceInterface(SmServerService.ROLE);
    }

    public static LogClientService getLogClientService()
    {
        return (LogClientService) getServiceInterface(LogClientService.ROLE);
    }

    //    public static LogServerService getLogServerService()
    //    {
    //        return (LogServerService) getServiceInterface(LogServerService.ROLE);
    //    }

    //    public static CmisService getCmisService()
    //    {
    //        return (CmisService) getServiceInterface(CmisService.ROLE);
    //    }

    //    public static CmisExtService getCmisExtService()
    //    {
    //        return (CmisExtService) getServiceInterface(CmisExtService.ROLE);
    //    }

    //    public static CmisClientService getCmisClientService()
    //    {
    //        return (CmisClientService) getServiceInterface(CmisClientService.ROLE);
    //    }

    //    public static MoGuiOperationService getMoGuiOperationService()
    //    {
    //        return (MoGuiOperationService) getServiceInterface(MoGuiOperationService.ROLE);
    //    }

    //    public static MifSnmpService getMifSnmpService()
    //    {
    //        return (MifSnmpService) getServiceInterface(MifSnmpService.ROLE);
    //    }

    //    public static MifSnmpClientService getMifSnmpClientService()
    //    {
    //        return (MifSnmpClientService) getServiceInterface(MifSnmpClientService.ROLE);
    //    }

    //    public static TopoClientService getTopoClientService()
    //    {
    //        return (TopoClientService) getServiceInterface(TopoClientService.ROLE);
    //    }

    //    public static TopoServerService getTopoServerService()
    //    {
    //        return (TopoServerService) getServiceInterface(TopoServerService.ROLE);
    //    }

    //    public static AlarmMessageServerService getAlarmMessageServerService()
    //    {
    //        return AlarmSendCenter.getAlarmMessageServerService();
    //    }

    //    public static AlarmUIClientService getAlarmUIClientService()
    //    {
    //        return (AlarmUIClientService) getServiceInterface(AlarmUIClientService.ROLE);
    //    }

    //    public static CrossJvmFileLockService getCrossJvmFileLockService()
    //    {
    //        return (CrossJvmFileLockService) getServiceInterface(CrossJvmFileLockService.ROLE);
    //    }

    //    public static ClusterTimerService getClusterTimerService()
    //    {
    //        return (ClusterTimerService) getServiceInterface(ClusterTimerService.ROLE);
    //    }

    //    public static JmsFwdService getJmsFwdService()
    //    {
    //        return (JmsFwdService) getServiceInterface(JmsFwdService.ROLE);
    //    }

    /**
     * 获取Spring的上下文
     */
    public static ISpringBeanLoader getSpringService()
    {
        return (ISpringBeanLoader) getServiceInterface(ISpringBeanLoader.ROLE);
    }

    public static void injectServiceBinding(String serviceID,
            Class interfaceClass, Object serviceImplObject)
    {
        serviceMap.put(serviceID, new LocalItem(serviceID, interfaceClass,
                serviceImplObject));
    }

    private static Object getServiceInterface(String serviceID)
    {
        try
        {
            return lookup(serviceID);
        }
        catch (ServiceNotFoundException e)
        {
            throw new Error("The service '" + serviceID + "' not found", e);
        }
    }

}
