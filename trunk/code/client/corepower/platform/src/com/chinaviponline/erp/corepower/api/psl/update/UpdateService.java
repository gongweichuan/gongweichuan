/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.update;

/**
 * <p>�ļ����ƣ�UpdateService.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-28</p>
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
public interface UpdateService
{

    public static final String ROLE =UpdateService.class.getName();

    public abstract ErpUpdateHisInfo[] getClntUpdateHis(String s);

    public abstract ErpUpdateHisInfo[] getSvrUpdateHis(String s, String s1);
    
}
