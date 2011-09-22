/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tomcat;

import java.awt.MenuItem;

import javax.swing.Action;

import org.apache.log4j.Logger;

/**
 * <p>文件名称：EmbeddedTomcatStopActionMenuItem.java</p>
 * <p>文件描述：停止Tomcat的菜单</p>
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
public class EmbeddedTomcatStopActionMenuItem extends MenuItem
{
    private static final Logger log=Logger.getLogger(EmbeddedTomcatStopActionMenuItem.class);
    private Action action;   
     
     public void init()
     {
         log.info("init action is:"+action);
         this.addActionListener(action);//操作       
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
