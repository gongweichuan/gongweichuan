/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

import com.chinaviponline.erp.corepower.api.util.ErrorCodeException;

/**
 * <p>�ļ����ƣ�FIException.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-17</p>
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
public class FIException extends ErrorCodeException
{
    public FIException(int errID, String debugMessage)
    {
        super(errID, debugMessage);
    }

    public FIException(int errID, String debugMessage, String arguments[])
    {
        super(errID, debugMessage, arguments);
    }

    public FIException(Throwable e, int errID)
    {
        super(e, errID);
    }

    public FIException(Throwable e, int errorCode, String arguments[])
    {
        super(e, errorCode, arguments);
    }

    public FIException(Throwable e, int errID, String debugMessage)
    {
        super(e, errID, debugMessage);
    }

    public FIException(Throwable e, int errorCode, String debugMessage, String arguments[])
    {
        super(e, errorCode, debugMessage, arguments);
    }

    public FIException(int category, int code, String debugMessage)
    {
        this(category, code, debugMessage, ((String []) (null)));
    }

    public FIException(int category, int code, String debugMessage, String arguments[])
    {
        super(category, code, debugMessage, arguments);
    }

    public FIException(Throwable source, int category, int code)
    {
        this(source, category, code, (String[])null);
    }

    public FIException(Throwable source, int category, int code, String arguments[])
    {
        super(source, category, code, arguments);
    }

    public FIException(Throwable source, int category, int code, String debugMessage)
    {
        this(source, category, code, debugMessage, null);
    }

    public FIException(Throwable source, int category, int code, String debugMessage, String arguments[])
    {
        super(source, category, code, debugMessage, arguments);
    }

}
