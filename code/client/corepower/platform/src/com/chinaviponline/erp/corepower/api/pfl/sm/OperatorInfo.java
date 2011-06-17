/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

/**
 * <p>�ļ����ƣ�OperatorInfo.java</p>
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
public class OperatorInfo
{

    public static String UNKNOWN = "UNKNOWN";

    public static String USER_GUI = "GUI";

    public static String USER_TELNET = "TELNET";

    public static String USER_WEB = "WEB";

    private String operatorName;

    private String operatorIP;

    private String connectMode;

    public OperatorInfo(String operatorName, String operatorIP,
            String connectMode)
    {
        this.operatorName = operatorName;
        this.operatorIP = operatorIP;
        this.connectMode = connectMode;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public String getOperatorIP()
    {
        return operatorIP;
    }

    public String getConnectMode()
    {
        return connectMode;
    }

}
