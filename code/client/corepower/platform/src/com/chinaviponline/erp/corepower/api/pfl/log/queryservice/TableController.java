/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.log.queryservice;

import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;

/**
 * <p>�ļ����ƣ�TableController.java</p>
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
public interface TableController
{
    public abstract JPopupMenu getPopMenu(TablePanel tablepanel);

    public abstract void keyReleased(KeyEvent keyevent, TablePanel tablepanel);

    public abstract void leftMouseClicked(TablePanel tablepanel);

    public abstract void refresh(TablePanel tablepanel);

    public abstract void firstPage(TablePanel tablepanel);

    public abstract void prePage(TablePanel tablepanel);

    public abstract void nextPage(TablePanel tablepanel);

    public abstract void lastPage(TablePanel tablepanel);

    public abstract void gotoPage(TablePanel tablepanel, int i);
}
