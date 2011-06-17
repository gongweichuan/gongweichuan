/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.hierarchy;

/**
 * <p>文件名称：HierarchyService.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-28</p>
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
public interface HierarchyService
{

    public static final String ROLE = HierarchyService.class.getName();

    public static final String NODE_ADD = "node_add";

    public static final String NODE_DEL = "node_del";

    public static final String NODE_INFO_CHANGE = "node_info_chg";

    public static final String NODE_STATE_ON = "node_stat_on";

    public static final String NODE_STATE_OFF = "node_stat_off";

    public static final String HIERARCHY_INFO_CHANGE_TOPIC = "topic/HIERARCHY_INFO_CHANGE_TOPIC";

    public abstract boolean isNodeAccessible(int i) throws GetNodeException;

    public abstract Object findService(String s) throws FindServiceException;

    public abstract Object findService(int i, String s)
            throws FindServiceException;

    public abstract Object findServiceFromSingleton(String s)
            throws FindServiceException;

    public abstract INode getNode() throws GetNodeException;

    public abstract INode getNode(int i) throws GetNodeException;

    public abstract INode[] getDirectSubnodes() throws GetNodeException;

    public abstract INode[] getDirectSubnodes(int i) throws GetNodeException;

    public abstract INode[] getAllSubnodes() throws GetNodeException;

    public abstract INode[] getAllSubnodes(int i) throws GetNodeException;

}
