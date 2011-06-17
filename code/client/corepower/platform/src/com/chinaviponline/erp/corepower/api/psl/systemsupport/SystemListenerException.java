/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.systemsupport;

/**
 * <p>�ļ����ƣ�SystemListenerException.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-10</p>
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
public class SystemListenerException extends Exception
{
    /**
     * add listener failed
     */
    public static final String ADD_FAILURE = "add listener failed";
    
    /**
     * remove listener failed
     */
    public static final String REMOVE_FAILURE = "remove listener failed";

    /**
     * ���캯��
     *
     */
    public SystemListenerException()
    {
        
    }

    /**
     * ���캯��
     * @param msg
     */
    public SystemListenerException(String msg)
    {
        super(msg);
    }

    /**
     * ���캯��
     * @param t
     */
    public SystemListenerException(Throwable t)
    {
        super(t);
    }

    /**
     * ���캯��
     * @param msg
     * @param t
     */
    public SystemListenerException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
