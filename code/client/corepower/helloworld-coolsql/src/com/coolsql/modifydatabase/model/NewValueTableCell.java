/*
 * Created on 2007-1-12
 */
package com.coolsql.modifydatabase.model;

import javax.swing.JTable;

/**
 * @author liu_xlin
 *需要更新的列所对应的表格元素定义
 */
public class NewValueTableCell extends BaseTableCell {

    /**
     * 表格元素所属表控件对象
     */
    private JTable table =null;
    
    /**
     * 新值
     */
    private String newValue=null;
    
    /**
     * 是否需要修改
     */
    private boolean isNeedModify;
    
    private String editorType=null;
    public NewValueTableCell(JTable table,String newValue,String editorType)
    {
        this(table,newValue,editorType,false);
    }
    
    public NewValueTableCell(JTable table,String newValue,String editorType,boolean isNeedModify)
    {
        this.table=table;
        this.newValue=newValue;
        this.editorType=editorType;
        this.isNeedModify=isNeedModify;
        
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getValue()
     */
    public Object getValue() {
        return newValue;
    }

    public void setValue(Object ob)
    {
        newValue=ob.toString();
    }
    /* (non-Javadoc)
     * @see com.coolsql.modifydatabase.TableCell#getTable()
     */
    public JTable getTable() {
        return table;
    }
    public boolean isNeedModify() {
        return isNeedModify;
    }
    /**
     * 设置该表格对象所对应的列字段需要被更新或插入
     * @param isNeedModify
     */
    public void setNeedModify(boolean isNeedModify)
    {
        this.isNeedModify=isNeedModify;
    }
    public String getEditorType() {
        return editorType;
    }
    /**
     * 重写设置是否可编辑方法，如果新值元素可编辑，被视为需要更改的列
     */
    public void setEditable(boolean isEditable)
    {
        super.setEditable(isEditable);
        setNeedModify(isEditable);
    }
    /**
     * 该方法必须实现
     */
    public String toString()
    {
        return newValue;
    }
}
