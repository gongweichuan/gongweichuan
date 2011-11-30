/*
 * 创建日期 2006-9-19
 */
package com.coolsql.pub.display;

import javax.swing.JMenuItem;

/**
 * @author liu_xlin
 * 对菜单项进行校验
 */
public abstract class CheckModel implements MenuCheckable {

    private JMenuItem menu=null;
    public CheckModel(JMenuItem item)
    {
        this.menu=item;
    }
    /**
     * @return 返回 menu。
     */
    public JMenuItem getMenu() {
        return menu;
    }
    /**
     * @param menu 要设置的 menu。
     */
    public void setMenu(JMenuItem menu) {
        this.menu = menu;
    }
}
