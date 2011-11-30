/*
 * Created on 2007-2-1
 */
package com.coolsql.view.resultset.action;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.coolsql.action.common.ActionCommand;
import com.coolsql.modifydatabase.UpdateRowDialog;
import com.coolsql.modifydatabase.insert.InsertDataDialog;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.sql.SQLResultSetResults;
import com.coolsql.sql.SQLResults;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 * 修改数据库数据，该执行类包含了插入新数据和更新行数据两种操作处理类型
 */
public class ModifyDataCommand implements ActionCommand {

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ModifyDataCommand.class);
	
    private int operatorType;

    private DataSetPanel pane;

    private DataSetTable table;

    public ModifyDataCommand(DataSetPanel pane, int operatorType) {
        this.operatorType = operatorType;
        this.pane = pane;
        if (pane != null) {
            JComponent com = pane.getContent();
            if (com instanceof DataSetTable) {
//                com=(JComponent)((JScrollPane)com).getViewport().getView();
                table = com instanceof DataSetTable ? (DataSetTable) com : null;
            }
            
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.action.common.ActionCommand#exectue()
     */
    public void exectue() {
        if (pane == null)
        {
        	if(operatorType == ModifyDataAction.INSERT)
        	{
        		InsertDataDialog dialog = new InsertDataDialog(GUIUtil.getMainFrame(),false,null);
                dialog.setVisible(true);
                return;
        	}
        }

        Entity[] entities=null;
        SQLResults tmp = pane.getSqlResult();
        if (tmp != null || tmp.isResultSet()) {
            SQLResultSetResults result = (SQLResultSetResults) tmp;
            entities = result.getEntities();
        }
        if (operatorType == ModifyDataAction.UPDATE) {
            if (entities != null && entities.length == 1) {
                UpdateRowDialog dialog = new UpdateRowDialog(entities[0],
                        getRowDataMap(table), false);
                dialog.setVisible(true);
            }
        } else if (operatorType == ModifyDataAction.INSERT) {
        	Entity entity=null;
        	if(entities == null ||entities.length==0)
        	{
        		JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(),stringMgr.getString("resultset.action.insertdata.noentity"),"warning",JOptionPane.WARNING_MESSAGE );
            }else if(entities.length==1)
            {
            	entity=entities[0];
            }else
            {
            	JOptionPane.showMessageDialog(GUIUtil.findLikelyOwnerWindow(), stringMgr.getString("resultset.action.insertdata.toomanyentities"),"warning",JOptionPane.WARNING_MESSAGE );
            }
        	InsertDataDialog dialog = new InsertDataDialog(GUIUtil.getMainFrame(),false,entity);
            dialog.setVisible(true);
        }
    }

    /**
     * 获取列名与行数据映射集合对象,该方法只适用于更新行数据。key:列名 value:表格对象值
     * 
     * @param table
     *            --数据源对象：表控件对象
     * @return --列名与行数据之间的映射对象，如果表控件被选择了多行或者没有选择行，返回一个空的映射对象
     */
    private Map<String,Object> getRowDataMap(DataSetTable table) {
        Map<String,Object> map = new HashMap<String,Object>();
        if (table.getSelectedRowCount() != 1)
            return map;

        int row = table.getSelectedRow();
        for (int i = 0; i < table.getColumnCount(); i++) {
            map.put(table.getColumnName(i).toUpperCase(), table.getValueAt(row,
                    i));
        }
        return map;
    }
}
