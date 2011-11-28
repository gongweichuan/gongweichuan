/*
 * 创建日期 2006-7-2
 *
 */
package com.coolsql.view.bookmarkview;

import java.io.Serializable;

import javax.swing.Icon;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.system.menubuild.IconResource;


/**
 * @author liu_xlin
 *涵盖了书签以及书签视图的所有信息
 */
public class BookMarkPubInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 节点类型定义
	 */
	public static final int NODE_HEADER=0;
	public static final int NODE_BOOKMARK=1;
	public static final int NODE_DATABASE=2;
	public static final int NODE_RECENTSQLGROUP=3;
	public static final int NODE_RECENTSQL=4;
	public static final int NODE_CATALOG=5;
	public static final int NODE_SCHEMA=6;
	public static final int NODE_VIEWS=7;
	public static final int NODE_VIEW=8;
	public static final int NODE_TABLES=9;
	public static final int NODE_TABLE=10;
	public static final int NODE_COLUMN=11;
	public static final int NODE_KEYCOLUMN=12;
	public static final int NODE_SYNONYM=13;
	public static final int NODE_SYSTEM_TABLES = 15;
	
	public static final int NODE_UNDEFINED = 14;//Undefined Type
	
	/**
	 * the state of bookmark
	 */
	public static final int BOOKMARKSTATE_NORMAL=0; 
	public static final int BOOKMARKSTATE_CONNECTING=1;
	public static final int BOOKMARKSTATE_DISCONNECTING=2;
	/**
	 * 节点图标定义
	 */
	private static Icon[] list=null;
	/**
	 * 获取节点图标列表
	 * @return 返回 list。
	 */
	public static Icon[] getIconList() {
		if(list==null)
			init();
		return list;
	}
	/**
	 * 根据数据类型获取对应的显示图标，目前只是定义了TABLE,VIEW类型的icon，其他类型暂时未定义
	 * @param tableType  --数据类型
	 * @return 
	 */
	public static Icon getTableTypeIcon(String tableType)
	{
		tableType=StringUtil.trim(tableType);
		if(tableType.equals("VIEW"))
		{
			return list[NODE_VIEW];
		}else if(tableType.equals("TABLE"))
		{
			return list[NODE_TABLE];
		}else
			return null;
	}
	private static void init()
	{
		list=new Icon[16];
		/**
		 * 根节点
		 */
		list[NODE_HEADER]=PublicResource.getIcon("bookmarkView.treenodeicon.header");
		/**
		 * 书签节点
		 */
		list[NODE_DATABASE]=PublicResource.getIcon("bookmarkView.treenodeicon.database");
	    /**
	     * 最近执行的sql节点上层图标
	     */		
		list[NODE_RECENTSQLGROUP]=PublicResource.getIcon("bookmarkView.treenodeicon.recentsql");
		/**
		 * 最近执行的sql语句节点
		 */
		list[NODE_RECENTSQL]=PublicResource.getIcon("bookmarkView.treenodeicon.sql");
		/**
		 * 分类节点
		 */
		list[NODE_CATALOG]=PublicResource.getIcon("bookmarkView.treenodeicon.catalog");
		
		/**
		 * 模式图标
		 */
		list[NODE_SCHEMA]=PublicResource.getIcon("bookmarkView.treenodeicon.schema");
		/**
		 * 视图组
		 */
		list[NODE_VIEWS]=PublicResource.getIcon("bookmarkView.treenodeicon.entitygroup");
		/**
		 * 视图节点
		 */
		list[NODE_VIEW]=PublicResource.getIcon("bookmarkView.treenodeicon.view");
		/**
		 * 表组
		 */
		list[NODE_TABLES]=list[NODE_VIEWS];
		/**
		 * 表节点
		 */
		list[NODE_TABLE]=PublicResource.getIcon("bookmarkView.treenodeicon.table");
		/**
		 * 列节点
		 */
		list[NODE_COLUMN]=PublicResource.getIcon("bookmarkView.treenodeicon.column");
		/**
		 * 主键列节点
		 */
		list[NODE_KEYCOLUMN]=PublicResource.getIcon("bookmarkView.treenodeicon.keycolumn");
		/**
		 * 处于未连接状态下的书签
		 */
		list[NODE_BOOKMARK]=PublicResource.getIcon("bookmarkView.treenodeicon.bookmark");
		
		/**
		 * synonym node
		 */
		list[NODE_SYNONYM]=PublicResource.getIcon("bookmarkView.treenodeicon.table");
		
		/**
		 * Return a blank icon if node is not defined.
		 */
		list[NODE_UNDEFINED]=IconResource.getBlankIcon();
		
		list[NODE_SYSTEM_TABLES] = list[NODE_TABLES];
	}
	/**
	 * get bookmark icon according to isConnected. 
	 * @param isConnected -- state of bookmark indicates whether bookmark is connected or not
	 */
	public static Icon getBookmarkIcon(boolean isConnected)
	{
		int index=isConnected?BookMarkPubInfo.NODE_DATABASE:BookMarkPubInfo.NODE_BOOKMARK;
		return getIconList()[index];
	}
	/**
	 * validate whether typeId is bookmark type.
	 * return true if it's bookmark type,otherwise return false.
	 */
	public static boolean isBookmarkNode(int typeId)
	{
		return typeId==BookMarkPubInfo.NODE_DATABASE||typeId==BookMarkPubInfo.NODE_BOOKMARK;
	}
}
