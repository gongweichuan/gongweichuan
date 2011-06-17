package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�RoleInfo.java</p>
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
public interface RoleInfo extends Serializable
{

    public abstract int getID();

    public abstract String getName();

    public abstract void setName(String s);

    public abstract String getDescription();

    public abstract void setDescription(String s);

    public abstract boolean getLocked();

    public abstract void setLocked(boolean flag);

    
}
