/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;

/**
 * <p>文件名称：CategoryInfo.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-12</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public class CategoryInfo
{
    /**
     * 名称
     */
    private String name;
    
    /**
     * 优先级
     */
    private String priority;
    
    /**
     * 追加字符串
     */
    private String appender;

    /**
     * 构造函数
     * @param name
     * @param priority
     */
    public CategoryInfo(String name, String priority)
    {
//        this.name = null;
//        this.priority = null;
//        this.appender = null;
        this.name = name;
        this.priority = priority;
    }

    /**
     * 构造函数
     * @param name
     * @param priority
     * @param appender
     */
    public CategoryInfo(String name, String priority, String appender)
    {
//        this.name = null;
//        this.priority = null;
//        this.appender = null;
        this.name = name;
        this.priority = priority;
        this.appender = appender;
    }

    /**
     * 
     * <p>功能描述：获取名称</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * 
     * <p>功能描述：获取优先级</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getPriority()
    {
        return priority;
    }

    /**
     * 
     * <p>功能描述：获取追加字符串</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-12</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getAppender()
    {
        return appender;
    }

    /**
     * 
     * 功能描述：改写toString
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String str = "Category information:{package name=" + name + "       priority=" + priority;
        if(appender == null || appender.trim().length() == 0)
        {
            str = str + "}.";            
        }
        else
        {
            str = str + "      appender-ref=" + appender + "}.";            
        }
        return str;
    }

}
