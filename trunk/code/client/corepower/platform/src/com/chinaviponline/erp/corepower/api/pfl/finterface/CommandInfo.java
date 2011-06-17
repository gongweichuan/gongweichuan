/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

/**
 * <p>�ļ����ƣ�CommandInfo.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-18</p>
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
