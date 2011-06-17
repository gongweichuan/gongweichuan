/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.jmswrapper;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * <p>�ļ����ƣ�Sender.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-26</p>
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
public interface Sender
    extends CapabilitySet
    {

        public abstract void send(Message message)
            throws JMSException;

        public abstract void send(Message message, int i, int j, long l)
            throws JMSException;

        public abstract void close()
            throws JMSException;

        public abstract Message[] dump()
            throws StillRunningException, CacheEmptyException;

    
}
