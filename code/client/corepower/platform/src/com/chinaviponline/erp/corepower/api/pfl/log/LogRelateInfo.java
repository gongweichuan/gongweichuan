/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log;

import java.util.HashMap;

/**
 * <p>�ļ����ƣ�LogRelateInfo.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-7-12</p>
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
public class LogRelateInfo
{

    private LogMessage logmsg[];

    private HashMap relationMap;

    public LogRelateInfo(LogMessage msgs[], HashMap relation)
    {
        logmsg = null;
        relationMap = null;
        logmsg = msgs;
        relationMap = relation;
    }

    public LogMessage[] getLogMessage()
    {
        return logmsg;
    }

    public HashMap getRelation()
    {
        return relationMap;
    }

}
