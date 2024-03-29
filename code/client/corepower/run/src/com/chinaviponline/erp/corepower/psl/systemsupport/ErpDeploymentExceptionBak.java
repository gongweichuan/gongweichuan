/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

/**
 * <p>文件名称：ErpDeploymentException.java</p>
 * <p>文件描述：部署异常</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-11</p>
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
public class ErpDeploymentExceptionBak extends Exception
{
    
    /**
     * 构造函数
     *
     */
    public ErpDeploymentExceptionBak()
    {
    }

    /**
     * 构造函数
     * @param msg
     */
    public ErpDeploymentExceptionBak(String msg)
    {
        super(msg);
    }

    /**
     * 构造函数
     * @param t
     */
    public ErpDeploymentExceptionBak(Throwable t)
    {
        super(t);
    }

    /**
     * 构造函数
     * @param msg
     * @param t
     */
    public ErpDeploymentExceptionBak(String msg, Throwable t)
    {
        super(msg, t);
    }
}
