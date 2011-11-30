/*
 * 创建日期 2006-12-26
 */
package com.coolsql.system;

/**
 * @author liu_xlin
 *系统处理接口
 */
public interface SystemProcess {

    /**
     * 启动系统处理
     *
     */
    public abstract void start();
    
    /**
     * 获取该处理的描述
     * @return
     */
    public abstract String getDescribe();
}
