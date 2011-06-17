/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.util.Date;

/**
 * <p>文件名称：UserDetailInfo.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-17</p>
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
public interface UserDetailInfo extends UserInfo
{
    public static interface LoginLimitInfo
    {

        public abstract String getLoginType();

        public abstract int getMaxConcurrentLogin();
    }


    public abstract void setName(String s);

    public abstract void setFullName(String s);

    public abstract void setDescription(String s);

    public abstract void setPhoneNumber(String s);

    public abstract void setEMailAddress(String s);

    public abstract void setDepartmentID(int i);

    public abstract String getPassword();

    public abstract void setPassword(String s);

    public abstract int getUserValidDays();

    public abstract void setUserValidDays(int i);

    public abstract int getPasswordValidDays();

    public abstract void setPasswordValidDays(int i);

    public abstract boolean isDisable();

    public abstract void setDisable(boolean flag);

    public abstract Date getCreateTime();

    public abstract Date getPasswordEnableTime();

    /**
     * @deprecated Method getMaxConcurrentLogin is deprecated
     */

    public abstract int getMaxConcurrentLogin();

    public abstract int getMaxConcurrentLogin(String s);

    public abstract LoginLimitInfo[] getConcurrentLoginLimit();

    /**
     * @deprecated Method setMaxConurrentLogin is deprecated
     */
    public abstract void setMaxConurrentLogin(int i);

    public abstract void setMaxConurrentLogin(String s, int i);

    public abstract String getWorkTime();

    public abstract void setWorkTime(String s);

    public abstract int[] getRoleArray();

    public abstract void setRoleArray(int ai[]);

    public abstract int[] getRoleSetArray();

    public abstract void setRoleSetArray(int ai[]);

    public abstract int getCreatorID();

    public abstract void setCreatorID(int i);

    public abstract boolean isPasswordModifiable();

    public abstract int getAutoLogoutTime();

}
