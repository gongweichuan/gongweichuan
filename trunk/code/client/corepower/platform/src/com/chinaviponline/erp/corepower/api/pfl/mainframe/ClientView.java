package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JComponent;
/**
 * <p>�ļ����ƣ�ClientView.java</p>
 * <p>�ļ���������ͼ�����ӿ�</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-5-2</p>
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
public interface ClientView
{
    public abstract void init(ClientViewContext clientviewcontext)
            throws InitialException;

    public abstract void hidedToActive();

    public abstract void deactiveToActive();

    public abstract void deactive();

    public abstract void hide();

    public abstract void destroy();

    public abstract JComponent getContentComponent();
}
