/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.systemsupport;

import java.io.Serializable;

/**
 * <p>文件名称：Par.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-13</p>
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
public final class Par implements Serializable
{

    /**
     * ID
     */
    private String id;

    /**
     * NAME
     */
    private String name;

    /**
     * VERSION
     */
    private String version;

    /**
     * BASEDIR
     */
    private String baseDir;

    /**
     * STATE MBEAN NAME
     */
    private String stateMbeanName;

    
    /**
     * 构造函数
     * @param id
     * @param version
     * @param baseDir
     * @param stateMbeanName
     */
    public Par(String id, String version, String baseDir,
            String stateMbeanName)
    {
//        this.id = null;
//        this.name = null;
//        this.version = null;
//        this.baseDir = null;
//        this.stateMbeanName = null;
        this.id = id;
        this.version = version;
        this.baseDir = baseDir;
        this.stateMbeanName = stateMbeanName;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getID()
    {
        return this.id;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getBaseDir()
    {
        return this.baseDir;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getStateMbeanName()
    {
        return this.stateMbeanName;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-13</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param parName
     */
    public void setName(String parName)
    {
        this.name = parName;
    }
}
