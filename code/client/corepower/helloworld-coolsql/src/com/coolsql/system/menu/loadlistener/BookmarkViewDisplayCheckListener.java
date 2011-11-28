﻿/**
 * 
 */
package com.coolsql.system.menu.loadlistener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.system.menubuild.IMenuLoadListener;
import com.coolsql.view.View;
import com.coolsql.view.ViewManage;

/**
 * @author kenny liu
 * 
 * 2007-12-13 create
 */
public class BookmarkViewDisplayCheckListener implements IMenuLoadListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.system.menubuild.IMenuLoadListener#action(javax.swing.JMenuItem)
	 */
	public void action(JMenuItem item) {
		if (item instanceof JCheckBoxMenuItem) {
			boolean isDisplay = Setting.getInstance().getBoolProperty(
					PropertyConstant.PROPERTY_VIEW_BOOKMARK_ISDISPLAY, true);
			if(!isDisplay)
				ViewManage.getInstance().getBookmarkView().hidePanel(false);
			final JCheckBoxMenuItem m=(JCheckBoxMenuItem)item;
			m.setSelected(isDisplay);
			
			ViewManage.getInstance().getBookmarkView().addPropertyChangeListener(new PropertyChangeListener()
			{

				public void propertyChange(PropertyChangeEvent evt) {
					if(View.PROPERTY_HIDDEN.equals(evt.getPropertyName()))
					{
						Object ob=evt.getNewValue();
						if(ob instanceof Boolean)
							m.setSelected(((Boolean)evt.getNewValue()).booleanValue());
					}
				}
				
			}
			);
		}
	}
}