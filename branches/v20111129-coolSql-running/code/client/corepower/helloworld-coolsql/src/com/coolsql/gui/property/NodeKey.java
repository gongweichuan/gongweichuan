/*
 * 创建日期 2006-9-11
 */
package com.coolsql.gui.property;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author liu_xlin 属性节点描述bean
 */
public class NodeKey {
    private String name; //节点描述

    private Icon icon; //节点图标

    private String displayName;
    
    private NodeKey parent; //上级节点
    public NodeKey(String name,String iconFilePath,NodeKey parent)
    {
        this.name=name;
        displayName=name;
        this.icon=new ImageIcon(iconFilePath);
        this.parent=parent;
    }
    public NodeKey(String name,Icon icon,NodeKey parent)
    {
        this.name=name;
        displayName=name;
        this.icon=icon;
        this.parent=parent;
    }
    /**
     * @return 返回 icon。
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            要设置的 icon。
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * @return 返回 name。
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            要设置的 name。
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return 返回 displayName。
     */
    public String getDisplayName() {
        return displayName;
    }
    /**
     * @param displayName 要设置的 displayName。
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String toString()
    {
        return name;
    }
    public boolean equals(Object ob)
    {
        if(ob==null)
            return false;
        if(!(ob instanceof NodeKey))
            return false;
        NodeKey key=(NodeKey)ob;
        
        if(parent!=key.getParent())
        	return false;
        
        if(key.getName()==null&&this.name!=null)
            return false;
        if(key.getName()!=null&&!key.getName().equals(name))
        	return false;
        
        if(key.getDisplayName()==null&&this.displayName!=null)
            return false;
        if(key.getDisplayName()!=null&&!key.getDisplayName().equals(displayName))
        	return false;
        
        return true;
    }
    /**
     * @return 返回 parent。
     */
    public NodeKey getParent() {
        return parent;
    }
    /**
     * @param parent 要设置的 parent。
     */
    public void setParent(NodeKey parent) {
        this.parent = parent;
    }
}
