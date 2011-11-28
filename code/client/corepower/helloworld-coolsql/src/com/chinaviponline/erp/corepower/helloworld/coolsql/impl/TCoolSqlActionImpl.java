/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.helloworld.coolsql.impl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.helloworld.coolsql.i.ICoolSql;
import com.chinaviponline.erp.corepower.helloworld.coolsql.i.ICoolSqlAction;

/**
 * <p>文件名称：TCoolSqlActionImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-11-13</p>
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
public class TCoolSqlActionImpl extends AbstractAction implements ICoolSqlAction
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Logger log=Logger.getLogger(this.getClass());
    
    private String command;//start or stop

    private ICoolSql coolSql;
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        log.info("e:"+e.getActionCommand());
        
        if("start".equalsIgnoreCase(command))
        {
            log.debug("start");
            coolSql.start();       
        }
        else//stop
        {
            log.debug("stop");
            coolSql.stop();     
        }
        
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public ICoolSql getCoolSql()
    {
        return coolSql;
    }

    public void setCoolSql(ICoolSql coolSql)
    {
        this.coolSql = coolSql;
    }

}
