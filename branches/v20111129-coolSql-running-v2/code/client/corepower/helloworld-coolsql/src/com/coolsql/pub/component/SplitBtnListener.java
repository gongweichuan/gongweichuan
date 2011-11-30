/*
 * 创建日期 2006-9-25
 *
 */
package com.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.util.EventListener;

/**
 * @author liu_xlin
 *
 */
public interface SplitBtnListener extends EventListener {

	/**
	 * 该方法在组合按钮触发按钮按下事件时被调用
	 * @param e   --按钮事件对象
	 * @param data  --数据对象，该对象保存在菜单项中，通过方法<a>JMenuItem.getClientProperty(Object key)</a>
	 */
	public abstract void action(ActionEvent e,Object data);
}
