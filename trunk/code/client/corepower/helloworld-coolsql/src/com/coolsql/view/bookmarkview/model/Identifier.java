/*
 * 创建日期 2006-7-1
 *
 */
package com.coolsql.view.bookmarkview.model;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.SQLException;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author liu_xlin 标识节点类型
 */
public abstract class Identifier implements TreeNodeEditable, Serializable,
        Comparable<Identifier>, NodeExpandable,ObjectHolder{
	private static final long serialVersionUID = 1L;
	
	public static final String groupTable="Tables";
    public static final String groupView="Views";
	public static final String TYPE_VIEW="VIEW";
	public static final String TYPE_TABLE="TABLE";
    /**
     * 是否有子节点，默认为有子节点
     */
    private boolean hasChildren = true;

    /**
     * 所属数签
     */
    private Bookmark bookmark = null;

    /**
     * 类型，标识不同节点的类型
     */
    private int type;

    /**
     * 节点值
     */
    private String content;

    /**
     * 节点显示的值。
     */
    private String displayLabel;

    protected PropertyChangeSupport pcs = null;

    private boolean isSelected=false;
    public Identifier() {
        this(-1,"",null);
    }

    public Identifier(int type, String content, Bookmark bookmark) {
        this(type,content,bookmark,true);
    }
    public Identifier(int type, String content,String displayLabel, Bookmark bookmark) {
        this(type,content,displayLabel,bookmark,true);
    }
    public Identifier(int type, String content, Bookmark bookmark,
            boolean isHasChild) {
        this(type,content,content,bookmark,isHasChild);
    }
    public Identifier(int type, String content,String displayLabel, Bookmark bookmark,
            boolean isHasChild) {
        this.type = type;
        this.content = content;
        this.displayLabel = displayLabel;
        this.bookmark = bookmark;
        pcs = new PropertyChangeSupport(this);
        this.hasChildren = isHasChild;
    }
    /**
     * @return 返回 content。
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            要设置的 content。
     */
    public void setContent(String content) {
        String oldvalue = this.content;
        this.content = content;
        if (oldvalue == displayLabel)
            displayLabel = content;
        pcs.firePropertyChange("Content", oldvalue, content);
    }

    /**
     * @return 返回 type。
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     *            要设置的 type。
     */
    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        return content;
    }

    public void copy() {
        StringSelection ss = new StringSelection(content);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.view.bookmarkview.model.TreeNodeEditable#property()
     */
    public void property() throws UnifyException, SQLException {

    }
    /*
     * 节点的刷新处理实现
     *  （非 Javadoc）
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#refresh(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void refresh(DefaultTreeNode parent,INodeFilter filter) throws SQLException,UnifyException
    {       
    }
    /**
     * @return 返回 bookmark。
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /**
     * @param bookmark
     *            要设置的 bookmark。
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    /*
     * 用于模式节点、表节点、视图节点、书签节点的排序
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Identifier arg0) {
        if (arg0 == null)
            return 1;
        Identifier id = arg0;

        return content.toLowerCase().compareTo(id.getContent());
    }

    public void addPropertyListener(PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(pl);
    }
    public void addPropertyListener(String name, PropertyChangeListener pl) {
        pcs.addPropertyChangeListener(name, pl);
    }

    public void removePropertyListener(PropertyChangeListener pl) {
        pcs.removePropertyChangeListener(pl);
    }
    
    public boolean isSelected()
    {
    	return isSelected;
    }
    public void setSelected(boolean isSelected)
    {
    	this.isSelected=isSelected;
    }

    public boolean equals(Object ob) {
        if (ob == null)
            return false;
        if (!(ob instanceof Identifier))
            return false;
        Identifier tmp = (Identifier) ob;
        if(bookmark==null)
        {
            if(tmp.bookmark==null)
                return true;
            else
                return false;
        }else
        {
            if(tmp.bookmark==null)
                return false;
        }
        if(content==null)
        {
        	if(tmp.content!=null)
        		return false;
        }else
        {
        	if(!content.equals(tmp.content))
        		return false;
        }
        if (bookmark.getAliasName().equals(tmp.getBookmark().getAliasName())
                && type == tmp.type )
            return true;
        else
            return false;
    }

    /**
     * @return 返回 displayLabel。
     */
    public String getDisplayLabel() {
        return displayLabel;
    }

    /**
     * @param displayLabel
     *            要设置的 displayLabel。
     */
    public void setDisplayLabel(String displayLabel) {
        String oldValue = this.displayLabel;
        this.displayLabel = displayLabel;
        pcs.firePropertyChange("displayLabel", oldValue, displayLabel);
    }

    /**
     * @return 返回 hasChildren。
     */
    public boolean isHasChildren() {
        return hasChildren;
    }

    /**
     * @param hasChildren
     *            要设置的 hasChildren。
     */
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    /**
     * 实现了接口ObjectHolder的方法
     */
    public Object getDataObject()
    {
        return null;
    }
}
