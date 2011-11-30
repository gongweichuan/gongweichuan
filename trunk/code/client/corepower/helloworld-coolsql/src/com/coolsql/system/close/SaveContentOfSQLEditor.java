/*
 * 创建日期 2006-12-25
 */
package com.coolsql.system.close;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.DoOnClosingSystem;
import com.coolsql.system.Task;

/**
 * @author liu_xlin
 *  
 */
public class SaveContentOfSQLEditor implements Task {

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {

        return PublicResource
                .getString("system.closetask.savecontentofsqleditor.describe");
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        DoOnClosingSystem.getInstance().saveContentOfSQLEditor();

    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
