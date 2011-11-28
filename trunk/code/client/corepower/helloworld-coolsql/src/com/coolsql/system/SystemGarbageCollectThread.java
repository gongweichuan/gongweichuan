/*
 * 创建日期 2006-10-25
 */
package com.coolsql.system;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 * 该线程主要用来加快系统资源的回收，以守护线程的形式运行
 */
public class SystemGarbageCollectThread extends Thread {
    /**
     * 该变量用来控制守护线程的结束和运行
     */
    private boolean isRun=true;
    
    /**
     * 循环的周期
     */
    private long timeGap=0;
    public SystemGarbageCollectThread(long timeGap)
    {
        super("garbageCollect");
        this.timeGap=timeGap;
        this.setDaemon(true);
        this.setPriority(Thread.NORM_PRIORITY);
    }
    public void run()
    {
        while(isRun)
        {
            try {
                circle();
            } catch (InterruptedException e) {
                LogProxy.internalError(e);
            } catch (UnifyException e) {
                LogProxy.errorReport(e);
            }
        }
    }
    /**
     * 循环等待执行
     * @throws InterruptedException
     * @throws UnifyException
     */
    private void circle() throws InterruptedException, UnifyException 
    {
//        System.out.println("(1)total:"+(float)Runtime.getRuntime().totalMemory()/1024);
//        System.out.println("(1)free:"+(float)Runtime.getRuntime().freeMemory()/1024);
        collect();
//        System.out.println("(2)total:"+(float)Runtime.getRuntime().totalMemory()/1024);
//        System.out.println("(2)free:"+(float)Runtime.getRuntime().freeMemory()/1024);
        if(getTimeGap()<1000)
            throw new UnifyException("circle period is too short!");
        Thread.sleep(getTimeGap());
        
    }
    private void collect()
    {
        System.gc();//进行一次垃圾回收请求
    }
    /**
     * 给当前线程加上停止标志
     *
     */
    public void stopRun()
    {
        if(isRun)
            isRun=false;
    }
    /**
     * @return 返回 timeGap。
     */
    public long getTimeGap() {
        return timeGap;
    }
    /**
     * @param timeGap 要设置的 timeGap。
     */
    public void setTimeGap(long timeGap) {
        this.timeGap = timeGap;
    }
}
