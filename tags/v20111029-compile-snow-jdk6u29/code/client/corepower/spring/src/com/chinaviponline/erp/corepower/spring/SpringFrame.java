package com.chinaviponline.erp.corepower.spring;

import com.chinaviponline.erp.corepower.api.BaseMBeanSupport;
import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;

/**
 * <p>文件名称：SpringFrame.java</p>
 * <p>文件描述：触发Spring、把控制权交给Spring</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-9</p>
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
public class SpringFrame extends BaseMBeanSupport implements SpringFrameMBean
{

    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getState()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getStateString()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     * 功能描述：触发Spring、把控制权交给Spring
    
     * @see org.jboss.system.ServiceMBeanSupport#create()
     */
    public void create() throws Exception
    {
        ISpringBeanLoader springBL=ServiceAccess.getSpringService();
        springBL.getBean("sqlMapClient");

    }

    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    public void start() throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void stop()
    {
        // TODO Auto-generated method stub

    }

}
