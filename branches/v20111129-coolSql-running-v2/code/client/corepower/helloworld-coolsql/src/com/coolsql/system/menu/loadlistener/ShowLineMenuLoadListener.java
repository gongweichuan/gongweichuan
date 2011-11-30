/**
 * 
 */
package com.coolsql.system.menu.loadlistener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.coolsql.system.Setting;
import com.coolsql.system.menubuild.IMenuLoadListener;

/**
 * 是否显示行号菜单项加载时的监听处理类
 * @author kenny liu
 *
 * 2007-12-7 create
 */
public class ShowLineMenuLoadListener implements IMenuLoadListener {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(JMenuItem item) {
		if(item instanceof JCheckBoxMenuItem)
		{
			JCheckBoxMenuItem i=(JCheckBoxMenuItem)item;
			i.setSelected(Setting.getInstance().getShowLineNumbers());
		}

	}

}
