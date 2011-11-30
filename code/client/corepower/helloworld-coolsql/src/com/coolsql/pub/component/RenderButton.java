/*
 * 创建日期 2006-7-8
 *
 */
package com.coolsql.pub.component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.plaf.metal.MetalTheme;



/**
 * @author liu_xlin 自定义渲染按钮
 */
public class RenderButton extends JButton {
	public RenderButton() {
		super();
		setBackColor();
	}

	public RenderButton(String txt) {
		super(txt);
		setBackColor();
	}
	public RenderButton(String txt, Icon icon) {
		super(txt, icon);
		setBackColor();
	}
	private void setBackColor()
	{
        MetalTheme theme=SubTheme.getCurrentTheme();
        if (theme!=null&&theme.getClass() == MyMetalTheme.class) {
			this.setBackground(DisplayPanel.getThemeColor());
		}
	}
}
