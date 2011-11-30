/*
 * Created on 2007-1-30
 */
package com.coolsql.modifydatabase.insert.action;

import java.awt.Cursor;

import com.coolsql.action.common.ActionCommand;
import com.coolsql.modifydatabase.insert.EditorTable;
import com.coolsql.modifydatabase.insert.EditorTableModel;

/**
 * @author liu_xlin
 *事件触发后，由该接口进行相应逻辑处理
 */
public class BatchPasteCommand implements ActionCommand{

    private EditorTable table=null;
    public BatchPasteCommand(EditorTable table)
    {
        this.table=table;
    }
    /* (non-Javadoc)
     * @see com.coolsql.action.common.ActionCommand#exectue()
     */
    public void exectue() {
        if(table==null)
            return;
        try {
            table.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            EditorTableModel model = (EditorTableModel) table
                    .getModel();
            model.batchPaste(table.getSelectedRow(), table
                    .getSelectedColumn(),table);
        } finally {
            table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    
}
