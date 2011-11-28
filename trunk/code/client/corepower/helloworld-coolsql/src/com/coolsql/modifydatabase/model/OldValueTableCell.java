/*
 * Created on 2007-1-12
 */
package com.coolsql.modifydatabase.model;

import javax.swing.JTable;

/**
 * @author liu_xlin
 *实体的列字段对应的原值所在列包含表格的定义
 */
public class OldValueTableCell extends BaseTableCell {

    /**
     * 字段原值对象,以文本展示
     */
    private String value=null;
    
    /**
     * 所属表控件
     */
    private JTable table=null;
    
    public OldValueTableCell(JTable table,String value)
    {
        this(table,value,false);
    }
    public OldValueTableCell(JTable table,String value,boolean isEditable)
    {
        super(isEditable,false,false);
        this.table=table;
        this.value=value;
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getValue()
     */
    public Object getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getTable()
     */
    public JTable getTable() {
        return table;
    }
    public String toString()
    {
        return value;
    }
}
