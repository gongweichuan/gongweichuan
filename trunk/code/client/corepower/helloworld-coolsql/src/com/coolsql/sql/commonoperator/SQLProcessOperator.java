/*
 * 创建日期 2006-10-20
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;
import java.util.List;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.RetriveDataThread;

/**
 * @author liu_xlin
 * 对已经执行过的sql结果信息进行相关的编辑处理
 */
public class SQLProcessOperator extends BaseOperator {

    /**结果集数据的处理操作类
     * @param arg List
     * 0 : DataSetPanel  数据面板
     * 1 : int  处理类型
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            return;
        if(!(arg instanceof List))
            throw new UnifyException("the type of argument is error,erorr type:"+arg.getClass());
        
        List list=(List)arg;
        DataSetPanel dataPane=(DataSetPanel)list.get(0);
        int processType=((Integer)list.get(1)).intValue();

        RetriveDataThread thread=new RetriveDataThread(dataPane,dataPane.getSql(),dataPane.getBookmark(),processType);
        dataPane.setCurrentThread(thread);
        thread.start();
    }
}
