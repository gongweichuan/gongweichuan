/*
 * 创建日期 2006-9-8
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *  
 */
public class DisconnectOperator implements Operatable {

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.sql.common.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if (arg == null)
            throw new UnifyException("no operate object!");
        if (!(arg instanceof Bookmark)) {
            throw new UnifyException("operate object error! class:"
                    + arg.getClass());
        }
        Bookmark bookmark = (Bookmark) arg;
        try {
            bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_DISCONNECTING);//使处于正在断开状态
            bookmark.disconnect();
        } finally {
            bookmark.setConnectState(BookMarkPubInfo.BOOKMARKSTATE_NORMAL); //使状态恢复正常
        }
        LogProxy log = LogProxy.getProxy();
        log.info("database:"+bookmark.getAliasName()+" disconnected!");
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.sql.common.Operatable#operate(java.lang.Object,
     *      java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException, SQLException {

    }

}
