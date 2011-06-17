/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.io.Serializable;

/**
 * <p>�ļ����ƣ�SmResource.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-17</p>
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
public class SmResource implements Serializable
{

    private String resourceDN;
    private String resourceName;
    private String resourceType;
    private String resourceSubType;

    public SmResource()
    {
        this("", "", "");
    }

    public SmResource(String dn, String name, String type)
    {
        this(dn, name, type, "");
    }

    public SmResource(String dn, String name, String type, String subType)
    {
        resourceDN = dn != null ? dn : "";
        resourceName = name != null ? name : "";
        resourceType = type != null ? type : "";
        resourceSubType = subType != null ? subType : "";
    }

    public final String getResourceType()
    {
        return resourceType;
    }

    public final void setResourceType(String aResourceType)
    {
        resourceType = aResourceType;
    }

    public final String getResourceSubType()
    {
        return resourceSubType;
    }

    public final void setResourceSubType(String aResourceSubType)
    {
        resourceSubType = aResourceSubType;
    }

    public final void setResourceDN(String aResourceDN)
    {
        resourceDN = aResourceDN;
    }

    public final String getResourceDN()
    {
        return resourceDN;
    }

    public final String getResourceName()
    {
        return resourceName;
    }

    public final void setResourceName(String aResourceName)
    {
        resourceName = aResourceName;
    }

    public boolean equals(Object object)
    {
        if(object == null)
        {
            return false;            
        }
        
        if(!(object instanceof SmResource))
        {
            return false;            
        }
        
        SmResource resourceInfo = (SmResource)object;
        return resourceDN.equals(resourceInfo.resourceDN) && resourceType.equals(resourceInfo.resourceType);
    }

    public int hashCode()
    {
        return resourceDN.hashCode() + resourceType.hashCode();
    }

}
