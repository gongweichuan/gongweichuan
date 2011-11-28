/*
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
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.View;


/**
 * @author liu_xlin
 *断开与数据库的连接
 */
public class DisconnectAction extends PublicAction {
    public DisconnectAction(View view)
    {
       super(view);	
    }
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	    /**
	     * 获取所选书签节点
	     */
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Object userOb=node.getUserObject();
		
		/**
		 * 如果所选节点为书签节点，断开连接
		 */
		if(userOb instanceof Bookmark)
		{
		    Operatable operator;
            try {
                operator = OperatorFactory.getOperator(com.coolsql.sql.commonoperator.DisconnectOperator.class);
                operator.operate(userOb);
                BookmarkManage.getInstance().nextConnectedBookmarkAsDefault();
            } catch(Exception e1)
            {
                JOptionPane.showMessageDialog(CommonFrame.getParentFrame(getComponent()),e1.getMessage(),"error",0); 
            }
            
		}
	}

}
