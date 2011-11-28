/*
 * 创建日期 2006-9-8
 */
package com.coolsql.view.bookmarkview.model;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author liu_xlin 书签根节点类
 */
public class RootNode extends Identifier {

	private static final long serialVersionUID = 1L;

	public RootNode() {
		super();
	}
	public RootNode(String content, Bookmark bookmark) {
		super(BookMarkPubInfo.NODE_HEADER, content, bookmark);
	}
	public RootNode(String content, Bookmark bookmark, boolean isHasChild) {
		super(BookMarkPubInfo.NODE_HEADER, content, bookmark, isHasChild);
	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql
	 * .view.bookmarkview.model.ITreeNode)
	 */
	public void expand(DefaultTreeNode parent, INodeFilter filter) {

	}

}
