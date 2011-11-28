/*
 * 创建日期 2006-10-9
 */
package com.coolsql.pub.component;

import javax.swing.JFrame;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *主题窗口,基本信息的显示,主要针对特定主题软件的初始化
 */
public class BaseFrame extends JFrame implements Individuable{
    public BaseFrame()
    {
        super();
        iInit();
    }
    public BaseFrame(String title)
    {
        super(title);
        iInit();
    }
    /**用于定制合适外观的方法
     * @see com.coolsql.pub.component.Individuable#iInit()
     */
    public void iInit() {
        this.setIconImage(PublicResource.getIcon("frame.icon").getImage());
    }
    public void toCenter()
    {
        GUIUtil.centerFrameToFrame(GUIUtil.getMainFrame(),this);
    }
}
