package com.chinaviponline.erp.corepower.api;

import java.util.EventListener;

/**
 * <p>�ļ����ƣ�CloseableTabbedPaneListener.java</p>
 * <p>�ļ�������
 * ����:http://forum.java.sun.com/thread.jspa?threadID=337070&start=15&tstart=0
 * �ڵ���ر�ǰ�����ж� ���絯����ȷ�϶Ի���֮���</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2007-7-22</p>
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
public interface CloseableTabbedPaneListener extends EventListener
{

    /**
     * 
     * <p>�����������ر�ǰ�Ļص�����</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2007-7-22</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param tabIndexToClose �����رյ�Tabҳ��Index
     * @return �Ƿ�ȷʵҪ�ر�
     */
    public boolean closeTab(int tabIndexToClose);
}
