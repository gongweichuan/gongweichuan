/*
 * Created on 2007-1-31
 */
package com.coolsql.modifydatabase.insert;

import com.coolsql.sql.model.Column;

/**
 * @author liu_xlin
 * 编辑表控件的表头元素值类型的定义
 */
public class EditeTableHeaderCell implements TableHeaderCell {
    private boolean isSelected;  //对应的列是否有效,选种(true):有效  

    private Column column;   //列对象

    public EditeTableHeaderCell(boolean isSelected, Column column) {
        this.isSelected = isSelected;
        this.column = column;
    }

    public boolean getState() {

        return isSelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.modifydatabase.insert.TableHeaderCell#getHeaderValue()
     */
    public Object getHeaderValue() {
        return column;
    }

    /**
     * @param column
     *            The column to set.
     */
    public void setColumn(Column column) {
        this.column = column;
    }

    /**
     * @param isSelected
     *            The isSelected to set.
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public String toString() {
        return column.getName();
    }
    
}
