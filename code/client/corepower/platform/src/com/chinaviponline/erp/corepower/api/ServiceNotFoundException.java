
package com.chinaviponline.erp.corepower.api;

/**
 * <p>�ļ����ƣ�ServiceNotFoundException.java</p>
 * <p>�ļ�������û���ҵ������쳣</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-2</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�
 *  �汾�ţ�
 *  �޸��ˣ�
 *  �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
 * @email  gongweichuan(AT)gmail.com
 */
public class ServiceNotFoundException extends Exception
{
    /**
     * 
     * @param message �쳣��Ϣ
     */
    public ServiceNotFoundException(String message)
    {
        super(message);
    }

    /**
     * 
     * @param message �쳣��Ϣ
     * @param cause �쳣
     */
    public ServiceNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
