/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.registry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * <p>文件名称：ICOREPOWERRegistry.java</p>
 * <p>文件描述：注册服务的接口类</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-3</p>
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
public interface ICOREPOWERRegistry
{
    /**
     * 序列化后存储的文件名
     */
    public static final String NAME = "UEPRegistry.bin";

    /**
     * 
     * <p>功能描述：从文件中读取已经注册的服务信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-6-3</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param s
     * @return
     */
    public abstract ObjectInputStream getFromRegistry(String s);

    /**
     * 
     * <p>功能描述：添加服务信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-6-3</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param s
     * @param abyte0
     * @throws IOException
     */
    public abstract void setIntoRegistry(String s, byte abyte0[])
        throws IOException;

    /**
     * 
     * <p>功能描述：加载服务注册信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-6-3</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public abstract void loadRegistry()
        throws IOException, ClassNotFoundException;

    /**
     * 
     * <p>功能描述：保存已经注册的服务信息</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-6-3</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public abstract void saveRegistry()
        throws FileNotFoundException, IOException;
}
