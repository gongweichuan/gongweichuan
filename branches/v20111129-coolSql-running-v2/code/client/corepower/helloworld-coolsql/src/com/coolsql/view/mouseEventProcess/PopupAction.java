/*
 * 创建日期 2006-5-31
 *
 */
package com.coolsql.view.mouseEventProcess;

import java.awt.event.MouseEvent;

import com.coolsql.pub.display.PopMenuMouseListener;
import com.coolsql.view.View;


/**
 * @author liu_xlin
 *视图鼠标右键事件处理
 */
public class PopupAction extends PopMenuMouseListener {
    private View view;
    
	public PopupAction(View view)
	{
	   	this.view=view;
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(isPopupTrigger(e))
		{
			view.popupMenu(e.getX(),e.getY());			
		}
	}
	/**
	 * @return 返回 view。
	 */
	public View getView() {
		return view;
	}
	/**
	 * @param view 要设置的 view。
	 */
	public void setView(View view) {
		this.view = view;
	}
}
