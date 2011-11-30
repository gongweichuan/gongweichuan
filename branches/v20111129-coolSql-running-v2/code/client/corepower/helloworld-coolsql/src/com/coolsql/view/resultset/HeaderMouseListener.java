/*
 * 创建日期 2006-12-15
 */
package com.coolsql.view.resultset;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.table.JTableHeader;

import com.coolsql.view.ViewManage;

/**
 * @author liu_xlin 表头组件的右键监听处理
 */
public class HeaderMouseListener implements MouseListener {

	boolean isPopupTriggerWhenPress;
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
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {

    }

    /*
     * （非 Javadoc）
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {

    }

    /*
     * （非 Javadoc）
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    	isPopupTriggerWhenPress=e.isPopupTrigger();
    }

    /*
     * （非 Javadoc）
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        if (!(e.getSource() instanceof JTableHeader))
            return;

        if (isPopupTriggerWhenPress||e.isPopupTrigger()) {
            TableHeaderMenuManage headerMenu = ViewManage.getInstance()
                    .getResultView().getHeaderMenu();
            //更新当前弹出菜单所依附的表头组件
            headerMenu.setComponent((JTableHeader) e.getSource());
            //更新当前菜单弹出的位置
            headerMenu.setPoint(e.getPoint());

            headerMenu.setComponent((JComponent) e.getSource()); //更新菜单管理器所属组件
            headerMenu.getPopMenu().show((JComponent) e.getSource(), e.getX(),
                    e.getY());
        }
    }

}
