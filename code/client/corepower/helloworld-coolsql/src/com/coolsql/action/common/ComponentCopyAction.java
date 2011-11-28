/*
 * 创建日期 2006-9-15
 */
package com.coolsql.action.common;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import com.coolsql.pub.display.Copyable;

/**
 * @author liu_xlin
 * 组件复制的事件处理类
 */
public abstract class ComponentCopyAction extends AbstractAction implements Copyable{

    private JComponent com;
    public ComponentCopyAction(JComponent com)
    {
        this.com=com;
    }
    /**
     * @return 返回 com。
     */
    public JComponent getComponent() {
        return com;
    }
    /**
     * @param com 要设置的 com。
     */
    public void setComponent(JComponent com) {
        this.com = com;
    }
}
