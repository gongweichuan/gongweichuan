/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�USBKeyLoginRequest.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-10-1</p>
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
public class USBKeyLoginRequest implements Serializable
{

    private int id;

    private String text;

    public USBKeyLoginRequest(int id, String text)
    {
        this.id = id;
        this.text = text;
    }

    public int getID()
    {
        return id;
    }

    public String getText()
    {
        return text;
    }

}
