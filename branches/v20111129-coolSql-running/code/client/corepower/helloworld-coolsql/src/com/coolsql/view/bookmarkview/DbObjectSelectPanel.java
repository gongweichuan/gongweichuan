/**
 * 
 */
package com.coolsql.view.bookmarkview;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;

import com.coolsql.pub.component.BaseTree;
import com.coolsql.pub.component.selectabletree.SelectableTree;
import com.coolsql.view.bookmarkview.model.DefaultTreeNode;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-3-16 create
 */
public class DbObjectSelectPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private BaseTree tree;
	
	public DbObjectSelectPanel(DefaultTreeNode rootNode)
	{
		setLayout(new BorderLayout());
		tree=new SelectableTree(rootNode);
		tree.setShowsRootHandles(true);
		
		add(new JScrollPane(tree),BorderLayout.CENTER);
	}
	public void setRoot(DefaultTreeNode root)
	{
		DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
		model.setRoot(root);
	}
	public BaseTree getTree()
	{
		return tree;
	}
}
