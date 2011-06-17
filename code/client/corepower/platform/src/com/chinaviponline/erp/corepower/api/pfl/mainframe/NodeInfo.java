/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import java.util.*;
import javax.swing.Icon;

/**
 * <p>�ļ����ƣ�NodeInfo.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-30</p>
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
public class NodeInfo
{

    private String dn;

    private String type;

    private String name;

    private Object extraObject;

    private Icon icon;

    private NodeInfo parent;

    private List children;

    public NodeInfo(String dn, String type)
    {
        this.dn = null;
        this.type = null;
        name = null;
        extraObject = null;
        icon = null;
        parent = null;
        children = new ArrayList();
        this.dn = dn;
        this.type = type;
        name = "";
    }

    public NodeInfo(String dn, String type, String name, Icon icon)
    {
        this.dn = null;
        this.type = null;
        this.name = null;
        extraObject = null;
        this.icon = null;
        parent = null;
        children = new ArrayList();
        this.dn = dn;
        this.type = type;
        this.name = name;
        this.icon = icon;
    }

    public NodeInfo(String dn, String type, String name, Icon icon,
            Object extraObject)
    {
        this.dn = null;
        this.type = null;
        this.name = null;
        this.extraObject = null;
        this.icon = null;
        parent = null;
        children = new ArrayList();
        this.dn = dn;
        this.type = type;
        this.name = name;
        this.icon = icon;
        this.extraObject = extraObject;
    }

    public String getDN()
    {
        return dn;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Icon getIcon()
    {
        return icon;
    }

    public void setIcon(Icon icon)
    {
        this.icon = icon;
    }

    public Object getExtraObject()
    {
        return extraObject;
    }

    public void setExtraObject(Object extraObject)
    {
        this.extraObject = extraObject;
    }

    public void addChild(NodeInfo child)
    {
        if (child.getParent() != null)
        {
            child.getParent().removeChild(child);
        }

        children.add(child);
        child.parent = this;
    }

    public void removeChild(NodeInfo child)
    {
        children.remove(child);
    }

    public int getChildCount()
    {
        return children.size();
    }

    public NodeInfo[] getChildren()
    {
        return (NodeInfo[]) children.toArray(new NodeInfo[children.size()]);
    }

    public NodeInfo getChildAt(int childIndex)
    {
        return (NodeInfo) children.get(childIndex);
    }

    public int getChildIndex(NodeInfo child)
    {
        int childIndex = 0;
        for (Iterator it = children.iterator(); it.hasNext()
                && it.next() != child;)
        {
            childIndex++;
        }

        return childIndex;
    }

    public NodeInfo getParent()
    {
        return parent;
    }

    public void release()
    {
        NodeInfo child;
        for (Iterator it = children.iterator(); it.hasNext(); child.release())
        {
            child = (NodeInfo) it.next();
        }

        children.clear();
        parent = null;
    }

}
