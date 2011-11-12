/**
 * 
 */
package com.chinaviponline.erp;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.StringTokenizer;

import com.chinaviponline.erp.corepower.psl.systemsupport.classloader.SelfAdaptClassLoader;

/**
 * <p>文件名称：BoostMain.java</p>
 * <p>文件描述：Load主程序</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-8</p>
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
public class BoostMain
{

    /**
     * 入口方法名
     */
    private static final String MAIN = "main";

    /**
     * 客户端字符串标示
     */
    private static final String CLIENT = "client";

    /**
     * erp.boot.bootclass 启动类
     */
    private static final String BOOTBOOTCLASS = "erp.boot.bootclass";

    /**
     * erp.boot.classpathes 类路径
     */
    private static final String BOOTCLASSPATHES = "erp.boot.classpathes";

    /**
     * erp.boot.type 启动类型Client或者Server
     */
    private static final String BOOTTYPE = "erp.boot.type";

    /**
     * 字符串编码 UTF-8
     */
    private static final String UTF8 = "UTF-8";

    /**
     * 属性文件
     */
    public static final String BOOT_INFO_FILE_NAME = "bin/bootinfo.properties";

    /**
     * Home Dir
     */
    public static String homeDir = null;

    /**
     * 构造函数 单例
     *
     */
    private BoostMain()
    {

    }

    /**
     * 
     * <p>功能描述：主函数,入口</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-9</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param args 命令行参数
     * @throws Exception IO异常,找不CLASS异常等
     */
    public static void main(String args[]) throws Exception
    {
        Properties p = new Properties();
        String jarPath = (BoostMain.class).getProtectionDomain()
                .getCodeSource().getLocation().getFile();
        jarPath = URLDecoder.decode(jarPath, UTF8);

        // 主目录
        homeDir = (new File(jarPath)).getParentFile().getParent()
                + File.separator;

        // 属性文件
        String binDir = homeDir + BoostMain.BOOT_INFO_FILE_NAME;
        FileInputStream inS = null;
        try
        {
            inS = new FileInputStream(binDir);
            p.load(inS);
        }
        finally
        {

            // 关闭文件
            if (inS != null)
            {
                try
                {
                    inS.close();
                }
                catch (Exception ignore)
                {
                }

            }
        }

        String type = p.getProperty(BOOTTYPE);
        String classpathes = p.getProperty(BOOTCLASSPATHES);

        StringTokenizer st = new StringTokenizer(classpathes, ";");
        int size = st.countTokens();
        URL initURLs[] = new URL[size];
        for (int i = 0; st.hasMoreElements(); i++)
        {
            //initURLs[i] = (new File(st.nextToken())).toURL();
            //toURL() 已过时。 此方法不会自动转义 URL 中的非法字符
            initURLs[i] = (new File(st.nextToken())).toURI().toURL();
        }

        String bootclassName = p.getProperty(BOOTBOOTCLASS);
        ClassLoader p1 = Thread.currentThread().getContextClassLoader();
        URLClassLoader theNewClassLoader;

        if (type.equalsIgnoreCase(CLIENT)) // 不区分大小写
        {
            theNewClassLoader = new SelfAdaptClassLoader(initURLs, p1);
        }
        else
        {
            theNewClassLoader = new URLClassLoader(initURLs, p1);
        }

        Thread.currentThread().setContextClassLoader(theNewClassLoader);
        // Class bootClass = theNewClassLoader.loadClass(bootclassName);
        //泛型的例子
        Class<?> bootClass = theNewClassLoader.loadClass(bootclassName);
        Method mainMethod = bootClass.getMethod(MAIN,
                new Class[] {java.lang.String[].class});
        mainMethod.invoke(null, new Object[] {args});
    }
}
