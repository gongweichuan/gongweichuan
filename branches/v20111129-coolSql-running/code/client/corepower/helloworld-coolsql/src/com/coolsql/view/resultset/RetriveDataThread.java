package com.coolsql.view.resultset;

import com.coolsql.bookmarkBean.Bookmark;


/**
 * 
 * @author liu_xlin 对数据库执行sql,对结果集进行展示,该类主要为了以多线程来解决swing组件线程安全上的问题
 */
public class RetriveDataThread extends Thread {
    private ResultSetDataProcess process;
    
    public RetriveDataThread(DataSetPanel dataPane, String sql,
            Bookmark bookmark, int processType)
    {
        process=new ResultSetDataProcess(dataPane,sql,bookmark,processType);
    }
    /**
     * 重写线程方法
     */
    public void run()
    {
        process.process();
    }
}
