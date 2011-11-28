/*
 * 创建日期 2006-6-6
 */
package com.coolsql.pub.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JSplitPane;

import com.coolsql.main.frame.MainFrame;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin
 * 
 * 自定义工作面板 面板标题为了显示好看，使用16*16的图片
 */
public class DisplayPanel extends BasePanel {

	public static final String PROPERTY_HIDDEN="hidden";
	private static final long serialVersionUID = 1L;
	/**
     * 该类三个属性，因为在父类中初始化，所以在声明时不要赋值
     */
    private IconButton resizeBtn;
    private Icon toMax;      //最大化的按钮
    private Icon toNormal;   //恢复的按钮
    
	protected void extraInit() {
		this.getTopPane().addMouseListener(new ResizeMouse());
		toMax=PublicResource.getIcon("view.iconbutton.resize.tomax.icon");
		toNormal=PublicResource.getIcon("view.iconbutton.resize.tonormal.icon");
		addResizeButton();
		
	}
    private void addResizeButton()
    {
        resizeBtn = new IconButton(null);
        resizeBtn.addActionListener(new ActionListener()
                {

                    public void actionPerformed(ActionEvent e) {
                        resizePanel();
                    }
            
                }
        );

        topRight.add(resizeBtn);
        topRight.validate();
        super.addTopPanelListener(new ResizeListener());
    }
	public void sizeToMax() {
		Container p = this.getParent();
		Container tmp = this;//记录splitpane的直接子组件
		while (!(p instanceof MainFrame)) {
			if (p != null && p instanceof JSplitPane) {

				JSplitPane split = (JSplitPane) p;
				int type = GUIUtil.isMaxState(split);

				if (type != -1) {
					if (type == JSplitPane.HORIZONTAL_SPLIT) //如果是横向分布
					{
						int location = (int) split.getSize().getWidth();
						if (tmp == split.getLeftComponent()) {
							split.setDividerLocation(location);
						} else if (tmp == split.getRightComponent()) {
							split.setDividerLocation(0);
						}
					} else //竖向分布
					{
						int location = (int) split.getSize().getHeight();
						if (tmp == split.getTopComponent()) {
							split.setDividerLocation(location);
						} else if (tmp == split.getBottomComponent()) {
							split.setDividerLocation(0);
						}
					}
					split.putClientProperty("isModify",new Boolean(true));
				}
			}
			tmp = p;
			p = p.getParent();
		}
	}

	public void sizeToNormal() {
	    JSplitPane first=getSplitContainer();
		Container p = this.getParent();
//		Container tmp = this;//记录splitpane的直接子组件
		while (!(p instanceof MainFrame)) {
			if (p != null && p instanceof JSplitPane) {

				JSplitPane split = (JSplitPane) p;
				int type=GUIUtil.isMaxState(split);
				if(type==-1)
				{
				   Boolean isModify=(Boolean)split.getClientProperty("isModify");
				   if(first==split||(isModify!=null&&isModify.booleanValue()))
				   {
				     split.setDividerLocation(split.getLastDividerLocation());
				     isModify=new Boolean(false);
				     split.putClientProperty("isModify",isModify); 
				   }
				}
			}
//			tmp = p;
			p = p.getParent();
		}
	}
	/**
	 * 隐藏该视图
	 *
	 */
	public void hidePanel(boolean isFire)
	{
		JSplitPane split=getSplitContainer();
		if(split!=null)
		{
			boolean oldValue=isVisible();
			setVisible(false);
			if(isFire)
			{
				firePropertyChange(PROPERTY_HIDDEN,oldValue , false);
			}
		}
	}
    /**
     * 校验该面板所在splitpane是否是最大
     * @param con
     * @return  如果是最大，返回其splitpane，否则返回null
     */
	public JSplitPane isMax(Container con) {
		Container p;
		for (p = con.getParent(); p != null && !(p instanceof JSplitPane); p = p
				.getParent())
			;
		JSplitPane split = (JSplitPane) p;
		if(GUIUtil.isMaxSplitToParent(split))
			return split;
		else 
			return null;
	}
	public JSplitPane getSplitContainer()
	{
		return GUIUtil.getSplitContainer(this);
	}
	/**
	 * 调整该面板
	 *
	 */
    private void resizePanel()
    {
		JSplitPane pane=isMax(this);
		if (pane==null)
			sizeToMax();
		else
		{
			if(!pane.isEnabled())
				return;
			sizeToNormal();
		}
    }
	private class ResizeMouse extends MouseAdapter {
		public ResizeMouse() {

		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
			    resizePanel();
			}
		}
	}
	/**
	 * 当该面板发生调整变化时，更新调整按钮的状态
	 *
	 */
	public void updateResizeState()
	{
        JSplitPane pane=isMax(this);
		if (pane==null)  //正常状态
		{
			resizeBtn.setIcon(toMax);
			resizeBtn.setToolTipText(PublicResource.getString("view.iconbutton.resize.tomax.tooltip"));
		}
		else      //已经最大化
		{
			resizeBtn.setIcon(toNormal);
			resizeBtn.setToolTipText(PublicResource.getString("view.iconbutton.resize.tonormal.tooltip"));
		}
	}
	/**
	 * 
	 * @author liu_xlin
	 *对视图标题栏的调整进行监听，以便于做出正确的调整状态
	 */
	private class ResizeListener implements ComponentListener
	{

        /* （非 Javadoc）
         * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
         */
        public void componentResized(ComponentEvent e) {
            updateResizeState();
            
        }

        /* （非 Javadoc）
         * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
         */
        public void componentMoved(ComponentEvent e) {
        }

        /* （非 Javadoc）
         * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
         */
        public void componentShown(ComponentEvent e) {
            updateResizeState();
        }

        /* （非 Javadoc）
         * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
         */
        public void componentHidden(ComponentEvent e) {
            
        }
	    
	}
}
