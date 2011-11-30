/*
 * 创建日期 2006-6-7
 *
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;

import com.coolsql.action.common.PublicAction;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin
 *  树节点的复制Action
 */
public class CopyAction extends PublicAction {
	public CopyAction(BookmarkView view) {
		super(view);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Identifier userOb=(Identifier)(node.getUserObject());
		userOb.copy();
	}

	private ActionListener getDefaultCopyAction() {
		BookmarkView bmv = (BookmarkView) this.getComponent();
		InputMap inputMap = bmv.getConnectTree().getInputMap().getParent();
		KeyStroke key = KeyStroke.getKeyStroke("control C");
		Object ob=inputMap.get(key);
		ActionMap actionMap=bmv.getConnectTree().getActionMap().getParent();
		Action action=actionMap.get(ob);
		return action;
	}
}
