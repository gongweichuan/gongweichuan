package com.chinaviponline.erp.corepower.helloworld.snow.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jboss.logging.Logger;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.helloworld.snow.i.ISnow;
import com.sun.awt.AWTUtilities;
/**
 * <p>文件名称：TSnowImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-10-31</p>
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
public class TSnowImpl implements ISnow
{
    private String icon;//雪花图片的位置
    
    private static JFrame frame;
    
    private static Random rand = new Random();
    
    private static Logger log=Logger.getLogger(TSnowImpl.class);
    
    /**
     * 
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.helloworld.snow.i.ISnow#start()
     */
    public boolean start()
    {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(3);
        frame.setUndecorated(true);
        frame.setExtendedState(6);
        AWTUtilities.setWindowOpaque(frame, false);

        JPanel pane =new JPanel()
        {
        private static final long serialVersionUID = 6579312966769103792L;
        private int[] snowX;
        private int[] snowY;
        private int[] angles;
        private int count=10;

          public void paint(Graphics g)
          {
            super.paint(g);
            Rectangle bounds = frame.getBounds();
            if (this.snowX == null)
            {
              this.snowX = new int[this.count];
              for (int i = 0; i < this.snowX.length; ++i) {
                this.snowX[i] = rand.nextInt(bounds.width);
              }
              this.snowY = new int[this.count];
              for (int i = 0; i < this.snowY.length; ++i) {
                this.snowY[i] = rand.nextInt(bounds.height);
              }
              this.angles = new int[this.count];
              for (int i = 0; i < this.snowY.length; ++i) {
                this.angles[i] = rand.nextInt(360);
              }
            }

            Graphics2D g2d = (Graphics2D)g;
            Image image = TSnowImpl.this.createImage();
            for (int i = 0; i < this.count; ++i) {
              this.snowX[i] += rand.nextInt(5) - 3;
              this.snowY[i] += 5;
              this.angles[i] += i / 5;
              this.snowY[i] = ((this.snowY[i] > bounds.height) ? 0 : this.snowY[i]);
              this.angles[i] = ((this.angles[i] > 360) ? 0 : this.angles[i]);
              int x = this.snowX[i];
              int y = this.snowY[i];
              int angle = this.angles[i];
              g2d.translate(x, y);
              double angleValue = Math.toRadians(angle);
              g2d.rotate(angleValue);
              g2d.drawImage(image, 0, 0, null);
              g2d.rotate(-angleValue);
              g2d.translate(-x, -y);
            }

          }

        };
        
        frame.setContentPane(pane);
        frame.setVisible(true);
        Thread thread = new Thread()
        {
          public void run()
          {
            while (true) {
              try {
                Thread.sleep(10L);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
              frame.repaint();
            }
          }
        };
        thread.start();
        return true;
    }

    /**
     * 
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.helloworld.snow.i.ISnow#stop()
     */
    public boolean stop()
    {      
       frame.dispose();
       return true;
    }

    private  Image createImage()
    {        
        File file = ServiceAccess.getSystemSupportService().getFile(icon==null?"snow.png":icon);//设定一个默认值
        
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

    public  String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }
}
