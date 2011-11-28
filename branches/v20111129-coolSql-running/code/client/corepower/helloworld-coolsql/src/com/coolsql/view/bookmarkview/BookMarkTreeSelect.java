/*
 * 创建日期 2006-7-2
 *
 */
package com.coolsql.view.bookmarkview;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.coolsql.view.BookmarkView;


/**
 * @author liu_xlin
 *书签视图中的书签树的选择监听，并作相应的处理
 */
public class BookMarkTreeSelect implements TreeSelectionListener {

	private BookmarkView view=null;
	public BookMarkTreeSelect(BookmarkView view)
	{
		this.view=view;
	}
	/* （非 Javadoc）
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) 
	{
		TreePath temp = e.getPath();

		DefaultMutableTreeNode n = (DefaultMutableTreeNode) temp.getLastPathComponent();
		DefaultTreeModel model = (DefaultTreeModel) (view.getConnectTree().getModel());

		if (!model.isLeaf(n)) {

		} else {
			temp = temp.getParentPath();
			n = (DefaultMutableTreeNode) temp.getLastPathComponent();
	
		}

	}

}
