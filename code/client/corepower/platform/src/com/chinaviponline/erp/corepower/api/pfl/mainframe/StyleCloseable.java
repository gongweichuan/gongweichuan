/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JFrame;

/**
 * <p>�ļ����ƣ�StyleCloseable.java</p>
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
public interface StyleCloseable
{


    public static final String EXTENSION_ID = "corepower.mainframe.sytleclose";

    public abstract boolean isCloseEnabled(String s, JFrame jframe);

    
}
