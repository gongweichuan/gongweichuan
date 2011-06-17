/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log.reserve;

import java.io.Serializable;

/**
 * <p>文件名称：ReserveCond.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-7-12</p>
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
public interface  ReserveCond extends Serializable
{

    public static final String RESERVE_1_NAME = "reserve1";

    public static final String RESERVE_2_NAME = "reserve2";

    public static final String RESERVE_3_NAME = "reserve3";

    public abstract String getSql();

    public abstract String getCondString();

    public abstract void setCondString(String s);

}
