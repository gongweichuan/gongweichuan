/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JPopupMenu;

/**
 * <p>�ļ����ƣ�AppTreeResponsable.java</p>
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
public interface AppTreeResponsable
{

    public abstract JPopupMenu getPopupMenu(NodeInfo nodeinfo);

    public abstract void expand(NodeInfo nodeinfo);

    public abstract void collapse(NodeInfo nodeinfo);

    public abstract void select(NodeInfo nodeinfo);
    
}
