/*
 * 创建日期 2006-6-27
 *
 */
package com.coolsql.pub.component;

import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalTheme;

import com.coolsql.pub.display.GUIUtil;


/**
 * @author liu_xlin
 * 
 * Custom text field component, add a text popup menu to component, including copy, paste, cut and select all.
 */
public class TextEditor extends JTextField {
	private static final long serialVersionUID = 1L;
	/**
     * 是否添加提示  true:添加提示信息   false:不添加提示信息
     */
    private boolean isTooltip=false;
    public TextEditor() {
        super();
        GUIUtil.installDefaultTextPopMenu(this);
        MetalTheme theme=SubTheme.getCurrentTheme();
        if (theme!=null&&theme.getClass() == MyMetalTheme.class) {
            this.setSelectionColor(DisplayPanel.getSelectionColor());
        }
    }

    public TextEditor(int length) {
        super(length);
        GUIUtil.installDefaultTextPopMenu(this);
        MetalTheme theme=SubTheme.getCurrentTheme();
        if (theme!=null&&theme.getClass() == MyMetalTheme.class) {
            this.setSelectionColor(DisplayPanel.getSelectionColor());
        }

    }
    public boolean isTooltip() {
        return isTooltip;
    }
    /**
     * 重写单行文本编辑器的方法
     *@param txt  --添加的文本信息
     */
    public void setText(String txt)
    {
        if(isTooltip && getToolTipText() == null)//如果允许提示,修改相应的提示信息
        {
            this.setToolTipText(txt);
        }
        super.setText(txt);
    }
    /**
     * 设置该文本框是否添加提示信息
     * @param isTooltip
     */
    public void setTooltipEnable(boolean isTooltip) {
        if(this.isTooltip==isTooltip)
            return;
        this.isTooltip = isTooltip;
        if(isTooltip)
        {
            this.setToolTipText(this.getText());
        }else
            this.setToolTipText(null);
    }
}
