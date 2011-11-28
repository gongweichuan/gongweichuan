/*
 * 创建日期 2006-12-26
 */
package com.coolsql.system.start;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.Task;
import com.coolsql.view.View;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *主窗口界面初始化
 */
public class MainFrameInitTask implements Task {

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        
        return PublicResource.getString("system.launch.initmainframe");
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
    	GUIUtil.getMainFrame();
    	for(View v:ViewManage.getInstance().getViews())
    	{
    		try {
				v.doAfterMainFrame();
			} catch (Exception e) {
				LogProxy.errorReport("processing view failed:"+e.getMessage(), e);
			}
    	}
    }

    /* （非 Javadoc）
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 2;
    }

}
