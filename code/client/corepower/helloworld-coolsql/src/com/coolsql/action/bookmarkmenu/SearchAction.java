/*
 * 创建日期 2006-6-7
 *
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.coolsql.action.framework.CsAction;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.search.SearchInfoDialog;

/**
 * @author liu_xlin 查找数据库中的相关资源
 */
public class SearchAction extends CsAction {
	
	private static final long serialVersionUID = 1L;
	
    public SearchAction()
    {
    	super();
    	initMenuDefinitionById("SearchAction");
    }
    /*
     * （非 Javadoc）
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void executeAction(ActionEvent e) {
        if (!hasDatabase()) {
            JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
                    .getSQLString("searchinfo.nodatabase"), "warning", 2);
            return;
        }
        if (!SearchInfoDialog.checkInstanced())
            SearchInfoDialog.getInstance(GUIUtil.getMainFrame());
    }
    /**
     * 是否进行了数据源的配置
     * @return
     */
    public boolean hasDatabase() {
        if (BookmarkManage.getInstance().getBookmarkCount() > 0) {
            return true;
        } else
            return false;
    }
}
