package com.chinaviponline.erp.corepower.api;

import java.util.EventListener;

/**
 * <p>文件名称：CloseableTabbedPaneListener.java</p>
 * <p>文件描述：
 * 来自:http://forum.java.sun.com/thread.jspa?threadID=337070&start=15&tstart=0
 * 在点击关闭前做个判断 例如弹出个确认对话框之类的</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2007-7-22</p>
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
public interface CloseableTabbedPaneListener extends EventListener
{

    /**
     * 
     * <p>功能描述：关闭前的回调动作</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2007-7-22</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param tabIndexToClose 即将关闭的Tab页的Index
     * @return 是否确实要关闭
     */
    public boolean closeTab(int tabIndexToClose);
}
