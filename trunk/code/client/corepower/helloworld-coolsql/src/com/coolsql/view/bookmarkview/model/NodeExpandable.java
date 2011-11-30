/*
 * 创建日期 2006-9-8
 */
package com.coolsql.view.bookmarkview.model;

import java.sql.SQLException;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author liu_xlin
 *树节点展开所使用的接口
 */
public interface NodeExpandable {
    /**
     * 当节点被展开时，调用此方法进行子节点的初始化
     * @param parent ITreeNode
     * @param filter --treenode filter
     * @throws UnifyException
     */
   public abstract void expand(DefaultTreeNode parent,INodeFilter filter) throws SQLException, UnifyException;
   
   /**
    * 刷新选中节点,重新获取节点下的所有子节点
    * @param parent
    * @param filter --treenode filter
    * @throws SQLException
    * @throws UnifyException
    */
   public abstract void refresh(DefaultTreeNode parent,INodeFilter filter) throws SQLException,UnifyException;
}
