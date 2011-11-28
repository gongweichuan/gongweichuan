/*
 * 创建日期 2006-11-22
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.coolsql.action.common.PublicAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.model.EntityImpl;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.View;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin
 *查询选中实体包含的全部数据
 */
public class QueryAllDataOfTableAction extends PublicAction {

    public QueryAllDataOfTableAction(View view)
    {
        super(view);
    }
    /* 
     * 获取选中的节点,对节点对应的实体进行数据查询
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Identifier userOb=(Identifier)(node.getUserObject());
		if(userOb==null||(userOb.getType()!=BookMarkPubInfo.NODE_VIEW&&userOb.getType()!=BookMarkPubInfo.NODE_TABLE))
		{
		    return ;
		}
		EntityImpl entity=(EntityImpl)userOb.getDataObject(); //实体对象
		Bookmark bookmark=userOb.getBookmark();  //书签对象
		
        Operatable operator = null;
        try {
            operator = OperatorFactory
                    .getOperator(com.coolsql.sql.commonoperator.SQLResultProcessOperator.class);
        } catch (Exception e1) {
            LogProxy.internalError(e1);
            return;
        }
        
        String sql="";
        if(bookmark.getAdapter()!=null)
        {
            sql=bookmark.getAdapter().getTableQuery(entity.getQualifiedName());
        }else  //如果没有数据库适配器对象，那么提示后直接返回
        {
            LogProxy.errorMessage("no database adapter object,bookmark:"+bookmark.getAliasName());
            return ;
        }
        
        List argList = new ArrayList();
        argList.add(bookmark);
        argList.add(sql);
        argList.add(new Integer(ResultSetDataProcess.EXECUTE));//设置sql的执行处理类型：首次执行
        try {
            operator.operate(argList);
        } catch (UnifyException e2) {
            LogProxy.errorReport(e2);
        } catch (SQLException e2) {
            LogProxy.SQLErrorReport(e2);
        }

    }

}
