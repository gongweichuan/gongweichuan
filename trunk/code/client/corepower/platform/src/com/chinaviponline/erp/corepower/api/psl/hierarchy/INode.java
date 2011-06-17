/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.hierarchy;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�INode.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-27</p>
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
public interface INode extends Serializable
{
    public static final int STATE_OFF = 0;
    public static final int STATE_ON = 1;
    public static final int STATE_UNKNOWN = 2;

    public abstract int getId();

    public abstract String getAlias();

    public abstract String getType();

    public abstract int getState();

    public abstract String getJndiUrl();

    public abstract String getJndiIp();

    public abstract int getJndiPort();

    public abstract int getParentId();

    public abstract String getParentJndiUrl();

    public abstract String getParentJndiIp();

    public abstract int getParentJndiPort();

    public abstract long getStateChangeTimeStamp();

    public abstract String getAliasWithId();

}
