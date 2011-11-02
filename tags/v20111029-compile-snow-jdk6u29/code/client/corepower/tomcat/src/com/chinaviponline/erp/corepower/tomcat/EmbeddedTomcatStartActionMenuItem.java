/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tomcat;

import java.awt.MenuItem;

import javax.swing.Action;

import org.apache.log4j.Logger;

/**
 * <p>文件名称：EmbeddedTomcatStartActionMenuItem.java</p>
 * <p>文件描述：启动Tomcat的菜单</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-14</p>
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
public class EmbeddedTomcatStartActionMenuItem extends MenuItem
{
    private Action action;
    private static final Logger log=Logger.getLogger(EmbeddedTomcatStartActionMenuItem.class);
    
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

/*    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }*/
}
