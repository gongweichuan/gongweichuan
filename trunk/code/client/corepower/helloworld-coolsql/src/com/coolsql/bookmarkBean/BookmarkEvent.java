/*
 * 创建日期 2006-7-1
 *
 */
package com.coolsql.bookmarkBean;

import java.util.EventObject;

/**
 * @author liu_xlin
 * 书签事件对象
 */
public class BookmarkEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	/**
	 * 添加新的书签
	 */
	public static final int BOOKMARK_ADD=0;
	/**
	 * 删除书签
	 */
	public static final int BOOKMARK_DELETE=1;
	/**
	 * 更新书签
	 */
	public static final int BOOKMARK_UPDATE=2;
	
	private Bookmark bookmark=null;
	public BookmarkEvent(BookmarkManage source,int type,Bookmark bookmark) {
		super(source);
		this.bookmark=bookmark;
	}

	/**
	 * @return 返回 bookmark。
	 */
	public Bookmark getBookmark() {
		return bookmark;
	}
}
