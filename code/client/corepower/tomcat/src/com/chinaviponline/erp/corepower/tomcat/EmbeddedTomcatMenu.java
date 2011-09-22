/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tomcat;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * <p>文件名称：EmbeddedTomcatMenu.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-14</p>
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
public class EmbeddedTomcatMenu extends Menu
{
   // private String label;//菜单文字
    private List  item;//子菜单
    
    public void init()
    {
     //   this.add(label);
        
        if(item!=null &&item.size()>0)
        {
            for(int i=0;i<item.size();i++)
            {
                this.add((MenuItem)item.get(i));
            }
        }
    }

//    public String getLabel()
//    {
//        return label;
//    }
//
//    public void setLabel(String label)
//    {
//        this.label = label;
//    }

    public List getItem()
    {
        return item;
    }

    public void setItem(List item)
    {
        this.item = item;
    }
}
