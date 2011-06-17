/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.io.Serializable;
import java.util.Date;

import com.chinaviponline.erp.corepower.api.pfl.log.reserve.ReserveCond;

/**
 * <p>文件名称：LogCond.java</p>
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
public class LogCond implements Serializable
{
    private long id[];

    private String ips[];

    private String details[];

    private ReserveCond reserve;

    private String reserveId;

    private long idStart;

    private long idEnd;

    private Date startTime;

    private Date endTime;

    private boolean needLogCount;

    public LogCond()
    {
        id = null;
        ips = null;
        details = null;
        reserve = null;
        reserveId = "";
        idStart = -1L;
        idEnd = -1L;
        startTime = null;
        endTime = null;
        needLogCount = true;
    }

    public long[] getId()
    {
        return id;
    }

    public void setId(long id[])
    {
        this.id = id;
    }

    public long getIdStart()
    {
        return idStart;
    }

    public void setIdStart(long id)
    {
        idStart = id;
    }

    public long getIdEnd()
    {
        return idEnd;
    }

    public void setIdEnd(long id)
    {
        idEnd = id;
    }

    public String[] getIp()
    {
        return ips;
    }

    public void setIp(String ip[])
    {
        ips = ip;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String[] getDetails()
    {
        return details;
    }

    public void setDetails(String paras[])
    {
        details = paras;
    }

    public void setReserve(ReserveCond cond)
    {
        reserve = cond;
    }

    public ReserveCond getReserve()
    {
        return reserve;
    }

    public String getReserveId()
    {
        return reserveId;
    }

    public void setReserveId(String rId)
    {
        reserveId = rId;
    }

    public void setNeedLogCount(boolean isNeed)
    {
        needLogCount = isNeed;
    }

    public boolean getNeedLogCount()
    {
        return needLogCount;
    }

}
