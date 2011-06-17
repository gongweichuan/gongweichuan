package com.chinaviponline.erp.corepower.mock;


import java.awt.Component;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.chinaviponline.erp.corepower.api.pfl.finterface.ClientMessageListener;
import com.chinaviponline.erp.corepower.api.pfl.finterface.CommandInfo;
import com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService;
import com.chinaviponline.erp.corepower.api.pfl.finterface.FIException;
import com.chinaviponline.erp.corepower.api.pfl.finterface.FIMessage;
import com.chinaviponline.erp.corepower.api.pfl.finterface.ResponseListener;
/**
 * 
 * <p>�ļ����ƣ�TIcanFClientService.java</p>
 * <p>�ļ�������Ϊ��Ԫ���Զ�д��ģ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��   ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-7-20</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�    �汾�ţ�    �޸��ˣ�    �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
 * @email gongweichuan(AT)gmail.com
 */
public class TMockFClientService implements FClientService {
    
    /**
     * ���������ڵ���requestx����ǰԤ���FIMesage���͵ķ��ؽ��
     * ע�⣺�����������returnFIMsg���Ա�֤�´ε���ʱ������ȷ�Ľ��
     * add by liwei
     */
    private List returnFIMsg = new LinkedList();
    
    /**
     * ϵͳ��¼ 
     * @param userName - �û���
     * @param passwd - ���루δ���ܣ�
     * @param srvIP - ������IP�Ͷ˿ڣ�IP:Prot�ĸ�ʽ��port����ʡ��
     */
    public int login(String userName, String passwd, String srvIP) throws FIException {
         return 0;
    }
    /** 
     * ϵͳע��
     */
    public void logout() throws FIException {
               
    }
    /**
     * ���ص�ǰ�ĻỰID 
     */
    public short getSessionID() throws FIException {
        //TODO Auto-generated method stub
        return 0;
    }
    /**
     * ��ȡ��ǰ�û������ƣ�δ��¼ʱ����null 
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Ϊ��Ԫ���Զ���ӵĺ������û��ڵ���getUserNameǰ������ô˺������Ա�getUserName�ܹ������Ҫ��ֵ��
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * �Ự��ǰ�û���ID��δ��¼ʱ����INVALID_USERID 
     */
    public int getUserID() {
        //TODO Auto-generated method stub
        return 0;
    }
    /**
     * ���ص�ǰ��¼��������IP��δ��¼ʱ����null 
     */
    public String getSvrIP() {
        
        // ����Ϊnull
        return "java/env/127.0.0.1";
    }
    /**
     * ���ص�ǰ��·��״̬���ֱ��ǣ� LINK_NORMAL:��·������LINK_BROKEN:��·˲�ϡ�LINK_DEAD��·�ر�
     */
    public int getSessionState() {
        //TODO Auto-generated method stub
        return 0;
    }
    /**
     * ���еȴ��Ի���ķ�ʽ����ͬ������
     * 
     * @author liwei
     */
    public FIMessage requestx(Component arg0, FIMessage arg1) throws FIException {
        return requestx(arg1);
    }
    /**
     * �Լ��޵ȴ��Ի������޳�ʱ�ķ�ʽ����ͬ������
     * 
     * @author liwei
     */
    public FIMessage requestx(FIMessage fm) throws FIException {
//        try {
//            Thread.sleep(3000);
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
            
        if (returnFIMsg.size() == 0) {            
            throw new FIException(-100, "TIcanFClientService: ������Ϊ"+fm.getCommandCode()+"��ReturnFIMsgΪ��", null);
        }
        
        SendFIReturnObject ro = null;
        
        if(!returnFIMsg.isEmpty()) {
            ro = (SendFIReturnObject) returnFIMsg.get(0);
            returnFIMsg.remove(0);
        }
                
        if (ro == null) {
            return null;
        }
        
        if (ro.raiseException) {
            throw new FIException(-100, "Communication��׮����", null);
        }
        
        return ro.fiMsg;      
    }
    /**
     * ���еȴ��Ի���ķ�ʽ���������� 
     */
    public FIMessage[] requestxBatch(Component arg0, FIMessage[] arg1) throws FIException {
        
        return null;
    }
    /**
     * ���еȴ��Ի���ķ�ʽ����������,�ύ��ʽΪ����ύ
     * 
     * @author liwei  
     */
    public Object[] requestBatchTryBest(Component arg0, FIMessage[] fmsgs) throws FIException {
        
        int count = 0;
        if (fmsgs != null) {
            count = fmsgs.length;          
        }
        
        if (returnFIMsg.size() < count) { 
            throw new FIException(-100, "TIcanFClientService: ������������requestBatchTryBest��returnCmdMsg�ռ䲻�㣬��Ҫ�Ŀռ�Ϊ" + count + ", ʵ�ʿռ�Ϊ" + returnFIMsg.size(), null);            
        }
        
        boolean raiseException = false;
        Object[] result = new Object[count];
        for(int i = 0; i < count; i++) {
            SendFIReturnObject temp = (SendFIReturnObject) returnFIMsg.get(0);
            
            raiseException = temp.raiseException;
            if(raiseException) {
                result[i] = new FIException(-100, "Communication��׮����", null);
            }
            else {
                result[i] = temp.fiMsg;
            }
            returnFIMsg.remove(0);
        }
        
        return result;
     }
    /**
     * �����첽���� 
     */
    public void requestxAsyn(FIMessage arg0, ResponseListener arg1) {
               
    }
    /**
     * ��ͻ�����Ϣ�����Ķ�����Ϣ
     */
    public void requestxAsyn(Component arg0, FIMessage arg1, ResponseListener arg2) {
            
    }
    /**
     * ��ͻ�����Ϣ����ȡ������
     */
    public void unRegisterAsynListener(FIMessage arg0) {
              
    }
    public void subscribeClientMsg(String arg0, ClientMessageListener arg1) {
              
    }
    public void unSubscribeClientMsg(String arg0, ClientMessageListener arg1) {
         
    }
    public void publishClientMsg(String arg0, FIMessage arg1) {
    
    }
    public String getOperationByCmd(int arg0) {
         return null;
    }
    public String[] getOperationDesc(String arg0) {
       return null;
    }
    public CommandInfo getCommandInfo(int arg0) {
       return null;
    }
    
