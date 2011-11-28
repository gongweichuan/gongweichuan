/*
 * 创建日期 2006-8-18
 *
 */
package com.coolsql.view.bookmarkview.model;

import java.sql.SQLException;

import com.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin
 *节点数据的操作接口
 *包含的功能有：节点的属性查看  
 */
public interface TreeNodeEditable {
    public abstract void property() throws SQLException,UnifyException;
    public abstract void copy();
}
