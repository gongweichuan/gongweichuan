/*
 * 创建日期 2006-12-21
 */
package com.coolsql.pub.component;

import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToolBar;


/**
 * @author liu_xlin
 *自定义工具条类
 */
public class DefinableToolBar extends JToolBar {

    public DefinableToolBar()
    {
        super();
        initBar();
    }
    public DefinableToolBar(int orientation)
    {
        super(orientation);
        initBar();
    }
    public DefinableToolBar(String name)
    {
        super(name);
        initBar();
    }
    public DefinableToolBar(String name,int orientation)
    {
        super(name,orientation);
        initBar();
    }
    protected void initBar()
    {
        setBackground(DisplayPanel.getThemeColor());
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorderPainted(true);
        setFloatable(true);
        setRollover(true);
        setMargin(new Insets(0,0,0,0));
    }
    /**
     * 向工具条添加按钮，该按钮被点击后的逻辑处理由action定义
     * @param action  --点击动作的处理逻辑
     * @param btn  --被添加的按钮
     * @param tip  --按钮提示信息
     * @return --返回被添加的图标按钮
     */
    public IconButton addIconButton(Icon icon,Action action,String tip)
    {
        IconButton btn=new IconButton(icon);       
        btn.addActionListener(action);
        btn.setToolTipText(tip);
        super.add(btn);
        return btn;
    }
}
