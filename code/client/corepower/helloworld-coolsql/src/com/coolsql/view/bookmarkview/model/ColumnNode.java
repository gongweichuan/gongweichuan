/*
 * 创建日期 2006-9-10
 */
package com.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.commonoperator.ColumnPropertyOperator;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.INodeFilter;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *
 */
public class ColumnNode extends Identifier {
	private static final long serialVersionUID = 1L;
	/**
     * 所属列信息类
     */
    private Column column;
	public ColumnNode()
	{
        super();
        super.setType(BookMarkPubInfo.NODE_COLUMN);
        super.setHasChildren(false);
	}
	public ColumnNode(String content,String displayLabel,Bookmark bookmark)
	{
        this(content,displayLabel,bookmark,null);
	}   	
	public ColumnNode(String content,String displayLabel,Bookmark bookmark,Column column)
	{
        super(BookMarkPubInfo.NODE_COLUMN,content,displayLabel,bookmark);
        super.setHasChildren(false);
        setType(BookMarkPubInfo.NODE_COLUMN);
        this.column=column;
        if(column!=null&&column.isPrimaryKey())  //判断是否是主键
            setType(BookMarkPubInfo.NODE_KEYCOLUMN);
	}  
    /* （非 Javadoc）
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) throws SQLException, UnifyException {

    }
    /**
     * 重写ObjectHolder的方法
     */
    public Object getDataObject()
    {
        return column;
    }
    /**
     * 重写属性查看方法
     * @throws SQLException
     * @throws UnifyException
     */
    public void property() throws UnifyException, SQLException
    {
        //初始化数据
        List<Object> list=new ArrayList<Object>();
        list.add(this.getBookmark());
        Entity entity=column.getParentEntity();
        list.add(entity.getCatalog());
        list.add(entity.getSchema());
        list.add(entity.getName());
        list.add(column.getName());
        list.add(GUIUtil.findLikelyOwnerWindow());
        
        Operatable operator;
        try {
            operator = OperatorFactory.getOperator(ColumnPropertyOperator.class);
        } catch (ClassNotFoundException e) {
            LogProxy.errorReport(e);
            return;
        } catch (InstantiationException e) {
            LogProxy.internalError(e);
            return;
        } catch (IllegalAccessException e) {
            LogProxy.internalError(e);
            return;
        }
        operator.operate(list);
    }
}
