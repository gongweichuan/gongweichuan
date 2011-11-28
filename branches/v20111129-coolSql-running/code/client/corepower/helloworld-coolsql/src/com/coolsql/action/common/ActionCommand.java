/*
 * Created on 2007-1-30
 */
package com.coolsql.action.common;

/**
 * @author liu_xlin
 *事件触发后，由该接口进行相应逻辑处理
 */
public interface ActionCommand {

    /**
     * 执行事件处理
     *
     */
    public void exectue() throws Exception;
}
