/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

import java.awt.Component;
import java.util.Calendar;

/**
 * <p>�ļ����ƣ�FClientService.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-18</p>
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
public interface FClientService
{


    public static final String ROLE = FClientService.class.getName();
    public static final short INVALID_SESSIONID = -1;
    public static final String LINK_STATE_TOPIC = "F_LinkTopic";
    public static final int LINK_NORMAL = 0;
    public static final int LINK_BROKEN = 1;
    public static final int LINK_DEAD = 2;
    public static final int INVALID_USERID = -1;
    public static final short ASYN_LISTENER_UNREGISTER = 3057;
    public static final short REQUEST_INTERRUPT_BYUSER = 3032;
    public static final short SERVICE_NOT_FOUND = 3007;
    public static final short SESSION_INEXISTENCE = 3011;

    public abstract int login(String s, String s1, String s2)
        throws FIException;

    public abstract void logout()
        throws FIException;

    public abstract short getSessionID()
        throws FIException;

    public abstract String getUserName();

    public abstract int getUserID();

    public abstract String getSvrIP();

    public abstract String getClientIP();

    public abstract int getSessionState();

    public abstract FIMessage requestx(Component component, FIMessage fimessage)
        throws FIException;

    public abstract FIMessage requestx(FIMessage fimessage)
        throws FIException;

    public abstract FIMessage[] requestxBatch(Component component, FIMessage afimessage[])
        throws FIException;

    public abstract Object[] requestBatchTryBest(Component component, FIMessage afimessage[])
        throws FIException;

    public abstract void requestxAsyn(FIMessage fimessage, ResponseListener responselistener);

    public abstract void requestxAsyn(Component component, FIMessage fimessage, ResponseListener responselistener);

    public abstract void unRegisterAsynListener(FIMessage fimessage);

    public abstract void subscribeClientMsg(String s, ClientMessageListener clientmessagelistener);

    public abstract void unSubscribeClientMsg(String s, ClientMessageListener clientmessagelistener);

    public abstract void publishClientMsg(String s, FIMessage fimessage);

    public abstract String getOperationByCmd(int i);

    public abstract String[] getOperationDesc(String s);

    public abstract CommandInfo getCommandInfo(int i);

    public abstract CommandInfo[] getAllcommandInfo();

    public abstract FIMessage requestx(Component component, FIMessage fimessage, boolean flag)
        throws FIException;

    public abstract FIMessage[] requestxBatch(Component component, FIMessage afimessage[], boolean flag)
        throws FIException;

    public abstract Object[] requestBatchTryBest(Component component, FIMessage afimessage[], boolean flag)
        throws FIException;

    public abstract int loginUseUSBKey(String s, String s1, String s2)
        throws FIException;

    public abstract int loginUseRadius(String s, String s1, String s2)
        throws FIException;

    public abstract Calendar getServerTime(int i)
        throws FIException;

    public abstract boolean isHiddenCommand(int i);

    public abstract boolean isHiddenOperation(String s);

    
}
