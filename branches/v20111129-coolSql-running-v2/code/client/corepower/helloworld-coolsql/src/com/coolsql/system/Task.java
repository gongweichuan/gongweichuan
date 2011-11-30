/*
 * 创建日期 2006-12-25
 */
package com.coolsql.system;

/**
 * @author liu_xlin
 *任务接口
 */
public interface Task {

    /**
     * 获取当前任务的描述
     * @return  --描述内容
     */
    public abstract String getDescribe();
    
    /**
     * 执行任务所定义的逻辑代码
     *
     */
    public abstract void execute();
    
    /**
     * 获取该任务的工作量
     * @return  --int，工作量
     */
    public abstract int getTaskLength();
}
