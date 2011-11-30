/*
 * 创建日期 2006-12-27
 */
package com.coolsql.system.start;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.LoadData;
import com.coolsql.system.Task;

/**
 * @author liu_xlin
 *装载sql编辑视图的内容
 */
public class LoadContentOfSqlEditorTask implements Task {

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.launch.loadcontentofsqleditor");
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        LoadData.getInstance().loadSqlEditor();
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {

        return 1;
    }

}