    /**
     * Ϊ��Ԫ���Զ���ӵı���
     */
    private String userName;
    /* (non-Javadoc)
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#requestx(java.awt.Component, com.chinaviponline.erp.corepower.api.pfl.finterface.FIMessage, boolean)
     */
    public FIMessage requestx(Component arg0, FIMessage arg1, boolean arg2) throws FIException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#requestxBatch(java.awt.Component, com.chinaviponline.erp.corepower.api.pfl.finterface.FIMessage[], boolean)
     */
    public FIMessage[] requestxBatch(Component arg0, FIMessage[] arg1, boolean arg2) throws FIException {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#requestBatchTryBest(java.awt.Component, com.chinaviponline.erp.corepower.api.pfl.finterface.FIMessage[], boolean)
     */
    public Object[] requestBatchTryBest(Component arg0, FIMessage[] arg1, boolean arg2) throws FIException {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 
    ******************************************************* 
    * ������: addRequestxReturnHelper<br>
    * ��  ��: ����Ԥ��requestx�����ķ��ؽ��<br>
    * ��  ��: @param fiMsg<br>
    * ����ֵ: <br>
    * ������: liwei<br>
    * ��  ��: 2006-3-7<br>
    * �޸ļ�¼: <br>
    * �޸���    �޸�����     �޸�����<br>
    *******************************************************
     */
    public void addRequestxReturnHelper(FIMessage fiMsg, boolean isRaiseExc) {
        returnFIMsg.add(new SendFIReturnObject(fiMsg, isRaiseExc));
    }
    
    /**
     * 
    ******************************************************* 
    * ������: clearRequestxReturnHelper<br>
    * ��  ��: �������Ԥ������ÿ������addRequestxReturnHelper������Ӧ�ں��ʵĵط����ø÷�����շ�����Ϣ����<br>
    * ��  ��: @param fiMsg<br>
    * ����ֵ: <br>
    * ������: liwei<br>
    * ��  ��: 2006-3-7<br>
    * �޸ļ�¼: <br>
    * �޸���    �޸�����     �޸�����<br>
    *******************************************************
     */
    public void clearRequestxReturnHelper() {
        returnFIMsg.clear();
    }
    
    
    
    /* (non-Javadoc)
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#getServerTime(int)
     */
    public Calendar getServerTime(int arg0) throws FIException {
        //TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#loginUseUSBKey(java.lang.String, java.lang.String, java.lang.String)
     */
    public int loginUseUSBKey(String arg0, String arg1, String arg2) throws FIException {
        //TODO Auto-generated method stub
        return 0;
    }
    private class SendFIReturnObject {
        
        FIMessage fiMsg=null;
        
        boolean raiseException=false;
        
        SendFIReturnObject (FIMessage cmdMsg, boolean raiseException) {
            this.fiMsg = cmdMsg;
            this.raiseException = raiseException;
        }
        
    }
    /**
     * ����������
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#getClientIP()
     */
    public String getClientIP()
    {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * ����������
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#isHiddenCommand(int)
     */
    public boolean isHiddenCommand(int arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     * ����������
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#isHiddenOperation(java.lang.String)
     */
    public boolean isHiddenOperation(String arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     *����������
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#loginUseRadius(java.lang.String, java.lang.String, java.lang.String)
     */
    public int loginUseRadius(String arg0, String arg1, String arg2) throws FIException
    {
        // TODO Auto-generated method stub
        return 0;
    }
    /**
     * 
     * ����������
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#getAllcommandInfo()
     */
    public CommandInfo[] getAllcommandInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
