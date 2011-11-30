/*
 * 创建日期 2006-12-25
 */
package com.coolsql.system.close;

import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.Task;

/**
 * @author liu_xlin 保存驱动程序类
 */
public class SaveDriverInfo implements Task {

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.closetask.savedriver.describe");
    }

    /**
     * 保存驱动信息
     */
    public void execute() {

        LoadJar.getInstance().writeClasspath();
        LoadJar.getInstance().saveExternalFiles();
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
