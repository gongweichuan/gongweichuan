/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�UserInfo.java</p>
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
public interface  UserInfo extends Serializable
{
    public abstract int getID();

    public abstract String getName();

    public abstract String getFullName();

    public abstract String getDescription();

    public abstract String getEMailAddress();

    public abstract String getPhoneNumber();

    public abstract int getDepartmentID();
}
