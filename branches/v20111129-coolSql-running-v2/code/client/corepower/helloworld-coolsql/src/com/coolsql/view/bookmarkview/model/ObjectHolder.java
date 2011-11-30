/*
 * 创建日期 2006-9-10
 */
package com.coolsql.view.bookmarkview.model;

/**
 * @author liu_xlin
 *节点内数据对象接口
 */
public interface ObjectHolder {
   /**
    * 各种实体节点中包含了具体的实体对象,该方法用于获取这个实体数据对象
    * @return
    */
   public abstract Object getDataObject();
   
   /**
    * check whether this object is selected or not
    * @return true if selected, otherwise false;
    */
   public boolean isSelected();
   
}
