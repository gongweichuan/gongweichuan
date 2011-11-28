/*
 * 创建日期 2006-10-25
 */
package com.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import com.coolsql.pub.component.MyTabbedPane;
import com.coolsql.sql.SQLResults;
import com.coolsql.sql.SQLStandardResultSetResults;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin
 *查询所有行数据的事件处理
 */
public class QueryAllRowsAction extends SQLPageProcessAction {

	private static final long serialVersionUID = 1L;
	/**
     * @param table
     */
    public QueryAllRowsAction() {
        super(ResultSetDataProcess.REFRESH);
        initMenuDefinitionById("QueryAllRowsAction");
    }
    public void executeAction(ActionEvent e)
    {
        ResultSetView view=ViewManage.getInstance().getResultView();
        MyTabbedPane pane=view.getResultTab();
        DataSetPanel dataPane=(DataSetPanel)pane.getSelectedComponent();
        if(!dataPane.isReady())  //如果内容面板的内容部件不为表控件,那么不做处理
        {
            return;
        }
        SQLResults result=dataPane.getSqlResult();
        if(result.isResultSet())
        {
            ((SQLStandardResultSetResults)result).setFullMode(true);
        }
        
        super.executeAction(e);
    }

}
