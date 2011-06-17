package com.chinaviponline.erp.corepower.api.util;

import java.text.MessageFormat;
/**
 * <p>文件名称：ErrorCodeException.java</p>
 * <p>文件描述：错误码</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-2</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：
 *  版本号：
 *  修改人：
 *  修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
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
