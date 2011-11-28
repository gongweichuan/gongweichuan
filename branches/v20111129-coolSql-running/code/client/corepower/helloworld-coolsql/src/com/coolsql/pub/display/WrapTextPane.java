/*
 * Created on 2007-1-8
 */
package com.coolsql.pub.display;

import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
 * @author liu_xlin
 *可换行编辑的文本编辑组件
 */
public class WrapTextPane extends JTextPane {
	private static final long serialVersionUID = 1L;
	/**
     * 是否自动换行
     */
	private boolean isWrap;
	
	public WrapTextPane()
	{
	    super();
	}
	public WrapTextPane(StyledDocument doc)
	{
	    super(doc);
	}
	/**
	 * 重写编辑窗口是否可滚动的校验方法
	 * 与方法setSize(Dimension)配合使用
	 */
    public boolean getScrollableTracksViewportWidth() {
        return isWrap ? super.getScrollableTracksViewportWidth()
                : (getSize().width < getParent().getSize().width - 1);
    }

    public void setSize(Dimension d) {
        if (!isWrap) {
            if (d.width < getParent().getSize().width) {
                d.width = getParent().getSize().width;
            }
            d.width += 1;
        }
        super.setSize(d);
    }
    /**
     * @return 返回 isWrap。
     */
    public boolean isWrap() {
        return isWrap;
    }
    /**
     * @param isWrap
     *            要设置的 isWrap。
     */
    public void setWrap(boolean isWrap) {
        this.isWrap = isWrap;
        this.updateUI();
    }
}
