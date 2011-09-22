/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;

/**
 * <p>文件名称：AddToTrayiconPopMenuAdvice.java</p>
 * <p>文件描述：把菜单加入到SystemTray的TrayIcon中</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-15</p>
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
public class AddToTrayiconPopMenuAdvice implements AfterReturningAdvice //,MethodBeforeAdvice,MethodInterceptor
{
    private static final String SYSTEMTRAY = "systemtray";
    private static final Logger log=Logger.getLogger(AddToTrayiconPopMenuAdvice.class);
    private ISpringBeanLoader sbLoader=ServiceAccess.getSpringService();
    
    /**
     * 功能描述：
     * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    public void before(Method m, Object[] args, Object target)
            throws Throwable
    {
        // Spring
        log.info("before(Method m, Object[] args, Object target)");
        

    }

    public void afterReturning(Object returnValue, Method m, Object[] args, Object
            target) throws Throwable
    {
        log.info("afterReturning(Object returnValue, Method m, Object[] args, Object");
        // init方法执行后
       Object b= sbLoader.getBean(SYSTEMTRAY);
       if(b!=null && b instanceof SystemTrayFrame)
       {
           SystemTrayFrame stf=(SystemTrayFrame)b;
           TrayIcon ti= stf.getTrayIcon();
           PopupMenu popM= ti.getPopupMenu();
           if("init".equalsIgnoreCase(m.getName()))
           {
               if(target instanceof MenuItem )
               {
                   popM.add((MenuItem)target);
               }
               else
               {
                   log.error("target is not instanceof MenuItem:"+target);
               }
               ti.setPopupMenu(popM);
           }
       }else
       {
           log.error(SYSTEMTRAY+" is "+b);
       }
        
    }

    public Object invoke(MethodInvocation arg0) throws Throwable
    {
        log.info("Object invoke(MethodInvocation arg0) throws Throwable");
        return null;
    }

}
