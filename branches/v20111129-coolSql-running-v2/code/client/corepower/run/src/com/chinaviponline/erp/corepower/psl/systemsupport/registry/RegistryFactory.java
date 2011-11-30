/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.registry;

/**
 * <p>文件名称：RegistryFactory.java</p>
 * <p>文件描述：服务注册工厂类</p>
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
public class RegistryFactory
{
    
    /**
     * 单例
     */
    private static ICOREPOWERRegistry registry = new DefaultRegistryImpl();

    /**
     * 构造函数
     *
     */
    public RegistryFactory()
    {
    }

    /**
     * 
     * <p>功能描述：单例</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-6-3</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static ICOREPOWERRegistry getInstance()
    {
        return registry;
    }
    
}
