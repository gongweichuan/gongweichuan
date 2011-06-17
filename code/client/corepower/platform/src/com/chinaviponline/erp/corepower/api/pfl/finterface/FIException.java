/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

import com.chinaviponline.erp.corepower.api.util.ErrorCodeException;

/**
 * <p>文件名称：FIException.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-17</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
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
