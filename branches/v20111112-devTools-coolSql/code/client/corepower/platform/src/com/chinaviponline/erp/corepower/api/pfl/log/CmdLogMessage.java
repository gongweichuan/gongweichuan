/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.sql.Timestamp;
import java.util.*;

import com.chinaviponline.erp.corepower.api.pfl.finterface.FIMessage;

/**
 * <p>文件名称：CmdLogMessage.java</p>
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
public class CmdLogMessage extends LogMessage
{
    private static final long serialVersionUID = 0xb6452222ca49L;

    protected long id;

    protected String userName;

    protected String cmdCode;

    protected String description;

    protected String operateSet;

    protected String para;

    protected String failReason;

    protected String clientIp;

    protected int isSucceed;

    protected String connectMode;

    protected String operatResource;

    protected Date logInitDate;

    protected Date logStartDate;

    protected Date logEndDate;

    protected long linkId;

    protected int logRank;

    protected int isFilter;

    protected String reserve[];

    private Vector resourceDn;

    private Vector resourceType;

    public CmdLogMessage()
    {
        id = -1L;
        userName = "";
        cmdCode = "";
        description = "";
        operateSet = "";
        para = "";
        failReason = "";
        clientIp = "";
        isSucceed = 0;
        connectMode = "";
        operatResource = "";
        logInitDate = null;
        logStartDate = null;
        logEndDate = null;
        linkId = -100L;
        logRank = -1;
        isFilter = 1;
        reserve = null;
        resourceDn = new Vector();
        resourceType = new Vector();
        setLogType(1);
    }

    public CmdLogMessage(String userName, String commandCode, String descInfo,
            String detailInfo, String failReason, int isSuccess,
            String clientIp, String connectMode, String resourceType[],
            String resourceDn[], Timestamp clientTime, Timestamp startTime,
            Timestamp endTime, long relateId, int logRank, int filter,
            String reserve[])
    {
        id = -1L;
        this.userName = "";
        cmdCode = "";
        description = "";
        operateSet = "";
        para = "";
        this.failReason = "";
        this.clientIp = "";
        isSucceed = 0;
        this.connectMode = "";
        operatResource = "";
        logInitDate = null;
        logStartDate = null;
        logEndDate = null;
        linkId = -100L;
        this.logRank = -1;
        isFilter = 1;
        this.reserve = null;
        this.resourceDn = new Vector();
        this.resourceType = new Vector();
        setLogType(1);
        StringBuffer resource = new StringBuffer();
        if (resourceDn != null && resourceType != null)
        {
            for (int i = 0; i < resourceDn.length; i++)
            {
                resource.append(resourceDn[i]);
                resource.append("(").append(resourceType[i]).append(")");
                
                if (i < resourceDn.length - 1)
                {
                    resource.append("|");                    
                }
                
            }

        }
        
        this.userName = userName;
        cmdCode = commandCode;
        description = descInfo;
        para = detailInfo;
        this.failReason = failReason;
        isSucceed = isSuccess;
        this.clientIp = clientIp;
        this.connectMode = connectMode;
        operatResource = resource.toString();
        logInitDate = clientTime;
        logStartDate = startTime;
        logEndDate = endTime;
        linkId = relateId;
        this.logRank = logRank;
        isFilter = filter;
        this.reserve = reserve;
        
        if (operatResource != null)
        {
            StringTokenizer st = new StringTokenizer(operatResource, "|");
            do
            {
                if (!st.hasMoreTokens())
                {
                    break;                    
                }
                
                String res = st.nextToken();
                int begin = 0;
                int end = 0;
                begin = res.indexOf("(");
                
                if (begin != -1)
                {
                    end = res.indexOf(")", begin);
                    if (end != -1)
                    {
                        String dn = res.substring(0, begin);
                        String type = res.substring(begin + 1, end);
                        this.resourceDn.add(dn);
                        this.resourceType.add(type);
                    }
                }
            }
            while (true);
        }
    }

    public CmdLogMessage(long id, String username, String cmdcode, String desc,
            String set, String pa, int issucceed, String failreason,
            String clientip, String connectmode, String operateresource,
            Date initdate, Date startdate, Date enddate, long linkid, int rank,
            int isfilter, String reserve[])
    {
        this.id = -1L;
        userName = "";
        cmdCode = "";
        description = "";
        operateSet = "";
        para = "";
        failReason = "";
        clientIp = "";
        isSucceed = 0;
        connectMode = "";
        operatResource = "";
        logInitDate = null;
        logStartDate = null;
        logEndDate = null;
        linkId = -100L;
        logRank = -1;
        isFilter = 1;
        this.reserve = null;
        resourceDn = new Vector();
        resourceType = new Vector();
        setLogType(1);
        this.id = id;
        userName = username;
        cmdCode = cmdcode;
        description = desc;
        operateSet = set;
        para = pa;
        failReason = failreason;
        isSucceed = issucceed;
        clientIp = clientip;
        connectMode = connectmode;
        operatResource = operateresource;
        logInitDate = initdate;
        logStartDate = startdate;
        logEndDate = enddate;
        linkId = linkid;
        logRank = rank;
        isFilter = isfilter;
        this.reserve = reserve;
        if (operatResource != null)
        {
            StringTokenizer st = new StringTokenizer(operatResource, "|");
            do
            {
                if (!st.hasMoreTokens())
                {
                    break;                    
                }
                
                String res = st.nextToken();
                int begin = 0;
                int end = 0;
                begin = res.indexOf("(");
                
                if (begin != -1)
                {
                    end = res.indexOf(")", begin);
                    if (end != -1)
                    {
                        String dn = res.substring(0, begin);
                        String type = res.substring(begin + 1, end);
                        resourceDn.add(dn);
                        resourceType.add(type);
                    }
                }
            }
            while (true);
        }
    }

    public CmdLogMessage(long logId, String detailInfo, int isSuccess,
            String failReason, Date enddate, String reserve[])
    {
        id = -1L;
        userName = "";
        cmdCode = "";
        description = "";
        operateSet = "";
        para = "";
        this.failReason = "";
        clientIp = "";
        isSucceed = 0;
        connectMode = "";
        operatResource = "";
        logInitDate = null;
        logStartDate = null;
        logEndDate = null;
        linkId = -100L;
        logRank = -1;
        isFilter = 1;
        this.reserve = null;
        resourceDn = new Vector();
        resourceType = new Vector();
        setLogType(1);
        id = logId;
        para = detailInfo;
        isSucceed = isSuccess;
        this.failReason = failReason;
        logEndDate = enddate;
        this.reserve = reserve;
    }

    public CmdLogMessage(FIMessage f, String userName, int isSuccess,
            String failReason, String hostIp, String reserve[])
    {
        id = -1L;
        this.userName = "";
        cmdCode = "";
        description = "";
        operateSet = "";
        para = "";
        this.failReason = "";
        clientIp = "";
        isSucceed = 0;
        connectMode = "";
        operatResource = "";
        logInitDate = null;
        logStartDate = null;
        logEndDate = null;
        linkId = -100L;
        logRank = -1;
        isFilter = 1;
        this.reserve = null;
        resourceDn = new Vector();
        resourceType = new Vector();
        setLogType(1);
        StringBuffer resource = new StringBuffer();
        int count = f.getResourceCount();
        for (int i = 0; i < count; i++)
        {
            String type = f.getResourceType(i);
            String dn[] = f.getResourceDN(i);
            for (int j = 0; j < dn.length; j++)
            {
                resource.append(dn[j]);
                resource.append("(").append(type).append(")");
                resource.append("|");
            }

        }

        Date time = new Date();
        this.userName = userName;
        cmdCode = Integer.toString(f.getCommandCode());
        
        if (f.getLogDetail() == null || f.getLogDetail().equals(""))
        {
            para = f.toString();            
        }
        else
        {
            para = f.getLogDetail();            
        }
        
        this.failReason = failReason;
        isSucceed = isSuccess;
        clientIp = hostIp;
        connectMode = "GUI";
        operatResource = resource.toString();
        logInitDate = time;
        logStartDate = time;
        logEndDate = time;
        linkId = 0L;
        logRank = 4;
        isFilter = 1;
        this.reserve = reserve;
        if (operatResource != null)
        {
            StringTokenizer st = new StringTokenizer(operatResource, "|");
            do
            {
                if (!st.hasMoreTokens())
                {
                    break;                    
                }
                
                String res = st.nextToken();
                int begin = 0;
                int end = 0;
                begin = res.indexOf("(");
                
                if (begin != -1)
                {
                    end = res.indexOf(")", begin);
                    if (end != -1)
                    {
                        String dn = res.substring(0, begin);
                        String type = res.substring(begin + 1, end);
                        resourceDn.add(dn);
                        resourceType.add(type);
                    }
                }
            }
            while (true);
        }
    }

    public String getUserName()
    {
        return userName;
    }

    public String getCmdCode()
    {
        return cmdCode;
    }

    public String getOperateSet()
    {
        return operateSet;
    }

    public String getPara()
    {
        return para;
    }

    public int getSucess()
    {
        return isSucceed;
    }

    public String getFailReason()
    {
        return failReason;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public String getConnectMode()
    {
        return connectMode;
    }

    public String getOperatResource()
    {
        return operatResource;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public int getIsFilter()
    {
        return isFilter;
    }

    /**
     * @deprecated Method getLinkId is deprecated
     */

    public long getLinkId()
    {
        return linkId;
    }

    public long getRelateId()
    {
        return linkId;
    }

    public Date getLogEndDate()
    {
        return logEndDate;
    }

    public Date getLogInitDate()
    {
        return logInitDate;
    }

    public Date getLogStartDate()
    {
        return logStartDate;
    }

    public int getLogRank()
    {
        return logRank;
    }

    public String[] getReserve()
    {
        return reserve;
    }

    public int getIsSucceed()
    {
        return isSucceed;
    }

    public String getDescription()
    {
        return description;
    }

    public String[] getAllResourceType()
    {
        Vector uniqueType = new Vector();
        for (int i = 0; i < resourceType.size(); i++)
        {
            String type = (String) resourceType.get(i);
            
            if (!uniqueType.contains(type))
            {
                uniqueType.add(type);                
            }
        }

        String types[] = new String[uniqueType.size()];
        
        for (int i = 0; i < uniqueType.size(); i++)
        {
            types[i] = (String) uniqueType.get(i);            
        }

        return types;
    }

    public String[] getAllResourceDn()
    {
        String dns[] = new String[resourceDn.size()];
        
        for (int i = 0; i < resourceDn.size(); i++)
        {
            dns[i] = (String) resourceDn.get(i);            
        }

        return dns;
    }

    public String[] getResourceDn(String type)
    {
        Vector typeDn = new Vector();
        for (int i = 0; i < resourceDn.size(); i++)
        {
            String resType = (String) resourceType.get(i);
            
            if (resType.equals(type))
            {
                typeDn.add(resourceDn.get(i));                
            }
        }

        String types[] = new String[typeDn.size()];
        
        for (int i = 0; i < typeDn.size(); i++)
        {
            types[i] = (String) typeDn.get(i);            
        }

        return types;
    }

}
