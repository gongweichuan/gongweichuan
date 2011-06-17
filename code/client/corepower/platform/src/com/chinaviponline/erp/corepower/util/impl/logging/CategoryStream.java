/**
 * 
 */
package com.chinaviponline.erp.corepower.util.impl.logging;

import java.io.*;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;

/**
 * <p>�ļ����ƣ�CategoryStream.java</p>
 * <p>�ļ����������ؽ����������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-16</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�    �汾�ţ�    �޸��ˣ�    �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
 * @email  gongweichuan(AT)gmail.com
 */

public class CategoryStream extends PrintStream
{

    /**
     * �Ƿ����
     */
    public static final boolean trace;

    /**
     * ���������
     */
    private static PrintStream cachedSystemOutStream = null;

    /**
     * ���������Ϣ��
     */
    private static PrintStream cachedSystemErrStream = null;

    /**
     * Ŀ¼
     */
    private Category category;

    /**
     * ���ȼ�
     */
    private Priority priority;

    /**
     * ��д?
     */
    private boolean inWrite;

    /**
     * �Ƿ�ʹ�þ�����Ϣ
     */
    private boolean issuedWarning;

    // ��ʼ��
    static
    {
        trace = getBoolean((CategoryStream.class).getName() + ".trace", false);
    }

    /**
     * ���캯��
     * @param category
     */
    public CategoryStream(Category category)
    {
        this(category, Priority.INFO, System.out);
    }

    /**
     * ���캯��
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
     * <p>�������������������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-16</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param out
     */
    public static void setSystemOutStream(PrintStream out)
    {
        cachedSystemOutStream = out;
    }

    /**
     * 
     * <p>������������������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-16</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public static PrintStream getSystemOutStream()
    {
        return cachedSystemOutStream;
    }

    /**
     * 
     * <p>�������������ô�����Ϣ��</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-16</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param err
     */
    public static void setSystemErrStream(PrintStream err)
    {
        cachedSystemErrStream = err;
    }

    /**
     * 
     * <p>������������ȡ������Ϣ��</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-16</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public static PrintStream getSystemErrStream()
    {
        return cachedSystemErrStream;
    }

    /**
     * 
     * <p>�������������ز���ֵ</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-16</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
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
     * ������������ӡ
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
     * ������������ӡ��Ϣ
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
     * <p>����������д�ֽ�</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-16</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
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
     * ����������ͬ��д�ֽ�
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