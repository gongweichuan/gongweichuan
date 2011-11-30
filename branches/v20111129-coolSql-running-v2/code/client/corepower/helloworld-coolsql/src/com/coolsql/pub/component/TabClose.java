/*
 * 创建日期 2006-12-15
 */
package com.coolsql.pub.component;

import java.awt.Component;

/**
 * @author liu_xlin
 *可关闭tab组件删除页组件的处理接口
 */
public interface TabClose {
    
    /**
     * 当可关闭tab组件在点击删除图标并且删除指定页后，执行该方法
     *@param  --被删除的页组件
     */
    public abstract void doOnClose(Component pageComponent);
}
