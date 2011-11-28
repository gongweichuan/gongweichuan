/*
 * 创建日期 2006-12-25
 */
package com.coolsql.system.close;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.xml.XMLException;
import com.coolsql.system.DoOnClosingSystem;
import com.coolsql.system.Task;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *保存最近执行的sql信息
 */
public class SaveRecentSQLsTask implements Task {

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        
        return PublicResource.getString("system.closetask.saverecentsqls.describe");
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        try {
            /**
             * 保存执行的sql信息
             */
            DoOnClosingSystem.getInstance().saveRecentSQL();
            DoOnClosingSystem.getInstance().deleteNoUseFile();
            
        } catch (XMLException e1) {
            LogProxy.errorReport(e1);
        } catch (Exception e) {
            LogProxy.outputErrorLog(e);
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
