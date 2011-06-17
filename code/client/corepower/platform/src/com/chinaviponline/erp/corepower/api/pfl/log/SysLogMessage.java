/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.util.Date;

/**
 * <p>文件名称：SysLogMessage.java</p>
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
public class SysLogMessage extends LogMessage
{
    private static final long serialVersionUID = 0xb6452222d601L;

    protected long id;

    protected int rank;

    protected String source;

    protected String taskName;

    protected String detail;

    protected String hostName;

    protected Date logStartDate;

    protected Date logEndDate;

    protected int isFilter;

    protected long relateId;

    protected String reserve[];

    public SysLogMessage()
    {
        id = -1L;
        rank = 0;
        source = "";
        taskName = "";
        detail = "";
        hostName = "";
        logStartDate = null;
        logEndDate = null;
        isFilter = 1;
        relateId = -100L;
        reserve = null;
        setLogType(2);
    }

    public SysLogMessage(long id, int rank, String source, String task,
            String detail, String ip, Date start, Date end, int filter,
            long relateId, String reserve[])
    {
        this.id = -1L;
        this.rank = 0;
        this.source = "";
        taskName = "";
        this.detail = "";
        hostName = "";
        logStartDate = null;
        logEndDate = null;
        isFilter = 1;
        this.relateId = -100L;
        this.reserve = null;
        setLogType(2);
        this.id = id;
        this.rank = rank;
        this.source = source;
        taskName = task;
        this.detail = detail;
        hostName = ip;
        logStartDate = start;
        logEndDate = end;
        isFilter = filter;
        this.relateId = relateId;
        this.reserve = reserve;
    }

    public SysLogMessage(int sysRank, String sysSource, String taskName,
            String sysDetail, String sysHostIP, Date startDate, Date endDate,
            int isFilter, long relateId, String reserve[])
    {
        id = -1L;
        rank = 0;
        source = "";
        this.taskName = "";
        detail = "";
        hostName = "";
        logStartDate = null;
        logEndDate = null;
        this.isFilter = 1;
        this.relateId = -100L;
        this.reserve = null;
        setLogType(2);
        rank = sysRank;
        source = sysSource;
        this.taskName = taskName;
        detail = sysDetail;
        hostName = sysHostIP;
        logStartDate = startDate;
        logEndDate = endDate;
        this.isFilter = isFilter;
        this.relateId = relateId;
        this.reserve = reserve;
    }

    public SysLogMessage(long logId, int sysRank, String sysDetail, Date end,
            String reserve[])
    {
        id = -1L;
        rank = 0;
        source = "";
        taskName = "";
        detail = "";
        hostName = "";
        logStartDate = null;
        logEndDate = null;
        isFilter = 1;
        relateId = -100L;
        this.reserve = null;
        setLogType(2);
        id = logId;
        rank = sysRank;
        detail = sysDetail;
        logEndDate = end;
        this.reserve = reserve;
    }

    public final long getId()
    {
        return id;
    }

    public final void setId(long id)
    {
        this.id = id;
    }

    public final int getRank()
    {
        return rank;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public final String getDetail()
    {
        return detail;
    }

    public final String getHostName()
    {
        return hostName;
    }

    public final String getSource()
    {
        return source;
    }

    public int getIsFilter()
    {
        return isFilter;
    }

    public Date getLogStartDate()
    {
        return logStartDate;
    }

    public Date getLogEndDate()
    {
        return logEndDate;
    }

    public String[] getReserve()
    {
        return reserve;
    }

    public long getRelateId()
    {
        return relateId;
    }

}
