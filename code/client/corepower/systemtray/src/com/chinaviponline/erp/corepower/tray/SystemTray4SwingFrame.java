/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tray;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.spring.ISpringBeanLoader;
import com.chinaviponline.erp.corepower.tray.i.IExtensionsFromDB;

/**
 * <p>文件名称：SystemTray4SwingFrme.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-20</p>
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
public class SystemTray4SwingFrame extends TrayIcon
{
    private static final Logger log=Logger.getLogger(SystemTray4SwingFrame.class);
    private JPopupMenu menu;

    /**
     * 图标名称
     */
    private static String picName;
    
    private static Image defaultImage;
    
    /**
     * 其他需要增加的菜单
     */
    private List menuItem;
    
    private static JDialog dialog;
    static
    {
        dialog = new JDialog((Frame) null, "TrayDialog");
        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(true);
        
        defaultImage=createDefaultImage();
    }

    private static PopupMenuListener popupListener = new PopupMenuListener()
    {
        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
        {
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
        {
            dialog.setVisible(false);
        }

        public void popupMenuCanceled(PopupMenuEvent e)
        {
            dialog.setVisible(false);
        }
    };

    public SystemTray4SwingFrame(int cdefault)
    {
        super(defaultImage);
    }
    public SystemTray4SwingFrame(Image image)
    {
        super(image);
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                showJPopupMenu(e);
            }

            public void mouseReleased(MouseEvent e)
            {
                showJPopupMenu(e);
            }
        });
    }

    private void showJPopupMenu(MouseEvent e)
    {
        if (e.isPopupTrigger() && menu != null)
        {
            Dimension size = menu.getPreferredSize();
            dialog.setLocation(e.getX(), e.getY() - size.height);
            dialog.setVisible(true);
            menu.show(dialog.getContentPane(), 0, 0);
            // popup works only for focused windows 
            dialog.toFront();
        }
    }

    public JPopupMenu getJPopupMenu()
    {
        return menu;
    }

    public void setJPopupMenu(JPopupMenu menu)
    {
        if (this.menu != null)
        {
            this.menu.removePopupMenuListener(popupListener);
        }
        this.menu = menu;
        menu.addPopupMenuListener(popupListener);
    }

    private  void createGui()
    {
        SystemTray4SwingFrame tray = new SystemTray4SwingFrame(createImage());
        tray.setJPopupMenu(createJPopupMenu());
        try
        {
            SystemTray.getSystemTray().add(tray);
        }
        catch (AWTException e)
        {
          //  e.printStackTrace();
            log.error("createGui:"+e.getMessage());
        }
    }

//    public static void main(String[] args) throws Exception
//    {
//
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        SwingUtilities.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//                createGui();
//            }
//        });
//    }
    
    public void init()
    {
//        try
//        {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        }
//        catch (ClassNotFoundException e)
//        {
//            log.error("init:"+e.getMessage());
//        }
//        catch (InstantiationException e)
//        {
//            log.error("init:"+e.getMessage());
//        }
//        catch (IllegalAccessException e)
//        {
//            log.error("init:"+e.getMessage());
//        }
//        catch (UnsupportedLookAndFeelException e)
//        {
//            log.error("init:"+e.getMessage());
//        }
        
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createGui();
            }
        });
    }

    private static  Image createImage()
    {
//        BufferedImage i = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = (Graphics2D) i.getGraphics();
//        g2.setColor(Color.RED);
//        g2.fill(new Ellipse2D.Float(0, 0, i.getWidth(), i.getHeight()));
//        g2.dispose();
//        return i;
        
        File file = ServiceAccess.getSystemSupportService().getFile(picName==null?"tray.gif":picName);//设定一个默认值
        
        Image image=null;
        try
        {
            image = Toolkit.getDefaultToolkit().getImage(file.toURI().toURL());
        }
        catch (MalformedURLException mue)
        {
           
            log.warn("find ImangeIcon:"+mue.getMessage());    
            image=createDefaultImage();//默认图标
        }
        
        return image;
    }

   /**
    * 
    * <p>功能描述：创建默认图标</p>
    * <p>创建人：龚为川</p>
    * <p>创建日期：2011-9-20</p>
    * <p>修改记录1：</p>
    * <pre>
    *  修改人：
    *  修改日期：
   
    *  修改内容：
   
    * </pre>
    * <p>修改记录2：</p>
    *
    * @return
    */
    private static Image createDefaultImage()
    {
        BufferedImage i = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
          Graphics2D g2 = (Graphics2D) i.getGraphics();
          g2.setColor(Color.RED);
          g2.fill(new Ellipse2D.Float(0, 0, i.getWidth(), i.getHeight()));
          g2.dispose();
          
          return i;
    }

    private JPopupMenu createJPopupMenu()
    {
        final JPopupMenu m = new JPopupMenu();
//        m.add(new JMenuItem("Item 1"));
//        m.add(new JMenuItem("Item 2"));
//        JMenu submenu = new JMenu("Submenu");
//        submenu.add(new JMenuItem("item 1"));
//        submenu.add(new JMenuItem("item 2"));
//        submenu.add(new JMenuItem("item 3"));
//        m.add(submenu);
        //扩展点100
       // JMenuItem devToolsItem=new JMenuItem("DevTools-100");
        
        ISpringBeanLoader sbl=ServiceAccess.getSpringService();
        IExtensionsFromDB efd=(IExtensionsFromDB)sbl.getBean("extensionsFromDB");
        List l=efd.findExtensions(Integer.parseInt("100"));
        if(l!=null && l.size()>0)
        {
            for(int index=0;index<l.size();index++)
            {
                Map map=(Map)l.get(index);
                log.debug("map is:"+map);
                String strExtensions=(String)map.get("EXTENSIONBEAN");
                Object objItem=sbl.getBean(strExtensions);
                if(objItem instanceof JMenuItem)
                {
                    m.add((JMenuItem)objItem);                           
                }                        
            }
        }
        m.addSeparator();
        
        if(menuItem!=null && menuItem.size()>0)
        {
            for(int i=0;i<menuItem.size();i++)
            {
                m.add((JMenuItem) menuItem.get(i));
            }
        }
        m.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        m.add(exitItem);
        return m;
    }

    public String getPicName()
    {
        return picName;
    }

    public void setPicName(String picName)
    {
//        this.picName = picName;
        SystemTray4SwingFrame.picName=picName;
    }
    public List getMenuItem()
    {
        return menuItem;
    }
    public void setMenuItem(List menuItem)
    {
        this.menuItem = menuItem;
    }
}