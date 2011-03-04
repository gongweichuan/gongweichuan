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
 * <p>�ļ����ƣ�BoostMain.java</p>
 * <p>�ļ�������Load������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-8</p>
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
public class BoostMain
{

    /**
     * ��ڷ�����
     */
    private static final String MAIN = "main";

    /**
     * �ͻ����ַ�����ʾ
     */
    private static final String CLIENT = "client";

    /**
     * erp.boot.bootclass ������
     */
    private static final String BOOTBOOTCLASS = "erp.boot.bootclass";

    /**
     * erp.boot.classpathes ��·��
     */
    private static final String BOOTCLASSPATHES = "erp.boot.classpathes";

    /**
     * erp.boot.type ��������Client����Server
     */
    private static final String BOOTTYPE = "erp.boot.type";

    /**
     * �ַ������� UTF-8
     */
    private static final String UTF8 = "UTF-8";

    /**
     * �����ļ�
     */
    public static final String BOOT_INFO_FILE_NAME = "bin/bootinfo.properties";

    /**
     * Home Dir
     */
    public static String homeDir = null;

    /**
     * ���캯�� ����
     *
     */
    private BoostMain()
    {

    }

    /**
     * 
     * <p>����������������,���</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-9</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param args �����в���
     * @throws Exception IO�쳣,�Ҳ�CLASS�쳣��
     */
    public static void main(String args[]) throws Exception
    {
        Properties p = new Properties();
        String jarPath = (BoostMain.class).getProtectionDomain()
                .getCodeSource().getLocation().getFile();
        jarPath = URLDecoder.decode(jarPath, UTF8);

        // ��Ŀ¼
        homeDir = (new File(jarPath)).getParentFile().getParent()
                + File.separator;

        // �����ļ�
        String binDir = homeDir + BoostMain.BOOT_INFO_FILE_NAME;
        FileInputStream inS = null;
        try
        {
            inS = new FileInputStream(binDir);
            p.load(inS);
        }
        finally
        {

            // �ر��ļ�
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
            initURLs[i] = (new File(st.nextToken())).toURL();
        }

        String bootclassName = p.getProperty(BOOTBOOTCLASS);
        ClassLoader p1 = Thread.currentThread().getContextClassLoader();
        URLClassLoader theNewClassLoader;

        if (type.equalsIgnoreCase(CLIENT)) // �����ִ�Сд
        {
            theNewClassLoader = new SelfAdaptClassLoader(initURLs, p1);
        }
        else
        {
            theNewClassLoader = new URLClassLoader(initURLs, p1);
        }

        Thread.currentThread().setContextClassLoader(theNewClassLoader);
        Class bootClass = theNewClassLoader.loadClass(bootclassName);
        Method mainMethod = bootClass.getMethod(MAIN,
                new Class[] {java.lang.String[].class});
        mainMethod.invoke(null, new Object[] {args});
    }
}
