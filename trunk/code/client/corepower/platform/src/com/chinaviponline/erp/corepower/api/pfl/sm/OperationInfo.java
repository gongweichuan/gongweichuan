/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

/**
 * <p>�ļ����ƣ�OperationInfo.java</p>
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
public interface OperationInfo
{
    public static final int DEFAULT_LEVEL = 4;

    public abstract String getOperationId();

    public abstract String getOperationName();

    public abstract int getLevel();
}
