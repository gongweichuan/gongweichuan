/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.systemsupport;

/**
 * <p>文件名称：SystemListenerException.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-10</p>
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
public class SystemListenerException extends Exception
{
    /**
     * add listener failed
     */
    public static final String ADD_FAILURE = "add listener failed";
    
    /**
     * remove listener failed
     */
    public static final String REMOVE_FAILURE = "remove listener failed";

    /**
     * 构造函数
     *
     */
    public SystemListenerException()
    {
        
    }

    /**
     * 构造函数
     * @param msg
     */
    public SystemListenerException(String msg)
    {
        super(msg);
    }

    /**
     * 构造函数
     * @param t
     */
    public SystemListenerException(Throwable t)
    {
        super(t);
    }

    /**
     * 构造函数
     * @param msg
     * @param t
     */
    public SystemListenerException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
