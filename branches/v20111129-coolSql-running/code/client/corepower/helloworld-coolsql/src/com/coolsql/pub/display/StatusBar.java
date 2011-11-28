/*
 * Created on 2007-1-31
 */
package com.coolsql.pub.display;

import javax.swing.BorderFactory;

import com.coolsql.pub.component.TextEditor;

/**
 * @author liu_xlin
 *状态栏定义，提供相关信息的显示
 */
public class StatusBar extends TextEditor {
    public StatusBar()
    {
        super();
        initGUI();
    }
    public StatusBar(int size)
    {
        super(size);
        initGUI();
    }
    /**
     * 初始化界面
     *
     */
    protected void initGUI()
    {
        setEditable(false);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createBevelBorder(1), BorderFactory.createEmptyBorder(0, 4, 0,
						4)));
    }
}
