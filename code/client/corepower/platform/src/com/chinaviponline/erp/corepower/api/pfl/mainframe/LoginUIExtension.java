package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JComponent;

/**
 * <p>�ļ����ƣ�LoginUIExtension.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-7-11</p>
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
public interface LoginUIExtension
{
    public static final String EXTENSION_ID = "corepower.mainframe.loginuiextension";

    public abstract JComponent getLoginExtensionUI();

    public abstract boolean isLoginOK();
}
