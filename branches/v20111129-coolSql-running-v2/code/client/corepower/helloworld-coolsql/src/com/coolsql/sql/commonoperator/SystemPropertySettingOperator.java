/**
 * 
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;
import java.util.Collection;

import javax.swing.Icon;

import com.coolsql.gui.property.NodeKey;
import com.coolsql.gui.property.PropertyFrame;
import com.coolsql.gui.property.global.BookmarkViewPropertyPane;
import com.coolsql.gui.property.global.GeneralPropertyPane;
import com.coolsql.gui.property.global.LogViewPropertyPane;
import com.coolsql.gui.property.global.LookAndFeelPropertyPane;
import com.coolsql.gui.property.global.ResultsetViewPropertyPane;
import com.coolsql.gui.property.global.SqlEditorViewPropertyPane;
import com.coolsql.plugin.PluginInfo;
import com.coolsql.plugin.PluginManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;

/**
 * @author 刘孝林(kenny liu)
 * 
 * 2008-1-17 create
 */
public class SystemPropertySettingOperator extends BaseOperator {

	 /** Internationalized strings for this class. */
    private static final StringManager s_stringMgr =
        StringManagerFactory.getStringManager(SystemPropertySettingOperator.class);
	/*
	 * 1、the nodekey of default node
	 * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
	 */
	public void operate(Object arg) throws UnifyException, SQLException {
		if (arg!=null&&!(arg instanceof NodeKey)) {
			throw new UnifyException("operate object error! class:"
					+ arg.getClass());
		}

		NodeKey defaultKey=(NodeKey)arg;
		
		PropertyFrame pf = new PropertyFrame(GUIUtil.getMainFrame());
		pf.setTitle("global setting");
		if(defaultKey!=null)
			pf.setDefaultCard(defaultKey);
		/**
		 * General setting
		 */
		NodeKey generalkey=new NodeKey(s_stringMgr.getString("systemproperty.general"),(Icon)null,PropertyFrame.getRootData());
		generalkey.setDisplayName(s_stringMgr.getString("systemproperty.general.displaystr"));
		pf.addCard(generalkey,GeneralPropertyPane.class,null);
        pf.setDefaultCard(generalkey);
        //look and feel setting.
        NodeKey lafKey=new NodeKey(s_stringMgr.getString("systemproperty.lookandfeel"),(Icon)null,generalkey);
        lafKey.setDisplayName(s_stringMgr.getString("systemproperty.lookandfeel.displaystr"));
        pf.addCard(lafKey, LookAndFeelPropertyPane.class);
		/**
		 * 
		 */
        NodeKey bookmarkViewkey=new NodeKey(s_stringMgr.getString("systemproperty.bookmarkview"),(Icon)null,PropertyFrame.getRootData());
        bookmarkViewkey.setDisplayName(s_stringMgr.getString("systemproperty.bookmarkview.displaystr"));
		pf.addCard(bookmarkViewkey,BookmarkViewPropertyPane.class,null);
		
		NodeKey sqlEditorkey=new NodeKey(s_stringMgr.getString("systemproperty.sqleditor"),(Icon)null,PropertyFrame.getRootData());
		sqlEditorkey.setDisplayName(s_stringMgr.getString("systemproperty.sqleditor.displaystr"));
		pf.addCard(sqlEditorkey,SqlEditorViewPropertyPane.class,null);
		
		NodeKey ResultViewkey=new NodeKey(s_stringMgr.getString("systemproperty.resultview"),(Icon)null,PropertyFrame.getRootData());
		ResultViewkey.setDisplayName(s_stringMgr.getString("systemproperty.resultview.displaystr"));
		pf.addCard(ResultViewkey,ResultsetViewPropertyPane.class,null);
		
		NodeKey logViewkey=new NodeKey(s_stringMgr.getString("systemproperty.logview"),(Icon)null,PropertyFrame.getRootData());
		logViewkey.setDisplayName(s_stringMgr.getString("systemproperty.logview.displaystr"));
		pf.addCard(logViewkey,LogViewPropertyPane.class,null);
        /**
         * 加载插件属性设置
         */
		Collection<PluginInfo> plugins=PluginManage.getInstance().getPlugins();
		for(PluginInfo info:plugins)
		{
			info.getPlugin().buildSettingPanel(pf);
		}
		pf.setVisible(true);
	}

}
