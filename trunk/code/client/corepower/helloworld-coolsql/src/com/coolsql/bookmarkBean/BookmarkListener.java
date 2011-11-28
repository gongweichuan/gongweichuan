/*
 * 创建日期 2006-7-1
 *
 */
package com.coolsql.bookmarkBean;

import java.util.EventListener;

/**
 * @author liu_xlin
 *书签改变监听器（书签被删除，书签被添加，书签被改变）
 */
public interface BookmarkListener extends EventListener {
	/**
	 * 添加书签的事件响应处理
	 * @param e
	 */
	public void bookmarkAdded(BookmarkEvent e);
	/**
	 * 删除书签的事件响应处理
	 * @param e
	 */
	public void bookmarkDeleted(BookmarkEvent e);
}
