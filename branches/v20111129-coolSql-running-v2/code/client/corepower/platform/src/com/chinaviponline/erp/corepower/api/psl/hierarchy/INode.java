/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.hierarchy;

import java.io.Serializable;

/**
 * <p>文件名称：INode.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-27</p>
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
public interface INode extends Serializable
{
    public static final int STATE_OFF = 0;
    public static final int STATE_ON = 1;
    public static final int STATE_UNKNOWN = 2;

    public abstract int getId();

    public abstract String getAlias();

    public abstract String getType();

    public abstract int getState();

    public abstract String getJndiUrl();

    public abstract String getJndiIp();

    public abstract int getJndiPort();

    public abstract int getParentId();

    public abstract String getParentJndiUrl();

    public abstract String getParentJndiIp();

    public abstract int getParentJndiPort();

    public abstract long getStateChangeTimeStamp();

    public abstract String getAliasWithId();

}
