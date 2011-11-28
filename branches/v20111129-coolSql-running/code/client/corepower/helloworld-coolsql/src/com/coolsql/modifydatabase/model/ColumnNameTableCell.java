/*
 * Created on 2007-1-12
 */
package com.coolsql.modifydatabase.model;

import javax.swing.JTable;

import com.coolsql.sql.model.Column;

/**
 * @author liu_xlin
 *该类的定义，对应了列名所在列的表格对象。该列表格不能编辑，只能作为显示内容。
 */
public class ColumnNameTableCell extends BaseTableCell {

    /**
     * 该表格对应的实体列对象
     */
    private Column columnValue=null;
    
    /**
     * 表格所属表控件对象
     */
    private JTable table=null;
    
    /**
     * 界面展示的值
     */
    private String displayValue=null;

    public ColumnNameTableCell(JTable table,Column column)
    {
        this(table,column,column.isPrimaryKey()?true:false);
    }
    public ColumnNameTableCell(JTable table,Column column,boolean isAsTerm)
    {
        super(false,isAsTerm,false);
        this.table=table;
        this.columnValue=column;

        if(column!=null)
        {
            displayValue=column.isPrimaryKey()?"<u>"+column.getName()+"</u>":column.getName();
        }
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getValue()
     */
    public Object getValue() {
        return columnValue;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getTable()
     */
    public JTable getTable() {
        return table;
    }
    public String toString()
    {
        return columnValue.getName();
    }
    public String getStyleString()
    {
        if(isAsTerm())
            return "<html><body><b>"+displayValue+"</i><body></html>";
        else
            return "<html><body>"+displayValue+"<body></html>";
    }
}
