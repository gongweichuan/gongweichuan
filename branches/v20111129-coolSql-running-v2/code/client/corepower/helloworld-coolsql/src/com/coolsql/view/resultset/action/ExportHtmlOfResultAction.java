/*
 * 创建日期 2006-11-1
 */
package com.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.coolsql.action.common.ExportHtmlOfTableAction;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *针对结果集数据表控件进行html文件的导出
 */
public class ExportHtmlOfResultAction extends ExportHtmlOfTableAction {
    /**
     * @param table
     */
    public ExportHtmlOfResultAction() {
        super(null);
    }
    public void actionPerformed(ActionEvent e)
    {
//        ResultSetView view=ViewManage.getInstance().getResultView();
//        ITabbedPane pane=view.getResultTab();
//        JComponent com=((DataSetPanel)pane.getSelectedComponent()).getContent();
//        if(!(com instanceof JScrollPane))
//        {
//            return;
//        }
//        com=(JComponent)((JScrollPane)com).getViewport().getView();
//        if(!(com instanceof JTable))  //如果不为表控件,那么不做处理
//        {
//            return;
//        }
        JPopupMenu popMenu=GUIUtil.getTopMenu((JMenuItem)e.getSource());
        if(popMenu==null)
            return ;
        
        DataSetTable table=(DataSetTable)popMenu.getClientProperty(ResultSetView.DataTable);
        if(table==null)
        {
            LogProxy.errorMessage("can't get data table object!");
            return ;
        }
        this.setTable(table);
        
        super.actionPerformed(e);
    }
}
