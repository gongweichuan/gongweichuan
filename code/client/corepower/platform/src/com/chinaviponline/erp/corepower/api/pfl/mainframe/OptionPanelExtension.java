/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.chinaviponline.erp.corepower.api.pfl.finterface.FIException;

/**
 * <p>文件名称：OptionPanelExtension.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-23</p>
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
public interface OptionPanelExtension
{


    public static final String EXTENSION_ID = "corepower.mainframe.OptionPanelExtension";
    public static final boolean inStyleDialog = true;

    public abstract void init(JTabbedPane jtabbedpane);

    public abstract String getTitle();

    public abstract JPanel getPanel();

    public abstract boolean check()
        throws FIException;

    public abstract void save()
        throws FIException;

    public abstract boolean isDataChanged();

    
}
