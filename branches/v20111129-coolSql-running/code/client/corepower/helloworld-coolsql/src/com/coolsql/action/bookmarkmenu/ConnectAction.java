/*
 * 创建日期 2006-6-7
 *
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import com.coolsql.action.common.PublicAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.sql.DBThread;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.View;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;


/**
 * @author liu_xlin
 *连接数据库的事件处理类
 */
public class ConnectAction extends PublicAction {
	private static final long serialVersionUID = 1L;
	public ConnectAction(View view)
    {
       super(view);	
    }
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		if(node==null)
			return;
		Object userOb=node.getUserObject();
		if(userOb instanceof Bookmark)
		{
		    Bookmark bm=(Bookmark)userOb;
		    if(bm.isConnected()||bm.getConnectState()==BookMarkPubInfo.BOOKMARKSTATE_CONNECTING||
		    		bm.getConnectState()==BookMarkPubInfo.BOOKMARKSTATE_DISCONNECTING)
		    	return;
		    DBThread dbthread=new DBThread(bm);
		    dbthread.start();
		}

	}

}
