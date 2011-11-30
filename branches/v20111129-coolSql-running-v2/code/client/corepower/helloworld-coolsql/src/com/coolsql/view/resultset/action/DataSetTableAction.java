/*
 * 创建日期 2006-12-19
 */
package com.coolsql.view.resultset.action;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.coolsql.action.framework.CsAction;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.DataSetTable;

/**
 * @author liu_xlin
 *数据面板中的结果集数据展示表控件的编辑处理
 */
public abstract class DataSetTableAction extends CsAction {

    public DataSetTableAction()
    {
        super();
    }
    /**
     * 根据事件对象,获取当前被操作的表控件
     * @param e   --事件对象
     * @return  --被操作的表控件
     */
    protected DataSetTable getCurrentTable(ActionEvent e)
    {
        if(!(e.getSource() instanceof JMenuItem))
        	return null;
        
        JPopupMenu popMenu=GUIUtil.getTopMenu((JMenuItem)e.getSource());
        if(popMenu==null)
            return null;
        
        DataSetTable table=(DataSetTable)popMenu.getClientProperty(ResultSetView.DataTable);
        if(table==null)
        {
            LogProxy.errorMessage("can't get data table object!");
            return null;
        }
        
        return table;
    }
}
