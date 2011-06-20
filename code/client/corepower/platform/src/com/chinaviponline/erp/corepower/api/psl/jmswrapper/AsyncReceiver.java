/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.jmswrapper;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * <p>文件名称：AsyncReceiver.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-26</p>
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
public interface AsyncReceiver extends CapabilitySet, RegistryInfo
{
    public abstract void close() throws JMSException;

    public abstract Message[] dump() throws StillRunningException,
            CacheEmptyException;
}
