/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

/**
 * <p>�ļ����ƣ�SecurityRule.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-10-1</p>
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
public interface SecurityRule
{


    public abstract int getPasswordMinimumLength();

    public abstract int getPasswordMaximumLength();

    public abstract boolean isWeakPasswordCheckEnabled();

    public abstract int getLockThreshold();

    public abstract int getAutoUnLockTime();

    public abstract int getPasswordExpireAdvancedDays();

    public abstract int getPasswordRepeatCheckDays();

    public abstract int getPasswordRepeatCheckTimes();
    
}
