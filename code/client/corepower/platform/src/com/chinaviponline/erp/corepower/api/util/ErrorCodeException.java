package com.chinaviponline.erp.corepower.api.util;

import java.text.MessageFormat;
/**
 * <p>�ļ����ƣ�ErrorCodeException.java</p>
 * <p>�ļ�������������</p>
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
public class ErrorCodeException extends Exception
{

    public ErrorCodeException(int code, String debugMessage)
    {
        this(code, debugMessage, ((String[]) (null)));
    }

    public ErrorCodeException(int code, String debugMessage, String arguments[])
    {
        super(debugMessage);
        categoryCode = 0;
        errorCode = 1;
        this.arguments = null;
        errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int code)
    {
        this(source, code, (String[]) null);
    }

    public ErrorCodeException(Throwable source, int code, String arguments[])
    {
        super(source);
        categoryCode = 0;
        errorCode = 1;
        this.arguments = null;
        errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int code, String debugMessage)
    {
        this(source, code, debugMessage, ((String[]) (null)));
    }

    public ErrorCodeException(Throwable source, int code, String debugMessage,
            String arguments[])
    {
        super(debugMessage, source);
        categoryCode = 0;
        errorCode = 1;
        this.arguments = null;
        errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(int category, int code, String debugMessage)
    {
        this(category, code, debugMessage, ((String[]) (null)));
    }

    public ErrorCodeException(int category, int code, String debugMessage,
            String arguments[])
    {
        super(debugMessage);
        categoryCode = 0;
        errorCode = 1;
        this.arguments = null;
        categoryCode = category;
        errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int category, int code)
    {
        this(source, category, code, (String[]) null);
    }

    public ErrorCodeException(Throwable source, int category, int code,
            String arguments[])
    {
        super(source);
        categoryCode = 0;
        errorCode = 1;
        this.arguments = null;
        categoryCode = category;
        errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int category, int code,
            String debugMessage)
    {
        this(source, category, code, debugMessage, null);
    }

    public ErrorCodeException(Throwable source, int category, int code,
            String debugMessage, String arguments[])
    {
        super(debugMessage, source);
        categoryCode = 0;
        errorCode = 1;
        this.arguments = null;
        categoryCode = category;
        errorCode = code;
        this.arguments = arguments;
    }

    public int getCategory()
    {
        return categoryCode;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String[] getArguments()
    {
        return arguments;
    }

    public String getDisplayText()
    {
        ErrorCodeI18n.ErrorItem item = ErrorCodeI18n.getInstance()
                .getErrorItem(categoryCode, errorCode);
        if (item == null)
            return "Unknown errorcode :" + errorCode;
        String displayMessage = item.getLabel();
        if (arguments != null)
            displayMessage = MessageFormat.format(displayMessage, arguments);
        return displayMessage;
    }

    public String getDetailText()
    {
        ErrorCodeI18n.ErrorItem item = ErrorCodeI18n.getInstance()
                .getErrorItem(categoryCode, errorCode);
        if (item == null)
            return "Unknown errorcode :" + errorCode;
        String detailText = item.getDetail();
        if (detailText != null && arguments != null)
            detailText = MessageFormat.format(detailText, arguments);
        return detailText;
    }

    public int getErrorLevel()
    {
        ErrorCodeI18n.ErrorItem item = ErrorCodeI18n.getInstance()
                .getErrorItem(categoryCode, errorCode);
        if (item == null)
            return 0;
        else
            return item.getLevel();
    }

    private int categoryCode;

    private int errorCode;

    private String arguments[];
}
