/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JComponent;

/**
 * <p>�ļ����ƣ�AppFrameService.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-30</p>
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
public interface AppFrameService
{
    public static final String ROLE = "appframe-service";

    public abstract void addNode(String s, String s1, NodeInfo nodeinfo);

    public abstract void removeNode(String s, String s1);

    public abstract void modifyNode(String s, NodeInfo nodeinfo);

    public abstract void selectNode(String s, String s1);

    public abstract void setRightComponent(String s, JComponent jcomponent);

}