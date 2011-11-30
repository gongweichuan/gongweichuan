/*
 * 创建日期 2006-9-17
 */
package com.coolsql.search;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.display.CommonDataTable;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.CommonTableModel;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.SqlUtil;
import com.coolsql.sql.commonoperator.ColumnPropertyOperator;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.ColumnImpl;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.EntityFactory;
import com.coolsql.view.bookmarkview.model.ColumnNode;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *列查询结果显示窗口
 */
public class SearchOfColumnFrame extends SearchResultFrame {

	private static final long serialVersionUID = 1L;
	
	public SearchOfColumnFrame(JFrame con,Bookmark bookmark)
    {
        super(con,bookmark);
        this.setTitle("result of column querying");
        super.setPrompt(PublicResource.getSQLString("searchinfo.query.result.bookmark")+bookmark.getAliasName());
    }
    public SearchOfColumnFrame(JDialog con,Bookmark bookmark)
    {
        super(con,bookmark);
        this.setTitle("result of column querying");
        super.setPrompt(PublicResource.getSQLString("searchinfo.query.result.bookmark")+bookmark.getAliasName());
    }
    /* （非 Javadoc）
     * @see com.coolsql.view.bookmarkview.SearchResultFrame#initContent()
     */
    protected CommonDataTable initContent() {
        Vector<String> header=new Vector<String>();
        for(int i=0;i<5;i++)
        {
            header.add(PublicResource.getSQLString("searchinfo.query.entityresult.column"+i));
        }
        
        CommonDataTable table=new CommonDataTable(null,header,new int[]{0});
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); //自动调整宽度
        table.setEnableToolTip(false);//不允许提示
        return table ;
    }
    /**
     * 添加一行数据
     * @param row
     */
    public void addRow(Object[] row)
    {
        CommonTableModel model=(CommonTableModel)this.getTable().getModel();
        model.addRow(row);
    }
    /* （非 Javadoc）
     * @see com.coolsql.view.bookmarkview.SearchResultFrame#detailInfo()
     */
    public void detailInfo() throws UnifyException, SQLException {
        //初始化数据
        List<Object> list=new ArrayList<Object>();
        if(this.getBookmark()==null)  //书签不能为空
            throw new UnifyException("There is no bookmark information");
        list.add(this.getBookmark());
        
        int row=getTable().getSelectedRow();
        String catalog=SqlUtil.validateSqlParam((String)getTable().getValueAt(row,4));
        
        String schema=SqlUtil.validateSqlParam((String)getTable().getValueAt(row,3));  //模式
        list.add(catalog);
        list.add(schema);
        String entity=(String)getTable().getValueAt(row,1);
        list.add(entity);
        String column=getTable().getValueAt(row,0).toString();
        list.add(column);
        list.add(GUIUtil.getMainFrame());
        
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
    /* （非 Javadoc）
     * @see com.coolsql.querydbinfo.SearchResultFrame#getSelectObject()
     */
    protected Identifier getSelectObject() throws UnifyException {
        if(this.getBookmark()==null)  //书签不能为空
            throw new UnifyException("no bookmark information");
        
        int row=getTable().getSelectedRow();
        String catalog=SqlUtil.validateSqlParam((String)getTable().getValueAt(row,4));
        
        String schema=SqlUtil.validateSqlParam((String)getTable().getValueAt(row,3));  //模式
        String type=(String)getTable().getValueAt(row,2);  //实体类型
        String entityName=SqlUtil.validateSqlParam((String)getTable().getValueAt(row,1));  //实体名
        String column=SqlUtil.validateSqlParam(getTable().getValueAt(row,0).toString()); //列名
        
        Entity entity=EntityFactory.getInstance().create(this.getBookmark(),catalog,schema,entityName,type, "", false);
        Column columnData=new ColumnImpl(entity,column,"",-1,0,0,false,0,"",2,0,"",0);
        Identifier id=new ColumnNode(column,column,this.getBookmark(),columnData);
        return id;
    }
    /* (non-Javadoc)
     * @see com.coolsql.querydbinfo.SearchResultFrame#adjustGUI()
     */
    public void adjustGUI() {
        getTable().columnsToFitWidth(0);
        
    }
}
