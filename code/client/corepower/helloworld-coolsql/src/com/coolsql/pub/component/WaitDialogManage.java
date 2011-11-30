/*
 * 创建日期 2006-12-13
 */
package com.coolsql.pub.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.coolsql.pub.display.exception.NotRegisterException;

/**
 * @author liu_xlin
 *等待对话框的管理类
 */
public class WaitDialogManage {

    /**
     * 将该对象实例保存内存中
     */
    private static WaitDialogManage manage=null;
    
    /**
     * 保存申请的对话框信息
     * key:current thread     value:waiterDialog
     */
    private Map map=null;
    private WaitDialogManage()
    {
        map=Collections.synchronizedMap(new HashMap());
    }
    public synchronized static WaitDialogManage getInstance()
    {
        if(manage==null)
        {
            manage=new WaitDialogManage();
        }
        return manage;
    }
    /**
     * 获取当前线程对应的等待对话框
     * @throws NotRegisterException  --如果线程没有注册，将抛出此异常
     * @return --等待对话框实例
     */
    public WaitDialog getDialogOfCurrent() throws NotRegisterException
    {
        Thread th=Thread.currentThread();
        WaitDialog dialog=(WaitDialog)map.get(th);
        if(dialog==null)
            throw new NotRegisterException("current thread haven't registered to waiter dialog manager");
        
        return dialog;
    }
    /**
     * 注册当前线程对应的等待对话框,只有注册后才能方便的获取等待对话框的实例
     * @param th  --被注册的线程对象
     * @param owner  --等待对话框的父窗口
     * @return   --WaitDialog 类型的等待对话框
     */
    public WaitDialog register(Thread th,Window owner)
    {
        WaitDialog dialog=getWaiterDialog(owner);
        map.put(th,dialog);
        return dialog;
    }
    public WaitDialog register(Window owner)
    {
       return register(Thread.currentThread(),owner);
    }
    /**
     * 获取等待对话框实例
     * @param owner  --等待对话框的父窗口
     * @return  --等待对话框的
     */
    private WaitDialog getWaiterDialog(Window owner)
    {
        if(owner instanceof Frame)
            return new WaitDialog((Frame)owner);
        else if(owner instanceof Dialog)
            return new WaitDialog((Dialog)owner);
        else
            throw new IllegalArgumentException("owner of wait dialog must be frame or dialog，error owner:"+owner.getClass());
    }
    /**
     * 注销线程的访问
     * @param th  --指定的线程对象
     * @return   --被删除的值
     */
    public WaitDialog disposeRegister(Thread th)
    {
        return (WaitDialog)map.remove(th);
    }
}
