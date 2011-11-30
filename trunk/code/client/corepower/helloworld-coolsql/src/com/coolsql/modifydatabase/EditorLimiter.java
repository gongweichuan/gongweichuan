/*
 * Created on 2007-1-23
 */
package com.coolsql.modifydatabase;

/**
 * @author liu_xlin
 *编辑器的相关显示接口
 */
public interface EditorLimiter {

    /**
     * 编辑器可输入内容的最大长度，该方法适用于所有编辑器
     *@param size  --可输入的最大长度
     */
    public abstract void setSize(int size);
    
    /**
     * 如果编辑器的类型为数字输入，该方法可以使用
     *
     */
    public abstract void setDigits(int digits);
}
