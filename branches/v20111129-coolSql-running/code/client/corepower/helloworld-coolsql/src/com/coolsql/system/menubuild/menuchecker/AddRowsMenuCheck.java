/**
 * 
 */
package com.coolsql.system.menubuild.menuchecker;

import javax.swing.JComponent;

import com.coolsql.sql.SQLResultSetResults;
import com.coolsql.system.menubuild.MenuItemEnableCheck;
import com.coolsql.view.ViewManage;
import com.coolsql.view.resultset.DataSetPanel;

/**
 * 向数据表添加数据 的系统菜单的可用性校验
 * @author kenny liu
 *
 * 2007-11-11 create
 */
public class AddRowsMenuCheck implements MenuItemEnableCheck {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		JComponent com=ViewManage.getInstance().getResultView().getDisplayComponent();
		if(com instanceof DataSetPanel)
		{
			DataSetPanel pane=(DataSetPanel)com;
            //校验更新菜单
            if( pane.getSqlResult() != null
                    && pane.getSqlResult().isResultSet()
                    && ((SQLResultSetResults) pane.getSqlResult()).getEntities().length == 1)
            {
                return true;
            }else
                return false;
		}else
			return false;
	}

}
