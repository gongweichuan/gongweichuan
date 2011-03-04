/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.classloader;

/**
 * <p>文件名称：ClassCacheInfo.java</p>
 * <p>文件描述：缓存类信息</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-18</p>
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
public class ClassCacheInfo
{
    /**
     * 类名
     */
    public String className;
    
    /**
     * 偏移 是否应该用长整型呢?
     */
    public int offset;
    
    /**
     * 长度
     */
    public int len;

    /**
     * 构造函数
     * @param className 类名
     * @param offset    偏移
     * @param len   长度
     */
    ClassCacheInfo(String className, int offset, int len)
    {
        this.className = null;
        this.offset = 0;
        this.len = 0;
        this.className = className;
        this.offset = offset;
        this.len = len;
    }
}
