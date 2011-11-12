/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tomcat.impl;

import javax.swing.Action;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.tomcat.i.IActionJMenuItem;

/**
 * <p>文件名称：TActionJMenuItem.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-21</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：

 *  版本号：
 *  修改人：
 *  修改内容：

 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川

 * @email  gongweichuan(AT)gmail.com
 */
public class TActionJMenuItem extends JMenuItem implements IActionJMenuItem
{
    private Action action;
    private static final Logger log=Logger.getLogger(TActionJMenuItem.class);
    
    public void init()
    {
        this.addActionListener(action);//操作
        log.info("init action is:"+action); 
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }
}
