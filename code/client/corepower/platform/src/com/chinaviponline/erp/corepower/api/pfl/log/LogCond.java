/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.io.Serializable;
import java.util.Date;

import com.chinaviponline.erp.corepower.api.pfl.log.reserve.ReserveCond;

/**
 * <p>�ļ����ƣ�LogCond.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-7-12</p>
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
