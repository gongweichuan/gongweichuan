/*
 * 创建日期 2006-9-8
 */
package com.coolsql.view.bookmarkview;

import java.awt.Cursor;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.coolsql.view.bookmarkview.model.DefaultTreeNode;

/**
 * @author liu_xlin
 *书签树的展开，
 */
public class TreeExpandListener implements TreeExpansionListener {
    public TreeExpandListener()
    {
    }
    /* （非 Javadoc）
     * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
     */
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath path = event.getPath();
        if(path == null || path.getLastPathComponent() == null)
            return;
        JTree tree = (JTree)event.getSource();
        tree.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        DefaultTreeNode node = (DefaultTreeNode)path.getLastPathComponent();
        node.expand();
       
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        treeModel.nodeStructureChanged(node);
        
        tree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /* （非 Javadoc）
     * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
     */
    public void treeCollapsed(TreeExpansionEvent event) {

    }

}
