/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.awt.Frame;
import javax.swing.AbstractButton;

import com.chinaviponline.erp.corepower.api.pfl.finterface.FIException;
import com.chinaviponline.erp.corepower.api.util.ErrorCodeException;

/**
 * <p>�ļ����ƣ�SmClientService.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-16</p>
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
public interface SmClientService
{
    public static final String ROLE = SmClientService.class.getName();
    
    public static final String RIGHTCHANGE_TOPIC = "modify_sm_rightinfo";
    
    public static final int INVALID_COMMAND_CODE = -1;

    public abstract void enableButton(AbstractButton aabstractbutton[]);

    public abstract boolean checkOperation(String s);

    public abstract boolean checkCommandCode(int i);

    public abstract boolean checkRight(int i, String as[], String s);

    public abstract boolean checkRight(String s, String as[], String s1);

    public abstract String[] getResource(String s, String s1);

    public abstract String[] getResource(String s, int i);

    public abstract String[] getAllUsers();

    public abstract String[] getAllUser(int i);

    public abstract String getUserNameByID(int i);

    public abstract String encrypt(String s);

    public abstract void changePassword(String s, String s1)
        throws ErrorCodeException;

    public abstract UserInfo[] showUserSelectDialog(Frame frame, int ai[], UserSelectionFilter userselectionfilter);

    public abstract Object[] showUserDepartmentSelectDialog(Frame frame, int ai[], UserSelectionFilter userselectionfilter, int ai1[], DepartmentSelectionFilter departmentselectionfilter);

    public abstract boolean checkPassword(String s, String s1)
        throws ErrorCodeException;

    public abstract AbstractOperationTree getOperationTree();

    public abstract AbstractResourceTree getResourceTree();

    public abstract AbstractOperationCommandTree getOperationCommandTree();

    public abstract String getResourceName(String s, String s1);

    public abstract UserDetailInfo getCurrentUserInfo()
        throws FIException;

    public abstract void updateCurrentInfo(String s, String s1)
        throws FIException;

    public abstract DepartmentInfo[] getAllDepartmentInfo()
        throws FIException;

    public abstract UserInfo[] getAllUserInfo()
        throws FIException;

    public abstract boolean checkUSBKey(String s, String s1)
        throws ErrorCodeException;

    public abstract boolean checkRadiusPassword(String s, String s1)
        throws ErrorCodeException;

    public abstract UserInfo[] getAllSubDepartmentUserInfo(int i)
        throws FIException;

    public abstract String getPasswordProtectQuestion(String s, String s1)
        throws FIException;

    public abstract String getPasswordbyProtectAnswer(String s, String s1, String s2)
        throws FIException;

    public abstract boolean checkHiddenFunctionPassword(String s);

    public abstract boolean isSuperUser();

}
