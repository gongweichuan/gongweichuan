/*
 * Created on 2007-5-25
 */
package com.coolsql.system.menubuild.menuchecker;

import com.coolsql.system.menubuild.MenuItemEnableCheck;
import com.coolsql.view.ViewManage;

/**
 * @author liu_xlin
 *the checker of undo menu'availability
 */
public class UndoMenuChecker implements MenuItemEnableCheck {

    /* (non-Javadoc)
     * @see com.coolsql.system.menubuild.MenuItemEnableCheck#check()
     */
    public boolean check() {
    	if(!ViewManage.getInstance().getSqlEditor().getEditorPane().isFocusOwner())
    		return false;
        return ViewManage.getInstance().getSqlEditor().getEditorPane().getDocument().canUndo();
    }

}
