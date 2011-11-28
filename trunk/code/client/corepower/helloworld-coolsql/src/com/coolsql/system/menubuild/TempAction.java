/*
 * Created on 2007-5-18
 */
package com.coolsql.system.menubuild;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import com.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 *菜单项事件监听处理类，该类封装了外层传入的ActionListener
 */
public class TempAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private ActionListener listener;
    
    private MenuItemEnableCheck menuChecker=null;
    public TempAction(ActionListener listener,MenuItemEnableCheck menuChecker)
    {
        if(listener==null)
            throw new IllegalArgumentException("no listener object was passed");
        this.listener=listener;
        this.menuChecker=menuChecker;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==GUIUtil.getMainFrame().getJMenuBar())  //if the acton is global
		{
			if(menuChecker!=null&&!menuChecker.check())
			{
				return ;
			}
		}
        MenubarAvailabilityManage.getInstance().processState();
        listener.actionPerformed(e);
    }

}
