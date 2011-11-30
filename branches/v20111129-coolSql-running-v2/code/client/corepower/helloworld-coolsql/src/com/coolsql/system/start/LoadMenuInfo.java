/*
 * Created on 2007-5-18
 */
package com.coolsql.system.start;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.Task;
import com.coolsql.system.menubuild.MenuBuilder;
import com.coolsql.system.menubuild.MenubarAvailabilityManage;

/**
 * @author liu_xlin
 *装载系统菜单
 */
public class LoadMenuInfo implements Task {

    /* (non-Javadoc)
     * @see com.coolsql.system.Task#getDescribe()
     */
    public String getDescribe() {
        
        return PublicResource.getString("system.launch.loadmenuinfo");
    }

    /* (non-Javadoc)
     * @see com.coolsql.system.Task#execute()
     */
    public void execute() {
        /**
         * 加载菜单
         */
        MenuBuilder.getInstance().loadSystemMenu();
        JMenuBar mb=MenuBuilder.getInstance().getMenuBar();
        GUIUtil.getMainFrame().setJMenuBar(mb);
        registerMenuBarForEnableCheck();

    }
    /**
     * 将菜单栏中的顶层菜单对象注册可用性校验
     *
     */
    private void registerMenuBarForEnableCheck()
    {
        JMenuBar bar=MenuBuilder.getInstance().getMenuBar();
        for(int i=0;i<bar.getMenuCount();i++)
        {
            JMenu menu=bar.getMenu(i);

            MenubarAvailabilityManage.getInstance().register(menu);
        }
    }
    /* (non-Javadoc)
     * @see com.coolsql.system.Task#getTaskLength()
     */
    public int getTaskLength() {
        return 1;
    }

}
