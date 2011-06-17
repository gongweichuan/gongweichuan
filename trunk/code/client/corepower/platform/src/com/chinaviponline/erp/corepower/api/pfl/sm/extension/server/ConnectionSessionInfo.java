package com.chinaviponline.erp.corepower.api.pfl.sm.extension.server;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.HashMap;

/**
 * <p>�ļ����ƣ�ConnectionSessionInfo.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-9-24</p>
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
public interface ConnectionSessionInfo extends Serializable, Cloneable
{

    public abstract String getUserName();

    public abstract int getUserID();

    public abstract short getSessionID();

    public abstract String getConnectionType();

    public abstract InetAddress getClientIP();

    public abstract Calendar getLoginTime();

    public abstract HashMap getOtherInfo();
    
}
