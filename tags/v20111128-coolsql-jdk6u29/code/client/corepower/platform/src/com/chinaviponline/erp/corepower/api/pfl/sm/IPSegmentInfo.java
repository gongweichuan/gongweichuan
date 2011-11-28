/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * <p>文件名称：IPSegmentInfo.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-10-1</p>
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
public interface IPSegmentInfo extends Serializable
{

    public abstract InetAddress getIpStart();

    public abstract void setIpStart(InetAddress inetaddress);

    public abstract InetAddress getIpEnd();

    public abstract void setIpEnd(InetAddress inetaddress);

    public abstract String getDesc();

    public abstract void setDesc(String s);
    
}
