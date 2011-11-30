/*
 * Created on 2007-4-26
 */
package com.coolsql.pub.component;

import java.awt.Component;

import com.coolsql.pub.display.FindProcessConfig;

/**
 * @author liu_xlin
 *对于特定组件的查找处理，必须实现此接口。
 */
public interface FindProcess {

    /**
     * 对于特定组件，可能会以不同的方式进行数据的查找，此方法实现了数据的匹配查找功能。
     * @param config --查找的参数配置，见类型com.coolsql.data.display.FindProcessConfig定义
     * @param com --被查找的组件对象
     * @return --true:查找成功， false:查找失败
     * @throws Exception
     */
    public boolean find(FindProcessConfig config,Component com) throws Exception;
    
    /**
     * 对查找结果的描述。
     * @return
     */
    public String resultInfo();
}
