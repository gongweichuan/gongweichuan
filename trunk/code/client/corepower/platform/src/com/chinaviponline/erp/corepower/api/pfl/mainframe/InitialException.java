package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import com.chinaviponline.erp.corepower.api.util.ErrorCodeException;

/**
 * <p>�ļ����ƣ�InitialException.java</p>
 * <p>�ļ��������쳣</p>
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


public class InitialException extends ErrorCodeException
{

    public InitialException(int errorCode, String debugMessage)
    {
        super(errorCode, debugMessage);
    }

    public InitialException(int errorCode, String debugMessage,
            String arguments[])
    {
        super(errorCode, debugMessage, arguments);
    }

    public InitialException(Throwable source, int errorCode)
    {
        super(source, errorCode);
    }

    public InitialException(Throwable source, int errorCode, String arguments[])
    {
        super(source, errorCode, arguments);
    }

    public InitialException(Throwable source, int errorCode, String debugMessage)
    {
        super(source, errorCode, debugMessage);
    }

    public InitialException(Throwable source, int errorCode,
            String debugMessage, String arguments[])
    {
        super(source, errorCode, debugMessage, arguments);
    }

    public InitialException(int category, int code, String debugMessage)
    {
        this(category, code, debugMessage, ((String[]) (null)));
    }

    public InitialException(int category, int code, String debugMessage,
            String arguments[])
    {
        super(category, code, debugMessage, arguments);
    }

    public InitialException(Throwable source, int category, int code)
    {
        this(source, category, code, (String[]) null);
    }

    public InitialException(Throwable source, int category, int code,
            String arguments[])
    {
        super(source, category, code, arguments);
    }

    public InitialException(Throwable source, int category, int code,
            String debugMessage)
    {
        this(source, category, code, debugMessage, null);
    }

    public InitialException(Throwable source, int category, int code,
            String debugMessage, String arguments[])
    {
        super(source, category, code, debugMessage, arguments);
    }
}
