/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * <p>�ļ����ƣ�MenuToolBarExtension.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-25</p>
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
public interface MenuToolBarExtension
{
    public static final String EXTENSION_ID = "corepower.mainframe.MenuToolBarExtension";

    public abstract JMenu[] getAppendMenus();

    public abstract JComponent[] getAppendTools();
}
