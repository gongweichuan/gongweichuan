/*
 * 创建日期 2006-9-18
 */
package com.coolsql.search;

import com.coolsql.view.log.LogProxy;


/**
 * @author liu_xlin 查询数据列、实体信息线程，避免查询结果数量过大，而导致等待时间过长的问题
 */
public class QueryInfoThread extends Thread {
    private SearchResultFrame con = null; //操作对象

    private boolean isRun = true; //线程的结束标志
    
    private QueryDBInfo query=null; //用于获取数据库信息的处理类
    public QueryInfoThread(QueryDBInfo query) {
        super();
        this.query=query;
    }

    /**
     * 设置被操作的窗口
     * 
     * @param con
     */
    public void setOperateWindow(SearchResultFrame con) {
        this.con=con;
    }

    public void run() {
        while (isRun) {
            pause();
            if (con != null) {
               query.setResultFrame(con);
               try
               {
                  query.query();
               }catch(Exception e)
               {
                   LogProxy.internalError(e);
               }
            }
        }
    }

    /**
     * 使线程等待
     *  
     */
    public synchronized void pause() {
        try {
            super.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 唤醒该线程
     *  
     */
    public synchronized void launch() {
        super.notify();
    }

    /**
     * 结束该线程
     *  
     */
    public synchronized void dispose() {
        isRun = false;
        resetData();
        launch();
    }

    /**
     * @return 返回 isRun。
     */
    public boolean isRun() {
        return isRun;
    }
    /**
     * 清空所有数据
     *
     */
    public void resetData()
    {
        if(con!=null)
        {
            con.removeAll();
            con=null;
        }
    }
}
