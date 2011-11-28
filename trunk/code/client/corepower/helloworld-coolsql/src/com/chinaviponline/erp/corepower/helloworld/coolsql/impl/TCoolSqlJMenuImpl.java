/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.helloworld.coolsql.impl;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import com.chinaviponline.erp.corepower.helloworld.coolsql.i.ICoolSqlJMenu;

/**
 * <p>文件名称：TCoolSqlJMenuImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-11-13</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：

 *  版本号：
 *  修改人：
 *  修改内容：

 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川

 * @email  gongweichuan(AT)gmail.com
 */
public class TCoolSqlJMenuImpl extends JMenu  implements ICoolSqlJMenu
{
    /**
     * 
     */
    private static final long serialVersionUID = 2279832649483073381L;
    private List item;//子菜单

    public void init()
    {
        //   this.add(label);

        if (item != null && item.size() > 0)
        {
            for (int i = 0; i < item.size(); i++)
            {
                this.add((JMenuItem) item.get(i));
            }
        }
    }

    public List getItem()
    {
        return item;
    }

    public void setItem(List item)
    {
        this.item = item;
    }
}
