/*
 * 创建日期 2006-12-21
 */
package com.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.KeyStroke;

import com.coolsql.action.framework.CsAction;
import com.coolsql.sql.SQLResults;
import com.coolsql.sql.SQLStandardResultSetResults;
import com.coolsql.system.Setting;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin
 *在快速察看窗口中察看所有行的处理逻辑
 */
public class QueryAllRowsOfShortcutAction extends SQLPageProcessAction {
	
	private static final long serialVersionUID = 1L;
	
	public QueryAllRowsOfShortcutAction(DataSetPanel dataPane) {
        super(ResultSetDataProcess.REFRESH,dataPane);
        initMenuDefinitionById("QueryAllRowsAction");
        CsAction baseAction=Setting.getInstance().getShortcutManager().getActionByClass(QueryAllRowsAction.class);
        baseAction.addPropertyChangeListener(new PropertyChangeListener()
        {
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(Action.ACCELERATOR_KEY))
				{
					setAccelerator((KeyStroke)evt.getNewValue());
				}
			}
        	
        }
        );
    }
    public void executeAction(ActionEvent e)
    {
        if(!(getComponent() instanceof DataSetPanel))
            return;
        
        DataSetPanel dataPane=(DataSetPanel)getComponent();
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
