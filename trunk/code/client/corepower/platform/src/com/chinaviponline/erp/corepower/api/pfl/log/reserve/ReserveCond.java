/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log.reserve;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�ReserveCond.java</p>
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
public interface  ReserveCond extends Serializable
{

    public static final String RESERVE_1_NAME = "reserve1";

    public static final String RESERVE_2_NAME = "reserve2";

    public static final String RESERVE_3_NAME = "reserve3";

    public abstract String getSql();

    public abstract String getCondString();

    public abstract void setCondString(String s);

}
