/*
 * 创建日期 2006-12-18
 */
package com.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import com.coolsql.action.common.PublicAction;
import com.coolsql.pub.component.MyTabbedPane;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.ShortcutDialog;

/**
 * @author liu_xlin
 *弹出快速察看窗口，同时删除tab组件的当前页
 */
public class ShortcutAction extends PublicAction {

    public ShortcutAction()
    {
        super(null);
    }
    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        ResultSetView view=ViewManage.getInstance().getResultView();
        MyTabbedPane pane=view.getResultTab();
        Component com=pane.getSelectedComponent();

        if(com instanceof DataSetPanel)
        {
            DataSetPanel dataPane=(DataSetPanel)com;
            dataPane.setRemoving(false);  //将数据面板设置为非彻底删除
            pane.remove(dataPane);  //在结果集视图中删除数据页
            
            dataPane.setVisible(true);  //将被删除的数据面板设置为可视
            
            //弹出快速察看窗口
            ShortcutDialog shortcustDialog=new ShortcutDialog(dataPane);
            shortcustDialog.toCenter();
            shortcustDialog.setVisible(true);
        }
        
        
    }

}
