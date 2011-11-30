/*
 * 创建日期 2006-9-25
 *
 */
package com.coolsql.pub.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.plaf.metal.MetalComboBoxUI;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.sun.java.swing.plaf.windows.WindowsButtonUI;

/**
 * @author liu_xlin 具有组合选择特性的按钮,通过选择操作对象,然后对其进行操作
 */
public class ComboButton extends JComboBox {
	private static final long serialVersionUID = 1L;

	private static final String DATAKEY = "dataKey";

    /**
     * 左边按钮
     */
    private JButton leftBtn = null;

    /**
     * 右边按钮
     */
    private JButton rightBtn = null;

    /**
     * 鼠标事件监听处理
     */
    private MouseProcessListener listener = null;

    //按钮边框
    private ThreeDBorder raiseBorder = new ThreeDBorder(ThreeDBorder.RAISE);

    private ThreeDBorder lowBorder = new ThreeDBorder(ThreeDBorder.LOWER);

    /**
     * 弹出菜单
     */
    private SplitePopupMenu popMenu = null;

    /**
     * 左边按钮的事件监听集合
     */
    private Vector actions = null;

    /**
     * 该属性是该控件的核心数据,每次选中一个菜单项后,更新该值
     */
    private Object selectedData = null;

    private JMenuItem defaultItem = null;

    private MenuItemListener menuListener = null;

    /**
     * 菜单的分隔线
     */
    private JSeparator separator = null;

    public ComboButton(String label, Icon icon) {
        super();
        separator = new JSeparator();
        listener = new MouseProcessListener();
        popMenu = new SplitePopupMenu();
        defaultItem = new JMenuItem();
        menuListener = new MenuItemListener();
        defaultItem.addActionListener(menuListener);
        actions = new Vector();
        createButtons(label, icon);
        leftBtn.setFocusable(false);
        leftBtn.setBorder(null);
        leftBtn.addMouseListener(listener);
        rightBtn.setFocusable(false);
        rightBtn.setPreferredSize(new Dimension(13, 1));
        rightBtn.setBorder(null);
        rightBtn.addMouseListener(listener);
        
        this.setEditable(true);
        setUI(new SpliteButtonUI());
        setSize(80,30);
//        this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
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
     * 根据数据对象来删除菜单项
     * 
     * @param ob
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
     * 更新缺省菜单数据项
     *  
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
     * 初始化左右两边的按钮
     *  
     */
    private void createButtons(String label, Icon icon) {
        int preferSize = 0;
        if (leftBtn == null) {
            leftBtn = new JButton() {
                public void setUI(ButtonUI ui) {
                    if (ui instanceof WindowsButtonUI) {
                        ui = new BasicButtonUI();
                    }
                    super.setUI(ui);
                }
                public Dimension getPreferredSize() {
                    Dimension pref = super.getPreferredSize();
                    pref.height += 4;
                    return pref;
                }
                public Dimension getMinimumSize() {
                    Dimension min = super.getMinimumSize();
                    min.height += 4;
                    return min;
                }
            };
            if (label != null && !label.trim().equals("")) {
                leftBtn.setText(label);
                preferSize += leftBtn.getFontMetrics(leftBtn.getFont())
                        .stringWidth("abc"); //添加“abc”，以便能让按钮更宽一点
            }
            if (icon != null) {
                leftBtn.setIcon(icon);
                preferSize += icon.getIconWidth()
                        + leftBtn.getFontMetrics(leftBtn.getFont())
                                .stringWidth("ab");
            }
            leftBtn.setMargin(new Insets(0, 0, 0, 0));
            leftBtn.setBackground(BasePanel.getThemeColor());
            leftBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    leftBtn.setBorder(null);
                    rightBtn.setBorder(null);
//                    ComboButton.this.getSize();
                    runAction(e);
                }

            });
        }
        if (rightBtn == null) {
            rightBtn = new JButton() {
                public void setUI(ButtonUI ui) {
                    if (ui instanceof WindowsButtonUI) {
                        ui = new BasicButtonUI();
                    }
                    super.setUI(ui);
                }

                /**
                 * 右边按钮的图标显示
                 */
                public void paint(Graphics g) {
                    super.paint(g);
                    Polygon p = new Polygon();
                    int w = getWidth();
                    int y = (getHeight() - 4) / 2;
                    int x = (w - 6) / 2;
                    if (isSelected()) {
                        x += 1;
                    }
                    p.addPoint(x, y);
                    p.addPoint(x + 3, y + 3);
                    p.addPoint(x + 6, y);
                    g.fillPolygon(p);
                    g.drawPolygon(p);
                }
            };
            rightBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    rightBtn.setBorder(null);
                    if (popMenu.getComponentCount() > 0) {

                        int x = (int) (ComboButton.this.getWidth() - popMenu
                                .getPreferredSize().getWidth());
                        popMenu.show(ComboButton.this, x, ComboButton.this
                                .getHeight());
                    }
                }

            });
//            rightBtn.setUI(new BasicButtonUI());
            rightBtn.setMargin(new Insets(0, 0, 0, 0));
            rightBtn.setBackground(BasePanel.getThemeColor());
