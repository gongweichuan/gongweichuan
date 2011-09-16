/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tomcat;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;

/**
 * <p>文件名称：EmbedTomcatStopAction.java</p>
 * <p>文件描述：停止Tomcat</p>
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
public class EmbedTomcatStopAction implements Action
{
    private static final String EMBEDDEDTOMCATFRAME = "embeddedtomcatframe";
    private static final Logger log=Logger.getLogger(EmbedTomcatStopAction.class);
    
    /**
     * 功能描述：
    
     * @see javax.swing.Action#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        // TODO Auto-generated method stub

    }

    /**
     * 功能描述：
    
     * @see javax.swing.Action#getValue(java.lang.String)
     */
    public Object getValue(String key)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 功能描述：
    
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 功能描述：
    
     * @see javax.swing.Action#putValue(java.lang.String, java.lang.Object)
     */
    public void putValue(String key, Object value)
    {
        // TODO Auto-generated method stub

    }

    /**
     * 功能描述：
    
     * @see javax.swing.Action#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        // TODO Auto-generated method stub

    }

    /**
     * 功能描述：
    
     * @see javax.swing.Action#setEnabled(boolean)
     */
    public void setEnabled(boolean b)
    {
        // TODO Auto-generated method stub

    }

    /**
     * 功能描述：停止服务
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        Runnable r=new  Runnable()
        {
            public void run()
            {
                ISpringBeanLoader spring=ServiceAccess.getSpringService();
                if(spring!=null)
                {
                    Object tomcatFrame=spring.getBean(EMBEDDEDTOMCATFRAME);
                    if(tomcatFrame!=null && tomcatFrame instanceof EmbeddedTomcatFrame)
                    {
                        try
                        {
                            ((EmbeddedTomcatFrame)tomcatFrame).stop();
                        }
                        catch (Exception e)
                        {
                           log.error("when stop tomcat error:"+e.getMessage());                           
                        }
                        
                    }else
                    {
                        log.error("getBean embeddedtomcatframe is error:"+tomcatFrame);
                    }
                    
                }
                else
                {
                    log.error("Srping is null");
                }
                
            }
        };
     
        Thread t=new Thread();
        t.start();
        log.info("tomcat stoping");
    }

}
