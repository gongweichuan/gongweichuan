/*
 * 创建日期 2006-10-9
 */
package com.coolsql.pub.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JDialog;

import com.coolsql.pub.display.GUIUtil;

/**
 *  主题窗口,基本信息的显示,主要针对特定主题软件的初始化
 * @author liu_xlin
 */
@SuppressWarnings("serial")
public class BaseDialog extends JDialog implements Individuable{
	
    public BaseDialog()
    {
        super();
        iInit();
    }
    public BaseDialog(Frame owner)
    {
        super(owner);
        iInit();
    }
    public BaseDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner,modal);
        iInit();
    }
    public BaseDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);   
        iInit();
    }
    public BaseDialog(Frame owner, String title, boolean modal)
    {
        super(owner,title,modal);
        iInit();
    }
    public BaseDialog(Dialog owner)
    {
        super(owner);
        iInit();
    }
    public BaseDialog(Dialog owner, boolean modal)
    {
        super(owner,modal);
        iInit();
    }
    public BaseDialog(Dialog owner, String title)
    {
        super(owner,title);
        iInit();
    }
    public BaseDialog(Dialog owner, String title, boolean modal)
    {
        super(owner,title,modal);
        iInit();
    }
    /**
     * 用于定制合适外观的方法
     *
     */
    public void iInit()
    {
        
    }
    
    public void toCenter()
    {
    	Window owner = getOwner();
    	if (owner == null) {
    		owner = GUIUtil.findLikelyOwnerWindow();
    	}
        GUIUtil.centerFrameToFrame(owner, this);
    }
}
