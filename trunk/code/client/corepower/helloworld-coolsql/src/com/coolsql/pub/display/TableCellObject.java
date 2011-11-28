/*
 * 创建日期 2006-8-20
 */
package com.coolsql.pub.display;

import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author liu_xlin
 *表格元素的value，便于表格渲染（能够显示图标）
 */
public class TableCellObject implements Comparable,Serializable{

	private static final long serialVersionUID = -7845145524115346055L;
	/**
     * 渲染图标
     */
    private transient Icon icon=null;
    /**
     * 表格元素显示的文字
     */
    private String value=null;
    
    //used to display on ui
    private String displayValue=null;
    public TableCellObject()
    {
        this("",null);
    }
    public TableCellObject(String value)
    {
        this(value,null);
    }
    public TableCellObject(String value,Icon icon)
    {
        this(value,value,icon);
    }
    public TableCellObject(String value,String displayValue,Icon icon)
    {
        this.value=value;
        this.displayValue=displayValue;
        this.icon=icon;
    }
    /**
     * 检验表格元素是否带有图标的展示
     * @return
     */
    public boolean hasIcon()
    {
        return icon!=null;
    }
    /**
     * 重写toString()方法
     */
    public String toString()
    {
        return getDisplayValue();
    }
    /**
     * 重写equals()方法
     */
    public boolean equals(Object ob)
    {
        if(ob==null)
            return false;
        if(!(ob instanceof TableCellObject))
            return false;
        TableCellObject tmp=(TableCellObject)ob;
        if(tmp.icon.toString().equals(this.icon.toString())&&tmp.value.equals(value))
            return true;
        else
            return false;
    }
    /* （非 Javadoc）
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        if(arg0==null)
          return 1;
        if(!(arg0 instanceof TableCellObject))
            return -1;
        TableCellObject tmp=(TableCellObject)arg0;
        return value.compareTo(tmp.value);
    }
    /**
     * @return 返回 icon。
     */
    public Icon getIcon() {
        return icon;
    }
    /**
     * @param icon 要设置的 icon。
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    /**
     * @return 返回 value。
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value 要设置的 value。
     */
    public void setValue(String value) {
        this.value = value;
    }
	/**
	 * @return the displayValue
	 */
	public String getDisplayValue() {
		return this.displayValue;
	}
}
