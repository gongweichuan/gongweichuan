/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.jmswrapper;

import java.io.Serializable;
import javax.jms.*;

/**
 * <p>文件名称：MessageService.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-26</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public interface MessageService
{

    public static final String ROLE = MessageService.class.getName();

    public abstract BytesMessage createBytesMessage()
        throws JMSException;

    public abstract MapMessage createMapMessage()
        throws JMSException;

    public abstract ObjectMessage createObjectMessage()
        throws JMSException;

    public abstract ObjectMessage createObjectMessage(Serializable serializable)
        throws JMSException;

    public abstract StreamMessage createStreamMessage()
        throws JMSException;

    public abstract TextMessage createTextMessage()
        throws JMSException;

    public abstract TextMessage createTextMessage(String s)
        throws JMSException;

    public abstract Message createMessage()
        throws JMSException;

    public abstract Sender getSender(int i, String s)
        throws JMSException;

    public abstract Sender getSender(int i, String s, boolean flag)
        throws JMSException;

    public abstract Sender getSender(int i, String s, String s1)
        throws JMSException;

    public abstract Sender getSender(int i, String s, String s1, boolean flag)
        throws JMSException;

    public abstract Sender getSender(int i, String s, int j)
        throws JMSException;

    public abstract Sender getSender(int i, String s, int j, boolean flag)
        throws JMSException;

    public abstract Sender getSender(int i, String s, String s1, int j)
        throws JMSException;

    public abstract Sender getSender(int i, String s, String s1, int j, boolean flag)
        throws JMSException;

    public abstract Sender getSender(int i, String s, int j, long l)
        throws JMSException;

    public abstract Receiver getReceiver(int i, String s)
        throws JMSException;

    public abstract Receiver getReceiver(boolean flag, String s, String s1)
        throws JMSException;

    public abstract Receiver getReceiver(int i, String s, String s1)
        throws JMSException;

    public abstract Receiver getReceiver(int i, String s, String s1, String s2)
        throws JMSException;

    public abstract Receiver getReceiver(int i, String s, int j)
        throws JMSException;

    public abstract Receiver getReceiver(int i, String s, int j, String s1)
        throws JMSException;

    public abstract Receiver getReceiver(int i, String s, String s1, int j, String s2)
        throws JMSException;

    public abstract AsyncReceiver registAsyncReceive(int i, String s, MessageListener messagelistener)
        throws JMSException;

    public abstract AsyncReceiver registAsyncReceive(int i, String s, MessageListener messagelistener, String s1)
        throws JMSException;

    public abstract AsyncReceiver registAsyncReceive(int i, String s, String s1, MessageListener messagelistener)
        throws JMSException;

    public abstract AsyncReceiver registAsyncReceive(int i, String s, MessageListener messagelistener, String s1, int j, String s2)
        throws JMSException;

    public abstract AsyncReceiver registAsyncArrayReceiver(int i, String s, MessageArrayListener messagearraylistener)
        throws JMSException;

    public abstract AsyncReceiver registAsyncArrayReceiver(int i, String s, String s1, MessageArrayListener messagearraylistener)
        throws JMSException;

    public abstract void deRegistAsyncReceive(RegistryInfo registryinfo)
        throws JMSException;

    public abstract void deRegistAsyncReceive(AsyncReceiver asyncreceiver)
        throws JMSException;

    public abstract Sender getSenderDyn(int i, String s)
        throws JMSException;

    public abstract Sender getSenderDyn(int i, String s, int j, long l)
        throws JMSException;

    public abstract AsyncReceiver registAsyncReceiverDyn(int i, String s, MessageListener messagelistener)
        throws JMSException;

    public abstract AsyncReceiver registAsyncArrayReceiverDyn(int i, String s, MessageArrayListener messagearraylistener)
        throws JMSException;

    
}
