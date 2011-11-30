/*
 * 创建日期 2006-12-7
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.view.bookmarkview.RecentSQLDialog;

/**
 * @author liu_xlin
 *sql执行的历史记录
 */
public class SQLHistoryAction extends CsAction {

	private static final long serialVersionUID = 1L;
	public SQLHistoryAction()
    {
        super();
        initMenuDefinitionById("SQLHistoryAction");
    }
    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        if(RecentSQLDialog.getDisplayState())  //如果正在显示，直接返回
        {
            return;
        }
        RecentSQLDialog dialog=new RecentSQLDialog(GUIUtil.getMainFrame());
        dialog.setVisible(true);
    }

}
