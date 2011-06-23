/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemStateEvent;
import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemStateListener;

/**
 * <p>文件名称：SystemStateManager.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-12</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public class SystemStateManager
{
    private static List systemListenerList = Collections
            .synchronizedList(new ArrayList());

    public SystemStateManager()
    {
    }

    public static void addSystemStateListener(SystemStateListener listener)
    {
        systemListenerList.add(listener);
    }

    public static void removeSystemStateListener(SystemStateListener listener)
    {
        systemListenerList.remove(listener);
    }

    public static void notifySystemState(int stateValue)
    {
        SystemStateEvent event = new SystemStateEvent(new Object(), stateValue);
        SystemStateListener next;
        for (Iterator it = systemListenerList.iterator(); it.hasNext(); next
                .stateChanged(event))
        {
            next = (SystemStateListener) it.next();
        }

    }
}
