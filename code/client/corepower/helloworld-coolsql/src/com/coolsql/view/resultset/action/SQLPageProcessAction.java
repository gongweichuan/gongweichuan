/*
 * 创建日期 2006-10-17
 */
package com.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.pub.component.MyTabbedPane;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *完成数据面板结果集的数据处理
 */
public class SQLPageProcessAction extends CsAction {

	private static final long serialVersionUID = 1L;
	
	private static StringManager stringMgr=StringManagerFactory.getStringManager(SQLPageProcessAction.class);
	/**
     * 处理类型,与类com.coolsql.view.resultset.ResultSetDataProcess的处理类型定义一致
     */
    private int processType;
    private JComponent component;
    public SQLPageProcessAction(int processType)
    {
        this(processType,null);
    }
    public SQLPageProcessAction(int processType,JComponent com)
    {
        super();
        this.processType=processType;
        component=com;
    }
    /**处理数据
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @SuppressWarnings("unchecked")
    public void executeAction(ActionEvent e) {
        
        List list=new ArrayList();
        
        JComponent component=getProcessObject();
        if(component instanceof DataSetPanel)
        {
        	JComponent content=((DataSetPanel)component).getContent();
        	if(content instanceof DataSetTable)
        	{
        		DataSetTable dsTable=(DataSetTable)content;
        		if(dsTable.hasModified())
        		{
        			
        			boolean result=GUIUtil.getYesNo(GUIUtil.findLikelyOwnerWindow(),
        					stringMgr.getString("resultset.action.changedatamodel.prompt"));
        			if(!result)
        			{
        				return;
        			} else {
        				MyTabbedPane dataTab = ViewManage.getInstance()
						.getResultView().getResultTab();
        				int index = dataTab
						.indexOfComponent((DataSetPanel)component);
        				dataTab.setTabState(index, false);
        			}
        		}
        	}
        }
        
        list.add(component);
        list.add(new Integer(processType));
        
        Operatable operator=null;
        try {
            operator=OperatorFactory.getOperator(com.coolsql.sql.commonoperator.SQLProcessOperator.class);
        } catch (Exception e1) {
            LogProxy.internalError(e1);
        }
        
        try {
            operator.operate(list);
        } catch (UnifyException e2) {
            LogProxy.errorReport(e2);
        } catch (SQLException e2) {
            LogProxy.SQLErrorReport(e2);
        }
    }

    public int getProcessType() {
        return processType;
    }
    public void setProcessType(int processType) {
        this.processType = processType;
    }
    /**
     * 获取当前被处理的组件对象
     * @return  --返回（JComponent）对象
     */
    private JComponent getProcessObject()
    {
        if(component!=null)
            return component;
        else
        {
            ResultSetView view=ViewManage.getInstance().getResultView();
            MyTabbedPane pane=view.getResultTab();
            return (JComponent)pane.getSelectedComponent();
        }
    }
	/**
	 * @return the component
	 */
	public JComponent getComponent() {
		return this.component;
	}
}
