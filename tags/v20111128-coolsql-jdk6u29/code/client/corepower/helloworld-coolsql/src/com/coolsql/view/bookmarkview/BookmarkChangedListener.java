/*
 * 创建日期 2006-9-8
 */
package com.coolsql.view.bookmarkview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.tree.DefaultTreeModel;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin 对书签对象的属性变化进行监控
 */
public class BookmarkChangedListener implements PropertyChangeListener {

	public BookmarkChangedListener() {

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (name.equals("displayLabel")) //节点显示的字符串改变，相应的更新树的显示
		{
			BookmarkView view = ViewManage.getInstance().getBookmarkView();
			DefaultTreeModel model = (DefaultTreeModel) view.getConnectTree()
					.getModel();
			DefaultTreeNode node = BookmarkTreeUtil.getInstance().getBookMarkNodeByAlias(
					((Bookmark) evt.getSource()).getAliasName());
			model.nodeChanged(node);
		} else if (name.equals("connected")) //数据库的连接发生变化
		{
			boolean newValue = ((Boolean) evt.getNewValue()).booleanValue();
			if (!newValue) //连接断开
			{
				Bookmark bm = (Bookmark) evt.getSource();
				BookmarkView view = ViewManage.getInstance().getBookmarkView();
				DefaultTreeNode node = BookmarkTreeUtil.getInstance().getBookMarkNodeByAlias(
						((Bookmark) evt.getSource()).getAliasName());

				node.setExpanded(false);
				DefaultTreeModel model = (DefaultTreeModel) view
						.getConnectTree().getModel();

				if (node.getChildCount() > 0) //如果书签节点无子节点，那么直接返回
				{
					/**
					 * 获取最近执行过的sql
					 */
					DefaultTreeNode sqlGroup = (DefaultTreeNode) node.getChildAt(0); //获取sql组合节点
					String[] sql = new String[sqlGroup.getChildCount()];
					for (int i = 0; i < sql.length; i++) {
						Identifier ob = (Identifier) ((DefaultTreeNode) sqlGroup
								.getChildAt(i)).getUserObject();
						sql[i] = ob.getContent();
					}

					//    		    XMLParse saver=XMLParse.getParser();
					//    		    saver.saveBookmarkInfo(bm,sql); //保存节点信息

					BookmarkTreeUtil.removeChildrenOfNode(node); //删除所有的节点
				}
				bm.setHasChildren(false);
				model.nodeStructureChanged(node);

			} else //连接上数据库后，将书签节点设置为有子节点
			{
				Bookmark bm = (Bookmark) evt.getSource();
				bm.setHasChildren(true);
			}
		}
	}

}
