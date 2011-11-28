/*
 * 创建日期 2006-11-9
 */
package com.coolsql.view.bookmarkview;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;

/**
 * @author liu_xlin 最近执行sql的信息类 1、sql语句 2、执行sql所需要的时间 3、执行sql的时间点
 */
public class RecentSQL implements Serializable {
    private String sql = null; //sql语句

    private long costTime = 0L; //执行sql所需时间

    private transient Bookmark bookmark;//所述书签

    private long time = 0; //执行sql的时间点

    public RecentSQL() {
        this("", 0, 0, null);
    }

    public RecentSQL(String sql, long costTime, long time, Bookmark bookmark) {
        this.sql = sql;
        this.costTime = costTime;
        this.bookmark = bookmark;
        this.time = time;
    }

    /**
     * 该方法防止在保存的时候，将书签对象作为保存元素
     * 
     * @return
     */
    public Bookmark bookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean equals(Object ob) {
        if (ob == null)
            return false;
        if (!(ob instanceof RecentSQL))
            return false;
        RecentSQL tmp = (RecentSQL) ob;
        if (sql.equals(tmp.sql) && costTime == tmp.costTime && time == tmp.time
                && bookmark.getAliasName().equals(tmp.bookmark.getAliasName()))
            return true;
        else
            return false;
    }

    /**
     * 将对象转化成向量。
     * @param seqNo  --当前sql对象的序号，如果该值小于0，不添加此项
     * @return   --转化后的向量对象
     */
    public Vector converToVector(int seqNo) {
        Vector v = new Vector();
        if (seqNo >= 0)
            v.add(seqNo + ""); //序号
        v.add(bookmark.getAliasName()); //书签
        v.add(getSql()); //sql
        v.add(StringUtil.transDate(new Date(getTime()))); //执行时间点
        v.add(String.valueOf(getCostTime())
                + PublicResource.getSQLString("sql.execute.time.unit")); //花费时间
        return v;
    }
}
