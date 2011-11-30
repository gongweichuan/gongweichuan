/*
 * 创建日期 2006-9-19
 */
package com.coolsql.pub.display;

import java.awt.Component;

/**
 * @author liu_xlin
 *
 */
public interface MenuCheckable {
  
    /**
     * 对于新添加的菜单项，在每次弹出时调用该接口进行校验，察看该菜单项是否可用
     * @param com  菜单管理器所属的组件对象。
     */
    public abstract void check(Component com);
}
