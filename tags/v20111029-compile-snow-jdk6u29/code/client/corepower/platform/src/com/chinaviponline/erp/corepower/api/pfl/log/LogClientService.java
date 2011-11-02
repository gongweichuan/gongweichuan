/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.sql.Timestamp;

import com.chinaviponline.erp.corepower.api.pfl.finterface.FIException;
import com.chinaviponline.erp.corepower.api.pfl.log.queryservice.TablePanel;
import com.chinaviponline.erp.corepower.api.pfl.log.reserve.ReserveCond;
import com.chinaviponline.erp.corepower.api.pfl.log.reserve.ReserveValueCond;

/**
 * <p>文件名称：LogClientService.java</p>
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

public interface LogClientService
{

    public static final String ROLE = LogClientService.class.getName();

    /**
     * @deprecated Method insert is deprecated
     */

    public abstract void insert(String s, String s1, String s2, String s3,
            int i, String s4);

    /**
     * @deprecated Method insert is deprecated
     */

    public abstract void insert(int i, String s, String s1);

    public abstract long insertCmdLog(String s, String s1, String s2,
            String s3, String s4, int i, String s5, String s6, String as[],
            String as1[], Timestamp timestamp, Timestamp timestamp1,
            Timestamp timestamp2, long l, int j, int k, String as2[]);

    public abstract void updateCmdLog(long l, String s, int i, String s1,
            Timestamp timestamp, String as[]);

    /**
     * @deprecated Method insertSysLog is deprecated
     */

    public abstract long insertSysLog(int i, String s, String s1, String s2,
            String s3, Timestamp timestamp, Timestamp timestamp1, int j,
            String as[]);

    public abstract long insertSysLog(int i, String s, String s1, String s2,
            String s3, Timestamp timestamp, Timestamp timestamp1, int j,
            long l, String as[]);

    /**
     * @deprecated Method updateSysLog is deprecated
     */

    public abstract void updateSysLog(long l, String s, Timestamp timestamp,
            String as[]);

    public abstract void updateSysLog(long l, int i, String s,
            Timestamp timestamp, String as[]);

    public abstract LogMessage[] queryLog(LogCond logcond, int i, int j)
            throws FIException;

    public abstract CmdLogMessage[] queryCmdLog(CmdLogCond cmdlogcond, int i,
            int j) throws FIException;

    public abstract SysLogMessage[] querySysLog(SysLogCond syslogcond, int i,
            int j) throws FIException;

    public abstract SecLogMessage[] querySecLog(SecLogCond seclogcond, int i,
            int j) throws FIException;

    public abstract long insertCmdLog(CmdLogMessage cmdlogmessage);

    public abstract long insertSysLog(SysLogMessage syslogmessage);

    public abstract void updateCmdLog(CmdLogMessage cmdlogmessage);

    public abstract void updateSysLog(SysLogMessage syslogmessage);

    public abstract ReserveValueCond getLogDefaultCond();

    public abstract ReserveCond getReserveCond(String s);

    public abstract LogRelateInfo queryCmdRelateLog(CmdLogCond cmdlogcond,
            int i, int j);

    public abstract LogRelateInfo querySysRelateLog(SysLogCond syslogcond,
            int i, int j) throws FIException;

    public abstract TablePanel getRelateLogPanel(LogCond logcond)
            throws FIException;

}
