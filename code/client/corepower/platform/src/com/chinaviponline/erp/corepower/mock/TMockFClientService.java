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
 * <p>文件名称：TIcanFClientService.java</p>
 * <p>文件描述：为单元测试而写的模拟服务类</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公   司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-7-20</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email gongweichuan(AT)gmail.com
 */
public class TMockFClientService implements FClientService {
    
    /**
     * 用来保存在调用requestx方法前预设的FIMesage类型的返回结果
     * 注意：用完后务必清空returnFIMsg，以保证下次调用时返回正确的结果
     * add by liwei
     */
    private List returnFIMsg = new LinkedList();
    
    /**
     * 系统登录 
     * @param userName - 用户名
     * @param passwd - 密码（未加密）
     * @param srvIP - 服务器IP和端口，IP:Prot的格式，port可以省略
     */
    public int login(String userName, String passwd, String srvIP) throws FIException {
         return 0;
    }
    /** 
     * 系统注销
     */
    public void logout() throws FIException {
               
    }
    /**
     * 返回当前的会话ID 
     */
    public short getSessionID() throws FIException {
        //TODO Auto-generated method stub
        return 0;
    }
    /**
     * 获取当前用户的名称，未登录时返回null 
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * 为单元测试而添加的函数，用户在调用getUserName前必须调用此函数，以便getUserName能够获得需要的值。
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * 会话当前用户的ID，未登录时返回INVALID_USERID 
     */
    public int getUserID() {
        //TODO Auto-generated method stub
        return 0;
    }
    /**
     * 返回当前登录服务器的IP，未登录时返回null 
     */
    public String getSvrIP() {
        
        // 不能为null
        return "java/env/127.0.0.1";
    }
    /**
     * 返回当前链路的状态，分别是： LINK_NORMAL:链路正常、LINK_BROKEN:链路瞬断、LINK_DEAD链路关闭
     */
    public int getSessionState() {
        //TODO Auto-generated method stub
        return 0;
    }
    /**
     * 用有等待对话框的方式发送同步命令
     * 
     * @author liwei
     */
    public FIMessage requestx(Component arg0, FIMessage arg1) throws FIException {
        return requestx(arg1);
    }
    /**
     * 以既无等待对话框、又无超时的方式发送同步命令
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
            throw new FIException(-100, "TIcanFClientService: 命令码为"+fm.getCommandCode()+"的ReturnFIMsg为空", null);
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
            throw new FIException(-100, "Communication打桩测试", null);
        }
        
        return ro.fiMsg;      
    }
    /**
     * 用有等待对话框的方式发送批命令 
     */
    public FIMessage[] requestxBatch(Component arg0, FIMessage[] arg1) throws FIException {
        
        return null;
    }
    /**
     * 用有等待对话框的方式发送批命令,提交方式为最大提交
     * 
     * @author liwei  
     */
    public Object[] requestBatchTryBest(Component arg0, FIMessage[] fmsgs) throws FIException {
        
        int count = 0;
        if (fmsgs != null) {
            count = fmsgs.length;          
        }
        
        if (returnFIMsg.size() < count) { 
            throw new FIException(-100, "TIcanFClientService: 批量发送命令requestBatchTryBest的returnCmdMsg空间不足，需要的空间为" + count + ", 实际空间为" + returnFIMsg.size(), null);            
        }
        
        boolean raiseException = false;
        Object[] result = new Object[count];
        for(int i = 0; i < count; i++) {
            SendFIReturnObject temp = (SendFIReturnObject) returnFIMsg.get(0);
            
            raiseException = temp.raiseException;
            if(raiseException) {
                result[i] = new FIException(-100, "Communication打桩测试", null);
            }
            else {
                result[i] = temp.fiMsg;
            }
            returnFIMsg.remove(0);
        }
        
        return result;
     }
    /**
     * 发送异步命令 
     */
    public void requestxAsyn(FIMessage arg0, ResponseListener arg1) {
               
    }
    /**
     * 向客户端消息咱中心订阅消息
     */
    public void requestxAsyn(Component arg0, FIMessage arg1, ResponseListener arg2) {
            
    }
    /**
     * 向客户端消息中心取消订阅
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
     * 为单元测试而添加的变量
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
    * 函数名: addRequestxReturnHelper<br>
    * 描  述: 用于预设requestx方法的返回结果<br>
    * 参  数: @param fiMsg<br>
    * 返回值: <br>
    * 创建人: liwei<br>
    * 日  期: 2006-3-7<br>
    * 修改记录: <br>
    * 修改人    修改日期     修改描述<br>
    *******************************************************
     */
    public void addRequestxReturnHelper(FIMessage fiMsg, boolean isRaiseExc) {
        returnFIMsg.add(new SendFIReturnObject(fiMsg, isRaiseExc));
    }
    
    /**
     * 
    ******************************************************* 
    * 函数名: clearRequestxReturnHelper<br>
    * 描  述: 用于清空预设结果，每次用完addRequestxReturnHelper方法后应在合适的地方调用该方法清空返回消息队列<br>
    * 参  数: @param fiMsg<br>
    * 返回值: <br>
    * 创建人: liwei<br>
    * 日  期: 2006-3-7<br>
    * 修改记录: <br>
    * 修改人    修改日期     修改描述<br>
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
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#getClientIP()
     */
    public String getClientIP()
    {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#isHiddenCommand(int)
     */
    public boolean isHiddenCommand(int arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#isHiddenOperation(java.lang.String)
     */
    public boolean isHiddenOperation(String arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     *功能描述：
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#loginUseRadius(java.lang.String, java.lang.String, java.lang.String)
     */
    public int loginUseRadius(String arg0, String arg1, String arg2) throws FIException
    {
        // TODO Auto-generated method stub
        return 0;
    }
    /**
     * 
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.api.pfl.finterface.FClientService#getAllcommandInfo()
     */
    public CommandInfo[] getAllcommandInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
