/**
 * 
 */
package com.coolsql.pub.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideSplitButton;

/**
 * @author kenny liu
 *
 * 2007-11-17 create
 */
public class SplitButton extends JideSplitButton {

	private static final long serialVersionUID = 1L;

	private static final String DATAKEY = "dataKey";

    /**
     * 下拉选择菜单
     */
    private JPopupMenu popMenu=null;
    
    /**
     * 该属性是该控件的核心数据,每次选中一个菜单项后,更新该值
     */
    private Object selectedData = null;
    
    /**
     * 当前控件的默认选择菜单项，如果点击左边按钮，将会执行该菜单项所对应的操作。
     */
    private JMenuItem defaultItem = null;
    
    private MenuItemClickListener menuListener = null;
    
    /**
     * 菜单的分隔线，用于将defautItem与菜单项分隔开。
     */
    private JSeparator separator = null;
    
    /**
     * 左边按钮的事件监听集合
     */
    private Vector actions = null;
    public SplitButton(Icon icon)
    {
    	super();
    	setAction(getLeftButtonAction());
    	setIcon(icon);
    	init();
    }
    public SplitButton(String label, Icon icon)
    {
    	super();
    	setAction(getLeftButtonAction());
    	setText(label);
    	setIcon(icon);
    	init();
    }
    /**
     * 左边按钮点击动作的监听处理。
     * @return
     */
    protected Action getLeftButtonAction()
    {
    	return new AbstractAction()
    	{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				runAction(e);
				
			}
    		
    	};
    }
    protected void init() {
		actions = new Vector();
		separator = new JSeparator();
		popMenu=getPopupMenu();
		defaultItem = new JMenuItem();
		menuListener = new MenuItemClickListener();
		defaultItem.addActionListener(menuListener);
		setBackgroundOfState(ThemePainter.STATE_ROLLOVER,BasePanel.getThemeColor());
//		setBackgroundOfState(ThemePainter.STATE_PRESSED,BasePanel.getThemeColor());
		
		setPopupMenuCustomizer(new JideMenu.PopupMenuCustomizer()
		{

			public void customize(JPopupMenu menu) {
			}			
		}
		);
	}
    public void setPopupMenuVisible(boolean b)
    {
    	super.setPopupMenuVisible(b);
    }
    /**
	 * 选择某一菜单项后，更新缺省菜单数据项。
	 * 
	 * @param selectedItem
	 *            选中的菜单项对象
	 */
    private void updateDefaultItemData(JMenuItem selectedItem) {
        JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
        if (tmpItem == defaultItem) //已经添加了缺省菜单项
        {
            if (selectedItem.getClientProperty(DATAKEY) != defaultItem
                    .getClientProperty(DATAKEY)) //如果缺省数据与第一个菜单项(除去缺省菜单和分隔线)对应的数据不相等,更新缺省菜单项数据
            {
                defaultItem.setText(selectedItem.getText());
                defaultItem.setIcon(selectedItem.getIcon());
                Object ob = selectedItem.getClientProperty(DATAKEY);
                defaultItem.putClientProperty(DATAKEY, ob);
                
                Object oldObject=this.selectedData;
                selectedData = ob;
                this.firePropertyChange("selectedData",oldObject,selectedData);
            }
        }
    }
    /**
     * 执行按钮事件处理
     * 
     * @param e
     */
    protected void runAction(ActionEvent e) {
        if (selectedData == null && popMenu.getComponentCount() == 1)//如果弹出菜单只有一项,那么默认选取该项数据
        {
            Object oldObject=this.selectedData;
            selectedData = ((JMenuItem) popMenu.getComponent(0))
                    .getClientProperty(DATAKEY);//取第一项
            this.firePropertyChange("selectedData",oldObject,selectedData);
        }
        for (int i = 0; i < actions.size(); i++) {
            SplitBtnListener action = (SplitBtnListener) actions.elementAt(i);
            action.action(e, selectedData);
        }
    }
    /**
     * 添加左边按钮事件处理
     * 
     * @param action
     */
    public void addAction(SplitBtnListener action) {
        actions.add(action);
    }
    /**
     * 添加弹出菜单项
     * 
     * @param item
     * @throws UnifyException
     */
    public synchronized void addDataItem(String label, Icon icon, Object data)
            throws UnifyException {
        if (data == null)
            throw new IllegalArgumentException("argument:data is null!");
        if (checkExist(data)) {
            throw new UnifyException(PublicResource
                    .getString("sqlEditorView.combobutton.datarepeat"));
        }
        JMenuItem item = new JMenuItem(label, icon);
        item.putClientProperty(DATAKEY, data);
        item.addActionListener(menuListener);
        popMenu.add(item);

        addDefaultItem();
    }
    /**
     * 根据数据对象来删除菜单项
     * 
     * @param ob --已经加入下拉菜单项的数据对象。
     */
    public synchronized void deleteDataItem(Object ob) {
        for (int i = 0; i < popMenu.getComponentCount(); i++) {
            Component com = popMenu.getComponent(i);
            if (com instanceof JMenuItem && com != defaultItem) { //将分隔线和缺省菜单除外
                JMenuItem tmpItem = (JMenuItem) com;
                if (tmpItem.getClientProperty(DATAKEY) == ob) {
                    tmpItem.removeActionListener(menuListener);
                    popMenu.remove(tmpItem);
                    if (selectedData == ob)
                        selectedData = null;

                    Component firstComponent = popMenu.getComponentCount() > 0 ? popMenu
                            .getComponent(0)
                            : null;
                    if (firstComponent != null) {
                        if (firstComponent == defaultItem) { //如果缺省菜单项已经添加

                            if (ob == defaultItem.getClientProperty(DATAKEY)) //如果与缺省菜单项一致，则重新更新缺省菜单项信息
                            {
                                updateDefaultItemData((JMenuItem) popMenu
                                        .getComponent(2)); //更新为第一个菜单的信息(除去缺省菜单和分隔线)
                            }
                            if (popMenu.getComponentCount() == 3) //只剩下一个有效菜单
                            {
                                popMenu.remove(defaultItem); //去除缺省菜单
                                popMenu.remove(separator); //去除分隔线
                                
                                Object oldObject=this.selectedData;
                                selectedData = ((JMenuItem) popMenu
                                        .getComponent(0))
                                        .getClientProperty(DATAKEY);//设置为唯一菜单项的值
                                this.firePropertyChange("selectedData",oldObject,selectedData);
                            }
                        }
                    } else //没有菜单项
                    {
                        selectedData = null; //清除对数据的引用
                    }
                    return;
                }

            }
        }
    }
    /**
     * 返回当前组合按钮默认选择项数据
     * 
     * @return
     */
    public Object getSelectData() {
        if (selectedData == null && popMenu.getComponentCount() == 1)//如果弹出菜单只有一项,那么默认选取该项数据
        {
            Object oldObject=this.selectedData;
            selectedData = ((JMenuItem) popMenu.getComponent(0))
                    .getClientProperty(DATAKEY);//取第一项
            this.firePropertyChange("selectedData",oldObject,selectedData);
        }
        return selectedData;
    }
    /**
     * 校验对应的数据对象是否已经存在
     * 
     * @param data
     * @return
     */
    public boolean checkExist(Object data) {
        if (getItemByData(data) != null)
            return true;
        else
            return false;
    }
    /**
     * 通过数据对象来查找菜单项
     * 
     * @param data
     * @return
     */
    private JMenuItem getItemByData(Object data) {
        for (int i = 0; i < popMenu.getComponentCount(); i++) {
            Component com = popMenu.getComponent(i);
            if (com instanceof JMenuItem && com != defaultItem) { //将分隔线和缺省菜单除外
                JMenuItem tmpItem = (JMenuItem) com;
                if (data == tmpItem.getClientProperty(DATAKEY)) //找到对应菜单项
                    return tmpItem;
            }
        }
        return null;
    }
    /**
     * 改变缺省菜单项，通过给定的数据对象（data），首先找到对应的菜单项，然后更新缺省菜单项的显示以及数据对象
     * 
     * @param data
     *            --数据对象，保存在菜单项中
     * @param isAdd
     *            --在没有显示缺省菜单的情况下，是否添加缺省菜单  true:在不存在的情况下增加，false：不存在的情况下不添加
     * @throws UnifyException
     *             --如果找不到对应的data对象，抛出此异常
     */
    public void changeDefaultItem(Object data, boolean isAdd)
            throws UnifyException {
        JMenuItem item = getItemByData(data);
        if (item == null) {
            throw new UnifyException("data don't exist!");
        }

        /**
         * 判断是缺省菜单是否已经添加，如果没有，根据设置的参数（isAdd）来判断是否添加
         */
        JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
        if (tmpItem != defaultItem) //没有添加缺省菜单项
        {
            if (isAdd) {
                popMenu.insert(defaultItem, 0);
                popMenu.insert(separator, 1);
            } else
                return;
        }

        /**
         * 将数据对象data对应的菜单项更新为缺省菜单
         */
        updateDefaultItemData(item);
    }
    /**
     * 获取对应数据项的菜单图标
     * 
     * @param data
     * @return
     */
    public Icon getItemIconByData(Object data) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return null;
        else
            return item.getIcon();
    }

    /**
     * 获取对应数据项的标签
     * 
     * @param data
     * @return
     */
    public String getLabelByData(Object data) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return null;
        else
            return item.getText();
    }
    /**
     * 更新数据菜单项
     * 
     * @param data
     * @param newLabel
     * @param newIcon
     */
    public void updateItemByData(Object data, String newLabel, Icon newIcon) {
        JMenuItem item = this.getItemByData(data);
        if (item == null)
            return;
        String label = StringUtil.trim(newLabel);
        if (!label.equals("")) {
            item.setText(label);
            if (data == defaultItem.getClientProperty(DATAKEY))
                defaultItem.setText(label);
        }

        if (newIcon != null) {
            item.setIcon(newIcon);
            if (data == defaultItem.getClientProperty(DATAKEY))
                defaultItem.setIcon(newIcon);
        }
    }
    /**
     * 添加菜单项后,如果菜单项多于两天,则向菜单中增加默认菜单项
     *  
     */
    private void addDefaultItem() {
        if (popMenu.getComponentCount() > 1) //如果菜单项大于1
        {
            JMenuItem tmpItem = (JMenuItem) popMenu.getComponent(0);
            if (tmpItem != defaultItem) //没有添加缺省菜单项
            {
                popMenu.insert(defaultItem, 0);
                popMenu.insert(separator, 1);
                updateDefaultItemData(tmpItem);
            }
        }
    }
    /**
     * 去除缺省菜单项
     *  
     */
    private void removeDefaultItem() {
        popMenu.remove(0); //去除缺省菜单
        popMenu.remove(1); //去除分隔线
    }
    /**
     * 
     * @author liu_xlin 菜单项的事件处理
     */
    private class MenuItemClickListener implements ActionListener {
        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() != defaultItem) //普通菜单项
            {
                JMenuItem item = (JMenuItem) e.getSource();
                updateDefaultItemData(item);//更新缺省菜单项

            }
            runAction(e); //同时执行添加的事件处理
        }

    }
}
