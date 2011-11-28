/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.net.InetAddress;
import java.rmi.RemoteException;

import com.chinaviponline.erp.corepower.api.pfl.finterface.FIException;
import com.chinaviponline.erp.corepower.api.pfl.finterface.LoginResult;
import com.chinaviponline.erp.corepower.api.pfl.sm.extension.server.ConnectionSessionInfo;

/**
 * <p>文件名称：SmServerService.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-8-16</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public interface SmServerService
{
    public static final String ROLE = SmServerService.class.getName();

    public static final int INVALID_USERID = -1;

    public static final int AC_HAVE_RIGHT = 0;

    public static final int LOGIN_SUCCESS = 0;

    public static final int LOGIN_PWD_NOT_VALID = 2005;

    public static final int LOGIN_PWD_EXPIRE = 2009;

    public static final int LOGIN_PWD_WEAK = 2012;

    public static final int LOGIN_PWD_LENGTH_ERROR = 2013;

    public static final int FORCE_CHANGE_PASSWORD = 2014;

    public static final int SUPER_USER_ID = 0xfffe4735;

    public static final String SUPER_USER_NAME = "tywgptrepus";

    public static final String SESSION_CHANGE_TOPIC = "UEP_SM_SESSION_CHANGE_TOPIC";

    public static final int SESSION_TOPIC_LOGIN = 1;

    public static final int SESSION_TOPIC_LOGOUT = 2;

    /**
     * @deprecated Method checkAccess is deprecated
     */

    public abstract int checkAccess(int i, String s, String as[],
            InetAddress inetaddress, String s1) throws RemoteException;

    /**
     * @deprecated Method checkAccessx is deprecated
     */

    public abstract int checkAccessx(int i, String s, String as[],
            InetAddress inetaddress, String s1) throws FIException,
            RemoteException;

    public abstract int checkAccessx(int i, String s, String as[],
            InetAddress inetaddress, String s1, String s2) throws FIException,
            RemoteException;

    public abstract LoginResult checkLogin(String s, String s1,
            InetAddress inetaddress, String s2) throws FIException,
            RemoteException;

    public abstract LoginResult checkLoginUseRadius(String s, String s1,
            InetAddress inetaddress, String s2) throws FIException,
            RemoteException;

    public abstract void logout(ConnectionSessionInfo connectionsessioninfo)
            throws RemoteException;

    public abstract void updatePassWord(int i, String s, String s1)
            throws FIException, RemoteException;

    public abstract void updatePassWord(int i, String s, String s1,
            OperatorInfo operatorinfo) throws FIException, RemoteException;

    public abstract UserDetailInfo getUserInfoByID(int i) throws FIException,
            RemoteException;

    public abstract String[] getResource(int i, String s, String s1)
            throws FIException, RemoteException;

    public abstract String[] getResource(int i, String s, int j)
            throws FIException, RemoteException;

    public abstract SmResource[] getResource(int i, String s)
            throws FIException, RemoteException;

    public abstract UserDetailInfo getUserDetailInfoInstance()
            throws RemoteException;

    public abstract UserDetailInfo addNewUser(UserDetailInfo userdetailinfo)
            throws FIException, RemoteException;

    public abstract UserDetailInfo addNewUser(UserDetailInfo userdetailinfo,
            OperatorInfo operatorinfo) throws FIException, RemoteException;

    public abstract void deleteUser(int i) throws FIException, RemoteException;

    public abstract void deleteUser(int i, OperatorInfo operatorinfo)
            throws FIException, RemoteException;

    public abstract void updateUser(UserDetailInfo userdetailinfo)
            throws FIException, RemoteException;

    public abstract void updateUser(UserDetailInfo userdetailinfo,
            OperatorInfo operatorinfo) throws FIException, RemoteException;

    public abstract OperationInfo getOperationInfo(String s)
            throws RemoteException;

    public abstract UserDetailInfo[] getAllUsersInfo() throws FIException,
            RemoteException;

    public abstract RoleDetailInfo getRoleInfoByID(int i) throws FIException,
            RemoteException;

    public abstract RoleDetailInfo getRoleInfoByName(String s)
            throws FIException, RemoteException;

    public abstract RoleDetailInfo[] getAllRolesInfo() throws FIException,
            RemoteException;

    public abstract RoleSetDetailInfo getRoleSetInfoByID(int i)
            throws FIException, RemoteException;

    public abstract RoleSetDetailInfo getRoleSetInfoByName(String s)
            throws FIException, RemoteException;

    public abstract RoleSetDetailInfo[] getAllRoleSetsInfo()
            throws FIException, RemoteException;

    public abstract ConnectionSessionInfo getSessionInfo(short word0)
            throws RemoteException;

    public abstract UserInfo[] getUsersOfDepartment(int i) throws FIException,
            RemoteException;

    public abstract USBKeyLoginRequest requestUSBKeyLogin() throws FIException,
            RemoteException;

    public abstract LoginResult checkUSBCLogin(String s, int i, byte abyte0[],
            byte abyte1[], InetAddress inetaddress, String s1)
            throws FIException, RemoteException;

    public abstract DepartmentInfo[] getAllDepartment() throws FIException,
            RemoteException;

    public abstract DepartmentInfo getDepartmentByName(String s)
            throws FIException, RemoteException;

    public abstract DepartmentInfo getDepartmentByID(int i) throws FIException,
            RemoteException;

    public abstract UserDetailInfo getUserInfoByName(String s)
            throws FIException, RemoteException;

    public abstract ConnectionSessionInfo[] getAllSession()
            throws RemoteException;

    public abstract boolean deleteSessionForce(short word0)
            throws RemoteException;

    public abstract DepartmentInfo getDepartmentInfoInstance()
            throws RemoteException;

    public abstract DepartmentInfo addNewDepartment(
            DepartmentInfo departmentinfo) throws FIException, RemoteException;

    public abstract void deleteDepartment(int i) throws FIException,
            RemoteException;

    public abstract void updateDepartment(DepartmentInfo departmentinfo)
            throws FIException, RemoteException;

    public abstract String[] getAllConnectionType() throws RemoteException;

    public abstract void clearSmData();

    public abstract void refreshSmData();

    public abstract void assignResourceToAllRoles(SmResource smresource,
            boolean flag) throws FIException;

    public abstract SecurityRule getSecurityRule();

    public abstract boolean isSuperUser(int i);

}
