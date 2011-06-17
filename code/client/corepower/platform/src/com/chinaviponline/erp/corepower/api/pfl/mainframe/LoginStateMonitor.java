/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

/**
 * <p>�ļ����ƣ�LoginStateMonitor.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-27</p>
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
public interface LoginStateMonitor
{
    public static final String EXTENSION_ID = "corepower.mainframe.uistate";

    public abstract void notifyLogin();

    public abstract void notifyRelogin();

    public abstract void notifyLogout();

    public abstract void notifyExit();

    public abstract void notifyScreenLocked();

    public abstract void notifyScreenUnlocked();
}
