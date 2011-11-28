/*
 * Created on 2007-1-17
 */
package com.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.coolsql.action.common.ActionCommand;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin 更新结果集视图中某一查询结果中的一条记录。
 */
public class ModifyDataAction extends DataSetTableAction {
	private static final long serialVersionUID = 1L;
	/**
     * 操作类型的定义
     */
    public final static int INSERT=0;  //插入
    public final static int UPDATE=1;  //更新
    
    private int operatorType;
    public ModifyDataAction(int operatorType) {
        super();
        this.operatorType=operatorType;
    }

    /*
     * 1、获取结果集面板中的结果对象 2、检验结果集包含的实体是否有效（是否为null，是否只有一个实体）
     * 3、不允许选择多行，不允许不选择行
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        DataSetPanel pane = null;
        DataSetTable table = getCurrentTable(e);
//        DataSetTable table=ViewManage.getInstance().getResultView().getDisplayComponent().
        if (operatorType==UPDATE&&table.getSelectedRowCount() < 1) {
            JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
                    .getSQLString("rowupdate.table.noselect"), "warning", 1);
            return;
        }else if(operatorType==UPDATE&&table.getSelectedRowCount()>1)
        {
            JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
                    .getSQLString("rowupdate.table.selectmany"), "warning", 1);
            return;
        }
        pane = DataSetPanel.getDataPaneByContent(table);

        ActionCommand command=new ModifyDataCommand(pane,operatorType);
        try {
			command.exectue();
		} catch (Exception e1) {
			LogProxy.errorReport(e1);
		}
    }
   public DataSetTable getCurrentTable(ActionEvent e)
   {
	   JComponent c=ViewManage.getInstance().getResultView().getDisplayComponent();
	   if(c instanceof DataSetPanel)
	   {
		   return (DataSetTable)((DataSetPanel)c).getContent();
		   
	   }else
		   return null;
   }
}
