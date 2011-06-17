package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import com.chinaviponline.erp.corepower.api.util.ErrorCodeException;

/**
 * <p>文件名称：InitialException.java</p>
 * <p>文件描述：异常</p>
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
