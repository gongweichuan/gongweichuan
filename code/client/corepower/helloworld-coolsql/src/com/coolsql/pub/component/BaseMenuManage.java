/*
 * 创建日期 2006-7-2
 */
package com.coolsql.pub.component;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu.Separator;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.MenuCheckable;

/**
 * @author liu_xlin
 * 管理右键菜单的基类
 */
public abstract class BaseMenuManage {
    protected BasePopupMenu popMenu = null;
	private JComponent com=null;
    /**
     * 菜单的校验规则，放入该向量中
     */
    private Vector<MenuCheckable> checkSet=new Vector<MenuCheckable>();
    public BaseMenuManage(JComponent com)
    {
    	super();
    	this.com=com;
    }
    /**
     * 创建弹出菜单
     *
     */
    protected abstract void createPopMenu();
    /**
     * 对菜单项的可用性进行设置
     * @return
     */
    public abstract BasePopupMenu itemCheck();
    /**
     * 获取视图的右键菜单
     * @return
     */
    public abstract BasePopupMenu getPopMenu();
	/**
	 * @return 返回 view。
	 */
	public JComponent getComponent() {
		return com;
	}
	public void setComponent(JComponent com) {
	    this.com=com;
	}
	/**
	 * 创建新的菜单项
	 * 
	 * @param txt
	 * @param icon
	 * @param action
	 * @return
	 */
	protected JMenuItem createMenuItem(String txt, Icon icon, ActionListener action) {
		JMenuItem it = new JMenuItem(txt, icon);
		if (action != null)
			it.addActionListener(action);

		return it;
	}
	/**
	 * 绑定快捷键
	 * @param key  --快捷键
	 * @param action  --执行的动作
	 * @param isGlobal 是否是全局的使用该快捷键
	 */
	protected void bindKey(String key,Action action,boolean isGlobal)
	{
	    GUIUtil.bindShortKey(com,key,action,isGlobal);

	}
	/**
	 * 绑定快捷键,指定了具体的组件对象
	 * @param componnet  --需要被绑定快捷键的组件
	 * @param key   --快捷键
	 * @param action  --执行的动作
	 * @param isGlobal 是否是全局的使用该快捷键
	 */
	protected void bindKey(JComponent componnet,String key,Action action,boolean isGlobal)
	{
	    GUIUtil.bindShortKey(componnet,key,action,isGlobal);

	}
    /**
     * 添加菜单项
     * 
     * @param label
     *            标签
     * @param aciton
     *            菜单选中的事件处理
     * @param icon
     *            菜单项图标
     * @param isAddSeparator
     *            是否添加分隔线
     * @return 返回新创建的菜单项,如果弹出菜单没有初始化,将返回null值
     */
    public JMenuItem addMenuItem(String label, ActionListener action, Icon icon,
            boolean isAddSeparator) {
        if (popMenu == null)
            createPopMenu();

        JMenuItem tmpItem = null;
        int index = getAddPostion();
        if (index >= 0) {
            if (isAddSeparator) //添加分隔线
            {
                popMenu.insert(new Separator(), index);
                index++;
            }
            tmpItem = createMenuItem(label, icon, action);

            popMenu.insert(tmpItem, index);
        }
        return tmpItem;
    }
    /**
     * 添加菜单对象。如果想在该菜单下添加菜单项，必须先添加菜单，然后获取该菜单对象进行添加相应的菜单项。
     * @param label  --菜单的文本显示
     * @param isAddSeparator  --是否添加分隔线
     * @return  --被添加的菜单对象
     */
    public JMenu addMenu(String label,boolean isAddSeparator)
    {
        if (popMenu == null)
            createPopMenu();
        
        JMenu tmpMenu = null;
        int index = getAddPostion();
        if (index >= 0) {
            if (isAddSeparator) //添加分隔线
            {
                popMenu.insert(new Separator(), index);
                index++;
            }
            tmpMenu = new JMenu(label);

            popMenu.insert(tmpMenu, index);
        }
        return tmpMenu;
    }
    /**
     * 获取添加菜单的位置，该方法可以被覆盖
     * @return  --新菜单添加的位置，如果菜单未创建，返回-1
     */
    public int getAddPostion()
    {
        if(popMenu==null)
            return -1;
        
        return popMenu.getComponentCount();
    }
    /**
     * 添加菜单校验规则
     * @param checkable
     */
    public void addMenuCheck(MenuCheckable checkable)
    {
        if(checkable!=null)
        {
            checkSet.add(checkable);
        }
    }
    /**
     * 菜单项可用性处理
     *
     */
    protected void menuCheck()
    {
        for(int i=0;i<checkSet.size();i++)
        {
            MenuCheckable check=(MenuCheckable)checkSet.get(i);
            check.check(com);
        }
    }
}
