package com.chinaviponline.erp.corepower.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>�ļ����ƣ�FileScanner.java</p>
 * <p>�ļ��������������� lib �µ� .jar �ļ�</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2000-2010</p>
 * <p>��   ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2007-7-28</p>
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
 * @author ��Ϊ��gongwc
 */
public class FileScanner
{

    /**
     * �ļ��ָ���
     */
    private static final String separator = System
            .getProperty("file.separator");

    private static final String pathSeparator = System
            .getProperty("path.separator");

    /**
     * ��ǰ����Ŀ¼
     */
    private static final String usrDir = System.getProperty("user.dir");

    private List fileList = new ArrayList();

    private static FileScanner singleton = null;

    /**
     * ����
     *
     */
    private FileScanner()
    {

    }

    public static FileScanner getSingleton()
    {
        if (singleton == null)
        {
            singleton = new FileScanner();

        }
        return singleton;
    }

    /**
     * 
     * <p>����������Ѱ�� pathName Ŀ¼�������� extName ��β���ļ�·��</p>
     * <p>�����ˣ���Ϊ��gongwc</p>
     * <p>�������ڣ�2007-7-28</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param path ���������ļ�
     * @param extName ��׺��
     * @return
     */
    private void findExtFiles(File path, String extName)
    {
        if (!path.exists())
        {
            return;
        }

        if (path.isDirectory())
        {
            // ��Ŀ¼
            File allFiles[] = path.listFiles();

            for (int i = 0, count = allFiles.length; i < count; i++)
            {
                findExtFiles(allFiles[i], extName);
            }
        }
        else if (path.isFile())
        {
            // ���ļ�
            if (path.getName().endsWith(extName))
            {
                fileList.add(path.getParent() + separator + path.getName());
            }
        }

        return;
    }

    /**
     * 
     * <p>������������ѰĿ¼Ϊ lib ,��׺Ϊ</p>
     * <p>�����ˣ���Ϊ��gongwc</p>
     * <p>�������ڣ�2007-7-28</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param lib
     * @param ext
     * @return
     */
    public String findExts(String lib, String ext)
    {
        fileList = new ArrayList();
        File libPath = (new File(usrDir + separator + lib));
        findExtFiles(libPath, ext);

        StringBuffer stringbuffer = new StringBuffer();

        for (int i = 0, count = fileList.size(); i < count; i++)
        {
            stringbuffer.append((String) fileList.get(i));
            stringbuffer.append(pathSeparator);
        }

        return stringbuffer.toString();
    }

    /**
     * <p>����������</p>
     * <p>�����ˣ���Ϊ��gongwc</p>
     * <p>�������ڣ�2007-7-28</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param args
     */
    public static void main(String[] args)
    {
        FileScanner fs = FileScanner.getSingleton();
        System.out.println(fs.findExts("lib", ".jar"));

    }

}
