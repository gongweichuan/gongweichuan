/**
 * 
 */
package com.coolsql.system.menubuild.menuchecker;

import com.coolsql.view.ViewManage;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-2 create
 */
public class TextCaseMenuItemChecker extends BaseSqlEditorMenuItemChecker{

	/* (non-Javadoc)
	 * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
	 */
	public boolean check() {
		boolean superValue=super.check();
		if(!superValue)
			return false;
		String text=ViewManage.getInstance().getSqlEditor().getEditorPane().getSelectedText();
        if(text==null||text.equals(""))
            return false;
        else
            return true;
	}


}
