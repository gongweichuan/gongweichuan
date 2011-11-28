/*
 * 创建日期 2006-9-8
 */
package com.coolsql.view.bookmarkview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.coolsql.pub.component.BaseTree;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin 书签树的重写，主要为了让树的展开和折叠能够记录展开路径
 */
public class BookmarkTree extends BaseTree {

	private static final long serialVersionUID = 1662291272269068909L;

	/**
	 * determine whether or not row of bookmark tree displays border.
	 */
	private boolean isDisplayNodeBorder = Setting.getInstance().getBoolProperty(
			PropertyConstant.PROPERTY_VIEW_BOOKMARK_ISSHOWNODEBORDER, true);
	
	/**
	 * used to save the row of treenode  which mouse is on
	 */
	protected int nodeRow=-2; 
    public BookmarkTree(DefaultTreeNode node) {
        super(node);
        addMouseMotionListener(new MouseMoveListener());
        addMouseListener(new MouseExitListener());
        
        ToolTipManager.sharedInstance().registerComponent(this);  //注册信息提示
    }

    public String getToolTipText(MouseEvent e)
    {
        /**
         * 获取鼠标位置对应树控件节点
         */
        Point p = e.getPoint();
        int selRow = getRowForLocation(p.x, p.y);
        if(selRow<0)
            return null;
        TreePath path = getPathForRow(selRow);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        Object userOb=node.getUserObject();
        if(!(userOb instanceof Identifier))
           return null;
        
        Identifier id=(Identifier)userOb;
        if(id.getType()!=BookMarkPubInfo.NODE_RECENTSQL)  //如果该节点不为sql节点，不作提示
            return null;
        
        if(id.getContent().equals(id.getDisplayLabel()))  //如果sql节点信息完全展示，也不用提示
        	return null;
        
        return id.getContent();
    }
	/**
	 * @return the isDisplayNodeBorder
	 */
	public boolean isDisplayNodeBorder() {
		return this.isDisplayNodeBorder;
	}

	/**
	 * @param isDisplayNodeBorder the isDisplayNodeBorder to set
	 */
	public void setDisplayNodeBorder(boolean isDisplayNodeBorder) {
		this.isDisplayNodeBorder = isDisplayNodeBorder;
		repaint();
	}
    /**
     * 重写JTree的getToolTipLocation()方法
     * 将tip位置与组件对齐
     */
    public Point getToolTipLocation(MouseEvent e)
    {
        Point p = e.getPoint();
        int selRow = getRowForLocation(p.x, p.y);
        if(selRow<0)
            return null;
        TreePath path = getPathForRow(selRow);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        Object userOb=node.getUserObject();
        if(!(userOb instanceof Identifier))
           return null;
        Identifier id=(Identifier)userOb;
        if(id.getType()!=BookMarkPubInfo.NODE_RECENTSQL)  //如果该节点不为sql节点，不作提示
            return null;
       Rectangle rect=getPathBounds(path);
       return new Point((int)rect.getLocation().getX()+16,(int)rect.getLocation().getY()); //因为图标的原因，使位置向右偏移16个像素
    }

    private class MouseMoveListener extends MouseMotionAdapter
    {
    	@Override
    	public void mouseMoved(MouseEvent e)
    	{
    		processStat(e);
    	}
    	@Override
    	public void mouseDragged(MouseEvent e)
    	{
    		processStat(e);
    	}
    	private void processStat(MouseEvent e)
    	{
    		int current=getRowForLocation(e.getX(), e.getY());
    		if(current!=nodeRow)
    		{
    			nodeRow=current;
    			repaint();
    		}
    	}
    }
    private class MouseExitListener extends MouseAdapter
    {
    	@Override
    	public void mouseExited(MouseEvent e)
    	{
    		nodeRow=-1;
			repaint();
    	}
    }
}
