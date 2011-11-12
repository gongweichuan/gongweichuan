package com.chinaviponline.erp.corepower.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.api.ServiceAccess;

/**
 * <p>文件名称：SystemTrayFrame.java</p>
 * <p>文件描述：系统托盘的主类</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-13</p>
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
public class SystemTrayFrame
{
    /**
     * 日志
     */
    private static final Logger log;
    
    /**
     * 鼠标放上去的后的提示。
     */
    private String toolTipStr="Tray PRM";

    /**
     * 系统托盘实例
     */
   private  TrayIcon trayIcon;
   
   /**
    * 其他需要增加的菜单
    */
   private List menuItem;
   
   /**
    * 图标名称
    */
   private String picName;
   
    static
    {
        log = Logger.getLogger(SystemTrayFrame.class);
    }

    public SystemTrayFrame(String picName)
    {
        this.picName=picName;
    }
    
    public void init()
    {
        //   final TrayIcon trayIcon;

        if (SystemTray.isSupported())
        {

            SystemTray tray = SystemTray.getSystemTray();            
            File file = ServiceAccess.getSystemSupportService().getFile(picName==null?"tray.gif":picName);//设定一个默认值
            
            Image image=null;
            try
            {
                image = Toolkit.getDefaultToolkit().getImage(file.toURI().toURL());
            }
            catch (MalformedURLException mue)
            {
               
                log.warn("find ImangeIcon:"+mue.getMessage());
            }

            MouseListener mouseListener = new MouseListener()
            {

                public void mouseClicked(MouseEvent e)
                {
                    log.debug("Tray Icon - Mouse clicked!");
                }

                public void mouseEntered(MouseEvent e)
                {
                    log.debug("Tray Icon - Mouse entered!");
                }

                public void mouseExited(MouseEvent e)
                {
                    log.debug("Tray Icon - Mouse exited!");
                }

                public void mousePressed(MouseEvent e)
                {
                    log.debug("Tray Icon - Mouse pressed!");
                }

                public void mouseReleased(MouseEvent e)
                {
                    log.debug("Tray Icon - Mouse released!");
                }

            };

            ActionListener exitListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    log.info("Exiting...");
                    System.exit(0);
                }
            };

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            if(menuItem!=null && menuItem.size()>0)
            {
                for(int i=0;i<menuItem.size();i++)
                {
                    popup.add((MenuItem) menuItem.get(i));
                }
            }
                
            trayIcon = new TrayIcon(image, toolTipStr, popup);

            ActionListener actionListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    trayIcon.displayMessage("Action Event",
                            "An Action Event Has Been Peformed!",
                            TrayIcon.MessageType.INFO);
                }
            };

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            trayIcon.addMouseListener(mouseListener);

            //    Depending on which Mustang build you have, you may need to uncomment
            //    out the following code to check for an AWTException when you add 
            //    an image to the system tray.

            try
            {
                tray.add(trayIcon);
            }
            catch (AWTException e)
            {
                log.error("TrayIcon could not be added.");
            }

        }
        else
        {
            log.error("System tray is currently not supported.");
        }
    }

    public String getToolTipStr()
    {
        return toolTipStr;
    }

    public void setToolTipStr(String toolTipStr)
    {
        this.toolTipStr = toolTipStr;
    }

    public TrayIcon getTrayIcon()
    {
        return trayIcon;
    }

    public void setTrayIcon(TrayIcon trayIcon)
    {
        this.trayIcon = trayIcon;
    }

    public List getMenuItem()
    {
        return menuItem;
    }

    public void setMenuItem(List menuItem)
    {
        this.menuItem = menuItem;
//        this.menuItem = new ArrayList(menuItems.length);
//        for (int i = 0; i < menuItems.length; i++)
//        {
//            this.menuItem.add(StringUtils.trimWhitespace(menuItems[i]));
//        }

    }

    public String getPicName()
    {
        return picName;
    }

    public void setPicName(String picName)
    {
        this.picName = picName;
    }
}
