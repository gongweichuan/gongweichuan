/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log.reserve;

import javax.swing.JPanel;

/**
 * <p>�ļ����ƣ�ReservePanel.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-7-12</p>
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
public interface ReservePanel
{

    public abstract ReserveCond getCond();

    public abstract void setCond(ReserveCond reservecond);

    public abstract boolean check();

    public abstract JPanel getPanel();

    public abstract String getQueryId();
    
}
