package com.chinaviponline.erp.corepower.psl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>文件名称：ErpClassLoader.java</p>
 * <p>文件描述：自定义类加载器 用来实现jar包的动态加载 实现功能插件Gar [par]</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2007-7-20</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：
 *  版本号：
 *  修改人：
 *  修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public class ErpClassLoader extends URLClassLoader
{

    // 单子对象
    private static ErpClassLoader singleto = new ErpClassLoader();

    /**
     *  
     * 单例模式 构造函数
     */
    private ErpClassLoader()
    {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }
    
//    public ErpClassLoader()
//    {
//        super(new URL[0], ClassLoader.getSystemClassLoader());
//    }

    /**
     * 
     * <p>功能描述：获得单例对象</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2007-7-21</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public static ErpClassLoader getSingleton()
    {
        return singleto;
    }

    /**
     * 
     * <p>功能描述：增加路径</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2007-7-21</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param paths
     */
    public void addPath(String paths)
    {
        if (paths == null || paths.length() <= 0)
        {
            return;
        }
        String separator = System.getProperty("path.separator");
        String[] pathToAdds = paths.split(separator);
        for (int i = 0; i < pathToAdds.length; i++)
        {
            if (pathToAdds[i] != null && pathToAdds[i].length() > 0)
            {
                try
                {
                    
                    // 将此抽象路径名转换成一个 file: URL。该 URL 的具体形式与系统有关。
                    // 如果可以确定此抽象路径名表示的文件是一个目录，则得到的 URL 将以斜杠结束。 
                    // 使用注意事项：此方法不会自动避开 URL 中的非法字符。
                    // 建议新的代码将抽象路径名转换成一个 URL，
                    // 实现方式是先通过 toURI 方法将抽象路径名转换成 URI，然后通过 URI.toURL 方法将该 URI 转换成 URL。
                    File pathToAdd = new File(pathToAdds[i]).getCanonicalFile();
//                    System.out.println(pathToAdd.exists());
                    addURL(pathToAdd.toURL());
                }
                catch (IOException e)
                {
                    
                    // 此处输出信息可能要屏蔽
                    e.printStackTrace();
                    
                    // 直接return 还是break
                    return;
                }
            }
        }
    }
}
