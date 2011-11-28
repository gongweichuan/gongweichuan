/*
 * 创建日期 2006-6-8
 *
 */
package com.coolsql.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JComponent;

import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.DisplayPanel;
import com.coolsql.pub.display.PopMenuMouseListener;

/**
 * @author liu_xlin 视图类定义，定义视图的基本信息
 */
public abstract class View extends DisplayPanel implements Display {
	public static final String LASTLOCATION="lastlocation"; //上次在splitpane中的位置
	
    public static Color THEME_COLOR = new Color(160, 207, 184);

    //标题栏右键弹出菜单监听处理类
    protected PopMenuListener popMenuListener = null;

    /**
     * 自定义事件处理对象的集合
     */
    protected Map actionsMap;

    public View() {
        super();
        popMenuListener = new PopMenuListener();
        getTopPane().addMouseListener(popMenuListener);
        actionsMap=new HashMap();
        createActions();
    }
    /**
     * 创建缺省的时间处理对象
     *
     */
    protected abstract void createActions();
    /**
     * 视图名称
     * 
     * @return 返回 name。
     */
    public abstract String getName();

    /**
     * this method will be invoke after main frame has been initialized.
     *
     */
    public abstract void doAfterMainFrame() throws Exception;
    /**
     * show view if this view is hidden
     *@param isFire --notice all propertychangelistener that listen to property: hidden if true
     */
    public void showPanel(boolean isFire)
    {
    	boolean oldValue=isVisible();
    	setVisible(true);
    	if(isFire)
    		firePropertyChange(PROPERTY_HIDDEN,oldValue , true);
    }
    public boolean isViewVisible()
    {
    	return isVisible();
    }
	/**
	 * override parent'method,notice all listener that listen to property:"hidden".
	 */
//    @Override
//	public void setVisible(boolean isVisible)
//	{
//    	boolean oldValue=isVisible();
//		super.setVisible(isVisible);
//		firePropertyChange(PROPERTY_HIDDEN, oldValue, isVisible);
//	}
    /**
     * 获取key所对应的事件处理对象
     * @param key  --键值
     * @return  --预先实例化的事件处理对象,如果找不到对应的时间处理类,返回null
     */
    public Action getAction(Object key)
    {
        return (Action)actionsMap.get(key);
    }
    /**
     * 
     * @author liu_xlin 在视图标题栏上添加鼠标监听事件，主要处理右键弹出菜单的显示
     */
    protected class PopMenuListener extends PopMenuMouseListener {
        
        /**
         * 右键在标题上点击，但需要将视图作为组件传入
         */
        public void mouseReleased(MouseEvent e) {
            if(!isPopupTrigger(e))
                return;
                
            JComponent source = (JComponent) e.getSource();
            
            BaseMenuManage viewMenu=ViewManage.getInstance().getViewMenuManage();
            viewMenu.setComponent(View.this);
            viewMenu.getPopMenu().show(source,e.getX(),e.getY());
        }
    }
}
