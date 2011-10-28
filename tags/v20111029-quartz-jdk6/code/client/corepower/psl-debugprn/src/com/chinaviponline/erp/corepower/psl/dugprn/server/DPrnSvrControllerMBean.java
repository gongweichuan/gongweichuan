/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.dugprn.server;

import com.chinaviponline.erp.corepower.api.BaseMBean;
import java.util.*;

/**
 * <p>文件名称：DPrnSvrControllerMBean.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-4</p>
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
public interface DPrnSvrControllerMBean  extends BaseMBean
{

    public abstract void setFilter(String s, Integer integer);

    public abstract void setAppenderThreshold(String s, Integer integer)
        throws Exception;

    public abstract void setAppendersThreshold(HashSet hashset, HashMap hashmap)
        throws Exception;

    public abstract void startLog4jServer(String s)
        throws Exception;

    public abstract void stopLog4jServer(String s)
        throws Exception;

    public abstract String addSocketAppender(String s, Integer integer, String s1)
        throws Exception;

    public abstract void removeSocketAppender(String s)
        throws Exception;

    public abstract String removeSocketAppender(String s, Integer integer, String s1);

    public abstract String addJMSAppender(String s, String s1, String s2);

    public abstract void removeJMSAppender(String s);

    public abstract String getSuperHost();

    public abstract void setSuperHost(String s);

    public abstract Hashtable getLoggerTree();

    public abstract String getServerName();

    public abstract void setServerName(String s);

    public abstract Hashtable getServerInfo();

    public abstract void removeAllNetAppender();

    public abstract void setConfigureFile(String s);

    public abstract String getConfigureFile();

    public abstract String getFileLogLevel();

    public abstract String getConsoleLogLevel();

}
