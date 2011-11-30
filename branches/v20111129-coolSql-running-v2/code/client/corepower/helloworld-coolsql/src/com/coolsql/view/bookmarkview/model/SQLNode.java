/*
 * 创建日期 2006-9-8
 */
package com.coolsql.view.bookmarkview.model;

import java.sql.SQLException;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.INodeFilter;
import com.coolsql.view.bookmarkview.RecentSQL;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *最近执行sql的节点类
 */
public class SQLNode extends Identifier {
	private static final long serialVersionUID = 1L;

	public static int maxDisplayLength=25;
    
    private RecentSQL data=null;
	public SQLNode()
	{
        this("",null,null);
	}
	public SQLNode(String content,Bookmark bookmark,RecentSQL data)
	{
        this(content,bookmark,data,false);
	}  
	public SQLNode(String content,Bookmark bookmark,RecentSQL data,boolean isHasChild)
	{
        super(BookMarkPubInfo.NODE_RECENTSQL,content,bookmark,isHasChild);
        if(content!=null)
        {
            if(content.length()>maxDisplayLength)
            {
                this.setDisplayLabel(content.substring(0,maxDisplayLength)+"...");
            }
        }
        this.data=data;
	}  	
    /* （非 Javadoc）
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) {
        //Do nothing
    }
    public void property()
    {
        Operatable operator = null;
        try {
            operator = OperatorFactory
                    .getOperator(com.coolsql.sql.commonoperator.SQLPropertyOperator.class);
        } catch (Exception e1) {
            LogProxy.internalError(e1);
            return;
        }
        try {
            operator.operate(data);
        } catch (UnifyException e2) {
            LogProxy.errorReport(e2);
        } catch (SQLException e2) {
            LogProxy.SQLErrorReport(e2);
        }
    }
    /**
     * 实现了接口ObjectHolder的方法
     */
    public Object getDataObject()
    {
        return null;
    }
    public boolean equals(Object ob)
    {
        if(ob==null)
            return false;
        if(!(ob instanceof SQLNode))
            return false;
            
        SQLNode tmp=(SQLNode)ob;
        if(super.equals(ob)&&data.equals(tmp.data))
            return true;
        else
            return false;
    }
}
