/*
 * 创建日期 2006-9-15
 */
package com.coolsql.action.common;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JTable;

/**
 * @author liu_xlin
 * 表控件的复制事件处理
 */
public class TableCopyAction extends ComponentCopyAction {

    /**
     * @param com
     */
    public TableCopyAction(JTable com) {
        super(com);
    }
    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        copy(this.getComponent());
    }
    /* （非 Javadoc）
     * @see com.coolsql.action.button.ComponentCopyAction#copy()
     */
    public void copy(JComponent com) {
        if(!(com instanceof JTable))
            return;
        //获取选中的行和列
        int[] cols=((JTable)com).getSelectedColumns();
        int[] rows=((JTable)com).getSelectedRows();
        
        if(cols==null||rows==null||rows.length<1||cols.length<1)
            return;
        
        StringBuffer buffer=new StringBuffer();
        for(int i=0;i<rows.length;i++)
        {
            for(int j=0;j<cols.length;j++)
            {
                Object tmpOb=((JTable)this.getComponent()).getValueAt(rows[i],cols[j]);
                buffer.append((tmpOb!=null?tmpOb.toString():"")+"\t");
            }
            buffer.deleteCharAt(buffer.length() - 1);  //删除tab
            buffer.append("\n");   
        }
        buffer.deleteCharAt(buffer.length() - 1); //删除换行
        StringSelection ss = new StringSelection(buffer.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,ss);
    }

}
