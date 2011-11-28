﻿/*
 * 创建日期 2006-6-7
 *
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.coolsql.action.common.PublicAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.View;


/**
 * Delete the bookmark selected in the bookmark tree.
 * @author liu_xlin
 */
public class DeleteBookMarkAction extends PublicAction {
    public DeleteBookMarkAction(View view)
    {
       super(view);	
    }
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Object ob = node.getUserObject();
		if (ob instanceof Bookmark) {

			Bookmark bm = (Bookmark) ob;
			
			if(bm.getConnectState()!=0)
			{
			    JOptionPane.showMessageDialog(CommonFrame.getParentFrame(this
					.getComponent()),"please hold on!");
			    return ;
			}
			String aliasName = bm.getAliasName();
			int result=JOptionPane.showConfirmDialog(CommonFrame.getParentFrame(this
					.getComponent()), PublicResource
					.getString("bookmarkView.treenode.deleteconfirm")
					+ aliasName, "confirm!", JOptionPane.OK_CANCEL_OPTION);
			if(result==JOptionPane.OK_OPTION)
			{
				//If the bookmark is connected, must disconnect before deleting.
				if(bm.isConnected()) {
					 Operatable operator;
			            try {
			                operator = OperatorFactory.getOperator(com.coolsql.sql.commonoperator.DisconnectOperator.class);
			                operator.operate(bm);
			                BookmarkManage.getInstance().nextConnectedBookmarkAsDefault();
			            } catch(Exception e1)
			            {
			                JOptionPane.showMessageDialog(CommonFrame.getParentFrame(getComponent()),e1.getMessage(),"error",0); 
			            }
				}
				BookmarkManage.getInstance().removeBookmark(bm);
			}
		}
	}

}
