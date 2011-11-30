/*
 * Created on 2007-2-8
 */
package com.coolsql.view.sqleditor.pop;

import java.util.EventListener;

/**
 * @author liu_xlin
 *弹出窗口选中相应的调用该监听器的方法进行处理
 */
public interface SelectedListener extends EventListener{

    /**
     * 选择后的处理方法
     * @param value
     */
    public void selected(Object value);
}
