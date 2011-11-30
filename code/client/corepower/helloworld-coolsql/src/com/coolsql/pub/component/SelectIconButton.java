/*
 * Created on 2007-3-6
 */
package com.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author liu_xlin
 *具有选择项的
 */
public class SelectIconButton extends IconButton{

    private JPopupMenu popMenu;
    public SelectIconButton(Icon icon)
    {
        super(icon);
        popMenu=new JPopupMenu();
        addActionListener(new ClickAction());
    }
    /**
     * 添加选择菜单项
     * @param label  --选择菜单项的标签
     * @param icon  --选择菜单项的图标
     * @param action  --选择菜单项点击后触发的动作
     * @return  --返回菜单项对象
     */
    public JMenuItem addSelectItem(String label,Icon icon,ActionListener action)
    {
        JMenuItem item=new JMenuItem(label,icon);
        item.addActionListener(action);
        return popMenu.add(item);
        
    }
    /**
     * 
     * @author liu_xlin
     *按钮点击后，触发的事件。弹出选择项菜单
     */
    private class ClickAction implements ActionListener
    {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            int componentCount=popMenu.getComponentCount();
            if(componentCount<1)
            {
                return;
            }else if(componentCount==1)  //如果只有一个菜单项，直接执行唯一菜单项的处理
            {
                JMenuItem item=(JMenuItem)popMenu.getComponent(0);
                item.doClick();
            }else 
            {
                int x = (int) (SelectIconButton.this.getWidth() - popMenu
                        .getPreferredSize().getWidth());
                popMenu.show(SelectIconButton.this, x, SelectIconButton.this
                        .getHeight());
            }
        }
        
    }
}
