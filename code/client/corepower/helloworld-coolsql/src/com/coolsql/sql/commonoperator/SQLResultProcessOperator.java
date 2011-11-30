/*
 * 创建日期 2006-10-13
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;
import java.util.List;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.RetriveDataThread;

/**
 * @author liu_xlin
 *执行sql语句，同时将结果信息展示
 */
public class SQLResultProcessOperator extends BaseOperator  {

    /**执行sql语句，同时将结果信息展示
     * @param arg  必须为list类型，长度：3
     *           0：BookMark
     *           1: sql(String)
     *           2：Integer 处理类型
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null||!(arg instanceof List))
        {
            throw new IllegalArgumentException("typeof argument must be java.util.list,error type:"+arg);
        }
        
        List list=(List)arg;
        Bookmark bookmark=(Bookmark)list.get(0);
        String sql=(String)list.get(1);
        int processType=((Integer)list.get(2)).intValue();
        
        ResultSetView view=ViewManage.getInstance().getResultView();
        DataSetPanel com=view.addTab(bookmark);  //添加
        RetriveDataThread thread=new RetriveDataThread(com,sql,bookmark,processType);
        com.setCurrentThread(thread);
        thread.start();
        view.setSelectedTab(com);
    }
}
