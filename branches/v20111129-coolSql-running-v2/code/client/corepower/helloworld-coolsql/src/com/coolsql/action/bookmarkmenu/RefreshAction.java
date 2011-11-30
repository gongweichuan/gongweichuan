/*
 * 创建日期 2006-6-7
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JTree;

import com.coolsql.action.common.PublicAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.log.LogProxy;


/**
 * @author liu_xlin
 * 书签树节点的刷新事件处理
 */
public class RefreshAction extends PublicAction {
	private static final long serialVersionUID = 1L;
	
	public RefreshAction(BookmarkView view)
    {
       super(view);	
    }
	/* （非 Javadoc）
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	    JComponent com=(JComponent)e.getSource();
	    if(!com.isEnabled())   //如果不可用，直接返回
	        return ;
	    
	    JTree tree=((BookmarkView) getComponent()).getConnectTree();
		DefaultTreeNode node = (DefaultTreeNode) (tree.getLastSelectedPathComponent());
		Identifier userOb=(Identifier)(node.getUserObject());
		
		if(!isValidate(userOb))
		    return ;
		
		tree.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
            userOb.refresh(node,node.getNodeFilter());
        } catch (SQLException e1) {
            LogProxy.SQLErrorReport(e1);
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
        }finally
        {
            tree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

	}
	/**
	 * 校验节点刷新的有效性
	 * 1、如果为书签层节点之上的节点（更节点），不进行刷新
	 * 2、如果选中节点所属书签处于不正常状态，或者书签未进行连接，不进行刷新
	 * @param node  --节点数据对象
	 * @return  --true:有效  false:无效
	 */
	private boolean isValidate(Identifier node)
	{
	    if(node.getType()==0)
	        return false;
	        
	    Bookmark bookmark=node.getBookmark();
	    if(bookmark==null)
	        return false;
        if (bookmark.getConnectState() == 0) //如果书签节点状态正常
        {
            //设置刷新菜单是否可用
            if (bookmark.isConnected()) {
                return true;
            } else {
                return false;
            }
        }else
            return false;
	}
}
