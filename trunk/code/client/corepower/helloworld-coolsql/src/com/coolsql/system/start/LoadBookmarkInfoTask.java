/*
 * 创建日期 2006-12-26
 */
package com.coolsql.system.start;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.LoadData;
import com.coolsql.system.Task;

/**
 * @author liu_xlin
 *装载书签信息
 */
public class LoadBookmarkInfoTask implements Task {

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.launch.loadbookmarkinfo");
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        LoadData.getInstance().loadBookmarks();
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
