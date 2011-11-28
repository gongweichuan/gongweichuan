/*
 * 创建日期 2006-6-7
 *
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.coolsql.action.common.PublicAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.View;


/**
 * @author liu_xlin 对书签别名进行重命名
 */
public class ReNameAction extends PublicAction {
	public ReNameAction(View view) {
		super(view);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Object ob = node.getUserObject();
		if (ob instanceof Bookmark) {
			Bookmark bm = (Bookmark) ob;
			boolean isOk = false;
			while (!isOk) {
				String result = JOptionPane.showInputDialog(CommonFrame
						.getParentFrame(this.getComponent()), PublicResource
						.getString("aliasnamechangedialog.prompt"), bm
						.getAliasName());
				if (result != null && !result.trim().equals("")) {
					if (!bm.getAliasName().equals(result)) {
						if (!result.equals(result.trim())) //不能包含空格
						{
							JOptionPane
									.showMessageDialog(
											CommonFrame.getParentFrame(this
													.getComponent()),
											PublicResource
													.getString("aliasnamechangedialog.havespaces"),
											"warning", 2);
							continue;
						}
						if (BookmarkManage.getInstance().isExist(result)) //不允许别名重复
						{
							JOptionPane
									.showMessageDialog(
											CommonFrame.getParentFrame(this
													.getComponent()),
											PublicResource
													.getString("aliasnameinputdialog.aliasexist"),
											"warning", 2);
							continue;
						}
						bm.setAliasName(result);
						bm.setDisplayLabel(result);
						((BookmarkView) getComponent()).getConnectTree().updateUI();
						
						isOk=true;
					}else
					{
					    isOk=true;
					}
				} else {
                    if(result==null)  //撤销
                    	isOk=true;
                    else if(result.trim().equals("")) //为空串继续输入
                    {
						JOptionPane
						.showMessageDialog(
								CommonFrame.getParentFrame(this
										.getComponent()),
								PublicResource
										.getString("aliasnameinputdialog.noinput"),
								"warning", 2);
				        continue;                   	
                    }
				}
			}
		}

	}

}
