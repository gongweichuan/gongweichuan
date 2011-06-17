package com.chinaviponline.erp.corepower.psl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>�ļ����ƣ�ErpClassLoader.java</p>
 * <p>�ļ��������Զ���������� ����ʵ��jar���Ķ�̬���� ʵ�ֹ��ܲ��Gar [par]</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2007-7-20</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�
 *  �汾�ţ�
 *  �޸��ˣ�
 *  �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
 * @email  gongweichuan(AT)gmail.com
 */
public class ErpClassLoader extends URLClassLoader
{

    // ���Ӷ���
    private static ErpClassLoader singleto = new ErpClassLoader();

    /**
     *  
     * ����ģʽ ���캯��
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
     * <p>������������õ�������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2007-7-21</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public static ErpClassLoader getSingleton()
    {
        return singleto;
    }

    /**
     * 
     * <p>��������������·��</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2007-7-21</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
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
                    
                    // ���˳���·����ת����һ�� file: URL���� URL �ľ�����ʽ��ϵͳ�йء�
                    // �������ȷ���˳���·������ʾ���ļ���һ��Ŀ¼����õ��� URL ����б�ܽ����� 
                    // ʹ��ע������˷��������Զ��ܿ� URL �еķǷ��ַ���
                    // �����µĴ��뽫����·����ת����һ�� URL��
                    // ʵ�ַ�ʽ����ͨ�� toURI ����������·����ת���� URI��Ȼ��ͨ�� URI.toURL �������� URI ת���� URL��
                    File pathToAdd = new File(pathToAdds[i]).getCanonicalFile();
//                    System.out.println(pathToAdd.exists());
                    addURL(pathToAdd.toURL());
                }
                catch (IOException e)
                {
                    
                    // �˴������Ϣ����Ҫ����
                    e.printStackTrace();
                    
                    // ֱ��return ����break
                    return;
                }
            }
        }
    }
}
