/*
 * 创建日期 2006-12-26
 */
package com.coolsql.system.start;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.LoadData;
import com.coolsql.system.Task;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *装载最近执行的sql信息
 */
public class RecentSQLLoadTask implements Task {

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.launch.loadrecentsql");
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {       
        try {
            /**
             * 装载最近执行的sql语句
             */
            LoadData.getInstance().loadRecentSQL();
        } catch (Exception e) {
            LogProxy.errorReport(e);
        }
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