//            rightBtn.setPreferredSize(new Dimension(8,21));
//            rightBtn.setMaximumSize(new Dimension(8,21));
//            rightBtn.setSize(new Dimension(8,21));
            preferSize += rightBtn.getPreferredSize().getWidth();
            this.setPreferredSize(new Dimension(preferSize, 21)); //设置该按钮的合适大小
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
     * 添加左边按钮事件处理
     * 
     * @param action
     */
    public void addAction(SplitBtnListener action) {
        actions.add(action);
    }

    protected class MouseProcessListener implements MouseListener {

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent e) {

        }

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
         */
        public void mousePressed(MouseEvent e) {
            if (e.getModifiers() == InputEvent.BUTTON3_MASK
                    || e.getModifiers() == InputEvent.BUTTON2_MASK) //屏蔽鼠标滑轮和右键
                return;
            if (e.getSource() == leftBtn) //左边按钮
            {
                leftBtn.setBorder(lowBorder);
                rightBtn.setBorder(lowBorder);
            } else //右边按钮
            {
                rightBtn.setBorder(lowBorder);

            }
        }

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        public void mouseReleased(MouseEvent e) {
            if (e.getModifiers() == InputEvent.BUTTON3_MASK
                    || e.getModifiers() == InputEvent.BUTTON2_MASK)//屏蔽鼠标滑轮和右键
                return;
            if (popMenu.getComponentCount() > 0) {
                leftBtn.setBorder(null);
                rightBtn.setBorder(null);
            } else {
                leftBtn.setBorder(raiseBorder);
                rightBtn.setBorder(raiseBorder);
            }

        }

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent e) {
            if (!ComboButton.this.popMenu.isShowing()) //如果菜单没有弹出则显示凸出边框
            {
                leftBtn.setBorder(raiseBorder);
                rightBtn.setBorder(raiseBorder);
            }
        }

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent e) {
            leftBtn.setBorder(null);
            rightBtn.setBorder(null);
        }

    }

    /**
     * 
     * @author liu_xlin 菜单项的事件处理
     */
    private class MenuItemListener implements ActionListener {
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
    private class SpliteButtonUI extends MetalComboBoxUI
    {
        protected ComboBoxEditor createEditor() {        
            return new LeftButtonEditor();
        }
        /**
         * 该方法必须重写，并且不做任何事情，否则导致菜单无法弹出
         */
        public void setPopupVisible( JComboBox c, boolean v )
        {
        	
        }
        protected ComboPopup createPopup() {
        	if(popMenu==null)
        		popMenu=new SplitePopupMenu();
        	
            return popMenu;
        }
        protected Dimension getDefaultSize() {
        	return new Dimension(100,30);
        }
        protected JButton createArrowButton() {
            boolean iconOnly = (comboBox.isEditable());
            JButton button = new MetalComboBoxButton( comboBox,
                                                      new MetalComboBoxIcon(){
                /**
                 * Created a stub to satisfy the interface.
                 */
                public int getIconWidth() { return 6; }

                /**
                 * Created a stub to satisfy the interface.
                 */
                public int getIconHeight()  { return 4; }
            },
                                                      iconOnly,
                                                      currentValuePane,
                                                      listBox );
            rightBtn.setMargin( new Insets( 0, 1, 1, 3 ) );
//            if (MetalLookAndFeel.usingOcean()) {
//                // Disabled rollover effect.
//                button.putClientProperty(MetalBorders.NO_BUTTON_ROLLOVER,
//                                         Boolean.TRUE);
//            }
//            updateButtonForOcean(button);
            
            return button;
        }
    }
    /**
     * 重新调整了该控件的组件排列方向（从右至左），因而需要将编辑组件设置为rightBtn。
     * @author kenny liu
     *
     * 2007-11-17 create
     */
    private class LeftButtonEditor implements ComboBoxEditor,javax.swing.plaf.UIResource
    {

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#addActionListener(java.awt.event.ActionListener)
		 */
		public void addActionListener(ActionListener l) {
//			rightBtn.addActionListener(l);
			
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#getEditorComponent()
		 */
		public Component getEditorComponent() {
			return leftBtn;
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#getItem()
		 */
		public Object getItem() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
		 */
		public void removeActionListener(ActionListener l) {
//			rightBtn.removeActionListener(l);
			
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#selectAll()
		 */
		public void selectAll() {
		}

		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
		 */
		public void setItem(Object anObject) {
		}
    	
    }
    /**
     * 下拉选择菜单类。
     * @author kenny liu
     *
     * 2007-11-17 create
     */
    private class SplitePopupMenu extends BasePopupMenu implements ComboPopup
    {
		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getKeyListener()
		 */
		public KeyListener getKeyListener() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getList()
		 */
		public JList getList() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getMouseListener()
		 */
		public MouseListener getMouseListener() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#getMouseMotionListener()
		 */
		public MouseMotionListener getMouseMotionListener() {
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.swing.plaf.basic.ComboPopup#uninstallingUI()
		 */
		public void uninstallingUI() {
		}
    	
    }
}
