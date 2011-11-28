/**
 * 
 */
package com.coolsql.system.menubuild.menuchecker;

import com.coolsql.system.menubuild.MenuItemEnableCheck;
import com.coolsql.view.ViewManage;

/**
 * used to check whether sql editor has focus.
 * @author 刘孝林
 *
 * 2008-1-7 create
 */
public class SqlEditorIsFocusMenuCheck implements MenuItemEnableCheck {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		return ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner();
	}


}
