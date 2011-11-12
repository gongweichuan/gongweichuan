/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

import java.io.Serializable;
import java.util.*;

/**
 * <p>文件名称：FIMessage.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-18</p>
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
public class FIMessage implements Serializable
{


    private static final long serialVersionUID = 0xfff8faba6122dad0L;
    public static final int LOG_FLAG_RECORD = 0;
    public static final int LOG_FLAG_NOT_RECORD = 1;
    public static final int LOG_FLAG_NOT_SET = 2;
    public static final int DEFAULT_HIERARCHY_ID = -1;
    public static final char EQUALS_MARK = 61;
    public static final String PASSWORD_MARK = "******";
    public static final char COMMA_MARK = 44;
    public static final String SUSPENSION_POINTS_MARK = "...";
    public static final char QUOTATION_MARK = 34;
    public static final char JOIN_MARK = 38;
    public static final String COMPARTMENTATION_MARK = "|@#$%|";
    public static final char COLON_MARK = 58;
    public static final char ENTER_MARK = 10;
    private static volatile int curInvokeId = 0;
    private static final String INVOKEID_KEY = "InvokeID";
    private static final String CMDCODE_KEY = "CommandCode";
    private static final String ERRDESC_KEY = "ErrDesc";
    private static final String ERRID_KEY = "ErrID";
    private static final String SESSIONID_KEY = "SessionID";
    private static final String SOURCENODE_KEY = "SourceNode";
    private static final String TARGETNODE_KEY = "TargetNode";
    private static final String USERID_KEY = "UserID";
    private static final String VERSION_KEY = "VersionID";
    private static final String RESTYPE_KEY = "ResourceForCheckType";
    private static final String RESDN_KEY = "ResourceForCheckDN";
    private static final String RESCOUNT_KEY = "ResourceCount";
    private static final String LOG_KEY = "LogFlag";
    private static final String LOG_DETAIL_KEY = "Log_Detail";
    private static final String KEY_REACH_SERVER_TIME = "reach_server";
    private static final String KEY_DISPATCH_TIME = "begin_dispatch";
    private static final String KEY_RETURN_FROM_EBJ_TIME = "end_dispatch";
    private static final String KEY_LEAVE_SERVICE_TIME = "leave_server";
    private static final int INIT_BLOCK_NUM = 4;
    protected Properties header;
    private long state_time;
    private long log_id;
    protected ArrayList sections;

    private static synchronized int allocInvokeID()
    {
        return ++curInvokeId;
    }

    public FIMessage()
    {
        header = new Properties();
        state_time = 0L;
        log_id = 0L;
        sections = new ArrayList(4);
        setInvokeID(allocInvokeID());
    }

    public FIMessage(int cmdCode, String versionID, int targetNode)
    {
        this();
        setVersionID(versionID);
        setCommandCode(cmdCode);
        setTargetNode(targetNode);
    }

    public FIMessage(int cmdCode, String versionID)
    {
        this();
        setCommandCode(cmdCode);
        setVersionID(versionID);
    }

    public FIMessage(FIMessage fmsg)
    {
        this();
        copyHead(fmsg);
    }

    public FIMessage(int cmdCode)
    {
        this();
        setCommandCode(cmdCode);
    }

    public final void copyHead(FIMessage fmsg)
    {
        header = (Properties)fmsg.header.clone();
    }

    public final short getSessionID()
    {
        String strId = header.getProperty("SessionID");
        if(strId == null)
            return -1;
        else
            return Short.parseShort(strId);
    }

    public final int getTargetNode()
    {
        String strNode = header.getProperty("TargetNode");
        if(strNode == null)
            return -1;
        else
            return Integer.parseInt(strNode);
    }

    public final int getCommandCode()
    {
        String strCmdCode = header.getProperty("CommandCode");
        if(strCmdCode == null)
            return -1;
        else
            return Integer.parseInt(strCmdCode);
    }

    public final String getVersionID()
    {
        return header.getProperty("VersionID");
    }

    public final int getSourceNode()
    {
        String strNode = header.getProperty("SourceNode");
        if(strNode == null)
            return -1;
        else
            return Integer.parseInt(strNode);
    }

    public final int getUserID()
    {
        String strID = header.getProperty("UserID");
        if(strID == null)
            return -1;
        else
            return Integer.parseInt(strID);
    }

    public final void setSessionID(int id)
    {
        header.setProperty("SessionID", String.valueOf(id));
    }

    public final void setUserID(int id)
    {
        header.setProperty("UserID", String.valueOf(id));
    }

    public final void setTargetNode(int node)
    {
        header.setProperty("TargetNode", String.valueOf(node));
    }

    public final void setCommandCode(int code)
    {
        header.setProperty("CommandCode", String.valueOf(code));
    }

    public final void setVersionID(String ver)
    {
        if(ver == null)
        {
            return;
        } else
        {
            header.setProperty("VersionID", ver);
            return;
        }
    }

    public final void setInvokeID(int id)
    {
        header.setProperty("InvokeID", String.valueOf(id));
    }

    public final void setSourceNode(int node)
    {
        header.setProperty("SourceNode", String.valueOf(node));
    }

    public final int getInvokeID()
    {
        return Integer.parseInt(header.getProperty("InvokeID", "0"));
    }

    public final FISection getFISectionAt(int index)
    {
        if(index >= getSectionCount() || index < 0)
            return null;
        else
            return (FISection)sections.get(index);
    }

    public final int getSectionCount()
    {
        return sections.size();
    }

    public void addFISection(FISection section)
    {
        if(section == null)
        {
            return;
        } else
        {
            sections.add(section);
            return;
        }
    }

    /**
     * @deprecated Method setErrID is deprecated
     */

    public final void setErrID(int err)
    {
        header.setProperty("ErrID", String.valueOf(err));
    }

    /**
     * @deprecated Method getErrID is deprecated
     */

    public final int getErrID()
    {
        String strErrID = header.getProperty("ErrID");
        if(strErrID == null)
            return -1;
        else
            return Integer.parseInt(strErrID);
    }

    /**
     * @deprecated Method setErrDesc is deprecated
     */

    public final void setErrDesc(String err)
    {
        if(err == null)
        {
            return;
        } else
        {
            header.setProperty("ErrDesc", err);
            return;
        }
    }

    /**
     * @deprecated Method getErrDesc is deprecated
     */

    public final String getErrDesc()
    {
        return header.getProperty("ErrDesc");
    }

    public List getAllSection()
    {
        return sections;
    }

    public int getResourceCount()
    {
        String strCount;
        strCount = header.getProperty("ResourceCount");
        if(strCount == null)
            return 0;
        int iCount;
        try
        {
            iCount = Integer.parseInt(strCount);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
        return iCount;
    }

    private void setResourceCount(int iCount)
    {
        header.setProperty("ResourceCount", String.valueOf(iCount));
    }

    public void setResource(String ResType, String resDNs[])
    {
        if(ResType == null || resDNs == null || resDNs.length == 0)
        {
            return;
        } else
        {
            int index = getResourceCount();
            String values = MmlHelper.convergeValues(resDNs);
            header.setProperty("ResourceForCheckType" + index, ResType);
            header.setProperty("ResourceForCheckDN" + index, values);
            setResourceCount(index + 1);
            return;
        }
    }

    public String getResourceType(int index)
        throws IndexOutOfBoundsException
    {
        if(index >= getResourceCount())
            throw new IndexOutOfBoundsException();
        else
            return header.getProperty("ResourceForCheckType" + index);
    }

    public String[] getResourceDN(int index)
        throws IndexOutOfBoundsException
    {
        if(index >= getResourceCount())
            throw new IndexOutOfBoundsException();
        String values = header.getProperty("ResourceForCheckDN" + index);
        if(values == null)
            return new String[0];
        else
            return MmlHelper.decodeValues(values, "|$|");
    }

    protected final String fhead2String()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("CommandCode");
        sb.append('=');
        sb.append(getCommandCode());
        int targetId = getTargetNode();
        if(targetId != -1)
        {
            sb.append(',');
            sb.append("TargetNode");
            sb.append('=');
            sb.append(targetId);
        }
        return sb.toString();
    }

    protected final String fbody2String()
    {
        int sectionsLen = sections.size();
        StringBuffer mml = new StringBuffer();
        for(int i = 0; i < sectionsLen; i++)
        {
            FISection sec = (FISection)sections.get(i);
            mml.append(sec.toString());
            if(i < sections.size() - 1)
                mml.append(':');
        }

        for(; mml.length() > 0 && mml.charAt(mml.length() - 1) == ':'; mml.deleteCharAt(mml.length() - 1));
        mml.append(';');
        return mml.toString();
    }

    public String toString()
    {
        String str = fhead2String() + ':' + fbody2String();
        return MmlHelper.replace(str, "|$|", "&");
    }

    public void setLogFlag(boolean logFlag)
    {
        header.put("LogFlag", logFlag ? "TRUE" : "FALSE");
    }

    public int getLogFlag()
    {
        String logFlag = header.getProperty("LogFlag", "");
        if(logFlag.equals("TRUE"))
            return 0;
        return !logFlag.equals("FALSE") ? 2 : 1;
    }

    public void setLogDetail(String str)
    {
        header.setProperty("Log_Detail", str);
    }

    public String getLogDetail()
    {
        return header.getProperty("Log_Detail");
    }

    public void setCmdStartTime(long time)
    {
        state_time = time;
    }

    public long getCmdStartTime()
    {
        return state_time;
    }

    public void setLogId(long id)
    {
        log_id = id;
    }

    public long getLogId()
    {
        return log_id;
    }

    public void setLeaveServiceTime(long l)
    {
        header.setProperty("leave_server", String.valueOf(l));
    }

    public long getLeaveServiceTime()
    {
        String str = header.getProperty("leave_server", "-1");
        return Long.parseLong(str);
    }

    public void setEJBReturnTime(long l)
    {
        header.setProperty("end_dispatch", String.valueOf(l));
    }

    public long getEJBReturnTime()
    {
        String str = header.getProperty("end_dispatch", "-1");
        return Long.parseLong(str);
    }

    public void setReachServerTime(long l)
    {
        header.setProperty("reach_server", String.valueOf(l));
    }

    public long getReachServerTime()
    {
        String str = header.getProperty("reach_server", "-1");
        return Long.parseLong(str);
    }

    public void setDispatchTime(long l)
    {
        header.setProperty("begin_dispatch", String.valueOf(l));
    }

    public long getDispatchTime()
    {
        String str = header.getProperty("begin_dispatch", "-1");
        return Long.parseLong(str);
    }

    public void setValue(String key, String value)
    {
        header.setProperty(key, value);
    }

    public String getValue(String key)
    {
        String str = header.getProperty(key, "");
        return str;
    }

    
}
