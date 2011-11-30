/*
 * Created on 2007-3-6
 */
package com.coolsql.modifydatabase.insert;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

import com.coolsql.pub.component.StringEditor;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.sql.model.Column;

/**
 * @author liu_xlin 编辑表控件的编辑器组件，重写表控件的缺省编辑器，对编辑器进行输入上的控制，如输入的最大长度，格式等
 */
public class EditeTableEditor extends DefaultCellEditor {

    /**
     * 当前正在编辑的值
     */
    private Object currentValue;
    public EditeTableEditor() {
        super(new TextEditor());
        currentValue=null;
    }
    
    /**
     * 设置编辑器的值
     * @param ob
     */
    public void setValue(Object ob) {
        if (ob instanceof EditeTableCell) {
            EditeTableCell cell = (EditeTableCell) ob;
            delegate.setValue(cell.getDisplayLabel());
        } else
            delegate.setValue(ob);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        currentValue=value;
//        control(table,column);
        setValue(value);       
        return editorComponent;
    }
    /**
     *根据列定义， 限制编辑器的最大长度
     * @param t  --编辑器所对应的表控件
     * @param columnIndex  --当前编辑所对应的列索引
     */
    protected void control(JTable t,int columnIndex)
    {
        if(t instanceof EditorTable)
        {
            EditorTable table=(EditorTable)t;
            Object tmp=table.getColumnModel().getColumn(columnIndex).getHeaderValue();
            if(tmp instanceof TableHeaderCell)
            {
                Object headerValue=((TableHeaderCell)tmp).getHeaderValue();
                if(headerValue instanceof Column)
                {
                    Column column=(Column)headerValue;
                    ((StringEditor)editorComponent).setMaxLength((int)column.getSize());
                }
            }
        }
    }
    /**
     * Forwards the message from the <code>CellEditor</code> to
     * 
     */
    public Object getCellEditorValue() {
        Object ob=super.getCellEditorValue();
        if(currentValue instanceof EditeTableCell)
        {
            EditeTableCell cell=(EditeTableCell)currentValue;
            cell.setValue(ob);
        }else
            currentValue=ob;
        return currentValue;
    }
}
