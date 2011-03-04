/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.classloader;

/**
 * <p>�ļ����ƣ�ClassCacheInfo.java</p>
 * <p>�ļ���������������Ϣ</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-18</p>
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
public class ClassCacheInfo
{
    /**
     * ����
     */
    public String className;
    
    /**
     * ƫ�� �Ƿ�Ӧ���ó�������?
     */
    public int offset;
    
    /**
     * ����
     */
    public int len;

    /**
     * ���캯��
     * @param className ����
     * @param offset    ƫ��
     * @param len   ����
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
