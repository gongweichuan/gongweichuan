/*
 * 创建日期 2006-10-13
 */
package com.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.coolsql.action.common.ComponentCopyAction;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *结果视图中表控件的数据复制操作
 */
public class DataSetTableCopyAction extends ComponentCopyAction {

	private static final long serialVersionUID = 1L;

	/**
     * @param com
     */
    public DataSetTableCopyAction() {
        super(null);
    }

    /* （非 Javadoc）
     * @see com.coolsql.data.display.Copyable#copy(javax.swing.JComponent)
     */
    public void copy(JComponent com) {
        if(!(com instanceof DataSetTable))
            return;
        
        //获取选中的行和列
        int[] cols=((DataSetTable)com).getSelectedColumns();
        int[] rows=((DataSetTable)com).getSelectedRows();
        
        if(cols==null||rows==null||rows.length<1||cols.length<1)
            return;
        
        StringBuffer buffer=new StringBuffer();
        for(int i=0;i<rows.length;i++)
        {
            for(int j=0;j<cols.length;j++)
            {
                Object tmpOb=null;
                tmpOb=((DataSetTable)com).getDisplayData(rows[i], cols[j]);
                buffer.append(tmpOb!=null?tmpOb.toString():"").append("\t");
                
            }
            buffer.deleteCharAt(buffer.length()-1);
            buffer.append("\n");
        }
        StringSelection ss = new StringSelection(buffer.toString().substring(0,buffer.length()-1));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,ss);

    }

    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
//        JPopupMenu popMenu=GUIUtil.getTopMenu((JMenuItem)e.getSource());
//        if(popMenu==null)
//            return ;
        
        Component com=ViewManage.getInstance().getResultView().getDisplayComponent();
        DataSetTable table=null;
        if(e.getSource() instanceof DataSetTable)
        {
        	table=(DataSetTable)e.getSource();
        }else
        {
			if(com instanceof DataSetPanel)
			{
				if(!(((DataSetPanel)com).getContent() instanceof DataSetTable))
					return ;
				table=(DataSetTable)((DataSetPanel)com).getContent();
			}
        }
//        DataSetTable table=(DataSetTable)popMenu.getClientProperty(ResultSetView.DataTable);
        if(table==null)
        {
            LogProxy.errorMessage("can't get data table object!");
            return ;
        }
        copy(table);
    }

}
