/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

/**
 * <p>�ļ����ƣ�HelpTopicProvider.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-21</p>
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
public interface HelpTopicProvider
{
    
    public static final String TOPIC_ITEM_KEY = "corepower.pfl.mainframe.help-topic-item-key";

    public abstract String getRuntimeHelpTopic();
    
}
