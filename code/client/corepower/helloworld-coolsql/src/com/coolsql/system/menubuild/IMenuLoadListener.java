/**
 * 
 */
package com.coolsql.system.menubuild;

import java.util.EventListener;

import javax.swing.JMenuItem;

/**
 * 系统菜单加载时执行该接口定义的方法action();
 * @author kenny liu
 *
 * 2007-11-3 create
 */
public interface IMenuLoadListener extends EventListener{

	/**
	 * 
	 * @param item 已经被加载完成的菜单/菜单项对象
	 */
	public void action(JMenuItem item);
}
