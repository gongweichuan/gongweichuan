/*
 * Created on 2007-1-24
 */
package com.coolsql.modifydatabase.model;

/**
 * @author liu_xlin
 *更新行记录处理时,复选框列的元素对象类
 */
public class CheckBean {

    /**
     * 当前复选框的选择值
     */
    private Boolean selectValue=null;
    /**
     * 对应复选框是否可用
     */
    private boolean isEnable;
    public CheckBean(Boolean selectValue,boolean isEnable)
    {
        this.selectValue=selectValue;
        this.isEnable=isEnable;
    }
    /**
     * @return Returns the isEnable.
     */
    public boolean isEnable() {
        return isEnable;
    }
    /**
     * @param isEnable The isEnable to set.
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
    /**
     * @return Returns the selectValue.
     */
    public Boolean getSelectValue() {
        return selectValue;
    }
    /**
     * @param selectValue The selectValue to set.
     */
    public void setSelectValue(Boolean selectValue) {
        this.selectValue = selectValue;
    }
    public String toString()
    {
        return String.valueOf(selectValue==null?false:selectValue.booleanValue());
    }
}
