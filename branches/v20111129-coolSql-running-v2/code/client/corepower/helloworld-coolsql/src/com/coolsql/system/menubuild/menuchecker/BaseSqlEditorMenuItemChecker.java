/**
 * 
 */
package com.coolsql.system.menubuild.menuchecker;

import com.coolsql.system.menubuild.MenuItemEnableCheck;
import com.coolsql.view.ViewManage;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-2 create
 */
public class BaseSqlEditorMenuItemChecker implements MenuItemEnableCheck {

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		if(!ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner())
    		return false;
		else
			return true;
	}

}
