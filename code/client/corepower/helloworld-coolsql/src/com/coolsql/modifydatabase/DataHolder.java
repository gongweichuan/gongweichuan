/*
 * Created on 2007-1-17
 */
package com.coolsql.modifydatabase;

/**
 * @author liu_xlin
 *数据句柄获取接口，对于各种编辑组件，在编辑完成后，返回的相应的数据值
 */
public interface DataHolder {

    /**
     * 获取编辑组件当前的数据对象值
     * @return --编辑组件当前值
     */
    public abstract Object getHolderValue();
    
    /**
     * 设置编辑组件的值
     *
     */
    public abstract void setValue(Object value);
}
