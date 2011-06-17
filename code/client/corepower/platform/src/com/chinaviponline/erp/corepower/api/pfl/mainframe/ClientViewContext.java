/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.ImageIcon;
/**
 * <p>�ļ����ƣ�ClientViewContext.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-2</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�
 *  �汾�ţ�
 *  �޸��ˣ�
 *  �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
 * @email  gongweichuan(AT)gmail.com
 */
public interface ClientViewContext extends RuntimeContext
{

    public abstract String getClientViewID();

    public abstract boolean isDefaultClientView();

    public abstract String getTitle();

    public abstract ImageIcon getPresentationIcon();

    public abstract boolean isEmbededStyle();

    public abstract boolean isAhead();
}
