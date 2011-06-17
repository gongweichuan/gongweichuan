package com.chinaviponline.erp.corepower.api.psl.systemsupport;

import java.util.EventObject;

/**
 * <p>�ļ����ƣ�SystemStateEvent.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-12</p>
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
public class SystemStateEvent extends EventObject
{
    private int actionCommand;

    public static final int START_ACTION = 1;

    public static final int END_ACTION = 2;

    public SystemStateEvent(Object source, int action)
    {
        super(source);
        actionCommand = action;
    }

    public int getActionCommand()
    {
        return actionCommand;
    }
}
