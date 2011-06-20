/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

/**
 * <p>文件名称：SysLogCond.java</p>
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
public class SysLogCond extends LogCond
{

    private int logRank[];

    private String taskName[];

    private String logSource[];

    private int isFilter[];

    public SysLogCond()
    {
        logRank = null;
        taskName = null;
        logSource = null;
        isFilter = null;
    }

    public int[] getLogRank()
    {
        return logRank;
    }

    public void setLogRank(int logRank[])
    {
        this.logRank = logRank;
    }

    public String[] getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName[])
    {
        this.taskName = taskName;
    }

    public String[] getLogSource()
    {
        return logSource;
    }

    public void setLogSource(String logSource[])
    {
        this.logSource = logSource;
    }

    public void setIsFilter(int filter[])
    {
        isFilter = filter;
    }

    public int[] getIsFilter()
    {
        return isFilter;
    }

}
