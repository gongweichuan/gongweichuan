/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.util.Date;

/**
 * <p>文件名称：SecLogMessage.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-7-12</p>
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
public class SecLogMessage extends LogMessage
{
    private static final long serialVersionUID = 0xb6452222d219L;

    protected long id;

    protected String userName;

    protected String clientIp;

    protected String operate;

    protected String para;

    protected Date logTime;

    protected String connectMode;

    protected String reserve[];

    public SecLogMessage()
    {
        id = -1L;
        userName = "";
        clientIp = "";
        operate = "";
        para = "";
        logTime = null;
        connectMode = "";
        reserve = null;
        setLogType(3);
    }

    public SecLogMessage(long id, String user, String ip, String operate,
            String detail, Date logTime, String mode, String reserve[])
    {
        this.id = -1L;
        userName = "";
        clientIp = "";
        this.operate = "";
        para = "";
        this.logTime = null;
        connectMode = "";
        this.reserve = null;
        setLogType(3);
        this.id = id;
        userName = user;
        clientIp = ip;
        this.operate = operate;
        para = detail;
        this.logTime = logTime;
        connectMode = mode;
        this.reserve = reserve;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public String getPara()
    {
        return para;
    }

    public String getOperate()
    {
        return operate;
    }

    public String getConnectMode()
    {
        return connectMode;
    }

    public Date getLogTime()
    {
        return logTime;
    }

    public String[] getReserve()
    {
        return reserve;
    }

}
