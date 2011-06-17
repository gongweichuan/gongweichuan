/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

import java.io.Serializable;

/**
 * <p>文件名称：SmResource.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-17</p>
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
