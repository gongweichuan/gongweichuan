/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * <p>�ļ����ƣ�IPSegmentInfo.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-10-1</p>
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
public interface IPSegmentInfo extends Serializable
{

    public abstract InetAddress getIpStart();

    public abstract void setIpStart(InetAddress inetaddress);

    public abstract InetAddress getIpEnd();

    public abstract void setIpEnd(InetAddress inetaddress);

    public abstract String getDesc();

    public abstract void setDesc(String s);
    
}
