/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.update;

/**
 * <p>�ļ����ƣ�ErpUpdateHisInfo.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-28</p>
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
public interface ErpUpdateHisInfo
{

    public abstract String getUpdateStartTime();

    public abstract String getUpdateEndTime();

    public abstract String getUpdateSrcVersion();

    public abstract String getUpdateDesVersion();

    public abstract boolean updateSuccess();
    
}
