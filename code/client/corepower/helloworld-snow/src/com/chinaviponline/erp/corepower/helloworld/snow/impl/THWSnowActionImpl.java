package com.chinaviponline.erp.corepower.helloworld.snow.impl;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;
import com.chinaviponline.erp.corepower.helloworld.snow.i.IHWSnowAction;
import com.chinaviponline.erp.corepower.helloworld.snow.i.ISnow;

/**
 * <p>文件名称：THWSnowActionImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-11-1</p>
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
public class THWSnowActionImpl extends AbstractAction implements IHWSnowAction
{

    private Logger log=Logger.getLogger(this.getClass());
    
    private ISnow snow;//对应实现类
    
    private String twins;//start与stop对应、反之亦然、不能够是单例
    
    private String command;//start or stop
 

    public void actionPerformed(ActionEvent e)
    {
        log.info("e:"+e.getActionCommand());
        
        ISpringBeanLoader isbLoader= (ISpringBeanLoader)ServiceAccess.getSpringService();
        THWSnowActionImpl twinAction=(THWSnowActionImpl)isbLoader.getBean(twins);
        
        if("start".equalsIgnoreCase(command))
        {
            log.debug("start");
            snow.start();
            this.setEnabled(false);
            twinAction.setEnabled(true);
        }
        else//stop
        {
            log.debug("stop");
            snow.stop();
            this.setEnabled(false);
            twinAction.setEnabled(true);
        }
    }


    public ISnow getSnow()
    {
        return snow;
    }


    public void setSnow(ISnow snow)
    {
        this.snow = snow;
    }


    public String getTwins()
    {
        return twins;
    }


    public void setTwins(String twins)
    {
        this.twins = twins;
    }


    public String getCommand()
    {
        return command;
    }


    public void setCommand(String command)
    {
        this.command = command;
    }

}
