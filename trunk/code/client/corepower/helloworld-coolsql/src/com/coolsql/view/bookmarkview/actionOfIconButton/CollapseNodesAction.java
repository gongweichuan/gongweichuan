/*
 * 创建日期 2006-9-20
 */
package com.coolsql.view.bookmarkview.actionOfIconButton;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.coolsql.action.common.PublicAction;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.View;
import com.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *将书签视图的树节点全部合拢
 */
public class CollapseNodesAction extends PublicAction {
	private static final long serialVersionUID = 8315189701048073667L;
	public CollapseNodesAction(View view)
    {
        super(view);
    }
    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
         JComponent view=this.getComponent();
         if(!(view instanceof BookmarkView))
         {
             LogProxy.messageReport("the type of view is not correct,error type:"+view.getClass(),0);
             return ;
         }
         BookmarkTreeUtil.getInstance().collapseAllNodes();
    }

}
