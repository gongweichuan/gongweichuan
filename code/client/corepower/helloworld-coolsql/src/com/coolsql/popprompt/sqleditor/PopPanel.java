/*
 * Created on 2007-2-7
 */
package com.coolsql.popprompt.sqleditor;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * @author liu_xlin
 *组件弹出窗口面板类，所有弹出组件继承该面板类
 */
public class PopPanel extends JPanel {

    public PopPanel()
    {
        super();
    }
    public PopPanel(boolean isDoubleBuffer)
    {
        super(isDoubleBuffer);
    }
    public PopPanel(LayoutManager layout)
    {
        super(layout);
    }
    public PopPanel(LayoutManager layout,boolean isDoubleBuffer)
    {
        super(layout,isDoubleBuffer);
    }
}
