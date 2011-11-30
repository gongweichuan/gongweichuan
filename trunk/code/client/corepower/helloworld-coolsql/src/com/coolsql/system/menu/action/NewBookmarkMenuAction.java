/*
 * Created on 2007-5-28
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.action.framework.CsAction;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.view.ViewManage;
import com.coolsql.view.BookMarkwizard.BookMarkWizardFrame;

/**
 * @author liu_xlin
 *create a new bookmark relative with database when clicking the menuitem
 */
public class NewBookmarkMenuAction extends CsAction {

	private static final long serialVersionUID = 1L;

	public NewBookmarkMenuAction()
	{
		super();
		initMenuDefinitionById("NewBookmarkMenuAction");
	}
	/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void executeAction(ActionEvent e) {
        new BookMarkWizardFrame(GUIUtil.getMainFrame(), ViewManage.getInstance().getBookmarkView(), null)
        .setVisible(true);

    }

}
