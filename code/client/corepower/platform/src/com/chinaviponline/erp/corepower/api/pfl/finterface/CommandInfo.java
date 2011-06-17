/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

/**
 * <p>文件名称：CommandInfo.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-18</p>
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
public interface CommandInfo
{
    public static final int COMPULSIVE_RECORD = 1;
    public static final int COMPULSIVE_NOT_RECORD = 2;
    public static final int OPTIONAL_RECORD = 3;
    public static final int OPTIONAL_NOT_RECORD = 4;

    public abstract int getCommondCode();

    public abstract String getDesc();

    public abstract String getModule();

    public abstract String getModuleKey();

    public abstract String getOperation();

    public abstract String getVersion();

    public abstract int getLogType();

    public abstract boolean isDisplayMML();

    public abstract String getMMLType();

    public abstract String getPseudoCode();

    public abstract boolean isLogDisplayable();

}
