/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.util.Date;

/**
 * <p>�ļ����ƣ�UserDetailInfo.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-17</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�    �汾�ţ�    �޸��ˣ�    �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
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
