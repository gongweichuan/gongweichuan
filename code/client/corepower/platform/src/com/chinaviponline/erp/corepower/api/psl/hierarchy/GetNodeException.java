/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.hierarchy;

/**
 * <p>�ļ����ƣ�GetNodeException.java</p>
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
public class GetNodeException extends Exception
{

    public GetNodeException()
    {
    }

    public GetNodeException(String msg)
    {
        super(msg);
    }

    public GetNodeException(Throwable t)
    {
        super(t);
    }

    public GetNodeException(String msg, Throwable t)
    {
        super(msg, t);
    }

}
