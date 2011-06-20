/**
 * 
 */
package com.chinaviponline.erp.corepower.util.impl.logging;

import java.io.*;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;

/**
 * <p>文件名称：CategoryStream.java</p>
 * <p>文件描述：重载界面输出的类</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-16</p>
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

public class CategoryStream extends PrintStream
{

    /**
     * 是否跟踪
     */
    public static final boolean trace;

    /**
     * 缓存输出流
     */
    private static PrintStream cachedSystemOutStream = null;

    /**
     * 缓存错误信息流
     */
    private static PrintStream cachedSystemErrStream = null;

    /**
     * 目录
     */
    private Category category;

    /**
     * 优先级
     */
    private Priority priority;

    /**
     * 可写?
     */
    private boolean inWrite;

    /**
     * 是否使用警告信息
     */
    private boolean issuedWarning;

    // 初始化
    static
    {
        trace = getBoolean((CategoryStream.class).getName() + ".trace", false);
    }

    /**
     * 构造函数
     * @param category
     */
    public CategoryStream(Category category)
    {
        this(category, Priority.INFO, System.out);
    }

    /**
     * 构造函数
     * @param category
     * @param priority
     * @param ps
     */
    public CategoryStream(Category category, Priority priority, PrintStream ps)
    {
        super(ps);
        this.category = category;
        this.priority = priority;
    }

    /**
     * 
     * <p>功能描述：设置输出流</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param out
     */
    public static void setSystemOutStream(PrintStream out)
    {
        cachedSystemOutStream = out;
    }

    /**
     * 
     * <p>功能描述：获得输出流</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static PrintStream getSystemOutStream()
    {
        return cachedSystemOutStream;
    }

    /**
     * 
     * <p>功能描述：设置错误信息流</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param err
     */
    public static void setSystemErrStream(PrintStream err)
    {
        cachedSystemErrStream = err;
    }

    /**
     * 
     * <p>功能描述：获取错误信息流</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static PrintStream getSystemErrStream()
    {
        return cachedSystemErrStream;
    }

    /**
     * 
     * <p>功能描述：返回布尔值</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @param defaultValue
     * @return
     */
    private static boolean getBoolean(String name, boolean defaultValue)
    {
        String value = System.getProperty(name, null);
        if (value == null)
        {
            return defaultValue;
        }
        else
        {
            return (new Boolean(value)).booleanValue();
        }
    }

    /**
     * 
     * 功能描述：打印
     * @see java.io.PrintStream#println(java.lang.String)
     */
    public void println(String msg)
    {
        String tmpStr = msg;

        if (msg == null)
        {
            tmpStr = "null";
        }

        StackTraceElement steArray[] = (new Throwable()).getStackTrace();
        
        if (steArray != null && steArray.length > 1)
        {
            String tmpInfo = "";
            
            if (steArray[1] != null)
            {
                tmpInfo = tmpInfo + steArray[1].toString();
            }

            if (tmpInfo.indexOf("java.lang.Throwable.printStackTrace") < 0)
            {
                tmpStr = "[" + tmpInfo + "] " + tmpStr;
            }
            else if (!tmpStr.trim().startsWith("at ") && steArray.length > 3)
            {
                tmpStr = "[" + steArray[3].toString() + "] " + tmpStr;
            }
        }

        byte bytes[] = tmpStr.getBytes();
        write(bytes, 0, bytes.length);
    }

    /**
     * 
     * 功能描述：打印信息
     * @see java.io.PrintStream#println(java.lang.Object)
     */
    public void println(Object msg)
    {
        if (msg == null)
        {
            msg = "null";
        }
        String tmpStr = msg.toString();
        StackTraceElement steArray[] = (new Throwable()).getStackTrace();
        if (steArray != null && steArray.length > 1)
        {
            String tmpInfo = "";
            if (steArray[1] != null)
            {
                tmpInfo = tmpInfo + steArray[1].toString();
            }

            if (tmpInfo.indexOf("java.lang.Throwable.printStackTrace") < 0)
            {
                tmpStr = "[" + tmpInfo + "] " + tmpStr;
            }
            else if (!tmpStr.trim().startsWith("at ") && steArray.length > 3)
            {
                tmpStr = "[" + steArray[3].toString() + "] " + tmpStr;
            }
        }

        byte bytes[] = tmpStr.getBytes();
        write(bytes, 0, bytes.length);
    }

    /**
     * 
     * <p>功能描述：写字节</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-16</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param b
     */
    public void write(byte b)
    {
        byte bytes[] = {b};
        write(bytes, 0, 1);
    }

    /**
     * 
     * 功能描述：同步写字节
     * @see java.io.PrintStream#write(byte[], int, int)
     */
    public synchronized void write(byte b[], int off, int len)
    {
        if (inWrite)
        {
            if (!issuedWarning)
            {
                String msg = "ERROR: invalid console appender config detected, console stream is looping";
                try
                {
                    out.write(msg.getBytes());
                }
                catch (IOException ignore)
                {
                }
                issuedWarning = true;
            }
            return;
        }

        inWrite = true;

        for (; len > 0 && (b[len - 1] == 10 || b[len - 1] == 13) && len > off; len--)
        {
            ;
        }

        if (len != 0)
        {
            String msg = new String(b, off, len);
            if (trace)
            {
                category.log(priority, msg, new Throwable());
            }
            else
            {
                category.log(priority, msg);
            }
        }
        inWrite = false;
    }

}