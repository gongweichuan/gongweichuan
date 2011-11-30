/*
 * TableTypeProperty.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.coolsql.gui.property.database;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.gui.property.BasePropertyPane;
import com.coolsql.pub.display.CommonDataTable;
import com.coolsql.pub.display.TableScrollPane;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.view.log.LogProxy;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-6-8 create
 */
public class TableTypeProperty extends BasePropertyPane {
	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(SystemFunctionProperty.class);
	/**
	 * 
	 */
	public TableTypeProperty() {
		super();
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
	 */
	public boolean isNeedApply() {
		return false;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
	 */
	public void setData(Object ob) {
		Bookmark bookmark=(Bookmark)ob;
		if(bookmark.isConnected())  //连接状态，初始化数据类型面板。
        {
			panel.removeAll();
        	panel.setLayout(new BorderLayout());
        	
        	String[] header=new String[]{"Type Name"};
        	
        	try {
				String[] functions=bookmark.getDbInfoProvider().getDatabaseMetaData().getTableTypes();
				String[][] data=new String[functions.length][1];
				for(int i=0;i<functions.length;i++)
				{
					data[i][0]=functions[i];
				}
				CommonDataTable table=new CommonDataTable(data,header)
                {
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int row, int column)
                    {
                        return false;
                    }
                }
                ;
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                panel.add(new TableScrollPane(table),BorderLayout.CENTER);
                table.adjustPerfectWidth();
			} catch (Exception e) {
				LogProxy.errorReport(e);
			}
        	
        }else
        {
        	panel.removeAll();
        	panel.setLayout(new FlowLayout());
        	panel.add(new JLabel(stringMgr.getString("property.database.tabletype.noconnection")));
        }
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#apply()
	 */
	public void apply() {
		
	}
	/* (non-Javadoc)
	 * @see com.coolsql.gui.property.PropertyInterface#set()
	 */
	public boolean set() {
		return true;
	}
}
