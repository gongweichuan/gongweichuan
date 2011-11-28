/**
 * Create date:2008-5-1
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;

import com.coolsql.pub.util.StringUtil;
import com.coolsql.system.favorite.FavoriteManage;
import com.coolsql.view.ViewManage;

/**
 * Add selected text in the editor panel to favorite list.
 * @author 刘孝林(kenny liu)
 *
 * 2008-5-1 create
 */
public class AddSelectedTextAsFavoriteAction extends BaseEditorSelectAction {

	private static final long serialVersionUID = 1L;
	
	public AddSelectedTextAsFavoriteAction()
	{
		super();
		initMenuDefinitionById("AddSelectedTextAsFavoriteAction");
	}
	@Override
	public void executeAction(ActionEvent e)
	{
		String content=ViewManage.getInstance().getSqlEditor().getEditorPane().getSelectedText();
		content=StringUtil.trim(content);
		FavoriteManage.getInstance().addSQL(content);
	}

}
