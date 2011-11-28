/*
 * 创建日期 2006-7-2
 *
 */
package com.coolsql.gui.property;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author liu_xlin
 *属性树的选择监听，并作相应的处理
 */
public class PropertyTreeSelect implements TreeSelectionListener {

    private PropertyFrame main=null;
	public PropertyTreeSelect(PropertyFrame main)
	{
       this.main=main;
	}
	/* （非 Javadoc）
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) 
	{
		TreePath temp = e.getPath();

		DefaultMutableTreeNode n = (DefaultMutableTreeNode) temp.getLastPathComponent();
        
		main.showCard((NodeKey)n.getUserObject());
        
	}

}
