/*
 * 创建日期 2006-12-22
 */
package com.coolsql.pub.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.View;

/**
 * @author liu_xlin
 *视图标题栏的右键菜单
 */
public class ViewPopMenu extends BaseMenuManage {

    private JMenuItem maxItem=null;  //最大化菜单项
    private JMenuItem hiddenItem=null;   //隐藏菜单项
    private JMenuItem restore=null;  //恢复原始大小
    /**
     * @param com
     */
    public ViewPopMenu(View com) {
        super(com);
    }

    protected void createPopMenu()
    {
        if(popMenu==null)
        {
            popMenu=new BasePopupMenu();
            
            //最大化菜单项
            ActionListener toMaxListener=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
                    getView().sizeToMax();                    
                }
                
            };
            maxItem=createMenuItem(PublicResource.getString("view.popup.max.label"),null,toMaxListener);
            popMenu.add(maxItem);
           
            //恢复原始大小菜单项
            ActionListener restoreListener=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
                    getView().sizeToNormal();                    
                }
                
            };
            restore=createMenuItem(PublicResource.getString("view.popup.restore.label"),null,restoreListener);
            popMenu.add(restore);
            
            //隐藏视图菜单项
            ActionListener hiddenListener=new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
    				/**
    				 * 保存原始位置
    				 */
    				JSplitPane split=GUIUtil.getSplitContainer(getView());
    				int location=split.getDividerLocation();
    				split.putClientProperty(View.LASTLOCATION, location);
                	
                    getView().hidePanel(true);     
                    
                }
                
            };
            hiddenItem=createMenuItem(PublicResource.getString("view.popup.hidden.label"),null,hiddenListener);
            hiddenItem.setEnabled(true); 
            popMenu.add(hiddenItem);
        }
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        createPopMenu();
        
        JSplitPane pane=getView().isMax(getView());
        if(pane==null)  //非最大化
        {
            GUIUtil.setComponentEnabled(true,maxItem);           
            GUIUtil.setComponentEnabled(false,restore);
        }else   //已经处于最大化状态
        {
            GUIUtil.setComponentEnabled(false,maxItem);
            GUIUtil.setComponentEnabled(true,restore);
        }
        return popMenu;
    }

    /* （非 Javadoc）
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
       
        return  itemCheck();
    }
    public void setComponent(JComponent com)
    {
        if(!(com instanceof View))
            return ;
        
        super.setComponent(com);
    }
    protected View getView()
    {
        return (View)getComponent();
    }
}
