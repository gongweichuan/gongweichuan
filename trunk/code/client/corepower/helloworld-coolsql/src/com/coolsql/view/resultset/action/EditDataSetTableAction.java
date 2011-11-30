/*
 * 创建日期 2006-12-19
 */
package com.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *         结果集表控件右键刷新菜单项的监听处理，区别于结果集视图的刷新图标按钮。该处理逻辑需要判断显示数据的表控件是否显示在快捷窗口中
 */
public class EditDataSetTableAction extends DataSetTableAction {

	private static final long serialVersionUID = 1L;

	private int processType = 0;

    private JComponent component;
    /**
     * @param processType
     */
    public EditDataSetTableAction(int processType) {
        this(processType, null);
    }

    /**
     * @param processType
     */
    public EditDataSetTableAction(int processType, JComponent com) {
        super();
        this.component=com;
        this.processType = processType;
    }
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        DataSetPanel pane = null;
        DataSetTable table = getCurrentTable(e);
        if (table == null) {   //如果不是菜单项触发的事件
            if (component instanceof DataSetPanel) {
                pane = (DataSetPanel) component;
            } else
                return;
        } else {

            pane = DataSetPanel.getDataPaneByContent(table);
            if (pane == null)
                return;
        }
        List list = new ArrayList();
        list.add(pane);
        list.add(new Integer(processType));

        Operatable operator = null;
        try {
            operator = OperatorFactory
                    .getOperator(com.coolsql.sql.commonoperator.SQLProcessOperator.class);
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
}
