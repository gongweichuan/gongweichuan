/*
 * Created on 2007-5-18
 */
package com.coolsql.system.menubuild;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 *菜单栏中各级菜单(除了顶层菜单)可用性的校验
 */
public class MenubarAvailabilityManage{

    static String visibleProperty="isVisibled";
    public final static String CHECKER="checker";
    private AvailabilityListener listener=null;
    private static MenubarAvailabilityManage manager=null;
    public static MenubarAvailabilityManage getInstance()
    {
        if(manager==null)
            manager=new MenubarAvailabilityManage();
        
        return manager;
    }
    private MenubarAvailabilityManage()
    {
        listener=new AvailabilityListener();
    }
    /**
     * 对菜单级别进行可用性校验的注册
     * @param menu  --需要注册的菜单对象
     * @return
     */
    public boolean register(JMenu menu)
    {
        JPopupMenu pop=menu.getPopupMenu();
        pop.addPopupMenuListener(listener);
        return true;
    }
    private class AvailabilityListener implements PopupMenuListener
    {

        /* (non-Javadoc)
         * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuCanceled(PopupMenuEvent e) {
            processState();
        }

        /* (non-Javadoc)
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            
        }

        /* (non-Javadoc)
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
         */
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
           
            JPopupMenu pop=(JPopupMenu)e.getSource();
            String str=(String)pop.getClientProperty(visibleProperty);
            if(str==null)  //第一次需要校验
            {
                pop.putClientProperty(visibleProperty,"true");
                
                //对该弹出菜单中的所有菜单项进行校验
                processEnableCheck(pop);
            }
        }
        
    }
    /**
     * 去除所有菜单的状态
     *
     */
    public void processState()
    {
        JMenuBar bar=MenuBuilder.getInstance().getMenuBar();
        for(int i=0;i<bar.getMenuCount();i++)
        {
            JPopupMenu menu=bar.getMenu(i).getPopupMenu();
            menu.putClientProperty(MenubarAvailabilityManage.visibleProperty,null);
        }
    }
    /**
     * 处理弹出式菜单中菜单/菜单项的可用性校验
     * @param pop
     */
    public void processEnableCheck(JPopupMenu pop)
    {
        
        for(int i=0;i<pop.getComponentCount();i++)
        {
            Component com=pop.getComponentAtIndex(i);
            if(com instanceof JMenuItem)
            {
                processEnableCheck((JMenuItem)com);               
            }

        }
    }
    /**
     * 对菜单按层次依次进行可用性的校验
     * @param item
     */
    private void processEnableCheck( JMenuItem item)
    {
        MenuItemEnableCheck checker=(MenuItemEnableCheck)item.getClientProperty(CHECKER);
        if(checker!=null)
        {
            GUIUtil.setComponentEnabled(checker.check(),item);
        }
        if(item instanceof JMenu)  //如果为菜单
        {
            for(int i=0;i<item.getComponentCount();i++)
            {
                Component com=item.getComponent(i);
                if(com instanceof JMenuItem)
                {
                    processEnableCheck((JMenuItem)com);
                }
            }
        }
    }
}
