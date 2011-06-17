/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.systemsupport;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�Par.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-13</p>
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
public final class Par implements Serializable
{

    /**
     * ID
     */
    private String id;

    /**
     * NAME
     */
    private String name;

    /**
     * VERSION
     */
    private String version;

    /**
     * BASEDIR
     */
    private String baseDir;

    /**
     * STATE MBEAN NAME
     */
    private String stateMbeanName;

    
    /**
     * ���캯��
     * @param id
     * @param version
     * @param baseDir
     * @param stateMbeanName
     */
    public Par(String id, String version, String baseDir,
            String stateMbeanName)
    {
//        this.id = null;
//        this.name = null;
//        this.version = null;
//        this.baseDir = null;
//        this.stateMbeanName = null;
        this.id = id;
        this.version = version;
        this.baseDir = baseDir;
        this.stateMbeanName = stateMbeanName;
    }

    /**
     * 
     * <p>����������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-13</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public String getID()
    {
        return this.id;
    }

    /**
     * 
     * <p>����������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-13</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * 
     * <p>����������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-13</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public String getBaseDir()
    {
        return this.baseDir;
    }

    /**
     * 
     * <p>����������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-13</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public String getStateMbeanName()
    {
        return this.stateMbeanName;
    }

    /**
     * 
     * <p>����������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-13</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * <p>����������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2008-5-13</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�    �޸����ڣ�    �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param parName
     */
    public void setName(String parName)
    {
        this.name = parName;
    }
}
