/*
 * 创建日期 2007-1-8
 */
package com.coolsql.pub.component;

import java.awt.Component;

import com.jidesoft.swing.JidePopupMenu;

/**
 * @author liu_xlin
 *1、修改完善右键弹出菜单时，让激发组件获取焦点
 */
public class BasePopupMenu extends JidePopupMenu {

    public BasePopupMenu()
    {
        super();
    }
    public BasePopupMenu(String label)
    {
        super(label);
    }
    /**
     * 重写右键菜单弹出时，将出发组件获取焦点
     */
    public void show(Component invoker,int x,int y)
    {
        if(invoker!=null)
            invoker.requestFocusInWindow();
        super.show(invoker,x,y);
    }
}
