/**
 * 
 */
package com.chinaviponline.erp.corepower.api.psl.update;

/**
 * <p>文件名称：UpdateService.java</p>
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
public interface UpdateService
{

    public static final String ROLE =UpdateService.class.getName();

    public abstract ErpUpdateHisInfo[] getClntUpdateHis(String s);

    public abstract ErpUpdateHisInfo[] getSvrUpdateHis(String s, String s1);
    
}
