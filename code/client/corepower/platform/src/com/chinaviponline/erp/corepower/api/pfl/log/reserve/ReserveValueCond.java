/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log.reserve;

/**
 * <p>�ļ����ƣ�ReserveValueCond.java</p>
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
public interface ReserveValueCond extends ReserveCond
{
    
    public abstract String getFirstValue();

    public abstract String getSecondValue();

    public abstract String getThirdValue();

    public abstract void setQueryId(String s);

    public abstract void setFirstValue(String s);

    public abstract void setSecondValue(String s);

    public abstract void setThirdValue(String s);


}
