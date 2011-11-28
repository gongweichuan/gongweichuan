/*
 * 创建日期 2006-9-7
 */
package com.coolsql.sql;

import java.sql.SQLException;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 数据库连接执行操作时使用该线程，避免因为连接数据库或者执行sql时间过长，导致界面无刷新。
 */
public class DBThread extends Thread {
    private Bookmark bookmark = null;

    /**
     * 执行操作类型，0:数据库连接操作 1：执行sql
     */
    private int type = 0;


    public DBThread(Bookmark bookmark) {
        this.bookmark = bookmark;
        type = 0;
    }

    public DBThread(Bookmark bookmark, String sql) {
        this.bookmark = bookmark;
        type = 1;
        if (sql == null || sql.trim().equals("")) {
            throw new IllegalArgumentException(
                    " argument:sql value is null or space");
        }
    }

    public void run() {
        if (type == 0) { //数据库连接
            try {
                bookmark.setDisplayLabel(bookmark.getAliasName()
                        + "(connecting...)");
                bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_CONNECTING);//处理连接状态
                bookmark.connect();   //连接数据库
                bookmark.setDisplayLabel(bookmark.getAliasName()
                        + "(getting information...)");

                Operatable operator = OperatorFactory
                        .getOperator(com.coolsql.sql.commonoperator.RefreshTreeNodeOperator.class);
                operator.operate(bookmark);
            }catch(SQLException e)
            {
                /**
                 * 如果发生异常，提前恢复
                 */
                bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL);//恢复正常状态
                bookmark.setDisplayLabel(bookmark.getAliasName());
                LogProxy.SQLErrorReport(e);
            }
            catch (Exception e) {
                /**
                 * 如果发生异常，提前恢复
                 */
                bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL);//恢复正常状态
                bookmark.setDisplayLabel(bookmark.getAliasName());
                LogProxy.errorReport(e);
            } finally {
                if(bookmark.getConnectState()!=0)
                {
                    bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL);//恢复正常状态
                    bookmark.setDisplayLabel(bookmark.getAliasName());
                }
            }

        } else if (type == 1) //执行sql
        {

        }
    }
}
