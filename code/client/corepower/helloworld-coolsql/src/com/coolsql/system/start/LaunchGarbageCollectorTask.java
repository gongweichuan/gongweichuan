/*
 * 创建日期 2006-12-27
 */
package com.coolsql.system.start;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.SystemGarbageCollectThread;
import com.coolsql.system.Task;

/**
 * @author liu_xlin
 *启动垃圾回收线程
 */
public class LaunchGarbageCollectorTask implements Task {

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        return PublicResource.getString("system.launch.loadgarbagecollector");
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        SystemGarbageCollectThread sgc = new SystemGarbageCollectThread(60000);
        sgc.start();
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
