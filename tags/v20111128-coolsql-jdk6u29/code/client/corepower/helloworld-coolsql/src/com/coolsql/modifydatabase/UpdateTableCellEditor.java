/*
 * Created on 2007-1-16
 */
package com.coolsql.modifydatabase;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;

import com.coolsql.modifydatabase.model.BaseTableCell;
import com.coolsql.modifydatabase.model.NewValueTableCell;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.sql.model.Column;

/**
 * @author liu_xlin 更新行数据表控件的元素编辑渲染类
 */
public class UpdateTableCellEditor extends DefaultCellEditor {
    /**
     * 编辑组件的映射保存 key:编辑组件类型 value:编辑组件对象
     */
    private Map editor = null;

    /**
     * 当前编辑组件类型
     */
    private String currentEditorType;

    /**
     * 当前正在编辑的表格位置
     */
    private int currentRow; //行

    private int currentColumn; //列

    /**
     * 当前正在编辑表格的原值
     */
    private Object oldValue;

    public UpdateTableCellEditor() {
        super(new TextEditor());
        editor = new HashMap();
        currentEditorType = null;
        currentRow = -1;
        currentColumn = -1;
        oldValue = null;
    }

    /**
     * 重写父类方法
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        oldValue = value; //保存当前正在编辑表格的原值

        TableCell cellValue = null;
        if (value != null) {
            cellValue = (TableCell) value;
            ((BaseTableCell)cellValue).setIsNullValue(false);
            int columnIndex = table.getColumnModel().getColumnIndex(
                    UpdateRowTable.UpdateConstant.COLUMN_NAME);
            TableCell columnNameCell = (TableCell) table.getValueAt(row,
                    columnIndex);
            Column sqlField = (Column) columnNameCell.getValue();

            currentEditorType = cellValue.getEditorType();
            JComponent com = (JComponent) editor.get(currentEditorType);
            if (com != null) //如果已经创建了该编辑类型，将引用给当前编辑对象变量
            {
                editorComponent = com;
                if (editorComponent instanceof EditorLimiter) {  //重新设置编辑器的长度限制
                    ((EditorLimiter) editorComponent).setSize((int) sqlField
                            .getSize());
                    ((EditorLimiter) editorComponent).setDigits(sqlField
                            .getNumberOfFractionalDigits());
                }
            } else //创建该组件
            {
                editorComponent = EditorFactory.getEditorComponent(
                        currentEditorType, (int) sqlField.getSize(), sqlField
                                .getNumberOfFractionalDigits());
                editor.put(currentEditorType, editorComponent); //保存新创建的编辑组件
            }

        }
        //        delegate.setValue(cellValue == null ? "" : cellValue.toString());
        if (editorComponent instanceof DataHolder) {
            ((DataHolder) editorComponent).setValue(cellValue);
        }

        currentRow = row;
        currentColumn = column;
        return editorComponent;
    }

    /**
     * 重写元素编辑组件的编辑值的返回
     */
    public Object getCellEditorValue() {
        Object tmpValue = oldValue;

        if (editorComponent instanceof DataHolder) {
            DataHolder holder = (DataHolder) editorComponent;

            if (oldValue instanceof NewValueTableCell) {
                ((NewValueTableCell) tmpValue).setValue(holder.getHolderValue()
                        .toString());
            }
        }

        oldValue = null; //除去对原值的引用
        return tmpValue;
    }

    /**
     * 清除内存中的编辑组件对象
     *  
     */
    public void disposeEditor() {
        editor.clear();
        editor = null;
        editorComponent = null;
    }

    /**
     * @return Returns the currentColumn.
     */
    public int getCurrentColumn() {
        return currentColumn;
    }

    /**
     * @param currentColumn
     *            The currentColumn to set.
     */
    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }

    /**
     * @return Returns the currentRow.
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * @param currentRow
     *            The currentRow to set.
     */
    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }
}
