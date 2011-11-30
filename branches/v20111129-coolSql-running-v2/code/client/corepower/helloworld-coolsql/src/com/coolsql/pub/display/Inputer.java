/*
 * Created on 2007-1-15
 */
package com.coolsql.pub.display;

/**
 * @author liu_xlin
 *对于窗口之间进行数据传递时，使用该接口
 */
public interface Inputer {

    /**
     * 向实现了该接口的窗口对象传递数据对象
     *@param value  --被传递的数据对象
     */
    public abstract void setData(Object value);
}
