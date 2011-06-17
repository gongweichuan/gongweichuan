/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.chinaviponline.erp.corepower.api.pfl.finterface.FIException;

/**
 * <p>�ļ����ƣ�OptionPanelExtension.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-23</p>
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
public interface OptionPanelExtension
{


    public static final String EXTENSION_ID = "corepower.mainframe.OptionPanelExtension";
    public static final boolean inStyleDialog = true;

    public abstract void init(JTabbedPane jtabbedpane);

    public abstract String getTitle();

    public abstract JPanel getPanel();

    public abstract boolean check()
        throws FIException;

    public abstract void save()
        throws FIException;

    public abstract boolean isDataChanged();

    
}
