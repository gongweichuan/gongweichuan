/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.hierarchy;

/**
 * <p>�ļ����ƣ�FindServiceException.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-28</p>
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
public class FindServiceException extends Exception
{

    public FindServiceException()
    {
    }

    public FindServiceException(String info)
    {
        super(info);
    }

    public FindServiceException(Throwable t)
    {
        super(t);
    }

    public FindServiceException(String info, Throwable t)
    {
        super(info, t);
    }

}
